package com.example.smartguardian;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import java.io.File;

public class PlayerViewActivity extends AppCompatActivity {

    private ExoPlayer player;
    private StyledPlayerView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playerview);

        playerView = findViewById(R.id.styledPlayerView);

        // NotificationListActivity에서 저장한 비디오 파일 경로 가져오기
        String videoFilePath = NotificationListActivity.cachedVideoFilePath;
        if (videoFilePath != null) {
            playVideo(videoFilePath);
        } else {
            Log.e("PlayerViewActivity", "No cached video file path found.");
        }
    }

    private void playVideo(String videoFilePath) {
        File videoFile = new File(videoFilePath);
        if (!videoFile.exists()) {
            Log.e("PlayerViewActivity", "Video file not found: " + videoFilePath);
            return;
        }

        // ExoPlayer 설정
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        // MediaSource 설정 및 비디오 파일로부터 재생 시작
        DefaultDataSource.Factory dataSourceFactory = new DefaultDataSource.Factory(this);
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(videoFile.toURI().toString()));

        player.setMediaSource(mediaSource);
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
