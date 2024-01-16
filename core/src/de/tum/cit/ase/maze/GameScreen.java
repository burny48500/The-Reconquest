package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
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
    private final Player player;
    private final LoadMap loadMap;
    private final OrthographicCamera camera;
    private final BitmapFont font;
    private float stateTime = 0f;
    private static float characterX = 0;
    private static float characterY = 0;
    private float speed = 40;
    private boolean isMoving;
    private boolean isPaused = false;
    private BitmapFont pauseFont;

    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game, Player player, LoadMap loadMap) {
        this.game = game;
        this.player = player;
        this.loadMap = loadMap;
        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        camera.setToOrtho(true);
        camera.zoom = 0.15f;

        // Get the font from the game's skin
        font = game.getSkin().getFont("font");
        pauseFont = game.getSkin().getFont("font");
        pauseFont.getData().setScale(1f);
    }


    public void render(float delta) {
        if (!isPaused) {
            stateTime += Gdx.graphics.getDeltaTime();
            game.loadCharacterAnimation();

            handleInput(delta);

            ScreenUtils.clear(0, 0, 0, 1);

            camera.position.set(characterX, characterY, 0);
            camera.update();

            game.getSpriteBatch().begin();
            if (!isMoving) {
                stateTime = 0f;
            }
            game.getSpriteBatch().setProjectionMatrix(camera.combined);
            // Draw the character at the new position based on WASD key input
            game.getSpriteBatch().draw(
                    player.getCharacterAnimation().getKeyFrame(stateTime, isMoving),
                    characterX,
                    characterY,
                    10, // Width of the character
                    20 // Height of the character
            );
            loadMap.drawImagen();
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

            keysMovements(delta);
        }
    }
    private boolean checkWallCollision(Rectangle newPlayerRect) {
        // Check for collisions with walls
        for (int i = 0; i < loadMap.getCoordinateArray().length; i++) {
            if (loadMap.getCoordinateArray()[i][2] == 0) {
                Rectangle wallRect = new Rectangle(loadMap.getCoordinateArray()[i][0], loadMap.getCoordinateArray()[i][1], 14, 12);
                if (newPlayerRect.overlaps(wallRect)) {
                    return true; // Collision detected
                }
            }
        }
        return false; // No collision detected
    }
    private void keysMovements(float delta){
        float newCharacterX = characterX;
        float newCharacterY = characterY;

        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if (!checkWallCollision(new Rectangle(newCharacterX, characterY + speed * delta, 10, 16))){
                if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                    characterY += speed * 1.5 * delta;
                    isMoving = true;
                    player.setFrameDurationCharacter(0.05f);
                } else {
                    characterY += speed * delta;
                    isMoving = true;
                    player.setFrameDurationCharacter(0.1f);
                }
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if (!checkWallCollision(new Rectangle(newCharacterX, characterY - speed * delta, 10, 16))){
                if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                    characterY -= speed * 1.5 * delta;
                    isMoving = true;
                    player.setFrameDurationCharacter(0.05f);
                }else {
                    characterY -= speed * delta;
                    isMoving = true;
                    player.setFrameDurationCharacter(0.1f);
                }
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (!checkWallCollision(new Rectangle(newCharacterX - speed*delta, characterY, 10, 16))){
                if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
                    characterX -= speed * 1.5 * delta;
                    isMoving = true;
                    player.setFrameDurationCharacter(0.05f);
                } else {
                    characterX -= speed * delta;
                    isMoving = true;
                    player.setFrameDurationCharacter(0.1f);
                }

            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (!checkWallCollision(new Rectangle(newCharacterX+ speed*delta, characterY, 10, 16))){
                if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                    characterX += speed * 1.5 * delta;
                    isMoving = true;
                    player.setFrameDurationCharacter(0.05f);
                } else {
                    characterX += speed * delta;
                    isMoving = true;
                    player.setFrameDurationCharacter(0.1f);
                }
            }
        }
    }
    private void renderPauseMenu() {
        // Draw the pause menu overlay
        // You can use pauseFont to render text, buttons, etc.

        game.getSpriteBatch().begin();

        // Use a projection matrix set to screen coordinates
        game.getSpriteBatch().setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        // Calculate the center of the screen
        float centerX = Gdx.graphics.getWidth() / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f;

        // Adjust the text position so it's centered
        drawCenteredText(game.getSpriteBatch(), pauseFont, "Game Paused", centerX, centerY + 50);
        drawCenteredText(game.getSpriteBatch(), pauseFont, "Press Space to resume", centerX, centerY);
        drawCenteredText(game.getSpriteBatch(), pauseFont, "Press M for Menu Screen", centerX, centerY - 50);
        drawCenteredText(game.getSpriteBatch(), pauseFont, "Press X to quit", centerX, centerY - 100);


        game.getSpriteBatch().end();

        // Restore the projection matrix for game rendering
        game.getSpriteBatch().setProjectionMatrix(camera.combined);

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

    private void drawCenteredText(SpriteBatch spriteBatch, BitmapFont font, String text, float centerX, float centerY) {
        GlyphLayout layout = new GlyphLayout(); // In older libGDX versions, you may need to use BitmapFont.TextBounds
        layout.setText(font, text);
        float textWidth = layout.width;
        font.draw(spriteBatch, text, centerX - textWidth / 2, centerY);
    }

    private void togglePause() {
        isPaused = !isPaused;

        if (isPaused) {
            // Pause logic (e.g., stop animations, pause timers)
            player.getCharacterAnimation().setPlayMode(Animation.PlayMode.NORMAL); // Stop animations

            // Pause timers or other game logic
            // Example: timer.pause();

        } else {
            // Resume logic (e.g., resume animations, resume timers)
            player.getCharacterAnimation().setPlayMode(Animation.PlayMode.LOOP); // Resume animations

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

    public static void setCharacterX(float characterX) {
        GameScreen.characterX = characterX;
    }

    public static void setCharacterY(float characterY) {
        GameScreen.characterY = characterY;
    }
    // Additional methods and logic can be added as needed for the game screen
}