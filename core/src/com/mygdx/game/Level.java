package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

class Level {
    Vector2 playerPosition;
    Vector2 initialPlayerVelocity;
    AsteroidData[] asteroidsData;
    String backgroundPath;

    Level(Vector2 playerPosition, Vector2 playerVelocity, AsteroidData[] asteroidsData, String backgroundPath){
        this.playerPosition = playerPosition;
        this.initialPlayerVelocity = playerVelocity;
        this.asteroidsData = asteroidsData;
        this.backgroundPath = backgroundPath;
    }
}
