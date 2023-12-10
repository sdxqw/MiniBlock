package io.github.sdxqw.miniblock.animation;

import io.github.sdxqw.miniblock.sprite.SpriteSheets;
import io.github.sdxqw.miniblock.sprite.TextureID;

import java.util.List;

public class PlayerAnimation extends CustomAnimation {
    public PlayerAnimation() {
        super(new SpriteSheets("player/player.atlas"));
        createAnimation();
    }

    @Override
    public void createAnimation() {
        addAnimation(List.of(loadAnimation(TextureID.IDLE, 6),
                loadAnimation(TextureID.IDLE_BEHIND, 6),
                loadAnimation(TextureID.IDLE_RIGHT, 6),
                loadAnimation(TextureID.IDLE_LEFT, 6)));
    }
}
