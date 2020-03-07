package com.example.dto;

public class MusicInfo {

    /**
     * 播放进度:秒
     */
   private int currentSec;

    /**
     * 音乐长度：秒
     */
   private int musicMaxSec;

   //进度条最大长度
   private int SeekbarMaxSec;

   private String musicName ;

    public int getCurrentSec() {
        return currentSec;
    }

    public void setCurrentSec(int currentSec) {
        this.currentSec = currentSec;
    }

    public int getMusicMaxSec() {
        return musicMaxSec;
    }

    public void setMusicMaxSec(int musicMaxSec) {
        this.musicMaxSec = musicMaxSec;
    }

    public int getSeekbarMaxSec() {
        return SeekbarMaxSec;
    }

    public void setSeekbarMaxSec(int seekbarMaxSec) {
        SeekbarMaxSec = seekbarMaxSec;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }
}
