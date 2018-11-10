package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class AsteroidData {
    public Vector2 position;
    public int radius;
    public String texturePath;

    public AsteroidData(Vector2 _position, int _radius, String _texturePath) {
        position = _position;
        radius = _radius;
        texturePath = _texturePath;
    }
}
