package com.anecoz.br.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuState extends State{
    private Texture background;

    public MenuState(GameStateManager gsm, SpriteBatch sb) {
        super(gsm, sb);
        _cam.setToOrtho(false, 800 / 2, 600 / 2);
        background = new Texture("badlogic.jpg");
    }

    @Override
    public void handleInput() {
        if(Gdx.input.justTouched()){
            _gsm.set(new PlayState(_gsm, _sb));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render() {
        _sb.setProjectionMatrix(_cam.combined);
        _sb.begin();
        _sb.draw(background, 0, 0);
        _sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        System.out.println("Menu State Disposed");
    }
}