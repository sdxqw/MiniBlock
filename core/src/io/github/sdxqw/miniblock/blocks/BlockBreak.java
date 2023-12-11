package io.github.sdxqw.miniblock.blocks;

import io.github.sdxqw.miniblock.animation.BlockBreakAnimation;
import io.github.sdxqw.miniblock.world.WorldGame;
import lombok.Getter;

@Getter
public class BlockBreak {
    private final WorldGame worldGame;
    private BlockStack blockStack;
    private final float breakTime = 2f;
    private final BlockBreakAnimation blockBreakAnimation;

    public BlockBreak(WorldGame worldGame, BlockBreakAnimation blockBreakAnimation) {
        this.worldGame = worldGame;
        this.blockBreakAnimation = blockBreakAnimation;
    }

    public void setBlockToBreak(int x, int y)  {
        blockStack = worldGame.getWorldTerrain().getChunk(x, y).getBlock(x, y);
    }

    public void breakBlock(float deltaTime) {
        if (blockStack == null) return;
        Block topBlock = blockStack.getTopBlock();
        if (topBlock != null) {
            topBlock.setBreaking(true);
            topBlock.decreaseHealth(breakTime, deltaTime);
            startAnimationBreak(deltaTime);
            if (topBlock.isDestroyed()) {
                stopBreaking();
            }
        }
    }

    private void startAnimationBreak(float deltaTime) {
        if (blockStack == null) return;
        Block topBlock = blockStack.getTopBlock();
        if (topBlock != null) {
            blockBreakAnimation.setFrameDuration(topBlock.getBlockHealth());
            blockBreakAnimation.updateFrames(deltaTime);
            topBlock.decreaseHealth(breakTime, deltaTime);
        }
    }

    public void stopBreaking() {
        if (blockStack == null) return;
        Block topBlock = blockStack.getTopBlock();
        if (topBlock != null) {
            blockStack.removeTopBlock();
            topBlock.setBreaking(false);
            topBlock.setCurrentBlockHealth(topBlock.getBlockHealth());
            blockStack = null;
        }
    }
}