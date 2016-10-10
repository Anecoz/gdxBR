package com.anecoz.br.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public abstract class State {
    protected OrthographicCamera _cam;
    protected Vector3 _mouse;
    protected GameStateManager _gsm;
    protected SpriteBatch _sb;

    public State(GameStateManager gsm, SpriteBatch sb){
        _gsm = gsm;
        _cam = new OrthographicCamera();
        _mouse = new Vector3();
        _sb = sb;
    }

    protected abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render();
    public abstract void dispose();
}