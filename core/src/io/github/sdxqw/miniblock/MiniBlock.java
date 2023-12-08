package io.github.sdxqw.miniblock;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.sdxqw.miniblock.world.WorldGame;
import lombok.Getter;

@Getter
public class MiniBlock extends Game {
    public static final int V_WIDTH = 16;
    public static final int V_HEIGHT = 9;
    public static boolean DEBUG = true;
    private SpriteBatch batch;
    private BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        this.setScreen(new WorldGame(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}