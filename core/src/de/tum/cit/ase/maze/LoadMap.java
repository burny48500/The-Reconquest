package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import static de.tum.cit.ase.maze.GameScreen.setCharacterX;
import static de.tum.cit.ase.maze.GameScreen.setCharacterY;
import static java.util.Collections.max;

/**
 *
 * @author Usuario
 */
public class LoadMap {

    private String fileGame;
    float[][] coordinateArray;
    // Sprite Batch for rendering
    private SpriteBatch spriteBatch;
    private boolean keyCollected = false;
    private Texture basictiles = new Texture(Gdx.files.internal("basictiles.png"));
    private Texture things = new Texture(Gdx.files.internal("things.png"));
    private Texture objects = new Texture(Gdx.files.internal("objects.png"));
    private Texture mobs = new Texture(Gdx.files.internal("mobs.png"));

    private TextureRegion walls = new TextureRegion(basictiles,0,0,16,16);
    private TextureRegion floor = new TextureRegion(basictiles,0,128,16,16);
    private TextureRegion exit = new TextureRegion(things,0,32,16,16);
    private TextureRegion exitOpen = new TextureRegion(things,0,0,16,16);
    private TextureRegion static_trap = new TextureRegion(things,144,54,16,10);
    //private TextureRegion dynamic_trap = new TextureRegion(mobs,96,63,16,10);
    private TextureRegion key = new TextureRegion(objects,2,66,10,10);
    public int maximumX,maximumY = 0;
    private float elapsed;
    private Animation<TextureRegion> spike_trap = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("spike_trap.gif").read());
    private Animation<TextureRegion> dynamic_trap = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("dynamic_ghost.gif").read());
    private int[] movementDirectionX,movementDirectionY;
    private Random random = new Random();
    private int randomNumber = 0;

    public LoadMap(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        this.coordinateArray = new float[0][3];

    }

    public void setFileGame(String fileGame) {
        this.fileGame = fileGame;
    }

    public String getFileGame() {
        return fileGame;
    }

    public void readMap() {
        maximumX = 1;
        maximumY = 1;
        String archivo = fileGame;
        String[] lineas = archivo.split("\n");
        int index = 0;
        coordinateArray = new float[lineas.length][3];
        for (String linea : lineas) {
            if (linea.contains(",")) {
                String[] partes = linea.split("=");
                String[] coordenadas = partes[0].split(",");
                int x = Integer.parseInt(coordenadas[0])*16;
                maximumX = Math.max(maximumX, Integer.parseInt(coordenadas[0]));
                int y = Integer.parseInt(coordenadas[1])*16;
                maximumY = Math.max(maximumY, Integer.parseInt(coordenadas[1]));
                int typeImage = Integer.parseInt(partes[1].trim());
                // Sets the character for first time.
                if (typeImage == 1){
                    setCharacterX(x);
                    setCharacterY(y);
                }
                coordinateArray[index][0] = x;
                coordinateArray[index][1] = y;
                coordinateArray[index][2] = typeImage;
                index++;
            }
        }
        movementDirectionX = new int[index];
        movementDirectionY = new int[index];
        Random random = new Random();

        for (int i = 0; i < index; i++) {
            movementDirectionX[i] = random.nextBoolean() ? 1 : -1; // Randomly set initial direction
            movementDirectionY[i] = random.nextBoolean() ? 1 : -1; // Randomly set initial direction
        }
    }

    public void runDynamicTrap(int i) {
        float delta = Gdx.graphics.getDeltaTime();
        elapsed += delta * 0.01; // Adjust the speed as needed

        // Check for collisions with walls and change direction accordingly for the specific dynamic trap
        if (collidesWithWalls(coordinateArray[i][0], coordinateArray[i][1])) {
            // Change the movement direction for the specific dynamic trap when a collision occurs
            movementDirectionX[i] *= -1;
            movementDirectionY[i] *= -1;
        }

        // Move the dynamic trap based on its individual direction

        coordinateArray[i][0] -= 0.3 * movementDirectionX[i];
        coordinateArray[i][1] -= 0.3 * movementDirectionY[i];

        // Draw the dynamic trap
        spriteBatch.draw(dynamic_trap.getKeyFrame(elapsed), coordinateArray[i][0], coordinateArray[i][1], 32, 24);
    }

    private boolean collidesWithWalls(float x, float y) {
        // Check if the next position collides with walls
        // Iterate through the walls in coordinateArray and check for collisions
        for (int i = 0; i < coordinateArray.length; i++) {
            if (coordinateArray[i][2] == 0 || coordinateArray[i][2] == 1 || coordinateArray[i][2] == 2 || coordinateArray[i][2] == 3) { // Check if it's a wall,entry,exit,trap
                float wallX = coordinateArray[i][0];
                float wallY = coordinateArray[i][1];
                float wallWidth = 8;
                float wallHeight = 8;

                // Check for collision
                if (x < wallX + wallWidth &&
                        x + 16 > wallX &&
                        y < wallY + wallHeight &&
                        y + 12 > wallY) {
                    return true; // Collision detected with a wall
                }
            }
        }

        return false; // No collision with walls
    }



    public void drawImagen() {
        for (int x=0;x<maximumX*16;x=x+16) {
            for (int y = 0; y < maximumY*16; y=y+16) {
                spriteBatch.draw(floor, x, y, 16, 16);
            }
        }
        for (int i=0;i<coordinateArray.length;i++){
            switch ((int) coordinateArray[i][2]) {
                case 0: // WALLS
                    spriteBatch.draw(walls, coordinateArray[i][0], coordinateArray[i][1], 16, 16);
                    break;
                case 1:
                    spriteBatch.draw(floor, coordinateArray[i][0], coordinateArray[i][1], 16, 16);
                    break;
                case 2: // EXIT
                    if (keyCollected){
                        spriteBatch.draw(exit, coordinateArray[i][0], coordinateArray[i][1], 16, 16);
                    }else {
                        spriteBatch.draw(exitOpen, coordinateArray[i][0], coordinateArray[i][1], 16, 16);
                    }
                    break;
                case 3: // STATIC_TRAP
                    elapsed += Gdx.graphics.getDeltaTime()*0.03;
                    spriteBatch.draw(spike_trap.getKeyFrame(elapsed), coordinateArray[i][0], coordinateArray[i][1], 16, 12);
                    break;
                case 4:
                    runDynamicTrap(i);
                    break;
                case 5: // KEY
                    if (!keyCollected){
                        spriteBatch.draw(key, coordinateArray[i][0], coordinateArray[i][1], 10, 10);
                    }
                    break;
            }
        }
    }
    public float[][] getCoordinateArray() {
        return coordinateArray;
    }

    public boolean isKeyCollected() {
        return keyCollected;
    }

    public void setKeyCollected(boolean keyCollected) {
        this.keyCollected = keyCollected;
    }

}