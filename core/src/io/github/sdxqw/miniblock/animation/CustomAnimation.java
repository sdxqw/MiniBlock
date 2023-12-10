package io.github.sdxqw.miniblock.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import io.github.sdxqw.miniblock.sprite.SpriteSheets;
import io.github.sdxqw.miniblock.sprite.TextureID;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class CustomAnimation implements Disposable {
    private final List<Animation<TextureRegion>> animations;
    private final SpriteSheets spriteSheet;
    private float elapsedTime = 0f;
    private float frameDuration = 0.12f;

    public CustomAnimation(SpriteSheets spriteSheet) {
        this.spriteSheet = spriteSheet;
        this.animations = new ArrayList<>();
    }

    public void addAnimation(List<Animation<TextureRegion>> animation) {
        animations.addAll(animation);
    }

    public void updateFrames(float deltaTime) {
        elapsedTime += deltaTime;
    }

    public Animation<TextureRegion> loadAnimation(TextureID textureID, int frameCount) {
        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < frameCount; i++) {
            TextureRegion textureRegion = spriteSheet.getTextureRegion(textureID, i).orElseThrow();
            frames.add(textureRegion);
        }
        return new Animation<>(frameDuration, frames, Animation.PlayMode.LOOP);
    }

    public abstract void createAnimation();

    public TextureRegion getCurrentFrame(int index) {
        return getAnimations().get(index).getKeyFrame(getElapsedTime());
    }

    @Override
    public void dispose() {
        spriteSheet.dispose();
        animations.clear();
    }
}
