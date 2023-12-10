package io.github.sdxqw.miniblock.blocks;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BlockStack {
    private final List<Block> blocks;

    public BlockStack() {
        this.blocks = new ArrayList<>();
    }

    public void addBlock(Block block) {
        blocks.add(block);
    }

    public void removeTopBlock() {
        if (blocks.size() > 1) {
            blocks.remove(blocks.size() - 1);
        }
    }

    public Block getTopBlock() {
        return blocks.isEmpty() && !hasMoreThanOneBlock() ? null : blocks.get(blocks.size() - 1);
    }

    public boolean hasMoreThanOneBlock() {
        return blocks.size() > 1;
    }
}