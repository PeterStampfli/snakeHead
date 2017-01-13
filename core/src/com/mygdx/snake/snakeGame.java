package com.mygdx.snake;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.snake.utilities.Basic;
import com.mygdx.snake.utilities.Device;
import com.mygdx.snake.utilities.SimpleLoader;

public class snakeGame extends Game {
	// general assets _> device
	public Device device;
	public SpriteBatch batch;
	public BitmapFont font;
	public SimpleLoader loader;




	Texture snakeHead;
	
	@Override
	public void create () {
		device=new Device().createSpriteBatch();
		device.setLogging(true);
		batch=device.spriteBatch;
		loader=device.loader;
		snakeHead = loader.getTexture("snakeHead.jpg");
	}

	@Override
	public void render () {
		Basic.clearBackground(Color.DARK_GRAY);
		batch.begin();
		batch.draw(snakeHead, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		device.dispose();
	}
}
