package com.example.dto;

import android.media.MediaPlayer;
import android.widget.SeekBar;

public class CurrentMusic {
   private  MusicInfo musicInfo;
    private int index;
    private int maxSec;
    private int currentSec;

    public int getMaxSec() {
        return maxSec;
    }

    public void setMaxSec(int maxSec) {
        this.maxSec = maxSec;
    }

    public int getCurrentSec() {
        return currentSec;
    }

    public void setCurrentSec(int currentSec) {
        this.currentSec = currentSec;
    }

    public CurrentMusic(MusicInfo musicInfo, int index) {
        this.musicInfo = musicInfo;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }



    public MusicInfo getMusicInfo() {
        return musicInfo;
    }

    public void setMusicInfo(MusicInfo musicInfo) {
        this.musicInfo = musicInfo;
    }



}
