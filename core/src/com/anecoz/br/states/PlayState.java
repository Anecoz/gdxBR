package com.anecoz.br.states;

import com.anecoz.br.level.Level;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlayState extends State {
    private Level _level;

    public PlayState(GameStateManager gsm, SpriteBatch sb) {
        super(gsm, sb);

        init();
        _cam.setToOrtho(false, Level.MAP_WIDTH, Level.MAP_HEIGHT);
    }

    private void init() {
        _level = new Level("maps/map_01.tmx", _sb);
    }

    @Override
    protected void handleInput() {
    }

    @Override
    public void update(float dt) {
        handleInput();

        // If Game Over set this: gsm.set(new MenuState(gsm));

        _cam.update();
    }

    @Override
    public void render() {
        _sb.setProjectionMatrix(_cam.combined);
        _sb.begin();
        _level.render(_cam);
        _sb.end();
    }

    @Override
    public void dispose() {

        System.out.println("Play State Disposed");
    }
}