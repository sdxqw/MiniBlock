package io.github.sdxqw.miniblock;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("MiniBlock");
        config.setIdleFPS(30);
        config.setResizable(false);
        config.setWindowedMode(1280, 720);
        new Lwjgl3Application(new MiniBlock(), config);
    }
}
