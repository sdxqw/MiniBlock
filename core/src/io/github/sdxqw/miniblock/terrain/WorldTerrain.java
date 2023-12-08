package io.github.sdxqw.miniblock.terrain;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import io.github.sdxqw.miniblock.blocks.Block;
import io.github.sdxqw.miniblock.sprite.SpriteSheets;

import java.util.HashMap;
import java.util.Map;

import static io.github.sdxqw.miniblock.terrain.Chunk.CHUNK_SIZE;

public class WorldTerrain implements Disposable {
    private final Map<Vector2, Chunk> worldChunks = new HashMap<>();
    private final SpriteSheets spriteSheets;

    public WorldTerrain(int width, int height) {
        spriteSheets = new SpriteSheets("block/block.atlas");
        int worldWidth = width * CHUNK_SIZE;
        int worldHeight = height * CHUNK_SIZE;

        System.out.println("WORLD WIDTH: " + worldWidth + " WORLD HEIGHT: " + worldHeight);
        initializeWorld(worldWidth, worldHeight);
    }

    private void initializeWorld(int width, int height) {
        int numChunksX = width / CHUNK_SIZE;
        int numChunksY = height / CHUNK_SIZE;

        for (int x = 0; x < numChunksX; x++) {
            for (int y = 0; y < numChunksY; y++) {
                Vector2 chunkPosition = new Vector2(x, y);
                worldChunks.put(chunkPosition, new Chunk(x, y));
            }
        }
    }

    public Chunk getChunk(int x, int y) {
        int chunkX = Math.floorDiv(x, CHUNK_SIZE);
        int chunkY = Math.floorDiv(y, CHUNK_SIZE);
        Vector2 position = new Vector2(chunkX, chunkY);
        return worldChunks.get(position);
    }

    public void setBlock(int globalX, int globalY, Block block) {
        Chunk chunk = getChunk(globalX, globalY);
        if (chunk != null) {
            int localX = Math.floorMod(globalX, CHUNK_SIZE);
            int localY = Math.floorMod(globalY, CHUNK_SIZE);
            chunk.setBlock(localX, localY, block);
        }
    }


    public void renderWorld(SpriteBatch batch) {
        for (Chunk chunk : worldChunks.values()) {
            chunk.renderChunk(batch, spriteSheets);
        }
    }

    public void printChunksPos() {
        for (Chunk chunk : worldChunks.values()) {
            System.out.println("CHUNKS: " + chunk);
        }
    }

    @Override
    public void dispose() {
        spriteSheets.dispose();
    }
}
