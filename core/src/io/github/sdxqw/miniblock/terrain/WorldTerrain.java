package io.github.sdxqw.miniblock.terrain;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Disposable;
import io.github.sdxqw.miniblock.animation.CustomAnimation;
import io.github.sdxqw.miniblock.biomes.Biome;
import io.github.sdxqw.miniblock.biomes.SimpleBiome;
import io.github.sdxqw.miniblock.blocks.Block;
import io.github.sdxqw.miniblock.blocks.BlockBreakAnimation;
import io.github.sdxqw.miniblock.sprite.SpriteSheets;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.sdxqw.miniblock.terrain.Chunk.CHUNK_SIZE;

@Getter
public class WorldTerrain implements Disposable {
    public static final int VIEW_DISTANCE = 3;
    private final Map<GridPoint2, Chunk> worldChunks = new HashMap<>();
    private final SpriteSheets spriteSheets;
    private final SpriteSheets spriteSheets2;

    private final BlockBreakAnimation blockBreakAnimation;
    private final CustomAnimation customAnimation;

    public WorldTerrain() {
        spriteSheets = new SpriteSheets("block/block.atlas");
        spriteSheets2 = new SpriteSheets("block/block_breaking.atlas");
        customAnimation = new CustomAnimation(spriteSheets2);
        Animation<TextureRegion> breaking = new Animation<>(0.1f, spriteSheets2.getAtlas().findRegions("destroy_stage"));
        customAnimation.addAnimation(List.of(breaking));
        blockBreakAnimation = new BlockBreakAnimation(customAnimation, spriteSheets2);
    }

    public void generateWorld(int x, int y) {
        int chunkX = x / CHUNK_SIZE;
        int chunkY = y / CHUNK_SIZE;

        List<Biome> biomes = List.of(new SimpleBiome());
        if (worldChunks.get(new GridPoint2(chunkX, chunkY)) != null) return;
        for (Biome biome : biomes) {
            Chunk chunk = new Chunk(x, y, biome);
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
                chunk.renderChunk(batch, blockBreakAnimation, spriteSheets);
            }
        }
    }

    @Override
    public void dispose() {
        spriteSheets.dispose();
        spriteSheets2.dispose();
    }
}
