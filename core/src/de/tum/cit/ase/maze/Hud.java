package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Hud {
    private int numberOfLives = 3;
    private SpriteBatch spriteBatch;
    private Texture objects = new Texture(Gdx.files.internal("objects.png"));
    private GameScreen gameScreen;

    public Hud(GameScreen gameScreen, SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        this.gameScreen = gameScreen;
    }

    public void loseLive() {
        numberOfLives -= 1;
    }
    public void showLives() {
            TextureRegion hearts = new TextureRegion(objects, 64, 0, 16, 16);
            OrthographicCamera camera = gameScreen.getCamera();
            switch (numberOfLives){
                case 3:
                    spriteBatch.draw(hearts, gameScreen.getCharacterX()-40, gameScreen.getCharacterY()+60, 8, 8);
                    spriteBatch.draw(hearts, 0, -3, 8, 8);
                    spriteBatch.draw(hearts, 0, -3, 8, 8);
        }


    }

    public void dispose() {
    }
}
