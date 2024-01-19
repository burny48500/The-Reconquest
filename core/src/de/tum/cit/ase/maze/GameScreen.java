package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private final Player player;
    private Hud hud;
    private final LoadMap loadMap;
    private final OrthographicCamera camera;
    private float stateTime = 0f;
    private static float characterX = 0;
    private static float characterY = 0;
    private float speed = 40;
    private boolean isMoving;
    private boolean isPaused = false;
    private BitmapFont pauseFont;
    private long lastTrapActivationTime = 0;

    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game, Player player, LoadMap loadMap) {
        this.game = game;
        this.player = player;
        this.loadMap = loadMap;
        this.hud = game.getHud();
        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        camera.setToOrtho(true);
        camera.zoom = 0.15f;

        // Get the font from the game's skin
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
            // Draw the types of objects loaded in the map
            loadMap.drawImagen();

            // Draw the hearts
            hud.showLives();
            // Draw the keys
            hud.showKey();

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
    // Method that creates rectangles in every single wall
    private boolean checkWallCollision(Rectangle newPlayerRect) {
        // Check for collisions with walls
        for (int i = 0; i < loadMap.getCoordinateArray().length; i++) {
            if (loadMap.getCoordinateArray()[i][2] == 0) {
                Rectangle wallRect = new Rectangle(loadMap.getCoordinateArray()[i][0], loadMap.getCoordinateArray()[i][1], 14, 12);
                if (newPlayerRect.overlaps(wallRect)) {
                    return true; // Collision detected WALL
                }
            }

            if (loadMap.getCoordinateArray()[i][2] == 2){
                Rectangle exit = new Rectangle(loadMap.getCoordinateArray()[i][0], loadMap.getCoordinateArray()[i][1], 14, 14);
                if (newPlayerRect.overlaps(exit) && loadMap.isKeyCollected()) {
                    // HACER UNA PANTALLA QUE SE HA GANADO EL JUEGO (URKO)
                    System.out.println("HAS GANADO!!");
                    loadMap.setKeyCollected(false);
                }
                if (newPlayerRect.overlaps(exit) && !loadMap.isKeyCollected()){
                    return true;
                }
            }

            if (loadMap.getCoordinateArray()[i][2] == 3) {
                Rectangle static_trap = new Rectangle(loadMap.getCoordinateArray()[i][0]+3, loadMap.getCoordinateArray()[i][1]+2, 8, 6);
                if (newPlayerRect.overlaps(static_trap)) {
                    long currentTime = System.currentTimeMillis();

                    // Check if enough time has passed since the last activation
                    if (currentTime - lastTrapActivationTime >= 3000) {
                        // Perform the action
                        hud.loseLive();
                        Music key_music = Gdx.audio.newMusic(Gdx.files.internal("CollisionFire.mp3"));
                        key_music.setLooping(false);
                        key_music.play();
                        // Update the last activation time
                        lastTrapActivationTime = currentTime;
                    }
                    if (hud.getNumberOfLives()==0){
                        game.goToMenu();
                        System.out.println("HAS PERDIDO..");
                    }
                }
            }

            if (loadMap.getCoordinateArray()[i][2] == 5 && !loadMap.isKeyCollected()){
                Rectangle key = new Rectangle(loadMap.getCoordinateArray()[i][0], loadMap.getCoordinateArray()[i][1], 14, 14);
                if (newPlayerRect.overlaps(key)){
                    loadMap.setKeyCollected(true);
                    Music key_music = Gdx.audio.newMusic(Gdx.files.internal("keys_moving.mp3"));
                    key_music.setLooping(false);
                    key_music.play();
                }
            }
        }
        return false; // No collision detected
    }

    // Method that reads keys and do the movements in the coordinates of the character
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

    // Display Pause Menu
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

    // Method for centering the text of PauseMenu
    public void drawCenteredText(SpriteBatch spriteBatch, BitmapFont font, String text, float centerX, float centerY) {
        GlyphLayout layout = new GlyphLayout(); // In older libGDX versions, you may need to use BitmapFont.TextBounds
        layout.setText(font, text);
        float textWidth = layout.width;
        font.draw(spriteBatch, text, centerX - textWidth / 2, centerY);
    }

    // It stops the game and resumes the game
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

    // Setters
    public static void setCharacterX(float characterX) {
        GameScreen.characterX = characterX;
    }

    public static void setCharacterY(float characterY) {
        GameScreen.characterY = characterY;
    }

    public float getCharacterX() {
        return characterX;
    }

    public float getCharacterY() {
        return characterY;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void setHud(Hud hud) {
        this.hud = hud;
    }
    // Additional methods and logic can be added as needed for the game screen
}