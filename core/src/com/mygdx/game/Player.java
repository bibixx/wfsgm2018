package com.mygdx.game;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;


class Player {
    private ShapeRenderer shapeRenderer;

    private Animator animator;

    private Vector2 position;
    private final float circleRadius = 20f;

    Player(ShapeRenderer shapeRenderer, Vector2 position, SpriteBatch batch) {
        this.shapeRenderer = shapeRenderer;
        this.position = position;

        //animator  = new Animator(batch, "spritesheet.png", 0.5f, 4, 1);
    }

    void render() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(position.x, position.y, circleRadius);
        shapeRenderer.end();

        /*animator.update();
        animator.render(position);*/
    }

    void changePostion(Vector2 newPostion){
        this.position = newPostion;
    }

    Vector2 getPosition(){
        return position;
    }

    void update() {
    }

    void dispose() {
        //animator.dispose();
    }
}
