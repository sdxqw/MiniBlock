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

    public Block() {
        this.textureID = TextureID.AIR;
    }

    public void renderBlock(SpriteBatch batch, SpriteSheets spriteSheets, int x, int y) {
        if (this instanceof Air) return;

        if (textureID == null) throw new IllegalStateException("TextureID is null, please set it before rendering");

        Optional<TextureRegion> region = spriteSheets.getTextureRegion(textureID);
        if (region.isEmpty())
            throw new IllegalStateException("TextureRegion is null, please check if the textureID is valid");
        batch.draw(region.get(), x, y, Block.BLOCK_SIZE, Block.BLOCK_SIZE);
    }

    @Override
    public String toString() {
        return "Block{" + "textureID=" + textureID + '}';
    }
}

