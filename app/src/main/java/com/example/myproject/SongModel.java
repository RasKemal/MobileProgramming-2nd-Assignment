package com.example.myproject;

import java.io.Serializable;

public class SongModel implements Serializable {
    String title;
    String duration;
    String path;
    String id;
    String artist;


    public SongModel(String path, String title, String duration, String id,String artist){
        this.title = title;
        this.duration = duration;
        this.path = path;
        this.id = id;
        this.artist = artist;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String length) {
        this.duration = length;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
