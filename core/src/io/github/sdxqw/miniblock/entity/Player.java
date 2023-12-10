package io.github.sdxqw.miniblock.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
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
import io.github.sdxqw.miniblock.animation.PlayerAnimation;
import io.github.sdxqw.miniblock.blocks.BlockBreak;
import io.github.sdxqw.miniblock.blocks.BlockStack;
import io.github.sdxqw.miniblock.world.Box2DHelper;
import io.github.sdxqw.miniblock.world.WorldGame;
import lombok.Getter;
import lombok.Setter;

import static io.github.sdxqw.miniblock.entity.Player.State.*;
import static io.github.sdxqw.miniblock.terrain.WorldTerrain.VIEW_DISTANCE;

@Getter
public class Player implements Disposable {
    public static final int PLAYER_SIZE = 1;

    private final World world;
    private final Box2DDebugRenderer debugRenderer;
    private final Body body2D;
    private final WorldGame worldGame;
    private final ShapeRenderer shapeRenderer;
    private final float speed = .5f;
    private final PlayerAnimation playerAnimation;
    private State currentState;
    @Setter
    private float x = 1000;
    @Setter
    private float y = 1000;
    private BlockBreak blockBreak;

    public Player(World world, WorldGame worldGame) {
        this.world = world;
        this.worldGame = worldGame;
        this.shapeRenderer = new ShapeRenderer();
        this.body2D = Box2DHelper.createBody(world, PLAYER_SIZE / 2f, PLAYER_SIZE / 2f, new Vector2(getX(), getY()), BodyDef.BodyType.KinematicBody);
        this.debugRenderer = new Box2DDebugRenderer();
        this.playerAnimation = new PlayerAnimation();
        currentState = IDLE;
    }

    public void update(float deltaTime) {
        Camera camera = worldGame.getViewport().getCamera();
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePos);

        handleInput();
        updatePosition(mousePos, getX(), getY());
        interactWithBlock(mousePos, deltaTime);
        playerAnimation.updateFrames(deltaTime);
    }

    private void interactWithBlock(Vector3 mousePos, float deltaTime) {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            int blockX = (int) mousePos.x;
            int blockY = (int) mousePos.y;

            if (isInRange(mousePos)) {
                BlockStack blockStack = worldGame.getWorldTerrain().getChunk(blockX, blockY).getBlock(blockX, blockY);
                if (blockStack == null) return;

                blockBreak = new BlockBreak(blockStack, worldGame.getWorldTerrain().getBlockBreakAnimation());
                blockBreak.breakBlock(deltaTime);
            }
        } else {
            if (blockBreak != null) {
                blockBreak.stopBreaking();
            }
        }
    }

    private void updatePosition(Vector3 mousePos, float playerX, float playerY) {
        float dx = mousePos.x - playerX;
        float dy = mousePos.y - playerY;

        boolean abs = Math.abs(dx) > Math.abs(dy);
        if (abs) {
            currentState = dx > 0 ? IDLE_RIGTH : IDLE_LEFT;
        } else {
            currentState = dy > 0 ? IDLE_BEHIND : IDLE;
        }
    }

    private void handleInput() {
        float speedX = 0;
        float speedY = 0;

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
    }


    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = playerAnimation.getCurrentFrame(currentState.getIndex());
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
        if (isInRange(mousePos)) {
            shapeRenderer.setColor(0, 1, 0, 1);
            shapeRenderer.rect((int) mousePos.x, (int) mousePos.y, 1, 1);
        }
        shapeRenderer.end();
    }

    private boolean isInRange(Vector3 mousePos) {
        return Math.abs((int) getX() - mousePos.x) < VIEW_DISTANCE && Math.abs((int) getY() - mousePos.y) < VIEW_DISTANCE;
    }

    @Override
    public void dispose() {
        playerAnimation.dispose();
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
