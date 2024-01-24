/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.tum.cit.ase.maze;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author Usuario
 */
public class Player extends Game {
    private int xPos=0;
    private int yPos=0;
    
    private int selectedCharacterColumn = 0; // Column index of the third character (0-based index)
    private int selectedCharacterRow = 0;    // Row index of the selected character (0-based index)
    private float frameDurationCharacter = 0.1f;
    private Texture walkSheet = new Texture(Gdx.files.internal("character.png"));


    // Character animation downwards
    private Animation<TextureRegion> CharacterAnimation;
    
    public Player() {
        xPos = 1;
	yPos = 1;
    }
 
    public void loadCharacterAnimation(){

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

        CharacterAnimation = new Animation<>(frameDurationCharacter, walkFrames);
    }
    
     public void setFrameDurationCharacter(float frameDurationCharacter) {
        this.frameDurationCharacter = frameDurationCharacter;
    }
     
     public Animation<TextureRegion> getCharacterAnimation() {
        return CharacterAnimation;
    }

    @Override
    public void create() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
        
        
}
