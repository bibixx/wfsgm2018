package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;

public class CelestialBody {
    private static final float PPM = MyGdxGame.PPM;
    private int r;

    private Body body;

    CelestialBody(World world, int initialX, int initialY, int diameter, boolean isStatic) {
        r = diameter / 2;

        BodyDef def = new BodyDef();

        if (isStatic) {
            def.type = BodyDef.BodyType.StaticBody;
        } else {
            def.type = BodyDef.BodyType.DynamicBody;
        }

        def.position.set(initialX / PPM, initialY / PPM);
        def.fixedRotation = true;

        body = world.createBody(def);

        CircleShape shape = new CircleShape();
        shape.setRadius(r / PPM);

        body.createFixture(shape, 1.0f);
        shape.dispose();
    }

    public void orbit(CelestialBody body2) {
        double dX = Math.abs(body2.getBody().getPosition().x - body.getPosition().x);
        double dY = Math.abs(body2.getBody().getPosition().y - body.getPosition().y);

        double orbitSize =
            Math.sqrt(
                Math.pow(dY, 2)
                + Math.pow(dX, 2)
            );

        double forceScalar = (float)Math.sqrt(
                Math.abs(MyGdxGame.CONST_G * body2.getBody().getMass()) / (orbitSize)
        );


        body.setLinearVelocity(
            (float)((dY / orbitSize) * forceScalar),
            (float)((dX / orbitSize) * forceScalar)
        );
    }

    public Body getBody() {
        return body;
    }
}
