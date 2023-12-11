package io.github.sdxqw.miniblock.blocks;

import io.github.sdxqw.miniblock.sprite.TextureID;
import io.github.sdxqw.miniblock.world.WorldGame;

public class Plants extends Block {

    public Plants(int x, int y, int index, WorldGame game) {
        super(x, y, game);
        setIndexBlock(index);
        setBlockHealth(5);
        setCurrentBlockHealth(getBlockHealth());
        setTextureID(TextureID.MINIPLANT);
        setCanBeBroken(true);
    }
}
