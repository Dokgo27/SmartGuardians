package com.example.smartguardian;


import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button RealTimeViewButton;
    private PollingTask pollingTask;
    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 1;
    private Button AbnormalBehaviorListButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // PollingTask 시작
        pollingTask = new PollingTask(this);
        pollingTask.startPolling();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RealTimeViewButton = findViewById(R.id.realTimeViewButton);
        AbnormalBehaviorListButton = findViewById(R.id.abnormalBehaviorListButton);
        // 실시간 매장 보기 버튼 클릭 리스너 설정
        RealTimeViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlayerViewTestActivity.class); // 승언 작성
                startActivity(intent);
            }
        });

        // 이상행동 목록 버튼 클릭 리스너 설정
        AbnormalBehaviorListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotificationListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startPollingTask() {
        // PollingTask 시작
        pollingTask = new PollingTask(this);
        pollingTask.startPolling();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // PollingTask 중지
        if (pollingTask != null) {
            pollingTask.stopPolling();
        }
    }

    // 권한 요청 결과 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("MainActivity", "Notification permission granted");
                startPollingTask();
            } else {
                Log.d("MainActivity", "Notification permission denied");
            }
        }
    }
}
