package io.github.sdxqw.miniblock.biomes;

import io.github.sdxqw.miniblock.blocks.Block;
import io.github.sdxqw.miniblock.blocks.Dirt;
import io.github.sdxqw.miniblock.blocks.Grass;
import io.github.sdxqw.miniblock.blocks.Plants;

import java.util.ArrayList;
import java.util.List;

public class SimpleBiome extends Biome {

    @Override
    public List<Block> generateBlock(int x, int y) {
        List<Block> blocks = List.of(new Dirt(x, y), new Grass(x, y));
        List<Block> blockList = new ArrayList<>();
        blockList.add(blocks.get(Math.random() < 0.5 ? 0 : 1));
        if (blockList.get(0) instanceof Grass && Math.random() < 0.1) {
            blockList.add(new Plants(x, y, 1));
        }
        return blockList;
    }
}
