package com.anecoz.br;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.anecoz.br.states.GameStateManager;
import com.anecoz.br.states.MenuState;

public class BRGame extends ApplicationAdapter {
	private GameStateManager _gsm;
	private SpriteBatch _batch;
	
	@Override
	public void create () {
		_batch = new SpriteBatch();
		_gsm = new GameStateManager();

		_gsm.push(new MenuState(_gsm, _batch));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		_gsm.update(Gdx.graphics.getDeltaTime());
		_gsm.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
	}
}
