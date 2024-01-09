package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private final OrthographicCamera camera;
    private final BitmapFont font;
    private float stateTime = 0f;
    private float characterX = 0;
    private float characterY = 0;
    private float speed = 100;
    private boolean isMoving;
    private boolean isPaused = false;
    private BitmapFont pauseFont;

    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game) {
        this.game = game;

        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.zoom = 1f;

        // Get the font from the game's skin
        font = game.getSkin().getFont("font");
        pauseFont = game.getSkin().getFont("font");
        //pauseFont.getData().setScale(2.5f);
    }


    public void render(float delta) {
        if (!isPaused) {
            stateTime += Gdx.graphics.getDeltaTime();
            game.loadCharacterAnimation();

            handleInput(delta);

            ScreenUtils.clear(0, 0, 0, 1);

            camera.update();

            game.getSpriteBatch().begin();
            if (!isMoving) {
                stateTime = 0f;
            }
            game.getSpriteBatch().setProjectionMatrix(camera.combined);

            // Draw the character at the new position based on WASD key input
            game.getSpriteBatch().draw(
                    game.getCharacterDownAnimation().getKeyFrame(stateTime, isMoving),
                    characterX,
                    characterY,
                    64, // Width of the character
                    128 // Height of the character
            );
            if (game.getFileGame() != null){
                game.drawFiguras();
            }
            // End SpriteBatch
            game.getSpriteBatch().end();

        } else {
            // Render the pause menu
            renderPauseMenu();
        }
    }

    private void handleInput(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            togglePause();
        }

        if (!isPaused) {
            isMoving = false;
            //Normal
            if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                characterY += speed * delta;
                isMoving = true;
                game.setFrameDurationCharacter(0.1f);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                characterY -= speed * delta;
                isMoving = true;
                game.setFrameDurationCharacter(0.1f);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                characterX -= speed * delta;
                isMoving = true;
                game.setFrameDurationCharacter(0.1f);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                characterX += speed * delta;
                isMoving = true;
                game.setFrameDurationCharacter(0.1f);
            }
            //Running
            if ((Gdx.input.isKeyPressed(Input.Keys.R) && Gdx.input.isKeyPressed(Input.Keys.W)) ||
                    (Gdx.input.isKeyPressed(Input.Keys.R) && Gdx.input.isKeyPressed(Input.Keys.UP))) {
                characterY += speed * 2 * delta;
                isMoving = true;
                game.setFrameDurationCharacter(0.05f);
            }
            if ((Gdx.input.isKeyPressed(Input.Keys.R) && Gdx.input.isKeyPressed(Input.Keys.S)) ||
                    (Gdx.input.isKeyPressed(Input.Keys.R) && Gdx.input.isKeyPressed(Input.Keys.DOWN))) {
                characterY -= speed * 2 * delta;
                isMoving = true;
                game.setFrameDurationCharacter(0.05f);

            }
            if ((Gdx.input.isKeyPressed(Input.Keys.R) && Gdx.input.isKeyPressed(Input.Keys.A)) ||
                    (Gdx.input.isKeyPressed(Input.Keys.R) && Gdx.input.isKeyPressed(Input.Keys.LEFT))) {
                characterX -= speed * 2 * delta;
                isMoving = true;
                game.setFrameDurationCharacter(0.05f);

            }
            if ((Gdx.input.isKeyPressed(Input.Keys.R) && Gdx.input.isKeyPressed(Input.Keys.D)) ||
                    (Gdx.input.isKeyPressed(Input.Keys.R) && Gdx.input.isKeyPressed(Input.Keys.RIGHT))) {
                characterX += speed * 2 * delta;
                isMoving = true;
                game.setFrameDurationCharacter(0.05f);

            }
        }
    }


    private void renderPauseMenu() {
        // Draw the pause menu overlay
        // You can use pauseFont to render text, buttons, etc.

        game.getSpriteBatch().begin();

        float centerX = Gdx.graphics.getWidth() / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f;

        // Adjust the text position so it's centered
        pauseFont.draw(game.getSpriteBatch(), "Game Paused", centerX - pauseFont.getRegion().getRegionWidth() / 2 - 20, centerY + 100);
        pauseFont.draw(game.getSpriteBatch(), "Press Space to resume", centerX - pauseFont.getRegion().getRegionWidth() / 2 - 100, centerY + 20);
        pauseFont.draw(game.getSpriteBatch(), "Press M for Menu Screen", centerX - pauseFont.getRegion().getRegionWidth() / 2 - 100, centerY - 30);
        pauseFont.draw(game.getSpriteBatch(), "Press X to quit", centerX - pauseFont.getRegion().getRegionWidth() / 2 - 100, centerY - 80);
        game.getSpriteBatch().end();

        // Handle input for the pause menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            togglePause();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            Gdx.app.exit();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            pauseFont.getData().setScale(1f);
            game.goToMenu();
        }
    }

    private void togglePause() {
        isPaused = !isPaused;

        if (isPaused) {
            // Pause logic (e.g., stop animations, pause timers)
            game.getCharacterDownAnimation().setPlayMode(Animation.PlayMode.NORMAL); // Stop animations

            // Pause timers or other game logic
            // Example: timer.pause();

        } else {
            // Resume logic (e.g., resume animations, resume timers)
            game.getCharacterDownAnimation().setPlayMode(Animation.PlayMode.LOOP); // Resume animations

            // Resume timers or other game logic
            // Example: timer.resume();
        }
    }
    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false);
    }

    @Override
    public void pause() {
        togglePause();
    }

    @Override
    public void resume() {
        togglePause();
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

    // Additional methods and logic can be added as needed for the game screen
}