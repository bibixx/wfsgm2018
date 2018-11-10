package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;

public class AsteroidsSensors implements ContactListener {
    private World world;
    private Body player;
    private Body asteroid;
    private Body sensor;
    private boolean isInGravityField = false;

    public AsteroidsSensors(World _world, Body _player, Body _asteroid) {
        world = _world;
        player = _player;
        asteroid = _asteroid;

        int sensorWidth = 192;
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

    private boolean isContactBetweenPlayerAndSensor(Contact contact) {
        Object fixtureDataA = contact.getFixtureA().getBody().getUserData();
        Object fixtureDataB = contact.getFixtureB().getBody().getUserData();

        return (fixtureDataA.equals("asteroid-sensor") && fixtureDataB.equals("player"))
            || (fixtureDataB.equals("asteroid-sensor") && fixtureDataA.equals("player"));
    }

    public void beginContact(Contact contact) {
        if(!isContactBetweenPlayerAndSensor(contact)) {
            return;
        }

        isInGravityField = true;
    }

    @Override
    public void endContact(Contact contact) {
        if(!isContactBetweenPlayerAndSensor(contact)) {
            return;
        }

        isInGravityField = false;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    public void update() {
        if(!isInGravityField) {
            return;
        }

        double dX = player.getPosition().x - asteroid.getPosition().x;
		double dY = player.getPosition().y - asteroid.getPosition().y;

		double distanceSquared = Math.abs(
				Math.pow(dX, 2)
			  + Math.pow(dY, 2)
		);

		double planetMass = asteroid.getMass();
		double playerMass = player.getMass();

		double force = MyGdxGame.CONST_G * planetMass * playerMass /2
					 / distanceSquared;

		double forceX = (force / Math.sqrt(distanceSquared)) * dX;
        double forceY = (force / Math.sqrt(distanceSquared)) * dY;


        player.applyForceToCenter((float)forceX, (float)forceY, true);
    }
}
