package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class CelestialBody extends Entity {
    private Vector2 position;

    CelestialBody(World world, Vector2 position, int r, String spritename) {
        super(world, position, r, spritename);

        this.position = position;
    }

    public void render(Batch batch) {
        batch.draw(texture, position.x - w/2, position.y - h/2, w, h);
    }
}
