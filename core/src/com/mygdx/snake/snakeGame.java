package com.mygdx.snake;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.snake.utilities.Basic;
import com.mygdx.snake.utilities.Device;
import com.mygdx.snake.utilities.SimpleLoader;

public class snakeGame extends Game {
	// general assets _> device
	public Device device;

	public GameScreen gameScreen;



	@Override
	public void create () {
		device=new Device().createSpriteBatch();
		device.setLogging(true);
		gameScreen=new GameScreen(this);
		setScreen(gameScreen);
	}


	@Override
	public void dispose () {
		device.dispose();
	}
}
