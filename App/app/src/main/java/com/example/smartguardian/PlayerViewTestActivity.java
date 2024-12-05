package com.example.smartguardian;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;

public class PlayerViewTestActivity extends AppCompatActivity {

    private ExoPlayer player;
    private StyledPlayerView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_player);

        playerView = findViewById(R.id.styledPlayerView);

        // ExoPlayer 초기화
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        // Django 서버의 HTTP 스트림 URL 설정 (ngrok을 통해 공개된 URL 사용)
        String httpUrl = " https://89ad-220-69-208-114.ngrok-free.app/hls/stream.m3u8";  // <ngrok-id>에 ngrok에서 제공한 ID 입력
        MediaItem mediaItem = MediaItem.fromUri(httpUrl);

        // MediaItem을 ExoPlayer에 설정
        player.setMediaItem(mediaItem);

        // 준비 및 재생 시작
        player.prepare();
        player.play();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}