package com.anecoz.br.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class DrawableEntity {
    protected Texture _texture;
    protected Vector2 _pos;
    protected float _rotation;
    protected int _width;
    protected int _height;
    protected float _size;

    public DrawableEntity(Texture texture, Vector2 pos, float size) {
        _texture = texture;
        _pos = pos;
        _size = size;
        _width = _texture.getWidth();
        _height = _texture.getHeight();
        _rotation = 0.f;
    }

    public Vector2 getPosition() {
        return _pos;
    }

    public void setPosition(Vector2 pos) {
        _pos = pos;
    }

    public void render(SpriteBatch sb) {
        sb.draw(_texture,
                _pos.x, _pos.y,
                _size/2.0f, _size/2.0f,
                _size, _size,
                1.0f, 1.0f,
                _rotation,
                0, 0,
                _width, _height,
                false, false);
    }
}
