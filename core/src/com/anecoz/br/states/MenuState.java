package com.anecoz.br.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuState extends State{
    private Texture background;

    private String _name = "Anecoz";
    private boolean _launch = false;

    public MenuState(GameStateManager gsm, SpriteBatch sb) {
        super(gsm, sb);
        _cam.setToOrtho(false, 1280, 720);
        background = new Texture("badlogic.jpg");

        MyTextInputListener listener = new MyTextInputListener();
        Gdx.input.getTextInput(listener, "Enter name", _name, "");
    }

    public class MyTextInputListener implements Input.TextInputListener {
        @Override
        public void input (String text) {
            _name = text;
            _launch = true;
        }

        @Override
        public void canceled () {
            _launch = true;
        }
    }

    @Override
    public void handleInput() {
        if(_launch){
            _gsm.set(new PlayState(_gsm, _sb, _name));
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