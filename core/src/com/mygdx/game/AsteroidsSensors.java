package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Timer;

public class AsteroidsSensors implements ContactListener {

    private World world;
    private Player player;
    private ArrayMap<String, Body> sensors;
    private boolean isInGravityField = false;
    private EntityContainer entityContainer;
    private Body activeAsteroidBody;
    private boolean shouldRestorePlayer;
    private Runnable restoreLvlCb;
    private boolean didPlayerHitCelestial;
    private Runnable loadNextLvlCb;
    private boolean shouldLoadNextLvl;
    private SoundManager soundManager;

    public AsteroidsSensors(World _world, Player _player, EntityContainer _entityContainer, Runnable _restoreLblCb, Runnable _loadNextLevelCb, SoundManager soundManager) {
        world = _world;
        player = _player;
        entityContainer = _entityContainer;
        restoreLvlCb = _restoreLblCb;
        sensors = new ArrayMap<String, Body>();
        loadNextLvlCb = _loadNextLevelCb;
        this.soundManager = soundManager;

        for (String key: entityContainer.getKeys()) {
            Entity asteroid = entityContainer.getEntity(key);
            String sensorId = "asteroid-sensor-" + key.substring(key.length() - 2);

            Body sensor = createCircleSensor(
                    (int)(asteroid.getBody().getPosition().x * MyGdxGame.PPM),
                    (int)(asteroid.getBody().getPosition().y * MyGdxGame.PPM),
                    asteroid.getRadius() * 3,
                    sensorId
            );

            sensors.put(sensorId, sensor);
        }
        int halfScreenW = Gdx.graphics.getWidth() / 2;
        int halfScreenH = Gdx.graphics.getHeight() / 2;

        createBoundarySensor(new Vector2(-halfScreenW - 10, -halfScreenH), new Vector2(-halfScreenW - 10, halfScreenH), "wall");
        createBoundarySensor(new Vector2(-halfScreenW, halfScreenH + 10), new Vector2(halfScreenW, halfScreenH + 10), "wall");
        createBoundarySensor(new Vector2(-halfScreenW, -halfScreenH - 10), new Vector2(halfScreenW, -halfScreenH - 10), "wall");
        createBoundarySensor(new Vector2(halfScreenW + 64, halfScreenH), new Vector2(halfScreenW + 64, -halfScreenH), "gate");
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

    private Body createBoundarySensor(Vector2 from, Vector2 to, String userData) {
        final float PPM = MyGdxGame.PPM;
        Body pBody;
        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.StaticBody;

        def.position.set(0,0);
        def.fixedRotation = true;
        pBody = world.createBody(def);

        EdgeShape shape = new EdgeShape();
        shape.set(from.cpy().scl(1 / PPM),to.cpy().scl(1 / PPM));

        FixtureDef fdef = new FixtureDef();
        fdef.isSensor = true;
        fdef.shape = shape;
        pBody.createFixture(fdef);
        shape.dispose();

        pBody.setUserData(userData);
        return pBody;
    }

    private boolean doesContactContain(Contact contact, String userData) {
        Object fixtureDataA = contact.getFixtureA().getBody().getUserData();
        Object fixtureDataB = contact.getFixtureB().getBody().getUserData();

        return (fixtureDataA.equals(userData) || fixtureDataB.equals(userData));
    }

    private Object findOrbitSensorInContact(Contact contact) {
        Object fixtureDataA = contact.getFixtureA().getBody().getUserData();
        Object fixtureDataB = contact.getFixtureB().getBody().getUserData();

        String fixtureNameA = fixtureDataA.toString();
        String fixtureNameB = fixtureDataB.toString();

        if(fixtureNameA.contains("asteroid-sensor")) {
            return fixtureNameA.substring(fixtureNameA.length()-2);
        } else if(fixtureNameB.contains("asteroid-sensor")) {
            return fixtureNameB.substring(fixtureNameB.length()-2);
        }

        return null;
    }


    public void beginContact(Contact contact) {
        if(!doesContactContain(contact, "player")) {
            return;
        }

        Object sensorId = findOrbitSensorInContact(contact);
        if(sensorId != null) {
            handlePlayerSensorContactBegin(sensorId.toString());
            return;
        }

        if(doesContactContain(contact, "gate")) {
            shouldLoadNextLvl = true;
            return;
        }

        if (doesContactContain(contact, "wall")){
            didPlayerHitCelestial = false;
        } else {
            didPlayerHitCelestial = true;
            player.getBody().setLinearVelocity(0, 0);
        }

        isInGravityField = false;
        shouldRestorePlayer = true;
    }

    private void handlePlayerSensorContactBegin(String bodyId) {
        activeAsteroidBody = entityContainer.getEntity("asteroid-" + bodyId).getBody();

        isInGravityField = true;
    }

    @Override
    public void endContact(Contact contact) {
        if(!doesContactContain(contact, "player")) {
            return;
        }

        if(findOrbitSensorInContact(contact) == null) {
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
        if(shouldRestorePlayer) {
            if(didPlayerHitCelestial) {
                player.deathAnimation();
            } else {
                soundManager.playSound("fail-out-of-bounds");
            }

            shouldRestorePlayer = false;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    restoreLvlCb.run();
                }
            }, 2.5f);
            return;
        }

        if(shouldLoadNextLvl) {
            shouldLoadNextLvl = false;
            loadNextLvlCb.run();
        }

        if(!isInGravityField) {
            return;
        }


        double dX = player.getBody().getPosition().x - activeAsteroidBody.getPosition().x;
		double dY = player.getBody().getPosition().y - activeAsteroidBody.getPosition().y;

		double distanceSquared = Math.abs(
				Math.pow(dX, 2)
			  + Math.pow(dY, 2)
		);

		double planetMass = activeAsteroidBody.getMass();
		double playerMass = player.getBody().getMass();

		double force = MyGdxGame.CONST_G * planetMass * playerMass /2
					 / distanceSquared;

		if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
		    force *= -1;
        }

		double forceX = (force / Math.sqrt(distanceSquared)) * dX;
        double forceY = (force / Math.sqrt(distanceSquared)) * dY;


        player.getBody().applyForceToCenter((float)forceX, (float)forceY, true);
    }
}
