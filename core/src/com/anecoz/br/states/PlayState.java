package com.anecoz.br.states;

import com.anecoz.br.level.Level;
import com.anecoz.br.logic.Player;
import com.anecoz.br.utils.ResourceHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class PlayState extends State {
    private Level _level;
    private Player _player;

    public PlayState(GameStateManager gsm, SpriteBatch sb) {
        super(gsm, sb);
        init();
        _cam.setToOrtho(false, Level.MAP_WIDTH, Level.MAP_WIDTH);
    }

    private void init() {
        ResourceHandler.init();
        _level = new Level("maps/map_01.tmx", _sb);
        _player = new Player(ResourceHandler.PLAYER_TEXTURE, new Vector2(0, 0), 1.0f);
    }

    @Override
    protected void handleInput() {
        // Change these to translate the player position and NOT the Camera.
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            _cam.translate(0f, 1.0f);
            System.out.println(_cam.position);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            _cam.translate(0f, -1.0f);
            System.out.println(_cam.position);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            _cam.translate(-1.0f, 0f);
            System.out.println(_cam.position);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
           _cam.translate(1.0f, 0f);
            System.out.println(_cam.position);
        }
    }

    @Override
    public void update(float dt) {
        handleInput();

        //_cam.updateCam(dt, playerPosX, playerPosY); // This should be used when we have a player.
        _cam.update();
        _player.update();

        // If Game Over set this: gsm.set(new MenuState(gsm));
    }

    @Override
    public void render() {
        _sb.setProjectionMatrix(_cam.combined);
        _sb.begin();
        _level.render(_cam);
        _player.render(_sb);
        _sb.end();
    }

    @Override
    public void dispose() {
        ResourceHandler.dispose();
        System.out.println("Play State Disposed");
    }
}