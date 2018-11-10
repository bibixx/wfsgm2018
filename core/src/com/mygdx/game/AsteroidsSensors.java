package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ArrayMap;

public class AsteroidsSensors implements ContactListener {
    private World world;
    private Body player;
    private ArrayMap<String, Body> sensors;
    private boolean isInGravityField = false;
    private EntityContainer entityContainer;
    private Body activeAsteroidBody;

    public AsteroidsSensors(World _world, Body _player, EntityContainer _entityContainer) {
        world = _world;
        player = _player;
        entityContainer = _entityContainer;
        sensors = new ArrayMap<String, Body>();

        for (String key: entityContainer.getKeys()) {
            Entity asteroid = entityContainer.getEntity(key);
            String sensorId = "asteroid-sensor-" + key.charAt(key.length() - 1);

            Body sensor = createCircleSensor(
                    (int)(asteroid.getPosition().x * MyGdxGame.PPM),
                    (int)(asteroid.getPosition().y * MyGdxGame.PPM),
                    asteroid.getRadius() * 3,
                    sensorId
            );

            sensors.put(sensorId, sensor);
        }
    }

    private Body createCircleSensor(int x, int y, int radius, String userData) {
        final float PPM = MyGdxGame.PPM;
        Body pBody;
        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.StaticBody;

        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = true;
        pBody = world.createBody(def);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius / PPM);

        FixtureDef fdef = new FixtureDef();
        fdef.isSensor = true;
        fdef.shape = shape;
        pBody.createFixture(fdef);
        shape.dispose();

        pBody.setUserData(userData);
        return pBody;
    }

    private boolean isPlayerInContact(Contact contact) {
        Object fixtureDataA = contact.getFixtureA().getBody().getUserData();
        Object fixtureDataB = contact.getFixtureB().getBody().getUserData();

        return (fixtureDataA.equals("player") || fixtureDataB.equals("player"));
    }

    private Object findSensorInContact(Contact contact) {
        Object fixtureDataA = contact.getFixtureA().getBody().getUserData();
        Object fixtureDataB = contact.getFixtureB().getBody().getUserData();

        String fixtureNameA = fixtureDataA.toString();
        String fixtureNameB = fixtureDataB.toString();

        if(fixtureNameA.contains("asteroid-sensor")) {
            return fixtureNameA.charAt(fixtureNameA.length() - 1);
        } else if(fixtureNameB.contains("asteroid-sensor")) {
            return fixtureNameB.charAt(fixtureNameB.length() -1);
        }

        return null;
    }

    public void beginContact(Contact contact) {
        if(!isPlayerInContact(contact)) {
            return;
        }

        Object sensorId = findSensorInContact(contact);
        if(sensorId == null) {
            return;
        }
        
        String bodyId = sensorId.toString();

        activeAsteroidBody = entityContainer.getEntity("asteroid-" + bodyId).getBody();

        isInGravityField = true;
    }

    @Override
    public void endContact(Contact contact) {
        if(!isPlayerInContact(contact)) {
            return;
        }

        if(findSensorInContact(contact) == null) {
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

        double dX = player.getPosition().x - activeAsteroidBody.getPosition().x;
		double dY = player.getPosition().y - activeAsteroidBody.getPosition().y;

		double distanceSquared = Math.abs(
				Math.pow(dX, 2)
			  + Math.pow(dY, 2)
		);

		double planetMass = activeAsteroidBody.getMass();
		double playerMass = player.getMass();

		double force = MyGdxGame.CONST_G * planetMass * playerMass /2
					 / distanceSquared;

		double forceX = (force / Math.sqrt(distanceSquared)) * dX;
        double forceY = (force / Math.sqrt(distanceSquared)) * dY;


        player.applyForceToCenter((float)forceX, (float)forceY, true);
    }
}
