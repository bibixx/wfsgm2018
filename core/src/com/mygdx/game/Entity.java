package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;


class Entity {
    private static final float PPM = MyGdxGame.PPM;

    private ShapeRenderer shapeRenderer;

    private Animator animator;
    private CelestialBody body;

    private int r;

    Entity(SpriteBatch batch, ShapeRenderer shapeRenderer, String spritePath) {
        this.shapeRenderer = shapeRenderer;

        animator = new Animator(batch, spritePath, 0.5f, 4, 1);
    }

    public int getRadius() {return r;}

    public void createBody(World world, int x, int y, int d) {
        this.r = d / 2;

        this.body = new CelestialBody(world, x, y, d, false);
        body.getBody().setUserData("player");
    }

    void render() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(body.getBody().getPosition().x * PPM, body.getBody().getPosition().y * PPM, r);
        shapeRenderer.end();

        Vector2 position = new Vector2(body.getBody().getPosition().x * PPM, body.getBody().getPosition().y * PPM);

        animator.update();
        animator.render(position);
    }


    public Body getBody() {
        return body.getBody();
    }

    Vector2 getPosition(){
        return this.body.getBody().getPosition();
    }

    void update() {
    }

    void dispose() {
        animator.dispose();
    }
}

