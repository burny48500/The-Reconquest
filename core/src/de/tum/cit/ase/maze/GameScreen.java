package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private final OrthographicCamera camera;
    private final BitmapFont font;
    private float sinusInput = 0f;
    private float characterX = 0;
    private float characterY = 0;
    private float speed = 100;

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
        camera.zoom = 0.75f;

        // Get the font from the game's skin
        font = game.getSkin().getFont("font");
    }


    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {
        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }

        // Handle input for character movement
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            characterY += speed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            characterY -= speed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            characterX -= speed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            characterX += speed * delta;
        }

        // Clear the screen
        ScreenUtils.clear(0, 0, 0, 1);

        // Update the camera
        camera.update();
        game.getSpriteBatch().setProjectionMatrix(camera.combined);

        // Begin SpriteBatch
        game.getSpriteBatch().begin();

        // Draw the character at the new position based on WASD key input
        game.getSpriteBatch().draw(
                game.getCharacterDownAnimation().getKeyFrame(sinusInput, true),
                characterX,
                characterY,
                64, // Width of the character
                128 // Height of the character
        );

        // End SpriteBatch
        game.getSpriteBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
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
