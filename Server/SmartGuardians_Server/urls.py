from django.contrib import admin
from django.urls import path
from SmartGuardians import views  # 추가

### 승언 작성
from django.conf import settings  # 추가
from django.conf.urls.static import static  # 추가

urlpatterns = [
    path('admin/', admin.site.urls),
    # '/'으로 접속하면 views.py의 index 함수 실행
    path('', views.index),
    # get_notifications/로 접속하면 view.py의 get_notifications 함수 실행
    path('get_notifications/', views.get_notifications, name='get_notifications'),
    # api/notifications/로 접속하면 view.py의 notification_list 함수 실행
    path('api/notifications/', views.notification_list, name='notification_list'),
    # upload/로 접속하면 view.py의 upload_video 함수 실행
    path('get_video_data/<int:notification_id>/', views.get_video_data, name='get_video_data'),
]

### 승언 작성
urlpatterns += static('/hls/', document_root='C:/saveffmpeg')
