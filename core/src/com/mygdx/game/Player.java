package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends Entity {

    private Animator animator;

    private Vector2 initialPosition;
    private Vector2 initialSpeed;
    private SoundManager soundManager;

    Player(World world, Batch batch, Vector2 position, Vector2 speed, int w, int h, String spritename, SoundManager soundManager){
        super(world, batch, position, w, h, spritename);
        this.soundManager = soundManager;
        initialPosition = position;
        initialSpeed = speed;
        body.setLinearVelocity(speed.x, speed.y);
        animator = new Animator(batch, "spritesheet.png", 0.5f, 4, 2, 0, 1);
    }

    void deathAnimation(){
        soundManager.playSound("fail");

        animator.setDrawingSpriteSheet(4, 2, 1, 1);
    }

    @Override
    public void render(){
        super.render();
        Vector2 fixedPosition = new Vector2(body.getPosition().x * PPM - w/2, body.getPosition().y * PPM - h/2);
        animator.render(fixedPosition, w, h);
}

    @Override
    public void dispose(){
        super.dispose();
        animator.dispose();
    }
}
