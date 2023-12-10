package io.github.sdxqw.miniblock.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import io.github.sdxqw.miniblock.MiniBlock;
import io.github.sdxqw.miniblock.animation.CustomAnimation;
import io.github.sdxqw.miniblock.blocks.BlockBreak;
import io.github.sdxqw.miniblock.blocks.BlockStack;
import io.github.sdxqw.miniblock.blocks.Grass;
import io.github.sdxqw.miniblock.sprite.SpriteSheets;
import io.github.sdxqw.miniblock.world.Box2DHelper;
import io.github.sdxqw.miniblock.world.WorldGame;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static io.github.sdxqw.miniblock.entity.Player.State.*;
import static io.github.sdxqw.miniblock.terrain.WorldTerrain.VIEW_DISTANCE;

@Getter
public class Player implements Disposable {
    public static final int PLAYER_SIZE = 1;

    private final World world;
    private final CustomAnimation animations;
    private final SpriteSheets spriteSheet;
    private final Box2DDebugRenderer debugRenderer;
    private final Body body2D;
    private final WorldGame worldGame;
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final float speed = .5f;
    private State currentState;
    private float totalElapsedTime = 0f;
    @Setter
    private float x = 1000;
    @Setter
    private float y = 1000;

    private BlockBreak blockBreak;

    public Player(World world, WorldGame worldGame) {
        this.world = world;
        this.worldGame = worldGame;
        body2D = Box2DHelper.createBody(world, PLAYER_SIZE / 2f, PLAYER_SIZE / 2f, new Vector2(getX(), getY()), BodyDef.BodyType.KinematicBody);
        debugRenderer = new Box2DDebugRenderer();
        spriteSheet = new SpriteSheets("player/player.atlas");
        animations = new CustomAnimation(spriteSheet);
        Animation<TextureRegion> idle = animations.loadAnimation("IDLE", 5, "");
        Animation<TextureRegion> idleBehind = animations.loadAnimation("IDLE", 5, "_BEHIND");
        Animation<TextureRegion> idleRigth = animations.loadAnimation("IDLE", 5, "_RIGHT");
        Animation<TextureRegion> idleLeft = animations.loadAnimation("IDLE", 5, "_LEFT");

        animations.addAnimation(List.of(idle, idleBehind, idleRigth, idleLeft));
        currentState = IDLE;

    }

    public void update(float deltaTime) {
        float speedX = 0;
        float speedY = 0;

        Camera camera = worldGame.getViewport().getCamera();
        float playerX = getX();
        float playerY = getY();

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            speedY = speed;
            currentState = IDLE_BEHIND;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            speedY = -speed;
            currentState = IDLE;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            speedX = -speed;
            currentState = IDLE_LEFT;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            speedX = speed;
            currentState = IDLE_RIGTH;
        }

        body2D.setLinearVelocity(speedX, speedY);
        setX(body2D.getPosition().x);
        setY(body2D.getPosition().y);

        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePos);

        float dx = mousePos.x - playerX;
        float dy = mousePos.y - playerY;

        boolean abs = Math.abs(dx) > Math.abs(dy);
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            if (abs) {
                currentState = dx > 0 ? IDLE_RIGTH : IDLE_LEFT;
            } else {
                currentState = dy > 0 ? IDLE_BEHIND : IDLE;
            }

            int blockX = (int) mousePos.x;
            int blockY = (int) mousePos.y;
            if (Math.abs((int) getX() - mousePos.x) < VIEW_DISTANCE && Math.abs((int) getY() - mousePos.y) < VIEW_DISTANCE)
                worldGame.getWorldTerrain().setBlock(blockX, blockY, new Grass(blockX, blockY));
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (abs) {
                currentState = dx > 0 ? IDLE_RIGTH : IDLE_LEFT;
            } else {
                currentState = dy > 0 ? IDLE_BEHIND : IDLE;
            }

            int blockX = (int) mousePos.x;
            int blockY = (int) mousePos.y;

            if (Math.abs((int) getX() - mousePos.x) < VIEW_DISTANCE && Math.abs((int) getY() - mousePos.y) < VIEW_DISTANCE) {
                BlockStack blockStack = worldGame.getWorldTerrain().getChunk(blockX, blockY).getBlock(blockX, blockY);

                blockBreak = new BlockBreak(blockStack);
                blockBreak.breakBlock(deltaTime);
                if (blockStack.getTopBlock().isBreaking()) {
                    worldGame.getWorldTerrain().getBlockBreakAnimation().updateFrames(deltaTime);
                }
            }
        } else {
            if (blockBreak != null) {
                blockBreak.stopBreaking();
                blockBreak = null;
            }
        }

        totalElapsedTime += deltaTime;
    }


    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = animations.getAnimations().get(currentState.getIndex()).getKeyFrame(totalElapsedTime);
        batch.draw(currentFrame, getX() - PLAYER_SIZE / 2f, getY() - PLAYER_SIZE / 2f, PLAYER_SIZE, PLAYER_SIZE);
    }

    public void renderDebug(Camera camera) {
        if (MiniBlock.DEBUG) debugRenderer.render(world, camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.rect(16 * (int) (getX() / 16f), 16 * (int) (getY() / 16f), 16, 16);
        shapeRenderer.setColor(1, 1, 1, 1);
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePos);
        if (Math.abs((int) getX() - mousePos.x) < VIEW_DISTANCE && Math.abs((int) getY() - mousePos.y) < VIEW_DISTANCE) {
            shapeRenderer.setColor(0, 1, 0, 1);
            shapeRenderer.rect((int) mousePos.x, (int) mousePos.y, 1, 1);
        }
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        spriteSheet.dispose();
    }

    @Getter
    public enum State {
        IDLE(0), IDLE_BEHIND(1), IDLE_RIGTH(2), IDLE_LEFT(3);

        final int index;

        State(int index) {
            this.index = index;
        }
    }
}
