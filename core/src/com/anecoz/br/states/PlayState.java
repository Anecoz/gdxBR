package com.anecoz.br.states;

import com.anecoz.br.level.Level;
import com.anecoz.br.logic.Player;
import com.anecoz.br.utils.ResourceHandler;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class PlayState extends State {
    private Level _level;
    private Player _player;

    public PlayState(GameStateManager gsm, SpriteBatch sb) {
        super(gsm, sb);

        init();
        _cam.setToOrtho(false, Level.MAP_WIDTH, Level.MAP_HEIGHT);
    }

    private void init() {
        ResourceHandler.init();
        _level = new Level("maps/map_01.tmx", _sb);
        _player = new Player(ResourceHandler.PLAYER_TEXTURE, new Vector2(0, 0), 1.0f);
    }

    @Override
    protected void handleInput() {
    }

    @Override
    public void update(float dt) {
        handleInput();

        // If Game Over set this: gsm.set(new MenuState(gsm));

        _cam.update();
        _player.update();
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