package com.mygdx.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
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
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    private Texture snakeHead;
    private Texture apple;
    private boolean hasApple=false;
    private Vector2 applePosition=new Vector2();

    private static final float SNAKE_STEP=32;
    private Vector2 headPosition=new Vector2(0,0);
    private Vector2 oldHeadPosition=new Vector2();
    private Vector2 headStep=new Vector2(0,0);
    private Vector2 oldHeadStep=new Vector2(0,0);
    private Array<Vector2> bodyPartPositions=new Array<Vector2>();
    private Texture bodyPart;

    private final static float MOVE_TIME=0.4f;
    private float time=MOVE_TIME;

    private  int direction=0;
    private static final int UP=1;
    private static final int DOWN=2;
    private static final int RIGHT=3;
    private static final int LEFT=4;

    private enum State{
        PLAYING,GAME_OVER
    }
    private State state=State.PLAYING;
    private String text="Game over ... tap space to restart!";

    private GlyphLayout layout=new GlyphLayout();
    private int score=0;
    private int maxScore=0;
    private static final float WORLD_WIDTH=640;
    private static final float WORLD_HEIGHT=480;
    private Viewport viewport;


    public GameScreen(snakeGame game){
        this.game=game;
        device=game.device;
        batch=device.spriteBatch;
        loader=device.loader;
        shapeRenderer=device.shapeRenderer;
        snakeHead = loader.getTexture("snakehead.png");
        apple=loader.getTexture("apple.png");
        bodyPart=loader.getTexture("snakebody.png");
        font=device.bitmapFont;
        state=State.GAME_OVER;
        viewport=new FitViewport(WORLD_WIDTH,WORLD_HEIGHT);

    }

    private boolean isKeyPressed(int key){
        return Gdx.input.isKeyPressed(key);
    }

    private void drawGrid(){
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.setColor(0,0,1,1);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (int i=0;i<WORLD_WIDTH;i+=SNAKE_STEP){
            for (int j=0;j<WORLD_HEIGHT;j+=SNAKE_STEP){
                shapeRenderer.rect(i,j,SNAKE_STEP,SNAKE_STEP);
            }
        }
        shapeRenderer.end();
    }

    private void checkApple(){
        if (!hasApple){
            boolean wrongPosition=true;
            hasApple=true;
            while (wrongPosition){
                applePosition.x= SNAKE_STEP*MathUtils.random(Math.round(WORLD_WIDTH/SNAKE_STEP)-1);
                applePosition.y= SNAKE_STEP*MathUtils.random(Math.round(WORLD_HEIGHT/SNAKE_STEP)-1);
                wrongPosition=applePosition.epsilonEquals(headPosition,1);
                for (Vector2 bodyPartPosition:bodyPartPositions){
                    if (applePosition.epsilonEquals(bodyPartPosition,1)){
                        wrongPosition=true;
                        break;
                    }
                }
            }
        }
    }

    private void checkAppleCollision(){
        if (headPosition.epsilonEquals(applePosition,1)){
            hasApple=false;
            bodyPartPositions.insert(0,new Vector2());     // add body part, not yet placed
            score++;
        }
    }


    private boolean isGoodHeadPosition(){
        if ((headPosition.x<-1)||(headPosition.x>WORLD_WIDTH-1)
                ||(headPosition.y<-1)||(headPosition.y>WORLD_HEIGHT-1)){
            return false;
        }
        return true;
    }

    private void checkCollision(){
        for (Vector2 bodyPartPosition:bodyPartPositions){
            if (bodyPartPosition.epsilonEquals(headPosition,1)){
                state=State.GAME_OVER;
                maxScore=Math.max(maxScore,score);
            }
        }
    }

    private void checkInput(){
        if (isKeyPressed(Input.Keys.LEFT)&&(direction!=RIGHT)) {
            headStep.set(-SNAKE_STEP,0);
            direction=LEFT;
        }
        else if (isKeyPressed(Input.Keys.RIGHT)&&(direction!=LEFT)) {
            headStep.set(SNAKE_STEP,0);
            direction=RIGHT;
        }
        else if (isKeyPressed(Input.Keys.DOWN)&&(direction!=UP)) {
            headStep.set(0,-SNAKE_STEP);
            direction=DOWN;
        }
        else if (isKeyPressed(Input.Keys.UP)&&(direction!=DOWN)) {
            headStep.set(0,SNAKE_STEP);
            direction=UP;
        }
        if (bodyPartPositions.size==0){
            direction=0;
        }

    }

    private void moveSnake(){
        Vector2 bodyPartPosition;
        oldHeadPosition.set(headPosition);
        headPosition.add(headStep);
        if (isGoodHeadPosition()){                             // we made a step
            checkAppleCollision();                            // can collide with apple
            if (bodyPartPositions.size>0){                     // move body if there is one
                bodyPartPosition=bodyPartPositions.removeIndex(0);
                bodyPartPosition.set(oldHeadPosition);
                bodyPartPositions.add(bodyPartPosition);

            }
        }
        else {    // set back position of head
            headPosition.set(oldHeadPosition);
        }
    }
    @Override
    public void resize(int width,int height){
        viewport.update(width, height);
    }


    @Override
    public void render (float delta) {
        switch (state){
            case PLAYING:
                time-=delta;
                checkInput();
                if (time<0) {
                    time=MOVE_TIME;
                    moveSnake();
                    checkCollision();
                }
                checkApple();

                break;
            case GAME_OVER:
                if (isKeyPressed(Input.Keys.SPACE)){
                    state=State.PLAYING;
                    bodyPartPositions.clear();
                    direction=0;
                    time=MOVE_TIME;
                    headStep.set(0,0);
                    score=0;
                }


                break;
        }
        viewport.apply(true);
        Basic.clearBackground(Color.DARK_GRAY);
        drawGrid();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        for (Vector2 bodyPartPosition:bodyPartPositions){
            batch.draw(bodyPart,bodyPartPosition.x,bodyPartPosition.y);
        }
        batch.draw(snakeHead, headPosition.x, headPosition.y);
        if (hasApple){
            batch.draw(apple,applePosition.x,applePosition.y);
        }
        if (state==State.GAME_OVER){
            text="Game over ... tap space to restart!";
            layout.setText(font,text);
            float layoutHeight=layout.height;
            font.draw(batch,text,(WORLD_WIDTH-layout.width)/2,
                    (WORLD_HEIGHT-layout.height)/2);
            text="Score : "+score+"     Highest score: "+maxScore;
            layout.setText(font,text);
            font.draw(batch,text,(WORLD_WIDTH-layout.width)/2,
                    (WORLD_HEIGHT-layout.height)/2-2*layoutHeight);

        }
        else {
            text="score: "+score;
            layout.setText(font,text);
            font.draw(batch,text,(WORLD_WIDTH-layout.width)/2,
                    (WORLD_HEIGHT-layout.height));

        }

        batch.end();
    }

}
