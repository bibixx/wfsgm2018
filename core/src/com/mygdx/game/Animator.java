package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

class Animator {
    private Animation<TextureRegion> animation;
    private final Texture spriteSheet;
    private Batch batch;

    private float stateTime;

    private float frameDuration;

    Animator(Batch batch, String spritePath, float frameDuration, int frameCol, int frameRows){
        this.batch = batch;

        this.frameDuration = frameDuration;

        spriteSheet = new Texture(Gdx.files.internal(spritePath));

        setDrawingSpriteSheet(frameCol, frameRows);
    }

    void setDrawingSpriteSheet(int frameCol, int frameRows){
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth()/ frameCol, spriteSheet.getHeight()/ frameRows);
        TextureRegion[] animationFrames = new TextureRegion[frameCol * frameRows];

        int index = 0;

        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCol; j++) {
                animationFrames[index++] = tmp[i][j];
            }
        }

        animation = new Animation<TextureRegion>(frameDuration, animationFrames);

        stateTime = 0f;
    }

    void render(Vector2 position){
        stateTime += Gdx.graphics.getDeltaTime();

        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);

        batch.draw(currentFrame, position.x, position.y);
    }

    void update(){
    }

    void dispose(){
        spriteSheet.dispose();
    }
}
