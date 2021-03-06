package com.example.state;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.service.MusicService;

public class play implements state {

    @Override
    public void Do(MusicService musicService, Button playBtn) {
        MediaPlayer mediaPlayer = musicService.getMediaPlayer();
        //无法播放超出游标的音乐
        try {
            mediaPlayer.reset(); //重置多媒体
            String currentPath = musicService.getCurrentMusic().getMusicInfo().getMusicPath();
            mediaPlayer.setDataSource(currentPath);
            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build());
            mediaPlayer.prepare();
            musicService.getCurrentMusic().setMaxSec(mediaPlayer.getDuration()/1000); //getDuration得在prepare之后再调用
            mediaPlayer.start();
            //注册监听器：当音乐播放完毕要怎么办
            mediaPlayer.setOnCompletionListener((listener) -> {
                musicService.next(playBtn);
            });
            playBtn.setText(getNextState().getCurrentName());
        } catch (Exception e) {
            throw new RuntimeException("播放音乐失败", e);
        }
    }

    @Override
    public int getCurrentName() {
        return R.string.btn_play;
    }

    @Override
    public state getNextState() {
        return new pause();
    }
}
