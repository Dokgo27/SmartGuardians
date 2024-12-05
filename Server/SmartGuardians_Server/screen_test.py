import cv2
import matplotlib.pyplot as plt

# display_frames 함수 정의
def display_frames(frames):
    # 각 프레임을 시각화
    fig, axes = plt.subplots(1, len(frames), figsize=(15, 5))
    for idx, frame in enumerate(frames):
        axes[idx].imshow(cv2.cvtColor(frame, cv2.COLOR_BGR2RGB))  # OpenCV의 BGR을 RGB로 변환
        axes[idx].axis("off")
    plt.show()

# RTSP URL을 OpenCV VideoCapture로 설정
rtsp_url = 'rtsp://220.69.208.115:8554/screen'
cap = cv2.VideoCapture(rtsp_url)

# 초당 3프레임을 선택하도록 설정
frames_per_second = 3
frame_interval = int(cap.get(cv2.CAP_PROP_FPS) / frames_per_second)
frames_to_display = []
frame_count = 0

if not cap.isOpened():
    print("스트림을 열 수 없습니다.")
else:
    while True:
        ret, frame = cap.read()  # 프레임 읽기
        if not ret:
            break

        # 특정 간격에 해당하는 프레임만 추가
        if frame_count % frame_interval == 0:
            frames_to_display.append(frame)

            # 3개의 프레임이 모이면 display_frames 함수로 표시
            if len(frames_to_display) == 3:
                display_frames(frames_to_display)
                frames_to_display = []  # 표시 후 리스트 초기화

        frame_count += 1  # 프레임 카운터 증가

        # 'q' 키를 누르면 종료
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

# 자원 해제
cap.release()
