package com.example.smartguardian;

public class Notification {
    private int id;
    private String title;
    private String content;
    private String date;
    private String videoData;  // Base64로 인코딩된 비디오 데이터
    private String imageData;

    public Notification(int id, String title, String content, String date, String videoData, String imageData) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.videoData = videoData;
        this.imageData = imageData;
    }

    public int getId(){
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }
    public String getVideoData() {
        return videoData;
    }
    public String getImageData(){
        return imageData;
    }

}

