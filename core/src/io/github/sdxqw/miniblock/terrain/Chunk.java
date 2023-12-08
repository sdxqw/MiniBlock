package io.github.sdxqw.miniblock.terrain;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.sdxqw.miniblock.blocks.Block;
import io.github.sdxqw.miniblock.blocks.Dirt;
import io.github.sdxqw.miniblock.sprite.SpriteSheets;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


@Getter
public class Chunk {

    public static final int CHUNK_SIZE = 16;
    private final Map<Vector2, Block> blocks = new HashMap<>();

    public Chunk(int chunkX, int chunkY) {
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                int blockX = chunkX * CHUNK_SIZE + x;
                int blockY = chunkY * CHUNK_SIZE + y;
                blocks.put(new Vector2(blockX, blockY), new Dirt());
            }
        }
    }

    public void renderChunk(SpriteBatch batch, SpriteSheets spriteSheets) {
        for (Map.Entry<Vector2, Block> entry : blocks.entrySet()) {
            Vector2 position = entry.getKey();
            Block block = entry.getValue();
            block.renderBlock(batch, spriteSheets, (int) position.x, (int) position.y);
        }
    }

    public void setBlock(int x, int y, Block block) {
        blocks.put(new Vector2(x, y), block);
    }

    @Override
    public String toString() {
        return "Chunk Position: " + "x=" + blocks.keySet().stream().findFirst().get().x + ", y=" + blocks.keySet().stream().findFirst().get().y + ", blocks=" + blocks + '}';
    }
}
