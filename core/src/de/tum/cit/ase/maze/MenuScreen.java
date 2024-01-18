package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;
import games.spooky.gdx.nativefilechooser.NativeFileChooserCallback;
import games.spooky.gdx.nativefilechooser.NativeFileChooserConfiguration;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;

import java.util.Scanner;

/**
 * The MenuScreen class is responsible for displaying the main menu of the game.
 * It extends the LibGDX Screen class and sets up the UI components for the menu.
 */
public class MenuScreen implements Screen {

    private final Stage stage;
    private final MazeRunnerGame game;
    private String fileContent = ""; // String to store the content of the file
    private final LoadMap loadMap;
    private Texture menuScreenBackground;
    private SpriteBatch batch;
    private NativeFileChooser fileChooser;


    /**
     * Constructor for MenuScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game The main game class, used to access global resources and methods.
     *
     *
     */


    public MenuScreen(MazeRunnerGame game, LoadMap loadMap) {
        this.game = game;
        this.loadMap = loadMap;
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view

        menuScreenBackground = new Texture("menuScreenBackground.png");
        menuScreenBackground.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        batch = new SpriteBatch();
        Viewport viewport = new ScreenViewport(camera);
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        Table table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage

        // Add a label as a title
        table.add(new Label("Welcome to The Reconquest", game.getSkin(), "title")).padBottom(80).row();
        TextButton playRandomButton = new TextButton("Play", game.getSkin());
        TextButton selectMapButton = new TextButton("Select Map", game.getSkin());
        TextButton exitGameButton = new TextButton("Exit Game", game.getSkin());

        // Show Select Map Button
        table.add(playRandomButton).width(300).row();
        table.add(selectMapButton).width(300).row();
        table.add(exitGameButton).width(300).row();
        exitGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        playRandomButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                int min = 1; // Minimum value of range
                int max = 5; // Maximum value of range
                // Generate random int value from min to max
                int randomNum = (int)Math.floor(Math.random() * (max - min + 1) + min);
                String mapFilePath = "maps/level-"+randomNum+".properties";
                FileHandle mapFile = Gdx.files.internal(mapFilePath);

                // Check if the file exists to avoid errors
                if (mapFile.exists()) {
                    String mapContent = mapFile.readString();
                    loadMap.setFileGame(mapContent);
                    loadMap.readMap();
                    game.goToGame();
                } else {
                    // Handle the error, maybe show an error dialog or print to the console
                    Gdx.app.error("MenuScreen", "Map file not found: " + mapFilePath);
                }
            }
        });


        // Show Select Map Button
        selectMapButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectMapFile();
            }
        });


    }
    private void selectMapFile() {
        NativeFileChooserConfiguration conf = new NativeFileChooserConfiguration();
        // Configure the file chooser here (e.g., set title, start directory, etc.)
        conf.directory = Gdx.files.internal("maps");

        game.getFileChooser().chooseFile(conf, new NativeFileChooserCallback() {
            @Override
            public void onFileChosen(FileHandle file) {

                String fileContent = readFile(file.file().getAbsolutePath());
                if (fileContent != null) {
                    loadMap.setFileGame(fileContent);
                    loadMap.readMap();
                    game.goToGame();
                }
            }

            @Override
            public void onCancellation() {
                System.out.println("Process cancelled!");
            }

            @Override
            public void onError(Exception exception) {
                System.out.println("Try with another file!");
            }
        });
    }

    private String readFile(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                contentBuilder.append(currentLine).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return contentBuilder.toString();
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        batch.setProjectionMatrix(stage.getCamera().combined);
        batch.begin();
        batch.draw(menuScreenBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        stage.draw();
    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update the stage viewport on resize
        stage.getCamera().position.set(stage.getCamera().viewportWidth / 2f, stage.getCamera().viewportHeight / 2f, 0);
    }

    @Override
    public void dispose() {
        // Dispose of the stage when screen is disposed
        stage.dispose();
    }

    @Override
    public void show() {
        // Set the input processor so the stage can receive input events
        Gdx.input.setInputProcessor(stage);
    }

    // The following methods are part of the Screen interface but are not used in this screen.
    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    public String getFileContent() {
        return fileContent;
    }

}