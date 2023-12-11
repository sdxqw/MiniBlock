package io.github.sdxqw.miniblock.blocks;

import io.github.sdxqw.miniblock.sprite.TextureID;
import lombok.Getter;

@Getter
public class Grass extends Block {
    public Grass(int x, int y) {
        super(x, y, null);
        setTextureID(TextureID.GRASS);
    }
}
