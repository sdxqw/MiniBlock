package io.github.sdxqw.miniblock.terrain;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import io.github.sdxqw.miniblock.biomes.Biome;
import io.github.sdxqw.miniblock.blocks.Block;
import io.github.sdxqw.miniblock.blocks.BlockBreak;
import io.github.sdxqw.miniblock.blocks.BlockStack;
import io.github.sdxqw.miniblock.sprite.SpriteSheets;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Chunk {

    public static final int CHUNK_SIZE = 16;
    private final Map<GridPoint2, BlockStack> chunkBlocks = new HashMap<>();
    private final int chunkX;
    private final int chunkY;

    public Chunk(int chunkX, int chunkY, Biome biome) {
        this.chunkX = chunkX / CHUNK_SIZE;
        this.chunkY = chunkY / CHUNK_SIZE;
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                GridPoint2 position = new GridPoint2(x, y);
                BlockStack blockStack = new BlockStack();
                blockStack.getBlocks().addAll(biome.generateBlock(x, y));
                chunkBlocks.put(position, blockStack);
            }
        }
    }

    public void setBlock(int x, int y, Block block) {
        int blockX = x - (x / CHUNK_SIZE) * CHUNK_SIZE;
        int blockY = y - (y / CHUNK_SIZE) * CHUNK_SIZE;
        GridPoint2 position = new GridPoint2(blockX, blockY);
        BlockStack blockStack = chunkBlocks.get(position);
        if (blockStack != null) {
            blockStack.getBlocks().clear();
            blockStack.addBlock(block);
        }
    }

    public BlockStack getBlock(int x, int y) {
        int blockX = x - (x / CHUNK_SIZE) * CHUNK_SIZE;
        int blockY = y - (y / CHUNK_SIZE) * CHUNK_SIZE;

        GridPoint2 position = new GridPoint2(blockX, blockY);
        BlockStack blockStack = chunkBlocks.get(position);
        return blockStack != null ? blockStack : new BlockStack();
    }

    public void renderChunk(SpriteBatch batch, BlockBreak animation, SpriteSheets spriteSheets) {
        for (Map.Entry<GridPoint2, BlockStack> entry : chunkBlocks.entrySet()) {
            GridPoint2 position = entry.getKey();
            BlockStack blockStack = entry.getValue();
            int blockX = position.x + chunkX * CHUNK_SIZE;
            int blockY = position.y + chunkY * CHUNK_SIZE;
            for (Block block : blockStack.getBlocks()) {
                block.renderBlock(batch, animation, spriteSheets, blockX, blockY);
            }
        }
    }

    @Override
    public String toString() {
        return "Chunk Position: " + chunkX + ", " + chunkY + " " + chunkBlocks;
    }
}