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
        camera.zoom = 2.75f;

        // Get the font from the game's skin
        font = game.getSkin().getFont("font");
        pauseFont = game.getSkin().getFont("font");
        pauseFont.getData().setScale(2.5f);
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
            if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                characterY += speed * delta;
                isMoving = true;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                characterY -= speed * delta;
                isMoving = true;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                characterX -= speed * delta;
                isMoving = true;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                characterX += speed * delta;
                isMoving = true;
            }
        }
    }


    private void renderPauseMenu() {
        // Draw the pause menu overlay
        // You can use pauseFont to render text, buttons, etc.

        game.getSpriteBatch().begin();
        pauseFont.draw(game.getSpriteBatch(), "Game Paused", Gdx.graphics.getWidth() / 2f + 1000, Gdx.graphics.getHeight() / 2f + 1300);
        pauseFont.draw(game.getSpriteBatch(), "Press Space to resume", Gdx.graphics.getWidth() / 2f + 800, Gdx.graphics.getHeight() / 2f+1100);
        pauseFont.draw(game.getSpriteBatch(), "Press S to select another map", Gdx.graphics.getWidth() / 2f +800, Gdx.graphics.getHeight() / 2f +1010);
        pauseFont.draw(game.getSpriteBatch(), "Press X to exit", Gdx.graphics.getWidth() / 2f +800, Gdx.graphics.getHeight() / 2f + 920);
        game.getSpriteBatch().end();

        // Handle input for the pause menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            togglePause();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            Gdx.app.exit();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            try {
                JFileChooser fileChooser = new JFileChooser();

                // Specify the allowed file extension(s)
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Properties Files", "properties");
                fileChooser.setFileFilter(filter);

                int result = fileChooser.showOpenDialog(null);
                System.out.println("Result: " + result);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    System.out.println("Selected File: " + selectedFile.getAbsolutePath());
                    game.drawFiguras();

                    // Perform actions with the selected file here
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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