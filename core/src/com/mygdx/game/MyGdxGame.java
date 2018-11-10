package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class MyGdxGame extends Game {
	public static final double CONST_G = -6.67 * Math.pow(10, 1);
	public static final float PPM = 32;

	private boolean DEBUG = true;

	private OrthographicCamera camera;

	private Box2DDebugRenderer b2dr;
	private World world;

    private SpriteBatch batch;
    private SpriteBatch backgroundBatch;
	private BitmapFont font;

	private Texture backgroundTexture;

	private ShapeRenderer shapeRenderer;
	private Player player;

	private AsteroidsSensors sensors;

	EntityContainer entityContainer;

	Level level;

	@Override
	public void create () {
        batch = new SpriteBatch();
        backgroundBatch = new SpriteBatch();
		font = new BitmapFont();
		shapeRenderer = new ShapeRenderer();

		AsteroidData[] asteroidData = {
				new AsteroidData(new Vector2(-300, 100), 32, "planet1"),
				new AsteroidData(new Vector2(100, 300), 64, "planet2"),
				new AsteroidData(new Vector2(400, -50), 56, "planet3"),
				new AsteroidData(new Vector2(-400, -300), 64, "planet2"),
				new AsteroidData(new Vector2(-600, 450), 64, "planet1"),
				new AsteroidData(new Vector2(550, 500), 32, "planet3")
		};

		level = new Level(
				new Vector2(- Gdx.graphics.getWidth() / 2 + 64 , 0),
                new Vector2(5.3f, 0),
				asteroidData,
				"bg.jpg"
		);

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);
		Vector3 position = camera.position;
		position.x = 0;
		position.y = 0;
		camera.position.set(position);

		world = new World(new Vector2(0, 0f), false);
		b2dr = new Box2DDebugRenderer();

		spawnLevel(level);
	}

	private void spawnLevel(Level level) {
        player = new Player(world, batch, level.playerPosition, level.initialPlayerVelocity, (int)(64f * (61f / 118f)), 64, "laika");

        backgroundTexture = new Texture(level.backgroundPath);

		player.getBody().setUserData("player");

		entityContainer = new EntityContainer();

		for(int i = 0; i < level.asteroidsData.length; i++) {
			AsteroidData data = level.asteroidsData[i];
			Entity asteroid = new CelestialBody(world, batch, new Vector2(data.position), data.radius, data.texturePath);
			entityContainer.addEntity("asteroid-"+i, asteroid);
		}

		Runnable restoreLvlCb = () -> create();
		Runnable loadNextLvlCb = () -> System.out.println("not yet");
		sensors = new AsteroidsSensors(world, player, entityContainer, restoreLvlCb, loadNextLvlCb);
		world.setContactListener(sensors);
	}

	@Override
	public void render() {
		super.render();
		update(Gdx.graphics.getDeltaTime());

		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        backgroundBatch.begin();
        backgroundBatch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundBatch.end();

		if (DEBUG) {
			b2dr.render(world, camera.combined.cpy().scl(PPM));
		}

        batch.setProjectionMatrix(camera.combined.cpy());
		batch.begin();

        player.update();
		for(Entity asteroid : entityContainer.getValues()) {
			asteroid.update();
		}

        player.render();

        for(Entity asteroid : entityContainer.getValues()) {
        	asteroid.render();
		}

		batch.end();


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
}
