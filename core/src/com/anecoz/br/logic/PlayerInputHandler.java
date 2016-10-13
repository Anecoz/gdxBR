package com.anecoz.br.logic;

import com.anecoz.br.level.Level;
import com.anecoz.br.logic.collision.CollisionHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

// Handle input for the player, to clean up player code base
public class PlayerInputHandler {
    private Player _player;

    public PlayerInputHandler(Player player) {
        _player = player;
    }

    public void checkWeaponSwap() {
        // TODO
    }

    public void checkPickUp(Level level) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            // todo
        }
    }

    public void updateRotation(OrthographicCamera camera) {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.input.getY();
        float centerX = _player.getPosition().x + _player.getWidth()/2.0f;
        float centerY = _player.getPosition().y + _player.getHeight()/2.0f;
        Vector3 mouse = new Vector3(mouseX, mouseY, 0);
        camera.unproject(mouse);
        Vector2 tmp = new Vector2(mouse.x - centerX, mouse.y - centerY);
        tmp.nor();

        float angle = tmp.angle();

        _player.setRotation(angle - 90);
    }

    public void updateMovement(Level level) {
        Vector2 tmp = new Vector2();
        tmp.x = _player.getPosition().x;
        tmp.y = _player.getPosition().y;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            _player.getPosition().y += Player.SPEED;
            if (CollisionHandler.checkPlayerCollision(_player, level))
                _player.getPosition().y = tmp.y;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            _player.getPosition().y -= Player.SPEED;
            if (CollisionHandler.checkPlayerCollision(_player, level))
                _player.getPosition().y = tmp.y;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            _player.getPosition().x -= Player.SPEED;
            if (CollisionHandler.checkPlayerCollision(_player, level))
                _player.getPosition().x = tmp.x;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            _player.getPosition().x += Player.SPEED;
            if (CollisionHandler.checkPlayerCollision(_player, level))
                _player.getPosition().x = tmp.x;
        }
    }
}
