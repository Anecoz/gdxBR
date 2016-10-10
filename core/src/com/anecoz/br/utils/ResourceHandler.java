package com.anecoz.br.utils;

import com.badlogic.gdx.graphics.Texture;

public class ResourceHandler {
    public static Texture PLAYER_TEXTURE;

    static public void init() {
        PLAYER_TEXTURE = new Texture("characters/player.png");
    }

    static public void dispose() {
        PLAYER_TEXTURE.dispose();
    }
}
