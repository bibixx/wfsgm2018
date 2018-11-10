package com.mygdx.game;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import javax.swing.text.Position;


class Player {
    private ShapeRenderer shapeRenderer;

    private Vector2 position;
    private final float radius = 20f;

    Player(ShapeRenderer shapeRenderer, Vector2 position) {
        this.shapeRenderer = shapeRenderer;
        this.position = position;
    }

    void render() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(position.x, position.y, radius);
        shapeRenderer.end();
    }

    void changePostion(Vector2 newPostion){
        this.position = newPostion;
    }

    Vector2 getPosition(){
        return position;
    }

    void update() {
    }

    void dispose() {
    }
}
