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
    int[][] coordinateArray;
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
    private int maximumX,maximumY = 0;
    private float elapsed;
    private Animation<TextureRegion> spike_trap = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("spike_trap.gif").read());
    private Animation<TextureRegion> dynamic_trap = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("dynamic_ghost.gif").read());
    private Random random = new Random();
    private Vector2 targetPosition = new Vector2();

    public LoadMap(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        this.coordinateArray = new int[0][3];
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
        coordinateArray = new int[lineas.length][3];
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
    }
    public void runDynamicTrap(int i) {
        float speed = 30.0f;

        // If the trap is close to the target position, choose a new target
        if (targetPosition.dst(coordinateArray[i][0], coordinateArray[i][1]) < 5.0f || Gdx.graphics.getDeltaTime() == 0) {
            chooseNewTarget(i);
        }

        // Calculate the direction towards the target position
        float angle = MathUtils.atan2(targetPosition.y - coordinateArray[i][1], targetPosition.x - coordinateArray[i][0]);

        // Calculate the movement components
        float deltaX = speed * Gdx.graphics.getDeltaTime() * MathUtils.cos(angle);
        float deltaY = speed * Gdx.graphics.getDeltaTime() * MathUtils.sin(angle);

        // Update the position
        coordinateArray[i][0] += deltaX;
        coordinateArray[i][1] += deltaY;

        // Wrap the position around the map if it goes out of bounds
        coordinateArray[i][0] = (coordinateArray[i][0] + maximumX * 16) % (maximumX * 16);
        coordinateArray[i][1] = (coordinateArray[i][1] + maximumY * 16) % (maximumY * 16);

        // Draw the dynamic trap
        elapsed += Gdx.graphics.getDeltaTime() * 0.015;
        spriteBatch.draw(dynamic_trap.getKeyFrame(elapsed), coordinateArray[i][0], coordinateArray[i][1], 32, 24);
    }

    private void chooseNewTarget(int i) {
        // Set a new target position randomly
        targetPosition.set(random.nextFloat() * maximumX * 16, random.nextFloat() * maximumY * 16);

        // Adjust the target position to avoid going outside the map
        targetPosition.x = MathUtils.clamp(targetPosition.x, 0, maximumX * 16);
        targetPosition.y = MathUtils.clamp(targetPosition.y, 0, maximumY * 16);

        // If the new target is close to a wall, choose a new target
        while (isTargetNearWall()) {
            targetPosition.set(random.nextFloat() * maximumX * 16, random.nextFloat() * maximumY * 16);
            targetPosition.x = MathUtils.clamp(targetPosition.x, 0, maximumX * 16);
            targetPosition.y = MathUtils.clamp(targetPosition.y, 0, maximumY * 16);
        }
    }

    private boolean isTargetNearWall() {
        // Check if the target position is close to a wall (you may need to adjust the threshold)
        for (int j = 0; j < coordinateArray.length; j++) {
            if (coordinateArray[j][2] == 0 && targetPosition.dst(coordinateArray[j][0], coordinateArray[j][1]) < 20.0f) {
                return true;
            }
        }
        return false;
    }
    public void drawImagen() {
        for (int x=0;x<maximumX*16;x=x+16) {
            for (int y = 0; y < maximumY*16; y=y+16) {
                spriteBatch.draw(floor, x, y, 16, 16);
            }
        }
        for (int i=0;i<coordinateArray.length;i++){
            switch (coordinateArray[i][2]) {
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
                    elapsed += Gdx.graphics.getDeltaTime()*0.05;
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
    public int[][] getCoordinateArray() {
        return coordinateArray;
    }

    public boolean isKeyCollected() {
        return keyCollected;
    }

    public void setKeyCollected(boolean keyCollected) {
        this.keyCollected = keyCollected;
    }

}