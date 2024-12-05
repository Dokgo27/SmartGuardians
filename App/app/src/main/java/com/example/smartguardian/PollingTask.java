package com.example.smartguardian;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class PollingTask {

    private static final String TAG = "PollingTask";
    private static final long POLLING_INTERVAL = 5000; // 5초 간격
    private final Handler handler = new Handler();
    private final OkHttpClient client = new OkHttpClient();
    private final Context context;
    private int lastFlag = 0; // 이전 flag 값을 저장하는 변수

    public PollingTask(Context context) {
        this.context = context;
    }

    private final Runnable pollingRunnable = new Runnable() {
        @Override
        public void run() {
            new Thread(() -> {
                try {
                    Request request = new Request.Builder()
                            .url("https://89ad-220-69-208-114.ngrok-free.app/get_notifications/")  // Django 서버 URL
                            .build();

                    Response response = client.newCall(request).execute();

                    if (response.isSuccessful() && response.body() != null) {
                        String responseData = response.body().string();
                        Log.d(TAG, "Response from server: " + responseData);

                        // JSON 응답을 파싱하고 flag 값을 확인
                        JSONObject jsonResponse = new JSONObject(responseData);
                        int flag = jsonResponse.getInt("flag");

                        // 서버의 flag 값이 1이고, 이전 flag 값이 0인 경우에만 알림 표시
                        if (flag == 1) {
                            JSONArray notifications = jsonResponse.getJSONArray("notifications");
                            for (int i = 0; i < notifications.length(); i++) {
                                JSONObject notification = notifications.getJSONObject(i);
                                String title = notification.getString("title");
                                String content = notification.getString("content"); // content 추가
                                String date = notification.getString("date"); // date 추가

                                //실질적으로 알림 기능을 수행하는 함수 showNotification
                                showNotification(title, content, date);
                            }
                        }

                        // 현재 flag 상태를 lastFlag에 저장
                        lastFlag = flag;
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error fetching notifications", e);
                }

                // 다음 요청 예약 (메인 스레드에서 실행하도록 설정)
                handler.postDelayed(pollingRunnable, POLLING_INTERVAL);
            }).start();
        }
    };

    public void startPolling() {
        handler.post(pollingRunnable);
    }

    public void stopPolling() {
        handler.removeCallbacks(pollingRunnable);
    }

    // 알림 띄우는 기능
    private void showNotification(String title, String content, String date) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "polling_channel_id";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId, "Polling Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        // 알림의 내용을 title, content, date를 조합하여 표시
        String message = content + " - " + date;

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.shield) // 알림 아이콘
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        notificationManager.notify(1, notificationBuilder.build());
    }
}
