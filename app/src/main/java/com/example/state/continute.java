package com.example.state;

import android.media.MediaPlayer;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.service.MusicService;

public class continute implements state {
    @Override
    public void Do(MusicService musicService ,Button playBtn) {
        MediaPlayer mediaPlayer = musicService.mediaPlayer;
        if (mediaPlayer == null) {
            return;
        }
        //还在播放中，还继续播放个毛
        if (mediaPlayer.isPlaying()) {
            return;
        }
        int po = mediaPlayer.getCurrentPosition();
        int max = mediaPlayer.getDuration();
        mediaPlayer.seekTo(po);
        mediaPlayer.start();
        playBtn.setText(getNextState().getCurrentName());
    }
    @Override
    public int getCurrentName() {
        return R.string.btn_continute;
    }
    public state  getNextState(){
       return new pause();
    }
}
