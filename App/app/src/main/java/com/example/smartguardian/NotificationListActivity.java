package com.example.smartguardian;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// 리사이클 뷰로 알림을 받았던 리사이클 뷰에 대한 목록을 확인할 수 있는 엑티비티
// 어뎁터(목록의 아이템)을 따로 만들어야 한다.
// NotificationListActivity는 책장의 역할. NotificationAdapter는 책을 생각하면 편하다.
public class NotificationListActivity extends AppCompatActivity {
    private static final String TAG = "NotificationListActivity";
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<Notification> notificationList = new ArrayList<>();
    private OkHttpClient client = new OkHttpClient();
    private Context context;

    public static String cachedVideoFilePath;

    public NotificationListActivity() {
        // 기본 생성자
    }

    public NotificationListActivity(Context context) {
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listview);

        // 리사이클 뷰, 어뎁터 초기화
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter(this, notificationList);
        recyclerView.setAdapter(adapter);

        fetchNotifications();
    }

    public static String saveVideoToCache(Context context, String videoBase64) {
        byte[] videoData = Base64.decode(videoBase64, Base64.DEFAULT);
        File videoFile = new File(context.getCacheDir(), "cached_video.mp4");

        try (FileOutputStream fos = new FileOutputStream(videoFile)) {
            fos.write(videoData);
            Log.i("NotificationListActivity", "Video saved to cache: " + videoFile.getAbsolutePath());
            return videoFile.getAbsolutePath(); // 저장된 파일 경로 반환
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void fetchNotifications() {
        String url = "https://89ad-220-69-208-114.ngrok-free.app/api/notifications/";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Error fetching notifications", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        notificationList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String title = jsonObject.getString("title");
                            String content = jsonObject.getString("content");
                            String date = jsonObject.getString("date");
                            String videoData = jsonObject.optString("video_data", ""); // Base64 비디오 데이터 가져오기
                            String imageData = jsonObject.optString("image_data", ""); // Base64 비디오 데이터 가져오기
                            notificationList.add(new Notification(id, title, content, date, videoData, imageData));
                        }
                        runOnUiThread(() -> adapter.notifyDataSetChanged());
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON parsing error", e);
                    }
                }
            }
        });
    }
}

