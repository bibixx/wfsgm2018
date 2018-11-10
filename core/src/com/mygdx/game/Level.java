package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

class Level {
    Vector2 playerPosition;
    Vector2 initialPlayerVelocity;
    Vector2[] obstaclesPosition;
    Texture backgroundTexture;

    Level(Vector2 playerPosition, Vector2 playerVelocity, Vector2[] obstaclesPosition, String backgroundPath){
        this.playerPosition = playerPosition;
        this.initialPlayerVelocity = playerVelocity;
        this.obstaclesPosition = obstaclesPosition;
        this.backgroundTexture = new Texture(Gdx.files.internal(backgroundPath));
    }
}
