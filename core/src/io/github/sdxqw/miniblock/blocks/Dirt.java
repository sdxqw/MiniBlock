package io.github.sdxqw.miniblock.blocks;

import io.github.sdxqw.miniblock.sprite.TextureID;
import lombok.Getter;

@Getter
public class Dirt extends Block {
    public Dirt() {
        setTextureID(TextureID.DIRT);
    }
}
