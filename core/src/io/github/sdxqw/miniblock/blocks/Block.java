package io.github.sdxqw.miniblock.blocks;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.sdxqw.miniblock.sprite.SpriteSheets;
import io.github.sdxqw.miniblock.sprite.TextureID;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Setter
@Getter
public class Block {
    public static final int BLOCK_SIZE = 1;
    private TextureID textureID;
    private float blockHealth;
    private int x;
    private int y;

    private boolean isBreaking = false;
    private boolean canBeBroken = false;

    public Block(int x, int y) {
        this.x = x;
        this.y = y;
        this.blockHealth = 1;
        this.textureID = TextureID.AIR;
    }

    public void decreaseHealth(float amount, float deltaTime) {
        blockHealth -= amount * deltaTime;
        if (blockHealth <= 0) blockHealth = 0;
    }

    public void renderBlock(SpriteBatch batch, BlockBreakAnimation blockBreakAnimation, SpriteSheets spriteSheets, int x, int y) {
        if (this instanceof Air) return;

        if (textureID == null) throw new IllegalStateException("TextureID is null, please set it before rendering");

        Optional<TextureRegion> region = spriteSheets.getTextureRegion(textureID);
        if (region.isEmpty())
            throw new IllegalStateException("TextureRegion is null, please check if the textureID is valid");
        batch.draw(region.get(), x, y, Block.BLOCK_SIZE, Block.BLOCK_SIZE);

        if (!canBeBroken) return;

        if (blockBreakAnimation != null && isBreaking) {
            blockBreakAnimation.setFrameDuration(blockHealth);
            TextureRegion breakingFrame = blockBreakAnimation.getCurrentFrame();
            batch.draw(breakingFrame, x, y, Block.BLOCK_SIZE, Block.BLOCK_SIZE);
        }
    }


    public boolean isDestroyed() {
        return blockHealth <= 0;
    }

    @Override
    public String toString() {
        return "Block{" + "texture=" + textureID + '}';
    }
}

