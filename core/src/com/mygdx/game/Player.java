package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends Entity {

    private Animator animator;

    private Vector2 initialPosition;
    private Vector2 initialSpeed;
    private SoundManager soundManager;

    private boolean isDead;

    Player(World world, Batch batch, Vector2 position, Vector2 speed, int w, int h, String spritename, SoundManager soundManager){
        super(world, batch, position, w, h, spritename);
        this.soundManager = soundManager;
        initialPosition = position;
        initialSpeed = speed;
        body.setLinearVelocity(speed.x, speed.y);
        animator = new Animator(batch, "death.png", 1/20f, 56, 2, 0, 1);

        isDead = false;
    }



    void deathAnimation(){
        soundManager.playSound("fail");

        isDead = true;

        animator.setDrawingSpriteSheet(56, 2, 1, 1);
    }

    @Override
    public void render(){
        if (!isDead) {
            super.render();
        }

        Vector2 fixedPosition = new Vector2(body.getPosition().x * PPM - w * 3 /2, body.getPosition().y * PPM - h/2);
        animator.render(fixedPosition, w * 3, h);
    }

    @Override
    public void dispose(){
        super.dispose();
        animator.dispose();
    }
}
