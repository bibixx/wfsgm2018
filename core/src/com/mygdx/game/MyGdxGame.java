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
    private SpriteBatch grainBatch;

	private BitmapFont font;

	private Texture backgroundTexture;
	private Texture grainTexture;

	private ShapeRenderer shapeRenderer;
	private Player player;

	SoundManager soundManager;

	private AsteroidsSensors sensors;

	EntityContainer entityContainer;

	private LevelLoader levelLoader;

	private int currentLevel = 0;

	Animator animator;

	Level level;

	@Override
	public void create () {
        batch = new SpriteBatch();
        backgroundBatch = new SpriteBatch();
        grainBatch = new SpriteBatch();

        soundManager = new SoundManager();
        levelLoader = new LevelLoader();

        grainTexture = new Texture("grain-sprite-alpha.png");

        soundManager.addSound("fail", "sound1.mp3");
        soundManager.addSound("success", "sound1.mp3");
        soundManager.addSound("fail-out-of-bounds", "sound1.mp3");

        animator = new Animator(grainBatch, "grain-sprite-alpha.png", 1/16f, 4, 1, 0, 1);

        font = new BitmapFont();
		shapeRenderer = new ShapeRenderer();

        AsteroidData[][] levels = {
            new AsteroidData[]{
                new AsteroidData(new Vector2(-300, 100), 32, "planet1"),
                new AsteroidData(new Vector2(100, 300), 64, "planet2"),
                new AsteroidData(new Vector2(400, -50), 56, "planet3"),
                new AsteroidData(new Vector2(-400, -300), 64, "planet2"),
                new AsteroidData(new Vector2(-600, 450), 64, "planet1"),
                new AsteroidData(new Vector2(550, 500), 32, "planet3")
            },
            new AsteroidData[]{
                new AsteroidData(new Vector2(500, 450), 16, "planet1"),
                new AsteroidData(new Vector2(400, 350), 32, "planet2"),
                new AsteroidData(new Vector2(230, 180), 48, "planet3"),
                new AsteroidData(new Vector2(-350, 240), 64, "planet1"),
                new AsteroidData(new Vector2(800, -150), 32, "planet2"),
                new AsteroidData(new Vector2(-450, -200), 16, "planet3"),
                new AsteroidData(new Vector2(-300, -160), 16, "planet2"),
                new AsteroidData(new Vector2(-580, -120), 25, "planet1"),
                new AsteroidData(new Vector2(-530, -50), 16, "planet3"),
                new AsteroidData(new Vector2(-800, 200), 45, "planet2"),
                new AsteroidData(new Vector2(0, 350), 32, "planet2"),
                new AsteroidData(new Vector2(150, -260), 80, "planet2")
            },
        };

        for(AsteroidData[] iter : levels) {
			level = new Level(
					new Vector2(-Gdx.graphics.getWidth() / 2 + 64, 0),
					new Vector2(5.3f, 0),
					iter,
					"bg.jpg"
			);
			levelLoader.addLevel(level);
		}

		levelLoader.setCurrentLevel(currentLevel);

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

		spawnLevel(levelLoader.getCurrentLevel());
	}

	private void spawnLevel(Level level) {
        player = new Player(world, batch, level.playerPosition, level.initialPlayerVelocity, (int)(64f * (61f / 118f)), 64, "laika", soundManager);

        backgroundTexture = new Texture(level.backgroundPath);

		player.getBody().setUserData("player");

		entityContainer = new EntityContainer();

		for(int i = 0; i < level.asteroidsData.length; i++) {
			AsteroidData data = level.asteroidsData[i];
			Entity asteroid = new CelestialBody(world, batch, new Vector2(data.position), data.radius, data.texturePath);
			String key = "asteroid-"+(i<10 ? "0"+i : i);
			entityContainer.addEntity(key, asteroid);
		}

		Runnable restoreLvlCb = () -> {
            dispose();
		    create();
        };

		Runnable loadNextLvlCb = () -> {
            soundManager.playSound("success");

            currentLevel++;
            dispose();
            create();
        };
		sensors = new AsteroidsSensors(world, player, entityContainer, restoreLvlCb, loadNextLvlCb, soundManager);
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
        if(Gdx.input.isKeyJustPressed(Input.Keys.R)) create();
        if(Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            currentLevel++;
            create();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            currentLevel--;
            create();
        }

        grainBatch.begin();
        animator.render(new Vector2(0, 0), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        grainBatch.end();


//        grainBatch.begin();
//        grainBatch.draw(grainTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        grainBatch.end();
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
