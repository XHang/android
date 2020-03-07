package com.example.thread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.SeekBar;

import com.example.dto.MusicInfo;
import com.example.service.MusicService;

public class seekBarAutoFlow implements Runnable {
    private MusicService musicService ;
    private Handler handler;
    private SeekBar seekBar;
    public seekBarAutoFlow(Handler handler,SeekBar seekBar,MusicService service){
        this.handler = handler;
        this.seekBar =seekBar;
        this.musicService = service;
    }
    @Override
    public void run() {
        Looper.prepare();  //为该线程创建一个Looper
        while (!Thread.currentThread().isInterrupted()) {
            if (musicService.mediaPlayer!=null && musicService.mediaPlayer.isPlaying()) {
                Message msg = handler.obtainMessage();//获取一个Message
                MusicInfo info = new MusicInfo();
                //getCurrentPosition是毫秒，要转成秒
                info.setCurrentSec(musicService.mediaPlayer.getCurrentPosition()/1000);
                //getDuration是毫秒，要转成秒
                info.setMusicMaxSec(musicService.mediaPlayer.getDuration()/1000);
                info.setSeekbarMaxSec(seekBar.getMax());
                info.setMusicName(musicService.songName);
                msg.obj = info;
                handler.sendMessage(msg);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
