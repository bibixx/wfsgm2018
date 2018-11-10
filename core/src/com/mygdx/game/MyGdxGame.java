package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;

public class MyGdxGame extends ApplicationAdapter {
	public static final float PPM = 32;
	private boolean DEBUG = false;

	private OrthographicCamera camera;

	private Box2DDebugRenderer b2dr;
	private World world;
	private Body player;
	private Body planet;

    double constG = -6.67 * Math.pow(10, 1);

    @Override
	public void create () {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w/2, h/2);
		Vector3 position = camera.position;
		position.x = 0;
		position.y =  100;
		camera.position.set(position);

		world = new World(new Vector2(0, 0f), false);
		b2dr = new Box2DDebugRenderer();

		player = createCircle(0, 200, 32, false);
		planet = createCircle(0, 100, 64, false);

        player.setLinearVelocity(
            (float)Math.sqrt(
                    Math.abs(constG * planet.getMass()) / ((200 - 100) / PPM)
            )
        , 0f);

    }

	@Override
	public void render() {
		update(Gdx.graphics.getDeltaTime());

		// Render
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		b2dr.render(world, camera.combined.scl(PPM));

		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
	}


	@Override
	public void dispose() {
		world.dispose();
		b2dr.dispose();
	}

	public void update(float delta) {
		world.step(1 / 60f, 6, 2);

		//inputUpdate(delta);

		double dX = player.getPosition().x - planet.getPosition().x;
		double dY = player.getPosition().y - planet.getPosition().y;

		double distanceSquared = Math.abs(
				Math.pow(dX, 2)
			  + Math.pow(dY, 2)
		);

		double planetMass = planet.getMass();
		double playerMass = player.getMass();

		double force = constG * planetMass * playerMass
					 / distanceSquared;

		double forceX = (force / Math.sqrt(distanceSquared)) * dX;
        double forceY = (force / Math.sqrt(distanceSquared)) * dY;


        player.applyForceToCenter((float)forceX, (float)forceY, true);


        System.out.println(force);


		camera.update();
	}

	public void inputUpdate(float delta) {
		int horizontalForce = 0;

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			horizontalForce -= 1;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			horizontalForce += 1;
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			player.applyForceToCenter(0, 300, false);
		}

		player.setLinearVelocity(horizontalForce * 5, player.getLinearVelocity().y);
	}

	public void cameraUpdate() {
		Vector3 position = camera.position;
		position.x = player.getPosition().x * PPM;
		position.y = player.getPosition().y * PPM;
		camera.position.set(position);

		camera.update();
	}

	public Body createCircle(int x, int y, int width, boolean isStatic) {
		Body pBody;
		BodyDef def = new BodyDef();

		if(isStatic)
			def.type = BodyDef.BodyType.StaticBody;
		else
			def.type = BodyDef.BodyType.DynamicBody;

		def.position.set(x / PPM, y / PPM);
		def.fixedRotation = true;
		pBody = world.createBody(def);

		CircleShape shape = new CircleShape();
		shape.setRadius(width / 2 / PPM);

		pBody.createFixture(shape, 1.0f);
		shape.dispose();
		return pBody;
	}
}
