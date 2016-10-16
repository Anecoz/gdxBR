package com.anecoz.br.utils;

import com.badlogic.gdx.graphics.Texture;

public class ResourceHandler {
    public static Texture PLAYER_TEXTURE;
    public static Texture BULLET_TEXTURE;
    public static Texture RIFLE_TEXTURE;
    public static Texture BAD_LOGIC_TEXTURE;
    public static Texture INVENTORY_TEXTURE;

    static public void init() {
        PLAYER_TEXTURE = new Texture("characters/player.png");
        BULLET_TEXTURE = new Texture("weapons/bullet.png");
        RIFLE_TEXTURE = new Texture("weapons/assault_rifle_display.png");
        BAD_LOGIC_TEXTURE = new Texture("badlogic.jpg");
        INVENTORY_TEXTURE = new Texture("inventory.png");
    }

    static public void dispose() {
        PLAYER_TEXTURE.dispose();
        BULLET_TEXTURE.dispose();
        RIFLE_TEXTURE.dispose();
        INVENTORY_TEXTURE.dispose();
        BAD_LOGIC_TEXTURE.dispose();
    }
}
