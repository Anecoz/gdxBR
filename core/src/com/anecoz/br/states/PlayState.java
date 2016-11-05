package com.anecoz.br.states;

import com.anecoz.br.EntityManager;
import com.anecoz.br.network.client.ClientMasterHandler;
import com.anecoz.br.network.client.ClientSender;
import com.anecoz.br.systems.CameraSystem;
import com.anecoz.br.utils.ResourceHandler;
import com.anecoz.br.graphics.Camera;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.io.IOException;

public class PlayState extends State {
    private Engine _engine;
    private EntityManager _entityManager;

    public PlayState(GameStateManager gsm, SpriteBatch sb, String name) {
        super(gsm, sb);
        _cam.setToOrtho(false, CameraSystem.WIN_SIZE_X, CameraSystem.WIN_SIZE_Y);
        init(name);
    }

    private void init(String name) {
        ResourceHandler.init();
        _engine = new Engine();
        _entityManager = new EntityManager(_engine, _sb, _cam);
        ClientMasterHandler.init();
        ClientSender.registerPlayer(name, new Vector2(2, 2));
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            ClientMasterHandler._client.stop();

            _engine.removeAllEntities();
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
        ClientSender.disconnectPlayer();
        System.out.println("Play State Disposed");
    }
}