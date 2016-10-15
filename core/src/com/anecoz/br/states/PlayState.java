package com.anecoz.br.states;

import com.anecoz.br.EntityManager;
import com.anecoz.br.utils.ResourceHandler;
import com.anecoz.br.graphics.Camera;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlayState extends State {
    private Engine _engine;
    private EntityManager _entityManager;

    public PlayState(GameStateManager gsm, SpriteBatch sb) {
        super(gsm, sb);
        _cam.setToOrtho(false, Camera.WIN_SIZE_X, Camera.WIN_SIZE_Y);
        init();
    }

    private void init() {
        ResourceHandler.init();
        _engine = new Engine();
        _entityManager = new EntityManager(_engine, _sb, _cam);
    }

    @Override
    protected void handleInput() {
        // Exit the playstate with escape
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            _gsm.set(new MenuState(_gsm, _sb));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();

        _engine.update(dt);

        // Fps counter
        Gdx.graphics.setTitle("Epic BR Game. FPS: " + Integer.toString(Gdx.graphics.getFramesPerSecond()));
    }

    @Override
    public void render() {
    }

    @Override
    public void dispose() {
        ResourceHandler.dispose();
        System.out.println("Play State Disposed");
    }
}