package io.github.sdxqw.miniblock.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.Getter;

@Getter
public class HUD implements Disposable {
    private final Stage stage;
    private final Viewport viewport;

    private final Label fpsLabel;
    private final BitmapFont font;

    public HUD(SpriteBatch batch) {
        viewport = new ScreenViewport(new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Table table = new Table();
        table.top().left();
        table.setFillParent(true);

        font = new BitmapFont(Gdx.files.internal("font/pixeled.fnt"));

        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        font.getData().setScale(1f);

        fpsLabel = new Label("", new Label.LabelStyle(font, Color.WHITE));
        table.add(fpsLabel).expandX().padTop(0);
        stage.addActor(table);
    }


    public void update() {
        fpsLabel.setText(String.format("%d", Gdx.graphics.getFramesPerSecond()));
    }

    @Override
    public void dispose() {
        stage.dispose();
        font.dispose();
    }
}
