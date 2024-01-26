package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;

public class Hud {
    private int numberOfLives = 3;
    private SpriteBatch spriteBatch;
    private Texture objects = new Texture(Gdx.files.internal("objects.png"));
    private GameScreen gameScreen;
    private LoadMap loadMap;
    private TextureRegion key = new TextureRegion(objects, 2, 66, 10, 10);
    private TextureRegion notKey = new TextureRegion(objects, 15, 90, 17, 4);

    private TextureRegion hearts = new TextureRegion(objects, 64, 0, 16, 16);

    /**
     * Constructor initializes the variables
     * @param gameScreen Game class, used to access and write attributes and methods.
     * @param spriteBatch Takes the spritebatch
     * @param loadMap LoadMap class, used to access and write attributes and methods.
     */
    public Hud(GameScreen gameScreen,SpriteBatch spriteBatch,LoadMap loadMap) {
        this.gameScreen = gameScreen;
        this.loadMap = loadMap;
        this.spriteBatch = spriteBatch;
    }

    /**
     * Method responsible to lose a live.
     */
    public void loseLive() {
        numberOfLives -= 1;
    }

    /**
     * Method to show the lives of the character in the top left part of the screen
     */
    public void showLives() {
        spriteBatch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        switch (numberOfLives){
            case 3:
                spriteBatch.draw(hearts, Gdx.graphics.getWidth() / 15f, Gdx.graphics.getHeight() / 1.05f, 32, 32);
                spriteBatch.draw(hearts, Gdx.graphics.getWidth() / 15f+35, Gdx.graphics.getHeight() / 1.05f, 32, 32);
                spriteBatch.draw(hearts, Gdx.graphics.getWidth() / 15f+70, Gdx.graphics.getHeight() / 1.05f, 32, 32);
                break;
            case 2:
                spriteBatch.draw(hearts, Gdx.graphics.getWidth() / 15f, Gdx.graphics.getHeight() / 1.05f, 32, 32);
                spriteBatch.draw(hearts, Gdx.graphics.getWidth() / 15f+35, Gdx.graphics.getHeight() / 1.05f, 32, 32);
                break;
            case 1:
                spriteBatch.draw(hearts, Gdx.graphics.getWidth() / 15f, Gdx.graphics.getHeight() / 1.05f, 32, 32);
            default:
        }
    }

    /**
     * Method to show the key in the top left corner
     */
    public void showKey(){
        spriteBatch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        if (loadMap.isKeyCollected()){
            spriteBatch.draw(key, Gdx.graphics.getWidth() / 15f-50, Gdx.graphics.getHeight() / 1.05f, 32, 32);
        }else {
            spriteBatch.draw(notKey, Gdx.graphics.getWidth() / 15f-50, Gdx.graphics.getHeight() / 1.05f, 34, 8);
        }
    }

    public void dispose() {
    }

    public int getNumberOfLives() {
        return numberOfLives;
    }

    public void setNumberOfLives(int numberOfLives) {
        this.numberOfLives = numberOfLives;
    }
}