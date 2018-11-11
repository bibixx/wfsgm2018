package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap;

public class SoundManager {

    private Music backgroundMusic1;
    private Music backgroundMusic2;

    private ArrayMap<String, Sound> soundMap;

    private boolean isBackgroundPlaying;

    SoundManager(){
        soundMap = new ArrayMap<String, Sound>();
        isBackgroundPlaying = false;
    }

    void setBackgroundMusic(String musicPath1, String musicPath2){
        backgroundMusic1 = Gdx.audio.newMusic(Gdx.files.internal(musicPath1));
        backgroundMusic1.setVolume(0.5f);

        backgroundMusic2 = Gdx.audio.newMusic(Gdx.files.internal(musicPath2));
        backgroundMusic2.setVolume(0.5f);
    }

    void addSound(String soundName, String soundPath){
        Sound newSound = Gdx.audio.newSound(Gdx.files.internal(soundPath));
        soundMap.put(soundName, newSound);
    }

    void playSound(String soundName){
        soundMap.get(soundName).play(1f);
    }

    void playBackgroundMusic(){
        if (isBackgroundPlaying) {
            return;
        }

        isBackgroundPlaying = true;
        backgroundMusic1.play();

        backgroundMusic1.setOnCompletionListener((Music music) -> {
            System.out.println(1);
            backgroundMusic2.play();
        });

        backgroundMusic2.setLooping(true);
//        if(!backgroundMusic.isLooping())
//            backgroundMusic.setLooping(true);
    }

    void stopBackgroundMusic(){
        backgroundMusic1.stop();
        backgroundMusic2.stop();
        isBackgroundPlaying = false;
    }

    void dispose(){
        if(!(backgroundMusic1 == null))
            backgroundMusic1.dispose();

        if(!(backgroundMusic2 == null))
            backgroundMusic2.dispose();

        for(ObjectMap.Entry<String, Sound> iter : soundMap.entries()){
            iter.value.dispose();
        }
    }
}
