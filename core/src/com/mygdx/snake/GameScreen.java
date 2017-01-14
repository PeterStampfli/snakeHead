package com.mygdx.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.snake.utilities.Basic;
import com.mygdx.snake.utilities.Device;
import com.mygdx.snake.utilities.SimpleLoader;

/**
 * Created by peter on 1/13/17.
 */


public class GameScreen extends ScreenAdapter {

    public snakeGame game;
    public Device device;
    public SpriteBatch batch;
    public SimpleLoader loader;

    private Texture snakeHead;
    private Texture apple;
    private boolean hasApple=false;
    private Vector2 applePosition=new Vector2();

    private static final float SNAKE_STEP=32;
    private Vector2 headPosition=new Vector2(0,0);
    private Vector2 headStep=new Vector2();

    private final static float MOVE_TIME=0.2f;
    private float time=MOVE_TIME;

    public GameScreen(snakeGame game){
        this.game=game;
        device=game.device;
        batch=device.spriteBatch;
        loader=device.loader;
        snakeHead = loader.getTexture("snakehead.png");
        apple=loader.getTexture("apple.png");
    }

    private boolean isKeyPressed(int key){
        return Gdx.input.isKeyPressed(key);
    }

    private void checkApple(){
        if (!hasApple){
            hasApple=true;
            do {
                applePosition.x= SNAKE_STEP*MathUtils.random(Math.round(Gdx.graphics.getWidth()/SNAKE_STEP)-1);
                applePosition.y= SNAKE_STEP*MathUtils.random(Math.round(Gdx.graphics.getHeight()/SNAKE_STEP)-1);
            } while (applePosition.epsilonEquals(headPosition,1));
        }
    }

    private void checkAppleCollision(){
        if (headPosition.epsilonEquals(applePosition,1)){
            hasApple=false;
        }
    }


    private boolean isGoodHeadPosition(){
        if ((headPosition.x<-1)||(headPosition.x>Gdx.graphics.getWidth()-1)
                ||(headPosition.y<-1)||(headPosition.y>Gdx.graphics.getHeight()-1)){
            return false;
        }
        return true;
    }


    private void moveSnake(){
        if (isKeyPressed(Input.Keys.LEFT)) {
            headStep.set(-SNAKE_STEP,0);
        }
        else if (isKeyPressed(Input.Keys.RIGHT)) {
            headStep.set(SNAKE_STEP,0);
        }
        else if (isKeyPressed(Input.Keys.DOWN)) {
            headStep.set(0,-SNAKE_STEP);
        }
        else if (isKeyPressed(Input.Keys.UP)) {
            headStep.set(0,SNAKE_STEP);
        }
        else return;
        headPosition.add(headStep);
        if (isGoodHeadPosition()){

        }
        else {
            headPosition.sub(headStep);
        }
    }

    @Override

    public void render (float delta) {

        time-=delta;
        if (time<0) {
            time=MOVE_TIME;
            moveSnake();
        }
        checkAppleCollision();
        checkApple();
        Basic.clearBackground(Color.DARK_GRAY);
        batch.begin();
        batch.draw(snakeHead, headPosition.x, headPosition.y);
        if (hasApple){
            batch.draw(apple,applePosition.x,applePosition.y);
        }
        batch.end();
    }

}
