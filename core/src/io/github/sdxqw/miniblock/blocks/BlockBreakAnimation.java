package io.github.sdxqw.miniblock.blocks;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import io.github.sdxqw.miniblock.animation.CustomAnimation;
import io.github.sdxqw.miniblock.sprite.SpriteSheets;
import lombok.Getter;

@Getter
public class BlockBreakAnimation implements Disposable {
    private final CustomAnimation animation;
    private final SpriteSheets spriteSheets;
    private TextureRegion currentFrame;
    private float elapsedTime = 0f;

    public BlockBreakAnimation(CustomAnimation animation, SpriteSheets spriteSheets) {
        this.animation = animation;
        this.spriteSheets = spriteSheets;
    }

    public void updateFrames(float deltaTime) {
        elapsedTime += deltaTime;
        currentFrame = animation.getAnimations().get(0).getKeyFrame(elapsedTime);
    }

    public void setFrameDuration(float frameDuration) {
        animation.getAnimations().get(0).setFrameDuration(frameDuration);
    }

    @Override
    public void dispose() {
        spriteSheets.dispose();
    }
}
