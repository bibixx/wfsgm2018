package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class MyGdxGame extends Game {
	private SpriteBatch batch;
	private BitmapFont font;

	private int screenWidth,
		screenHeight;

	private ShapeRenderer shapeRenderer;

	private Player player;

	public MyGdxGame(int screenWidth, int screenHeight){
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		shapeRenderer = new ShapeRenderer();

		player = new Player(shapeRenderer, new Vector2(150, 150), batch);
		//this.setScreen();  // <-- change current screen
}

	@Override
	public void render () {
		super.render();
		Gdx.gl.glClearColor(0, 0, 1, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		player.update();
		player.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
		player.dispose();
		shapeRenderer.dispose();
	}
}
