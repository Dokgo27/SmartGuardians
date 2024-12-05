import os
import json
from django.shortcuts import render
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from datetime import datetime
from .models import Notification
from django.shortcuts import get_object_or_404
import subprocess
from django.http import StreamingHttpResponse

from django.http import JsonResponse
import base64
import os

# 알림 데이터를 저장하는 리스트
notifications = []
flag = 0  # 알림이 비활성화된 상태

# 테스트 뷰 (index.html)
def index(request):
    return render(request, 'index.html')

# 알림 함수 - 앱에서 폴링 방식으로 온 메세지에 대해서 응답해주는 함수
# GET으로 메세지를 보낼 시에는 플래그를 0으로 전달하여 보낸다.
# POST로 메세지를 보낼 시에 플래그를 1로 전달하여 보낸다.
# 앱에서는 플래그가 1일 때 알림을 표시한다.
@csrf_exempt
def get_notifications(request):
    global flag, notifications
    if request.method == "GET":
        # GET 요청 시, flag가 1이면 notifications을 보내고, flag를 다시 0으로 설정
        response = {"flag": flag, "notifications": notifications if flag == 1 else []}
        flag = 0  # 알림 읽기 후 flag 초기화
        return JsonResponse(response)

    elif request.method == "POST":
        # POST 요청에서 알림 데이터를 수신하여 notifications에 추가
        try:
            data = json.loads(request.body)
            title = data.get("title", "알림")
            content = data.get("content", "")
            # 현재 날짜를 "%Y-%m-%d %H:%M" 형식으로 저장
            date = data.get("date", datetime.now().strftime("%Y-%m-%d %H:%M"))  # 간단한 형식의 문자열로 변환
            video_data = data.get("video_data", "")  # Base64 인코딩된 비디오 데이터 수신
            image_data = data.get("image_data", "")  # Base64 인코딩된 이미지 데이터 수신

            # 알림 데이터를 리스트에 추가
            notifications.append({
                "title": title,
                "content": content,
                "date": date,
                "video_data": video_data,
                "image_data": image_data
            })

            # Notification 모델에 데이터 저장
            notification = Notification.objects.create(
                title=title,
                content=content,
                date=date,
                video_data=video_data,
                image_data=image_data  # 모델에 이미지 데이터 저장
            )

            # flag를 1로 설정하여 알림 활성화
            flag = 1
            return JsonResponse({"status": "success", "flag": flag})

        except json.JSONDecodeError:
            return JsonResponse({"status": "failed", "message": "Invalid JSON"}, status=400)

    return JsonResponse({"status": "failed"}, status=400)

# 알림 리스트 함수 - 모든 알림 데이터를 가져옵니다
def notification_list(request):
    notifications = Notification.objects.all().values('id', 'title', 'content', 'date', 'video_data', 'image_data')
    notification_list = list(notifications)
    return JsonResponse(notification_list, safe=False)

# 특정 알림의 비디오 데이터를 가져오는 API
def get_video_data(request, notification_id):
    try:
        # notification_id로 Notification 모델에서 객체를 가져옵니다.
        notification = get_object_or_404(Notification, id=notification_id)
        
        # 비디오 데이터가 있는지 확인
        if notification.video_data:
            response = {
                "video_data": notification.video_data  # Base64로 인코딩된 비디오 데이터 반환
            }
            return JsonResponse(response, status=200)
        else:
            return JsonResponse({"error": "No video data found for this notification"}, status=404)
    
    except Notification.DoesNotExist:
        return JsonResponse({"error": "Notification not found"}, status=404)
