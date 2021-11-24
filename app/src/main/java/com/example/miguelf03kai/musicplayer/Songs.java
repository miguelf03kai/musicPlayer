package com.example.miguelf03kai.musicplayer;

import android.net.Uri;

import java.io.Serializable;

public class Songs implements Serializable{

    private String artist;
    private String song;
    private String path;
    private String albumArt;
    private int index;

    public Songs(String artist, String path, String song,String albumArt) {
        this.artist = artist;
        this.path = path;
        this.song = song;
        this.albumArt = albumArt;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }
}