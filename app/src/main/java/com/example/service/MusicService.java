package com.example.service;

import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Looper;
import android.widget.Button;

import com.example.dto.CurrentMusic;
import com.example.dto.MusicInfo;
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
    private MediaPlayer mediaPlayer;
    private CurrentMusic currentMusic;
    private List<MusicInfo> musics;

    //播放器状态，当前是停止播放状态
    private state currentState = new stop();

    //初始化音乐列表并且
    public MusicService() {
        List<String> paths = this.getMusicPaths();
        if (paths.size() == 0) {
            return;
        }
        musics = new ArrayList<>(paths.size());
        for (int i = 0; i < paths.size(); i++) {
            String path = paths.get(i);
            try {
                MusicInfo musicInfo = new MusicInfo(path);
                musics.add(musicInfo);
            } catch (Exception e) {
                logService.Log.Error(e, "无法解析音乐");
                continue;
            }
        }
        if (musics.size()!=0){
            this.currentMusic = new CurrentMusic(musics.get(0), 0);
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnErrorListener((mp,what,extra)->{
            mp.reset();
            logService.Log.Error(null,"play error [%d]  play extra[%d]",what,extra);
            return true;
        });
    }

    //得到歌曲列表
    public List<MusicInfo> getMusics() {
        return musics;
    }

    //获取当前播放的歌曲
    public CurrentMusic getCurrentMusic() {
        return currentMusic;
    }

    //得到播放器
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    private List<String> getMusicPaths() {
        List<String> musicPaths = new ArrayList<>();
        //假设音乐文件夹就在根目录下面的music文件夹
        File[] arr = SDCardRood.listFiles();
        Collection<File> musicFiles = FileUtils.listFiles(SDCardRood, new IOFileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(".mp3");
            }

            @Override
            public boolean accept(File dir, String name) {
                return true;
            }
        }, TrueFileFilter.TRUE);

        if (musicFiles.size() == 0) {
            return null;
        }
        //目前只支持MP3格式啦
        for (File file : musicFiles) {
            if (!file.getName().endsWith(".mp3")) {
                continue;
            }
            musicPaths.add(file.getAbsolutePath());
        }
        return musicPaths;
    }

    public state getCurrentState() {
        return currentState;
    }

    public void setCurrentState(state currentState) {
        this.currentState = currentState;
    }


    public void next(Button playBtn) {
        int index = this.currentMusic.getIndex() + 1;
        play(index,playBtn);
    }

    public void last(Button playBtn) {
        int index = currentMusic.getIndex() - 1;
        play(index,playBtn);
    }

    public void pause() {
        if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
            return;
        }
        mediaPlayer.pause();
    }


    public String getPlayInfo(CurrentMusic music) {

        return String.format(Locale.CHINA + "正在播放%s \r\n  " +
                        "当前播放%d 秒  \r\n " +
                        "总共%d 秒",
                music.getMusicInfo().getMusicName(), this.getCurrentSec(), this.currentMusic.getMaxSec());
    }

    public int getCurrentSec() {
        return this.mediaPlayer.getCurrentPosition() / 1000;
    }

    public void play(int index,Button playBtn){
        if (index < 0) {
            return;
        }
        //只支持列表有限播放
        if (index >= musics.size()) {
            return;
        }
        this.currentMusic = new CurrentMusic(musics.get(index), index);
        currentState = new play();

        currentState.Do(this, playBtn);
    }
}
