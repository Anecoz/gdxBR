package com.anecoz.br.states;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlayState extends State {
    private Texture bg;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, 800 / 2, 600 / 2);
        bg = new Texture("badlogic.jpg");
    }

    @Override
    protected void handleInput() {
    }

    @Override
    public void update(float dt) {
        handleInput();

        // If Game Over set this: gsm.set(new MenuState(gsm));

        cam.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(bg, cam.position.x - (cam.viewportWidth / 2), 0);
        sb.end();
    }

    @Override
    public void dispose() {
        bg.dispose();

        System.out.println("Play State Disposed");
    }
}