package io.github.sdxqw.miniblock.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import io.github.sdxqw.miniblock.sprite.SpriteSheets;
import io.github.sdxqw.miniblock.sprite.TextureID;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CustomAnimation {
    private final List<Animation<TextureRegion>> animations;
    private final SpriteSheets spriteSheet;

    public CustomAnimation(SpriteSheets spriteSheet) {
        this.spriteSheet = spriteSheet;
        this.animations = new ArrayList<>();
    }

    public void addAnimation(List<Animation<TextureRegion>> animation) {
        animations.addAll(animation);
    }

    public Animation<TextureRegion> loadAnimation(String textureIDs, int index, String optional) {
        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < index; i++) {
            TextureRegion textureRegion = spriteSheet.getTextureRegion(TextureID.valueOf(textureIDs + i + optional)).orElseThrow();
            frames.add(textureRegion);
        }
        return new Animation<>(0.12f, frames, Animation.PlayMode.LOOP);
    }
}
