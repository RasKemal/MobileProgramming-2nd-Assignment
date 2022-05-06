package com.example.myproject;

import android.media.MediaPlayer;
import android.provider.MediaStore;

public class Player {
    static MediaPlayer instance;

    public static MediaPlayer getInstance(){
        if(instance == null){
            instance = new MediaPlayer();
        }
        return instance;
    }

    public static int currentIndex = -1;
}
