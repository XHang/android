package com.example.state;

import android.media.MediaPlayer;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.service.MusicService;

public class pause implements state {


    @Override
    public void Do(MusicService musicService, Button playBtn) {
        MediaPlayer mediaPlayer = musicService.mediaPlayer;
        if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
            return;
        }
        mediaPlayer.pause();
        playBtn.setText(getNextState().getCurrentName());
    }

    @Override
    public int getCurrentName() {
        return R.string.btn_pause;
    }

    @Override
    public state getNextState() {
        return new continute();
    }
}
