package com.anecoz.br.logic;

import com.anecoz.br.level.Level;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class DrawableEntity {
    protected Texture _texture;
    protected Vector2 _pos;
    protected float _rotation;
    protected float _width;
    protected float _height;
    protected float _scale;

    public DrawableEntity(Texture texture, Vector2 pos, float scale) {
        _texture = texture;
        _pos = pos;
        _scale = scale;
        _width = _scale * _texture.getWidth() * Level.PIX_TO_WORLD_FACTOR;
        _height = _scale * _texture.getHeight() * Level.PIX_TO_WORLD_FACTOR;
        _rotation = 0.f;
    }

    public Vector2 getPosition() { return _pos; }
    public float getWidth() {return _width;}
    public float getHeight() {return _height;}

    public void setPosition(Vector2 pos) { _pos = pos; }
    public void setRotation(float rotation) { _rotation = rotation; }

    public void render(SpriteBatch sb) {
        sb.draw(_texture,
                _pos.x, _pos.y,
                _width/2.0f, _height/2.0f,
                _width, _height,
                1.0f, 1.0f,
                _rotation,
                0, 0,
                _texture.getWidth(), _texture.getHeight(),
                false, false);
    }
}
