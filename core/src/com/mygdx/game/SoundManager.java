package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap;

public class SoundManager {

    private Music backgroundMusic;
    private ArrayMap<String, Sound> soundMap;

    SoundManager(){
        soundMap = new ArrayMap<String, Sound>();
    }

    void setBackgroundMusic(String musicPath){
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(musicPath));
        backgroundMusic.setVolume(0.5f);
    }

    void addSound(String soundName, String soundPath){
        Sound newSound = Gdx.audio.newSound(Gdx.files.internal(soundPath));
        soundMap.put(soundName, newSound);
    }

    void playSound(String soundName){
        soundMap.get(soundName).play(1f);
    }

    void playBackgroundMusic(){
        backgroundMusic.play();
        if(!backgroundMusic.isLooping())
            backgroundMusic.setLooping(true);
    }

    void stopBackgroundMusic(){
        backgroundMusic.stop();
    }

    void dispose(){
        if(!(backgroundMusic == null))
            backgroundMusic.dispose();

        for(ObjectMap.Entry<String, Sound> iter : soundMap.entries()){
            iter.value.dispose();
        }
    }
}
