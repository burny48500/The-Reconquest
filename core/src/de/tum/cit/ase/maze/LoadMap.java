/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import static de.tum.cit.ase.maze.GameScreen.setCharacterX;
import static de.tum.cit.ase.maze.GameScreen.setCharacterY;

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
        String archivo = fileGame;
        String[] lineas = archivo.split("\n");
        int index = 0;
        coordinateArray = new int[lineas.length][3];
        for (String linea : lineas) {
            if (linea.contains(",")) {
                String[] partes = linea.split("=");
                String[] coordenadas = partes[0].split(",");
                int x = Integer.parseInt(coordenadas[0])*16;
                int y = Integer.parseInt(coordenadas[1])*16;
                int tipoImagen = Integer.parseInt(partes[1]);
                // Sets the character for first time.
                if (tipoImagen == 1){
                    setCharacterX(x);
                    setCharacterY(y);
                }
                coordinateArray[index][0] = x;
                coordinateArray[index][1] = y;
                coordinateArray[index][2] = tipoImagen;

                index++;
            }
        }
    }
    public void drawImagen() {
        Texture basictiles = new Texture(Gdx.files.internal("basictiles.png"));
        Texture things = new Texture(Gdx.files.internal("things.png"));
        Texture objects = new Texture(Gdx.files.internal("objects.png"));
        for (int i=0;i<coordinateArray.length;i++){
            switch (coordinateArray[i][2]) {
                case 0: // WALLS
                    TextureRegion walls = new TextureRegion(basictiles,0,0,16,16);
                    spriteBatch.draw(walls, coordinateArray[i][0], coordinateArray[i][1], 16, 16);
                    break;
                case 2: // EXIT
                    if (keyCollected){
                        TextureRegion exit = new TextureRegion(things,0,32,16,16);
                        spriteBatch.draw(exit, coordinateArray[i][0], coordinateArray[i][1], 16, 16);
                    }else {
                        TextureRegion exit = new TextureRegion(things,0,0,16,16);
                        spriteBatch.draw(exit, coordinateArray[i][0], coordinateArray[i][1], 16, 16);
                    }
                    break;
                case 3: // STATIC_TRAP
                    TextureRegion static_trap = new TextureRegion(basictiles,83,112,13,15);
                    spriteBatch.draw(static_trap, coordinateArray[i][0], coordinateArray[i][1], 16, 16);
                    break;
                case 5: // KEY
                    if (!keyCollected){
                        TextureRegion key = new TextureRegion(objects,2,66,10,10);
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
