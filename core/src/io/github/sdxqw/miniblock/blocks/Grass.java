package io.github.sdxqw.miniblock.blocks;

import io.github.sdxqw.miniblock.sprite.TextureID;
import lombok.Getter;

@Getter
public class Grass extends Block {
    public Grass() {
        setTextureID(TextureID.GRASS);
    }
}
