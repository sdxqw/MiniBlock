package io.github.sdxqw.miniblock.animation;

import io.github.sdxqw.miniblock.sprite.SpriteSheets;
import io.github.sdxqw.miniblock.sprite.TextureID;
import lombok.Getter;

import java.util.List;

@Getter
public class BlockBreakAnimation extends CustomAnimation {
    public BlockBreakAnimation() {
        super(new SpriteSheets("block/block_breaking.atlas"));
        createAnimation();
    }

    @Override
    public void createAnimation() {
        addAnimation(List.of(loadAnimation(TextureID.DESTROY_STAGE, 10)));
    }
}
