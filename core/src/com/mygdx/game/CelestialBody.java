package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;

public class CelestialBody extends Entity {
    private Vector2 position;

    Texture invertedTexture;

    Texture glowTexture;
    Texture invertedGlowTexture;

    Texture orbitTexture;
    double glowOffset;


    CelestialBody(World world, Batch batch, Vector2 position, int r, String spritename) {
        super(world, batch, position, r, spritename);

        this.position = position;

        glowOffset = Math.random() * 40;

        invertedTexture = new Texture(spritename + "-invert.png");

        glowTexture = new Texture(spritename + "-glow.png");

        orbitTexture = new Texture("orbit.png");
    }

    public void render() {
        int wG = (int)(w * (1.33 + (Math.exp(Math.sin(TimeUtils.millis()/2000.0*Math.PI + glowOffset)) - 0.36787944)*0.05));
        int hG = (int)(h * (1.33 + (Math.exp(Math.sin(TimeUtils.millis()/2000.0*Math.PI + glowOffset)) - 0.36787944)*0.05));

        int wO = w * 3;
        int hO = h * 3;

        batch.draw(glowTexture, position.x - wG, position.y - hG, wG * 2, hG * 2);

        if (!Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            batch.draw(texture, position.x - w/2, position.y - h/2, w, h);
        } else {
            batch.draw(invertedTexture, position.x - w/2, position.y - h/2, w, h);
        }

        batch.draw(orbitTexture, position.x - wO/2, position.y - hO/2, wO, hO);
    }

    public void invert(boolean i) {
        this.inverted = i;
    }
}
