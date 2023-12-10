package io.github.sdxqw.miniblock.biomes;

import io.github.sdxqw.miniblock.blocks.Block;

import java.util.List;

public abstract class Biome {
    public abstract List<Block> generateBlock(int x, int y);
}
