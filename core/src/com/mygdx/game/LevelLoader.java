package com.mygdx.game;

import com.badlogic.gdx.utils.Array;

class LevelLoader {

    private Array<Level> levelsContainer;

    private Level currentLevel;

    LevelLoader(){
        levelsContainer = new Array<Level>();
    }

    void setCurrentLevel(int levelIndex){
        currentLevel = levelsContainer.get(levelIndex);
    }

    Level getCurrentLevel(){
        return currentLevel;
    }

    void addLevel(Level newLevel){
        levelsContainer.add(newLevel);
    }

    Array<Level> getLevelArray(){
        return levelsContainer;
    }
}
