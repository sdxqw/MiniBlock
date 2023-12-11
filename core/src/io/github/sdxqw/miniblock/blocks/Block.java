package io.github.sdxqw.miniblock.blocks;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.sdxqw.miniblock.sprite.SpriteSheets;
import io.github.sdxqw.miniblock.sprite.TextureID;
import io.github.sdxqw.miniblock.world.WorldGame;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Block {
    public static final int BLOCK_SIZE = 1;
    private float blockHealth;
    private float currentBlockHealth;
    private int indexBlock = -1;

    private boolean isBreaking = false;
    private boolean canBeBroken = false;

    private int x;
    private int y;

    private TextureID textureID = TextureID.AIR;

    private BlockBreak blockBreak;

    public Block(int x, int y, WorldGame game) {
        this.x = x;
        this.y = y;
        if (game == null) return;
        this.blockBreak = new BlockBreak(game, game.getBlockBreakAnimation());
    }

    public void decreaseHealth(float amount, float deltaTime) {
        currentBlockHealth -= amount * deltaTime;
    }

    public void renderBlock(SpriteBatch batch, SpriteSheets spriteSheets, int x, int y) {
        if (textureID == TextureID.AIR || this instanceof Air) return;
        validateTextureID();
        TextureRegion region = getTextureRegion(spriteSheets, textureID);
        batch.draw(region, x, y, Block.BLOCK_SIZE, Block.BLOCK_SIZE);

        if (blockBreak == null) return;
        if (canBeBroken && isBreaking && !isDestroyed()) {
            TextureRegion breakingFrame = blockBreak.getBlockBreakAnimation().getCurrentFrame(0);
            batch.draw(breakingFrame, x, y, Block.BLOCK_SIZE, Block.BLOCK_SIZE);
        }
    }

    private TextureRegion getTextureRegion(SpriteSheets spriteSheets, TextureID textureID) {
        return indexBlock == -1 ? spriteSheets.getTextureRegion(textureID).orElseThrow() : spriteSheets.getTextureRegion(textureID, indexBlock).orElseThrow();
    }

    private void validateTextureID() {
        if (textureID == null) throw new IllegalStateException("TextureID is null, please set it before rendering");
    }

    public boolean isDestroyed() {
        return currentBlockHealth <= 0;
    }

    @Override
    public String toString() {
        return "Block{" + "texture=" + textureID + '}';
    }
}