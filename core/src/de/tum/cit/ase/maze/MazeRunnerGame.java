package de.tum.cit.ase.maze;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static de.tum.cit.ase.maze.GameScreen.setCharacterX;
import static de.tum.cit.ase.maze.GameScreen.setCharacterY;

/**
 * The MazeRunnerGame class represents the core of the Maze Runner game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
public class MazeRunnerGame extends Game {
    // Screens
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private Player player;
    private SpriteBatch spriteBatch;
    private LoadMap loadMap;
    private Hud hud;
    public Music backgroundMusic;
    private final NativeFileChooser fileChooser;

    // UI Skin
    private Skin skin;


    /**
     * Constructor for MazeRunnerGame.
     *
     * @param fileChooser The file chooser for the game, typically used in desktop environment.
     */
    public MazeRunnerGame(NativeFileChooser fileChooser) {
        super();
        this.fileChooser = fileChooser;
    }

    /**
     * Called when the game is created. Initializes the SpriteBatch and Skin.
     */
    @Override
    public void create() {

        spriteBatch = new SpriteBatch(); // Create SpriteBatch
        loadMap = new LoadMap(spriteBatch);

        skin = new Skin(Gdx.files.internal("craft/craftacular-ui.json")); // Load UI skin
        //this.loadCharacterAnimation(); // Load character animation

        player = new Player();
        gameScreen = new GameScreen(this, player, loadMap);

        hud = new Hud(gameScreen,spriteBatch,loadMap);
        gameScreen.setHud(hud);

        // Play some background music
        // Background sound

        goToMenu(); // Navigate to the menu screen
    }
    /**
     * Switches to the menu screen.
     */
    public void goToMenu() {
        if (backgroundMusic != null){
            backgroundMusic.pause();
        }
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("MenuScreen.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
        menuScreen = new MenuScreen(this, loadMap);
        this.setScreen(menuScreen); // Set the current screen to MenuScreen
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
    }

    /**
     * Switches to the game screen.
     */
    public void goToGame() {
        if (backgroundMusic != null){
            backgroundMusic.pause();
        }
        hud.setNumberOfLives(3);
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("GameScreen.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
        backgroundMusic.setVolume(1f);
        this.setScreen(new GameScreen(this, player, loadMap)); // Set the current screen to GameScreen
        if (menuScreen != null) {
            menuScreen.dispose(); // Dispose the menu screen if it exists
            menuScreen = null;
        }
        loadMap.setKeyCollected(false);
    }

    public void goToGameOver() {
        if (backgroundMusic != null){
            backgroundMusic.pause();
        }
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("GameOverSoundFx.mp3"));
        backgroundMusic.stop();
        backgroundMusic.setLooping(false);
        backgroundMusic.play();
        backgroundMusic.setVolume(0.1f);
        this.setScreen(new GameOverScreen(this)); // Set the current screen to MenuScreen
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
    }

    public void goToWinScreen() {
        if (backgroundMusic != null){
            backgroundMusic.pause();
        }
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("GameScreen.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
        backgroundMusic.setVolume(0.1f);
        this.setScreen(new WinScreen(this)); // Set the current screen to MenuScreen
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
    }

    /**
     * Loads the character animation from the character.png file.
     */
    void loadCharacterAnimation() {
        player.loadCharacterAnimation();
    }

    /**
     * Cleans up resources when the game is disposed.
     */
    @Override
    public void dispose() {
        getScreen().hide(); // Hide the current screen
        getScreen().dispose(); // Dispose the current screen
        spriteBatch.dispose(); // Dispose the spriteBatch
        skin.dispose(); // Dispose the skin
    }

    /**
     * Getter methods
     */
    public Skin getSkin() {
        return skin;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }
    public NativeFileChooser getFileChooser() {
        return fileChooser;
    }
    public Hud getHud() {
        return hud;
    }

}

