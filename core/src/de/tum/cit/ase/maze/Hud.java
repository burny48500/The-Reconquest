package de.tum.cit.ase.maze;

import java.util.ArrayList;
import java.util.List;

public class Hud {
    private int numberOfLives = 3;
    public void loseLive(){
        numberOfLives =- 1;
    }
    public void showLives(){

    }
    public Hud() {
        this.numberOfLives = 3;
    }

}