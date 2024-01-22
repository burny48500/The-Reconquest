package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class WinScreen implements Screen {
    private final MazeRunnerGame game;
    private Stage stage;
    private Sprite backgroundSprite;
    private SpriteBatch batch;



    public WinScreen(MazeRunnerGame game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport()); // Initialize the stage here

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Texture backgroundImage = new Texture(Gdx.files.internal("menuScreenBackground.png"));
        backgroundSprite = new Sprite(backgroundImage);
        Viewport viewport = new ExtendViewport(1920, 1280, camera);
        stage = new Stage(viewport, game.getSpriteBatch());
        backgroundSprite.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        batch = game.getSpriteBatch();




        Table table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage

        // Add a label as a title
        table.add(new Label("You won!", game.getSkin(), "title")).padBottom(100).row();

        // Add a button to go to the main menu
        TextButton menuScreenButton = new TextButton("Go to Menu", game.getSkin());
        menuScreenButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                game.goToMenu();
            }
        });
        table.add(menuScreenButton).width(viewport.getWorldWidth() * 0.2f).height(viewport.getWorldHeight() * 0.07f).row();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage); // Set the input processor to the stage
    }

    @Override
    public void render(float delta) {
        // Clear the screen and render the stage
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose(); // Dispose of the stage
    }
}