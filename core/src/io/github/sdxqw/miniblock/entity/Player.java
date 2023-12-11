package io.github.sdxqw.miniblock.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import io.github.sdxqw.miniblock.MiniBlock;
import io.github.sdxqw.miniblock.animation.PlayerAnimation;
import io.github.sdxqw.miniblock.blocks.BlockBreak;
import io.github.sdxqw.miniblock.world.WorldGame;
import lombok.Getter;
import lombok.Setter;

import static io.github.sdxqw.miniblock.entity.Player.State.*;
import static io.github.sdxqw.miniblock.terrain.WorldTerrain.VIEW_DISTANCE;

@Getter
@Setter
public class Player extends Entity {
    private final Box2DDebugRenderer debugRenderer;
    private final ShapeRenderer shapeRenderer;
    private final PlayerAnimation playerAnimation;
    private WorldGame worldGame;
    private State currentState = IDLE;
    private BlockBreak blockBreak;

    public Player(int x, int y, float speed, World world, WorldGame worldGame) {
        super(x, y, speed, world);
        this.worldGame = worldGame;
        shapeRenderer = new ShapeRenderer();
        debugRenderer = new Box2DDebugRenderer();
        playerAnimation = new PlayerAnimation();
        blockBreak = new BlockBreak(worldGame, worldGame.getWorldTerrain().getBlockBreakAnimation());
    }

    @Override
    public void update(float deltaTime) {
        Camera camera = worldGame.getViewport().getCamera();
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePos);

        handleInput();
        interactWithBlock(mousePos, deltaTime);
        playerAnimation.updateFrames(deltaTime);
    }

    private void interactWithBlock(Vector3 mousePos, float deltaTime) {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && isInRange(mousePos)) {
            int blockX = (int) mousePos.x;
            int blockY = (int) mousePos.y;

            blockBreak.setBlockToBreak(blockX, blockY);

            if (blockBreak != null)
                blockBreak.breakBlock(deltaTime);

            float dx = mousePos.x - getX();
            float dy = mousePos.y - getX();
            setCurrentState(getDirection(dx, dy));
        } else if (blockBreak != null) {
            blockBreak.stopBreaking();
        }
    }

    private int getDirection(float dx, float dy) {
        if (dx > 0 && dy > 0) return 0;
        if (dx < 0 && dy > 0) return 1;
        if (dx > 0 && dy < 0) return 2;
        if (dx < 0 && dy < 0) return 3;
        return 0;
    }

    private void handleInput() {
        float speedX = 0;
        float speedY = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            speedY = getSpeed();
            setCurrentState(1);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            speedY = -getSpeed();
            setCurrentState(0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            speedX = -getSpeed();
            setCurrentState(3);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            speedX = getSpeed();
            setCurrentState(2);
        }

        getBody2D().setLinearVelocity(speedX, speedY);
        setX(getBody2D().getPosition().x);
        setY(getBody2D().getPosition().y);
    }

    private void setCurrentState(int direction) {
        currentState = switch (direction) {
            case 0 -> IDLE;
            case 1 -> IDLE_BEHIND;
            case 2 -> IDLE_RIGTH;
            case 3 -> IDLE_LEFT;
            default -> throw new IllegalStateException("Direction is invalid: " + direction);
        };
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = playerAnimation.getCurrentFrame(currentState.getIndex());
        batch.draw(currentFrame, getX() - ENTITY_SIZE / 2f, getY() - ENTITY_SIZE / 2f, ENTITY_SIZE, ENTITY_SIZE);
    }

    public void renderDebug(Camera camera) {
        if (MiniBlock.DEBUG) debugRenderer.render(getWorld(), camera.combined);
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
        return Math.abs(getX() - mousePos.x) < VIEW_DISTANCE && Math.abs(getY() - mousePos.y) < VIEW_DISTANCE;
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
