package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class MyGdxGame extends Game {
	public static final double CONST_G = -6.67 * Math.pow(10, 1);
	public static final float PPM = 32;

	private boolean DEBUG = true;

	private OrthographicCamera camera;

	private Box2DDebugRenderer b2dr;
	private World world;
//	private CelestialBody player;
//	private CelestialBody planet;

	private SpriteBatch batch;
	private BitmapFont font;

	private int screenWidth, screenHeight;

	private ShapeRenderer shapeRenderer;
	private Entity player;
	private Entity planet;

	private AsteroidsSensors sensors;

    public MyGdxGame(int screenWidth, int screenHeight){
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		shapeRenderer = new ShapeRenderer();

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w/2, h/2);
		Vector3 position = camera.position;
		position.x = 0;
		position.y = 100;
		camera.position.set(position);

		world = new World(new Vector2(0, 0f), false);
		b2dr = new Box2DDebugRenderer();

		planet = new Entity(batch, shapeRenderer, "spritesheet.png");
		planet.createBody(world, 0, 100, 64);

		player = new Entity(batch, shapeRenderer, "spritesheet.png");
		player.createBody(world, -300, 200, 32);

		sensors = new AsteroidsSensors(world, player.getBody(), planet.getBody());

		world.setContactListener(sensors);

		player.getBody().setLinearVelocity(6f, 0);
	}

	@Override
	public void render() {
		super.render();
		update(Gdx.graphics.getDeltaTime());

		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (DEBUG) {
			b2dr.render(world, camera.combined.cpy().scl(PPM));
		}

        batch.setProjectionMatrix(camera.combined.cpy());

        shapeRenderer.setProjectionMatrix(camera.combined.cpy().scl(1f));

        player.update();
        planet.update();

        player.render();
        planet.render();


        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
	}

	@Override
	public void dispose() {
		world.dispose();
		b2dr.dispose();
		batch.dispose();
		font.dispose();
		player.dispose();
		shapeRenderer.dispose();
	}

	public void update(float delta) {
		world.step(delta, 6, 2);

		sensors.update();

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
			player.getBody().applyForceToCenter(0, 300, false);
		}

		player.getBody().setLinearVelocity(horizontalForce * 5, player.getBody().getLinearVelocity().y);
	}

	public void cameraUpdate() {
		Vector3 position = camera.position;

		position.x = player.getBody().getPosition().x * PPM;
		position.y = player.getBody().getPosition().y * PPM;
		camera.position.set(position);

		camera.update();
	}
}
