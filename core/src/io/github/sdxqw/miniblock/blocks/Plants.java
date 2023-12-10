package io.github.sdxqw.miniblock.blocks;

import io.github.sdxqw.miniblock.sprite.TextureID;

public class Plants extends Block {

    public Plants(int x, int y, int index) {
        super(x, y);
        setIndexBlock(index);
        setBlockHealth(20);
        setTextureID(TextureID.MINIPLANT);
        setCanBeBroken(true);
    }
}
