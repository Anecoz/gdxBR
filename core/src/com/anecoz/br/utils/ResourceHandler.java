package com.anecoz.br.utils;

import com.badlogic.gdx.graphics.Texture;

public class ResourceHandler {
    public static Texture PLAYER_TEXTURE;
    public static Texture BULLET_TEXTURE;

    static public void init() {
        PLAYER_TEXTURE = new Texture("characters/player.png");
        BULLET_TEXTURE = new Texture("weapons/bullet.png");
    }

    static public void dispose() {
        PLAYER_TEXTURE.dispose();
        BULLET_TEXTURE.dispose();
    }
}
