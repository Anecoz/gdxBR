package com.anecoz.br.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuState extends State{
    private Texture background;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, 800 / 2, 600 / 2);
        background = new Texture("badlogic.jpg");
    }

    @Override
    public void handleInput() {
        if(Gdx.input.justTouched()){
            gsm.set(new PlayState(gsm));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(background, 0,0);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        System.out.println("Menu State Disposed");
    }
}