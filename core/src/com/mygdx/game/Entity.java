package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Entity {
    static public final float PPM = MyGdxGame.PPM;
    Batch batch;
    Body body;
    Texture texture;
    int r;

    int w;
    int h;

    boolean inverted;

    Entity(World world, Batch batch, Vector2 position, int r, String spritename) {
        this.batch = batch;
        this.r = r;
        this.w = r * 2;
        this.h = r * 2;

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;

        def.position.set(position.x / PPM, position.y / PPM);
        def.fixedRotation = true;

        body = world.createBody(def);

        CircleShape shape = new CircleShape();
        shape.setRadius(r / PPM);

        body.createFixture(shape, 1.0f);
        shape.dispose();

        texture = new Texture(spritename + ".png");
    }


    Entity(World world, Batch batch, Vector2 position, int w, int h, String spritename) {
        this.batch = batch;
        this.w = w;
        this.h = h;

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(position.x / PPM, position.y / PPM);
        def.fixedRotation = true;

        body = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(w / 2 / PPM, h / 2 / PPM);

        body.createFixture(shape, 1.0f);
        shape.dispose();

        texture = new Texture(spritename + ".png");
    }

    public int getRadius() {
        return r;
    }

    public double getArea() {
        if(r!=0) {
            return Math.PI * Math.pow(r, 2);
        }
        return w * h;
    }

    public Body getBody() {
        return body;
    }

    public void render() {
        batch.draw(texture, body.getPosition().x * PPM - w/2, body.getPosition().y * PPM - h/2, w, h);
    }

    public void update() {};

    public void dispose() {};
}
