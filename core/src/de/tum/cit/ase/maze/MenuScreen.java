package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * The MenuScreen class is responsible for displaying the main menu of the game.
 * It extends the LibGDX Screen class and sets up the UI components for the menu.
 */
public class MenuScreen implements Screen {

    private final Stage stage;

    /**
     * Constructor for MenuScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public MenuScreen(MazeRunnerGame game) {
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view

        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        Table table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage

        // Add a label as a title
        table.add(new Label("Welcome to The Reconquest", game.getSkin(), "title")).padBottom(80).row();

        // Create and add a button to go to the game screen
        TextButton goToGameButton = new TextButton("Play", game.getSkin());
        TextButton selectMapButton = new TextButton("Select Map", game.getSkin());
        TextButton exitGameButton = new TextButton("Exit Game", game.getSkin());
        table.add(goToGameButton).width(300).row();
        goToGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                game.goToGame();
            }
        });
        // Show Select Map Button
        table.add(selectMapButton).width(300).row();
        // Show Select Map Button
        selectMapButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String selectedFilePath = "";
                File selectedFile = null;
                String osName = System.getProperty("os.name").toLowerCase();

                try {
                    if (osName.contains("mac")) {
                        // macOS-specific logic to choose a file
                        String script = "set selectedFile to choose file " +
                                "with prompt \"Please select a .properties file\" " +
                                "default location (path to home folder) " +
                                "without invisibles\n" +
                                "return POSIX path of selectedFile";

                        Process process = Runtime.getRuntime().exec(new String[]{"/usr/bin/osascript", "-e", script});
                        int result = process.waitFor();
                        if (result == 0) {
                            selectedFilePath = new BufferedReader(new InputStreamReader(process.getInputStream())).readLine();
                        }
                    } else {
                        // Windows (and other OS) specific logic to choose a file
                        selectedFile = selectFile(null);
                        if (selectedFile != null) {
                            selectedFilePath = selectedFile.getAbsolutePath();
                        }
                    }

                    // Common file reading logic
                    if (selectedFilePath != null && !selectedFilePath.isEmpty()) {
                        System.out.println("Selected File: " + selectedFilePath);
                        readFile(selectedFilePath);
                    } else {
                        System.out.println("No file selected or invalid file type.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });



        // Show Exit button
        table.add(exitGameButton).width(300).row();
        exitGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }
    public static File selectFile(Component parent) {
        // macOS-specific file dialog
        FileDialog fileDialog = new FileDialog((Frame) null);
        fileDialog.setMode(FileDialog.LOAD);
        fileDialog.setVisible(true);
        String file = fileDialog.getFile();
        String dir = fileDialog.getDirectory();
        if (file != null && dir != null) {
            return new File(dir, file);
        }
        return null;
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update the stage
        stage.draw(); // Draw the stage
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update the stage viewport on resize
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

    }private void readFile(String filePath) {
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // Process the line
                System.out.println(line); // or handle the line as needed
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
            e.printStackTrace();
        }
    }



}