package com.example.dto;

import com.example.util.fileUtil;

import java.io.File;

public class MusicInfo {


   private String musicName ;

   private String musicPath;


    public String getMusicPath() {
        return musicPath;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    @Override
    public String toString() {
        return this.musicName;
    }

    public MusicInfo( String path ) throws Exception {
        this.musicPath = path;
        this.musicName = fileUtil.getFileName(path);
    }
}
