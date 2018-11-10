package com.mygdx.game;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;


class Player {
    private ShapeRenderer shapeRenderer;

    private Animator animator;
    private CelestialBody body;

    private Vector2 position;

    Player(ShapeRenderer shapeRenderer, Vector2 position, SpriteBatch batch, CelestialBody cb) {
        this.shapeRenderer = shapeRenderer;
        this.position = position;

        body = cb;

        animator = new Animator(batch, "spritesheet.png", 0.5f, 4, 1);
    }

    void render() {
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(body.getBody().getPosition().x * 32, body.getBody().getPosition().y * 32, 16f);

        animator.update();
        animator.render(position);
    }

    void changePostion(Vector2 newPostion){
        this.position = newPostion;
    }

    public void orbit(CelestialBody body2) {
        body.orbit(body2);
    }

    public Body getBody() {
        return body.getBody();
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
