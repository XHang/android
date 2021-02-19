package com.example.thread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.SeekBar;

import com.example.dto.MusicInfo;
import com.example.service.MusicService;
import com.example.service.logService;

public class seekBarAutoFlow implements Runnable {
    private MusicService musicService;
    private Handler handler;
    private SeekBar seekBar;

    public seekBarAutoFlow(Handler handler, SeekBar seekBar, MusicService service) {
        this.handler = handler;
        this.seekBar = seekBar;
        this.musicService = service;
    }

    @Override
    public void run() {
        Looper.prepare();  //为该线程创建一个Looper
        while (!Thread.currentThread().isInterrupted()) {
            if (musicService.getMediaPlayer() != null && musicService.getMediaPlayer().isPlaying()) {
                Message msg = handler.obtainMessage();//获取一个Message
                //getCurrentPosition是毫秒，要转成秒
                musicService.getCurrentMusic().setCurrentSec(musicService.getCurrentSec());
                //getDuration是毫秒，要转成秒
                msg.obj = musicService.getCurrentMusic();
                //如果当前歌曲的长度为0，跳过，不必渲染滚动条
                if (musicService.getCurrentMusic().getMaxSec() != 0){
                    handler.sendMessage(msg);
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logService.Log.Error(e,"无法渲染滚动条");
            }
        }
    }
}
