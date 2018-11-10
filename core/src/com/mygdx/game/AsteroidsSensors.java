package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;

public class AsteroidsSensors implements ContactListener {
    private World world;
    private Body player;
    private Body asteroid;
    private Body sensor;

    public AsteroidsSensors(World _world, Body _player, Body _asteroid) {
        world = _world;
        player = _player;
        asteroid = _asteroid;

        int sensorWidth = 128;
        sensor = createCircleSensor((int)(_asteroid.getPosition().x * MyGdxGame.PPM), (int)(_asteroid.getPosition().y * MyGdxGame.PPM),sensorWidth);
    }

    public Body createCircleSensor(int x, int y, int width) {
        final float PPM = MyGdxGame.PPM;
        Body pBody;
        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.StaticBody;

        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = true;
        pBody = world.createBody(def);

        CircleShape shape = new CircleShape();
        shape.setRadius(width / 2f / PPM);

        FixtureDef fdef = new FixtureDef();
        fdef.isSensor = true;
        fdef.shape = shape;
        pBody.createFixture(fdef);
        shape.dispose();

        pBody.setUserData("asteroid-sensor");
        return pBody;
    }

    public void beginContact(Contact contact) {
        Object fixtureDataA = contact.getFixtureA().getBody().getUserData();
        Object fixtureDataB = contact.getFixtureB().getBody().getUserData();

        if(!(fixtureDataA.equals("asteroid-sensor") && fixtureDataB.equals("player"))
          && !(fixtureDataB.equals("asteroid-sensor") && fixtureDataA.equals("player"))) {
            System.out.println("not interested: " + fixtureDataA + " | " + fixtureDataB);
            return;
        }

        System.out.println(fixtureDataA + " | " + fixtureDataB);
    }

    @Override
    public void endContact(Contact contact) {
//        Fixture fixtureA = contact.getFixtureA();
//        Fixture fixtureB = contact.getFixtureB();
//        Gdx.app.log("endContact", "between " + fixtureA.toString() + " and " + fixtureB.toString());
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}
