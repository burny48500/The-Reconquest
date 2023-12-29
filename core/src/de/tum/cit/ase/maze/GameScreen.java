package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
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
    private float stateTime = 0f;
    private float characterX = 0;
    private float characterY = 0;
    private float speed = 100;
    private boolean isMoving;
    private boolean isPaused = false;

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

    @Override
    public void render(float delta) {
        stateTime += Gdx.graphics.getDeltaTime();
        game.loadCharacterAnimation();
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            pause();
        }
        isMoving = false;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            characterY += speed * delta;
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            characterY -= speed * delta;
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            characterX -= speed * delta;
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            characterX += speed * delta;
            isMoving = true;
        }

        ScreenUtils.clear(0, 0, 0, 1);

        camera.update();
        game.getSpriteBatch().setProjectionMatrix(camera.combined);

        game.getSpriteBatch().begin();
        if (!isMoving){
            stateTime=0f;
        }

        // Draw the character at the new position based on WASD key input
        game.getSpriteBatch().draw(
                game.getCharacterDownAnimation().getKeyFrame(stateTime, isMoving),
                characterX,
                characterY,
                64, // Width of the character
                128 // Height of the character
        );

        // End SpriteBatch
        game.getSpriteBatch().end();
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