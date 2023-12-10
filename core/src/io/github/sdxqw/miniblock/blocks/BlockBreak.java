package io.github.sdxqw.miniblock.blocks;

import io.github.sdxqw.miniblock.animation.BlockBreakAnimation;
import lombok.Getter;

@Getter
public class BlockBreak {
    private final BlockStack blockStack;
    private final float breakTime = 2f;
    private final BlockBreakAnimation blockBreakAnimation;

    public BlockBreak(BlockStack blockStack, BlockBreakAnimation blockBreakAnimation) {
        this.blockStack = blockStack;
        this.blockBreakAnimation = blockBreakAnimation;
    }

    public void breakBlock(float deltaTime) {
        Block topBlock = blockStack.getTopBlock();
        if (topBlock != null) {
            blockBreakAnimation.setFrameDuration(topBlock.getBlockHealth());
            topBlock.setBreaking(true);
            topBlock.decreaseHealth(breakTime, deltaTime);
            blockBreakAnimation.updateFrames(deltaTime);
            if (topBlock.isDestroyed()) {
                blockStack.removeTopBlock();
                stopBreaking();
            }
        }
    }

    public void stopBreaking() {
        Block topBlock = blockStack.getTopBlock();
        if (topBlock != null) {
            topBlock.setBreaking(false);
            topBlock.setCurrentBlockHealth(topBlock.getBlockHealth());
        }
    }
}