package com.example.smartguardian;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

// NotificationListActivity에 들어갈 어뎁터(목록의 아이템)을 설정하는 클래스
// NotificationListActivity는 책장의 역할. NotificationAdapter는 책을 생각하면 편하다.
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<Notification> notificationList;
    private final Context context;
    // 생성자
    public NotificationAdapter(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notificationList.get(position);

        // content 표시
        holder.contentTextView.setText(notification.getContent());
        holder.idTextView.setText(String.valueOf(notification.getId()));  // ID 표시

        // date와 time 분리하여 표시
        String dateTime = notification.getDate();
        if (dateTime != null && dateTime.contains("T")) {
            // "T"를 기준으로 나누어 날짜와 시간 부분만 추출
            String[] dateTimeParts = dateTime.split("T");
            String datePart = dateTimeParts[0];
            String timePart = dateTimeParts[1].split("\\.")[0]; // 초와 밀리초 제거

            // 시간에서 초 부분 제거 (예: "16:26:00" -> "16:26")
            if (timePart.contains(":")) {
                String[] timeComponents = timePart.split(":");
                timePart = timeComponents[0] + ":" + timeComponents[1]; // 시:분 형식만 사용
            }

            holder.dateTextView.setText(datePart); // 날짜
            holder.timeTextView.setText(timePart); // 시간
        } else {
            // 포맷이 예상과 다를 경우 전체를 표시하거나 오류 방지
            holder.dateTextView.setText(dateTime);
            holder.timeTextView.setText("");
        }

        // Base64로 인코딩된 이미지 데이터를 디코딩하여 ImageView에 설정
        String imageData = notification.getImageData();
        if (imageData != null && !imageData.isEmpty()) {
            try {
                byte[] decodedString = Base64.decode(imageData, Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.imageView.setImageBitmap(decodedBitmap);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                // 디코딩 실패 시 기본 이미지 설정
                holder.imageView.setImageResource(R.drawable.noimage);
            }
        } else {
            // 이미지 데이터가 없을 경우 기본 이미지 설정
            holder.imageView.setImageResource(R.drawable.noimage);
        }

        // 항목 클릭 시 PlayerViewActivity로 이동
        holder.itemView.setOnClickListener(v -> {
            String videoData = notification.getVideoData();
            if (videoData != null && !videoData.isEmpty()) {
                // video_data를 디코딩하여 캐시에 저장하고 파일 경로 반환
                NotificationListActivity.cachedVideoFilePath = NotificationListActivity.saveVideoToCache(context, videoData);

                // PlayerViewActivity를 시작
                Intent intent = new Intent(context, PlayerViewActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList != null ? notificationList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView contentTextView, dateTextView, timeTextView, idTextView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            idTextView = itemView.findViewById(R.id.idTextView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
