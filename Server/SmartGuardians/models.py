from django.db import models

class Notification(models.Model):
    title = models.CharField(max_length=255)
    content = models.TextField()                            # date 필드를 CharField로 변경하여 문자열 형식으로 저장
    date = models.CharField(max_length=16)                  # 예: "2024-11-09 16:23"
    video_data = models.TextField(null=True, blank=True)    # Base64로 인코딩된 비디오 데이터 저장 필드
    image_data = models.TextField(null=True, blank=True)    # Base64로 인코딩된 이미지 데이터 저장 필드
    
    def __str__(self):
        return self.title