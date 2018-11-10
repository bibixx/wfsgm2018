package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class CelestialBody extends Entity {
    private Vector2 position;

    Texture glowTexture;
    Texture orbitTexture;

    CelestialBody(World world, Vector2 position, int r, String spritename) {
        super(world, position, r, spritename);

        this.position = position;

        glowTexture = new Texture(spritename + "-glow.png");
        orbitTexture = new Texture("planet1-orbit.png");
    }

    public void render(Batch batch) {
        batch.draw(glowTexture, position.x - w, position.y - h, w * 2, h * 2);

        batch.draw(texture, position.x - w/2, position.y - h/2, w, h);

        int wO = w * 3;
        int hO = h * 3;

        batch.draw(orbitTexture, position.x - wO/2, position.y - hO/2, wO, hO);
    }
}
