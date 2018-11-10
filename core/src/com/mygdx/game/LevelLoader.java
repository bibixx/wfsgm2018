package com.mygdx.game;

import com.badlogic.gdx.utils.Queue;

class LevelLoader {

    private Queue<Level> levelsQueue;

    private Level currentLevel;

    LevelLoader(){
        levelsQueue = new Queue<Level>();
    }

    Level nextLevel(){
        return levelsQueue.last();
    }

    void changeCurrentLevel(){
        currentLevel = levelsQueue.removeLast();
    }

    Level getCurrentLevel(){
        return currentLevel;
    }

    void addLevel(Level newLevel){
        levelsQueue.addFirst(newLevel);
    }
}
