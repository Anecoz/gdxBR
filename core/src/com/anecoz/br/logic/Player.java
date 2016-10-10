package com.anecoz.br.logic;

import com.anecoz.br.level.Level;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Player extends DrawableEntity {
    public static float SPEED = 0.06f;

    private PlayerInputHandler _inputHandler;

    public Player(Texture texture, Vector2 pos, float size) {
        super(texture, pos, size);

        _inputHandler = new PlayerInputHandler(this);
    }

    public void update(OrthographicCamera camera, Level level) {
        _inputHandler.updateRotation(camera);
        _inputHandler.uptadeMovement(level);
    }
}
