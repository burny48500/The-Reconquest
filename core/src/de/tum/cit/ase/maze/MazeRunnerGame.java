package de.tum.cit.ase.maze;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
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

/**
 * The MazeRunnerGame class represents the core of the Maze Runner game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
public class MazeRunnerGame extends Game {
    // Screens
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private int selectedCharacterColumn = 0; // Column index of the third character (0-based index)
    private int selectedCharacterRow = 0;    // Row index of the selected character (0-based index)
    private float frameDurationCharacter = 0.1f;

    // Sprite Batch for rendering
    private SpriteBatch spriteBatch;

    // UI Skin
    private Skin skin;

    // Character animation downwards
    private Animation<TextureRegion> characterDownAnimation;
    int[][] coordinateArray;


    public void setFileGame(String fileGame) {
        this.fileGame = fileGame;
    }

    public String getFileGame() {
        return fileGame;
    }

    private String fileGame;

    /**
     * Constructor for MazeRunnerGame.
     *
     * @param fileChooser The file chooser for the game, typically used in desktop environment.
     */
    public MazeRunnerGame(NativeFileChooser fileChooser) {
        super();
    }

    /**
     * Called when the game is created. Initializes the SpriteBatch and Skin.
     */
    @Override
    public void create() {
        spriteBatch = new SpriteBatch(); // Create SpriteBatch
        skin = new Skin(Gdx.files.internal("craft/craftacular-ui.json")); // Load UI skin
        //this.loadCharacterAnimation(); // Load character animation

        // Play some background music
        // Background sound
        Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
        goToMenu(); // Navigate to the menu screen
    }

    /**
     * Switches to the menu screen.
     */
    public void goToMenu() {
        this.setScreen(new MenuScreen(this)); // Set the current screen to MenuScreen
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
    }
    public void readMap() {
        String archivo = fileGame;
        String[] lineas = archivo.split("\n");
        System.out.println(archivo);
        int index = 0;
        coordinateArray = new int[lineas.length][3];
        for (String linea : lineas) {
            if (linea.contains(",")) {
                String[] partes = linea.split("=");
                String[] coordenadas = partes[0].split(",");
                int x = Integer.parseInt(coordenadas[0].trim())*10;
                int y = Integer.parseInt(coordenadas[1].trim())*10;
                int tipoImagen = Integer.parseInt(partes[1].trim());

                coordinateArray[index][0] = x;
                coordinateArray[index][1] = y;
                coordinateArray[index][2] = tipoImagen;

                index++;
            }
        }
    }
    public void drawImagen() {
        Texture things = new Texture(Gdx.files.internal("basictiles.png"));
        for (int i=0;i<coordinateArray.length;i++){
            switch (coordinateArray[i][2]) {
                case 0:
                    Animation<TextureRegion> walls = new Animation<>(0.1f, new TextureRegion(things, 0, 0, 16, 16));
                    spriteBatch.draw(walls.getKeyFrame(0), coordinateArray[i][0], coordinateArray[i][1], 16, 16);
                    break;
                case 1:
                    Animation<TextureRegion> die = new Animation<>(0.1f, new TextureRegion(things, 0, 0, 16, 16));
                    spriteBatch.draw(die.getKeyFrame(0), coordinateArray[i][0], coordinateArray[i][1], 16, 16);
                    break;
                // Agrega más casos según sea necesario para tus tipos de imágenes
                default:
                    // Manejo para otros tipos de imágenes si es necesario
                    break;
            }
        }
    }

    /**
     * Switches to the game screen.
     */
    public void goToGame() {
        this.setScreen(new GameScreen(this)); // Set the current screen to GameScreen
        if (menuScreen != null) {
            menuScreen.dispose(); // Dispose the menu screen if it exists
            menuScreen = null;
        }
    }

    /**
     * Loads the character animation from the character.png file.
     */
    void loadCharacterAnimation() {
        Texture walkSheet = new Texture(Gdx.files.internal("character.png"));

        int frameWidth = 16;
        int frameHeight = 32;
        int animationFrames = 4;

        // libGDX internal Array instead of ArrayList because of performance
        Array<TextureRegion> walkFrames = new Array<>(TextureRegion.class);

        // Assuming characters are arranged in a grid in the sprite sheet
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            selectedCharacterColumn = 0;
            selectedCharacterRow = 3;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            selectedCharacterColumn = 0;
            selectedCharacterRow = 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            selectedCharacterColumn = 0;
            selectedCharacterRow = 0;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)){
            selectedCharacterColumn = 0;
            selectedCharacterRow = 2;
        }
        // Add frames for the selected character to the animation
        for (int i=0;i<animationFrames;i++){
            int startX = selectedCharacterColumn * frameWidth + i * frameWidth;
            int startY = selectedCharacterRow * frameHeight;
            walkFrames.add(new TextureRegion(walkSheet, startX, startY, frameWidth, frameHeight));
        }

        characterDownAnimation = new Animation<>(frameDurationCharacter, walkFrames);

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

    // Getter methods
    public Skin getSkin() {
        return skin;
    }

    public Animation<TextureRegion> getCharacterDownAnimation() {
        return characterDownAnimation;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public void setFrameDurationCharacter(float frameDurationCharacter) {
        this.frameDurationCharacter = frameDurationCharacter;
    }
}