package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PlayerActivity extends AppCompatActivity {
    ImageView nextBtn,prevBtn,pausePlayBtn,albumPhoto;
    TextView songName, start, stop,artistName;
    SeekBar seekBar;
    SongModel curSong;
    MediaPlayer media_player = Player.getInstance();
    ArrayList<SongModel> songList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        initVariables();
        initMusic();
        progressBar();

    }

    public void initVariables(){
        songName = findViewById(R.id.txtsn);
        songName.setSelected(true);
        artistName = findViewById(R.id.txtartist);
        start = findViewById(R.id.txtstart);
        stop = findViewById(R.id.txtstop);
        seekBar = findViewById(R.id.seekBar);
        pausePlayBtn = findViewById(R.id.pauseBtn);
        nextBtn = findViewById(R.id.nextBtn);
        prevBtn = findViewById(R.id.prevBtn);
        albumPhoto = findViewById(R.id.musicIcon);
        songList = (ArrayList<SongModel>) getIntent().getSerializableExtra("LIST");
    }

    void initMusic(){
        curSong = songList.get(Player.currentIndex);
        songName.setText(curSong.getTitle());
        artistName.setText(curSong.getArtist());
        stop.setText(milliSeconds(curSong.getDuration()));
        Uri albumUri = Uri.parse("content://media/external/audio/media/"+curSong.getId()+"/albumart");
        albumPhoto.setImageURI(albumUri);
        pausePlayBtn.setOnClickListener(v-> pause_play());
        nextBtn.setOnClickListener(v-> playNext());
        prevBtn.setOnClickListener(v-> playPrev());
        playSong();
    }

    public void progressBar(){
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(media_player!=null){
                    seekBar.setProgress(media_player.getCurrentPosition());
                    start.setText(milliSeconds(media_player.getCurrentPosition()+""));

                    if(media_player.isPlaying()){
                        pausePlayBtn.setImageResource(R.drawable.ic_pause);
                    }else{
                        pausePlayBtn.setImageResource(R.drawable.ic_play);
                    }
                }
                new Handler().postDelayed(this,100);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(media_player!=null && b){
                    media_player.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }




    private void playSong(){
        media_player.reset();
        try {
            media_player.setDataSource(curSong.getPath());
            media_player.prepare();
            media_player.start();
            seekBar.setProgress(0);
            seekBar.setMax(media_player.getDuration());
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private void playNext(){
        if(Player.currentIndex == songList.size()-1){
            return;
        }

        Player.currentIndex += 1;
        media_player.reset();
        initMusic();
    }

    private void playPrev(){
        if(Player.currentIndex == 0){
            return;
        }

        Player.currentIndex -= 1;
        media_player.reset();
        initMusic();
    }

    private void pause_play(){
        if(media_player.isPlaying()){
            media_player.pause();
        }else{
            media_player.start();
        }
    }

    public static String milliSeconds(String duration){
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
}

