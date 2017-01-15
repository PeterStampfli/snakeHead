package com.mygdx.snake;

import com.badlogic.gdx.Game;
import com.mygdx.snake.utilities.Device;

public class snakeGame extends Game {
	// general assets _> device
	public Device device;

	public GameScreen gameScreen;



	@Override
	public void create () {
		device=new Device().createSpriteBatch().createShapeRenderer().createBitmapFont();
		device.setLogging(true);
		gameScreen=new GameScreen(this);
		setScreen(gameScreen);
	}


	@Override
	public void dispose () {
		device.dispose();
	}
}
