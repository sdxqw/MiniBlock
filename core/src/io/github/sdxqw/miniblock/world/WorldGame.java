package io.github.sdxqw.miniblock.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.sdxqw.miniblock.MiniBlock;
import io.github.sdxqw.miniblock.entity.Player;
import io.github.sdxqw.miniblock.hud.HUD;
import io.github.sdxqw.miniblock.terrain.WorldTerrain;
import lombok.Getter;

import static io.github.sdxqw.miniblock.terrain.Chunk.CHUNK_SIZE;

@Getter
public class WorldGame extends ScreenAdapter {

    private final MiniBlock game;
    private final Viewport viewport;
    private final HUD hud;

    private final Player player;
    private final World world;
    private final Vector2 playerPos;
    private final WorldTerrain worldTerrain;


    public WorldGame(MiniBlock game) {
        this.game = game;
        world = new World(new Vector2(0, 0), true);

        viewport = new ExtendViewport(MiniBlock.V_WIDTH, MiniBlock.V_HEIGHT);
        hud = new HUD(game.getBatch());

        worldTerrain = new WorldTerrain(this);

        player = new Player(1000, 1000, 0.2f, world, this);
        playerPos = player.getBody2D().getPosition();
    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            MiniBlock.DEBUG = !MiniBlock.DEBUG;
        }

        player.update(delta);
        updateWorldWithCamera();

        viewport.getCamera().position.set(playerPos.x, playerPos.y, 0f);
        viewport.apply();
        game.getBatch().setProjectionMatrix(viewport.getCamera().combined);

        game.getBatch().begin();
        worldTerrain.renderWorld(game.getBatch(), viewport.getCamera());
        player.render(game.getBatch());
        game.getBatch().end();

        player.renderDebug(getViewport().getCamera());

        game.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().draw();
        hud.update();


        world.step(1 / 60f, 6, 2);
    }

    private void updateWorldWithCamera() {
        int x = (int) viewport.getCamera().position.x;
        int y = (int) viewport.getCamera().position.y;

        for (int i = x - WorldTerrain.VIEW_DISTANCE * CHUNK_SIZE / 2; i < x + WorldTerrain.VIEW_DISTANCE * CHUNK_SIZE / 2; i += CHUNK_SIZE) {
            for (int j = y - WorldTerrain.VIEW_DISTANCE * CHUNK_SIZE / 2; j < y + WorldTerrain.VIEW_DISTANCE * CHUNK_SIZE / 2; j += CHUNK_SIZE) {
                if (i >= 0 && j >= 0) {
                    worldTerrain.generateWorld(i, j);
                }
            }
        }
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        hud.dispose();
        world.dispose();
        player.dispose();
    }
}
