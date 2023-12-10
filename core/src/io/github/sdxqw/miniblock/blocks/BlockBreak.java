package io.github.sdxqw.miniblock.blocks;

import lombok.Getter;

@Getter
public class BlockBreak {
    private final BlockStack blockStack;
    private final float breakTime = 3f;

    public BlockBreak(BlockStack blockStack) {
        this.blockStack = blockStack;
    }

    public void breakBlock(float deltaTime) {
        Block topBlock = blockStack.getTopBlock();
        if (topBlock != null) {
            topBlock.setBreaking(true);
            topBlock.decreaseHealth(breakTime, deltaTime);
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
        }
    }
}