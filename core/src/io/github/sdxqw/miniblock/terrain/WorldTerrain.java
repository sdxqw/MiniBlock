package io.github.sdxqw.miniblock.terrain;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Disposable;
import io.github.sdxqw.miniblock.animation.BlockBreakAnimation;
import io.github.sdxqw.miniblock.biomes.Biome;
import io.github.sdxqw.miniblock.biomes.SimpleBiome;
import io.github.sdxqw.miniblock.blocks.Block;
import io.github.sdxqw.miniblock.blocks.BlockBreak;
import io.github.sdxqw.miniblock.sprite.SpriteSheets;
import io.github.sdxqw.miniblock.world.WorldGame;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.sdxqw.miniblock.terrain.Chunk.CHUNK_SIZE;

@Getter
public class WorldTerrain implements Disposable {
    public static final int VIEW_DISTANCE = 3;
    private final Map<GridPoint2, Chunk> worldChunks = new HashMap<>();
    private final SpriteSheets blocks;

    private final BlockBreak blockBreak;
    private final WorldGame game;

    public WorldTerrain(WorldGame game, BlockBreakAnimation blockBreakAnimation) {
        this.game = game;
        this.blocks = new SpriteSheets("block/block.atlas");
        this.blockBreak = new BlockBreak(game, blockBreakAnimation);
    }

    public void generateWorld(int x, int y) {
        int chunkX = x / CHUNK_SIZE;
        int chunkY = y / CHUNK_SIZE;

        List<Biome> biomes = List.of(new SimpleBiome());
        if (worldChunks.get(new GridPoint2(chunkX, chunkY)) != null) return;
        for (Biome biome : biomes) {
            Chunk chunk = new Chunk(x, y, biome, game);
            worldChunks.put(new GridPoint2(chunkX, chunkY), chunk);
        }
    }

    public Chunk getChunk(int x, int y) {
        int chunkX = x / CHUNK_SIZE;
        int chunkY = y / CHUNK_SIZE;
        GridPoint2 position = new GridPoint2(chunkX, chunkY);
        return worldChunks.get(position);
    }

    public void setBlock(int globalX, int globalY, Block block) {
        Chunk chunk = getChunk(globalX, globalY);
        if (chunk != null) {
            if (chunk.getBlock(globalX, globalY) != null) {
                chunk.setBlock(globalX, globalY, block);
            }
        }
    }

    public void renderWorld(SpriteBatch batch, Camera camera) {
        for (Chunk chunk : worldChunks.values()) {
            int chunkX = chunk.getChunkX() * CHUNK_SIZE;
            int chunkY = chunk.getChunkY() * CHUNK_SIZE;

            if (chunkX >= 0 && chunkY >= 0 && camera.frustum.boundsInFrustum(chunkX, chunkY, 0, (float) CHUNK_SIZE / 2 * VIEW_DISTANCE, (float) CHUNK_SIZE / 2 * VIEW_DISTANCE, 0)) {
                chunk.renderChunk(batch, blocks);
            }
        }
    }

    @Override
    public void dispose() {
        blocks.dispose();
    }
}
