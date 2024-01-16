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
        System.out.println(archivo);
        int index = 0;
        coordinateArray = new int[lineas.length][3];
        for (String linea : lineas) {
            if (linea.contains(",")) {
                String[] partes = linea.split("=");
                String[] coordenadas = partes[0].split(",");
                int x = Integer.parseInt(coordenadas[0])*16;
                int y = Integer.parseInt(coordenadas[1])*16;
                int tipoImagen = Integer.parseInt(partes[1]);
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
        Texture things = new Texture(Gdx.files.internal("basictiles.png"));
        for (int i=0;i<coordinateArray.length;i++){
            switch (coordinateArray[i][2]) {
                case 0:
                    TextureRegion walls1 = new TextureRegion(things,0,0,16,16);
                    //Animation<TextureRegion> walls = new Animation<>(0.1f, new TextureRegion(things, 0, 0, 16, 16));
                    spriteBatch.draw(walls1, coordinateArray[i][0], coordinateArray[i][1], 16, 16);
                    break;
                case 2:
                    break;
                // Agrega más casos según sea necesario para tus tipos de imágenes
                default:
                    // Manejo para otros tipos de imágenes si es necesario
                    break;
            }
        }
    }

    public int[][] getCoordinateArray() {
        return coordinateArray;
    }
    
}
