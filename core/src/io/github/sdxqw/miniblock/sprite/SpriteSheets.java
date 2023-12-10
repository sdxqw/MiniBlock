package io.github.sdxqw.miniblock.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.sdxqw.miniblock.MiniBlock;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public class SpriteSheets {
    private final TextureAtlas atlas;
    @Getter
    private final Map<TextureID, TextureRegion> textureRegions;

    public SpriteSheets(String atlasPath) {
        if (!Gdx.files.internal(atlasPath).exists()) {
            throw new IllegalArgumentException("Atlas file not found at path: " + atlasPath);
        }

        atlas = new TextureAtlas(atlasPath);
        textureRegions = new HashMap<>();

        for (TextureID id : TextureID.values()) {
            TextureRegion region = atlas.findRegion(id.name().toLowerCase());
            if (region != null) {
                textureRegions.put(id, region);
            }
        }

        if (MiniBlock.DEBUG) {
            atlas.getRegions().forEach(region -> System.out.println("Loading Texture: " + region.name));
        }

        atlas.getTextures().forEach(texture -> texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest));
    }

    public Optional<TextureRegion> getTextureRegion(TextureID id, int index) {
        return Optional.ofNullable(atlas.findRegion(id.name().toLowerCase(), index));
    }

    public Optional<TextureRegion> getTextureRegion(TextureID id) {
        return Optional.ofNullable(textureRegions.get(id));
    }

    public void dispose() {
        atlas.dispose();
    }
}