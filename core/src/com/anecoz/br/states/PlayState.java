package com.anecoz.br.states;

import com.anecoz.br.level.Level;
import com.anecoz.br.logic.Player;
import com.anecoz.br.utils.ResourceHandler;
import com.anecoz.br.graphics.Camera;

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
        _cam.setToOrtho(false, Camera.WIN_SIZE_X, Camera.WIN_SIZE_Y);
    }

    private void init() {
        ResourceHandler.init();
        _level = new Level("maps/map_01.tmx", _sb);
        _player = new Player(ResourceHandler.PLAYER_TEXTURE, new Vector2(15, 15), .45f);
    }

    @Override
    protected void handleInput() {
        // Change these to translate the player position and NOT the Camera.
        /*if(Gdx.input.isKeyPressed(Input.Keys.W)){
            _cam.translate(0f, 1.0f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            _cam.translate(0f, -1.0f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            _cam.translate(-1.0f, 0f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
           _cam.translate(1.0f, 0f);
        }*/

        // Exit the playstate with escape
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            _gsm.set(new MenuState(_gsm, _sb));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();

        _cam.updateCam(dt, _player.getPosition().x, _player.getPosition().y);
        _cam.update();

        // If Game Over set this: _gsm.set(new MenuState(_gsm, _sb));
        _player.update(_cam, _level);
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