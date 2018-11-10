package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends Entity {

    private Animator animator;

    private Vector2 initialPosition;

    Player(World world, Batch batch, Vector2 position, int w, int h, String spritename){
        super(world, batch, position, w, h, spritename);
        initialPosition = position;
        animator = new Animator(batch, "spritesheet.png", 0.5f, 4, 1);
    }

    void deathAnimation(){
        animator.setDrawingSpriteSheet(4, 1);
    }

    @Override
    public void render(){
        super.render();
        Vector2 fixedPosition = new Vector2(body.getPosition().x * PPM - w/2, body.getPosition().y * PPM - h/2);
        animator.render(fixedPosition);
    }

    Vector2 getInitialPosition(){
        return initialPosition;
    }

    @Override
    public void dispose(){
        super.dispose();
        animator.dispose();
    }
}
