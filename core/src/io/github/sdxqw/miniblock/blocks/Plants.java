package io.github.sdxqw.miniblock.blocks;

import io.github.sdxqw.miniblock.sprite.TextureID;

import java.util.List;

public class Plants extends Block {

    public Plants(int index) {
        List<TextureID> plants = List.of(TextureID.MINIPLANT1, TextureID.MINIPLANT2, TextureID.MINIPLANT3);
        if (index < 0 || index >= plants.size()) {
            throw new IllegalArgumentException("index must be between 0 and " + (plants.size() - 1));
        }
        setTextureID(plants.get(index));
    }
}
