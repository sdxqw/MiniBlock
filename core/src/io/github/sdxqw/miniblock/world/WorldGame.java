package io.github.sdxqw.miniblock.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.sdxqw.miniblock.MiniBlock;
import io.github.sdxqw.miniblock.entity.Player;
import io.github.sdxqw.miniblock.hud.HUD;
import io.github.sdxqw.miniblock.terrain.WorldTerrain;
import lombok.Getter;

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
        this.world = new World(new Vector2(0, 0), true);

        viewport = new FitViewport(MiniBlock.V_WIDTH / MiniBlock.SCALE, MiniBlock.V_HEIGHT / MiniBlock.SCALE);
        hud = new HUD(game.getBatch());

        worldTerrain = new WorldTerrain(2, 2);

        player = new Player(world, this);
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

        viewport.getCamera().position.set(playerPos.x, playerPos.y, 0f);
        viewport.apply();
        game.getBatch().setProjectionMatrix(viewport.getCamera().combined);

        game.getBatch().begin();
        worldTerrain.renderWorld(game.getBatch());
        player.render(game.getBatch());
        game.getBatch().end();

        player.renderDebug(getViewport().getCamera());

        game.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().draw();
        hud.update();


        world.step(1 / 60f, 6, 2);
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
