package com.example.service;

import android.media.MediaPlayer;
import android.os.Environment;
import android.widget.Button;

import com.example.state.play;
import com.example.state.state;
import com.example.state.stop;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;


public class MusicService {
    private File SDCardRood = Environment.getExternalStorageDirectory();

    public List<String> getMusicPaths() {
        return musicPaths;
    }

    public void setMusicPaths(List<String> musicPaths) {
        this.musicPaths = musicPaths;
    }

    private List<String> musicPaths;
    public MediaPlayer mediaPlayer;
    public int currentMusic;
    public String songName;

    public state getCurrentState() {
        return currentState;
    }

    public void setCurrentState(state currentState) {
        this.currentState = currentState;
    }

    //播放器状态，当前是停止播放状态
    public state currentState = new stop();


    //初始化音乐列表并且
    public MusicService() {
        musicPaths = new ArrayList<>();
        //假设音乐文件夹就在根目录下面的music文件夹
        File[] arr = SDCardRood.listFiles();
        Collection<File> musicFiles = FileUtils.listFiles(SDCardRood, new IOFileFilter() {
            @Override
            public boolean accept(File file) {
                return   file.getName().endsWith(".mp3");
            }
            @Override
            public boolean accept(File dir, String name) {
                return true;
            }
        }, TrueFileFilter.TRUE);

        if (musicFiles.size() == 0) {
            return;
        }
        //目前只支持MP3格式啦
        for (File file : musicFiles) {
            if (!file.getName().endsWith(".mp3")) {
                continue;
            }
            musicPaths.add(file.getAbsolutePath());
        }
        mediaPlayer =new MediaPlayer();
    }



    public void setCurrentMusicName(String path) {
        try {
            File file = new File(path);
            String name = file.getName();
            int index = name.indexOf(".");
            this.songName = name.substring(0, index);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void next(Button playBtn) {
        int index = currentMusic + 1;
        //只支持列表有限播放
        if (index >= musicPaths.size()) {
            return;
        }
        currentMusic = index;
        currentState = new play();

        currentState.Do(this,playBtn);

    }

    public void last(Button playBtn){
        int index = currentMusic - 1;
        //只支持列表有限播放
        if (index < 0) {
            return;
        }
        currentMusic = index;
        currentState = new play();
        currentState.Do(this,playBtn);
    }

    public void pause() {
        if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
            return;
        }
        mediaPlayer.pause();
    }



    public String getPlayInfo(int currentSec, int musicMaxSec, String musicName) {

        return String.format(Locale.CHINA+"正在播放%s \r\n  " +
                           "当前播放%d 秒  \r\n " +
                            "总共%d 秒",
                            musicName, currentSec, musicMaxSec);
    }
}
