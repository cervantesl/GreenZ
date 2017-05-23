package com.mygdx.greenz;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.greenz.screens.GameScreen;

public class MainGame extends Game {

	public SpriteBatch batch;

	public static final float PPM = 100;

	public static final int V_WIDTH = 420; //450
	public static final int V_HEIGHT = 205; //220

	@Override
	public void create () {
		batch = new SpriteBatch();

		setScreen(new GameScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
