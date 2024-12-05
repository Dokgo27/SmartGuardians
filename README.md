# SmartGuardians

### 캡스톤 디자인: 편의점 이상행동 감지 시스템

---

## 프로젝트 개요
**SmartGuardians**는 편의점에서 발생할 수 있는 이상행동을 감지하는 딥러닝 기반 시스템입니다. 해당 시스템은 실시간 영상 데이터를 처리하여 이상행동을 효과적으로 탐지하며, 이를 통해 안전하고 신뢰할 수 있는 환경을 조성하는 것을 목표로 합니다.

#### 캡스톤 디자인 포스터:
![ConvLSTM 모델 구조](https://github.com/user-attachments/assets/adbfa348-18c5-45e3-96c5-1908e81218cb)

---

## 학습 모델
본 프로젝트에서는 다음과 같은 두 가지 주요 딥러닝 모델을 사용합니다:

### 1. **UniPose**
- **UniPose**는 사람의 자세를 추출하는 데 최적화된 딥러닝 모델입니다.
- 편의점 내 사람의 자세를 분석하여 행동 패턴을 파악하고 이상행동을 탐지합니다.

#### UniPose 모델 구조:
![UniPose 모델 구조](https://github.com/user-attachments/assets/c63d7a3c-f9ce-4edf-b9df-1de3602f926d)
#### UniPose + LSTM 구조:
![UniPose 모델과 LSTM 구조](https://github.com/user-attachments/assets/a2dfb819-7a2e-4002-96cc-1752152d9fb7)

---

### 2. **ConvLSTM**
- **ConvLSTM**은 시계열 데이터에서 공간적 및 시간적 특징을 모두 학습할 수 있는 모델입니다.
- UniPose로 추출된 행동 데이터를 기반으로 행동의 변화를 분석하고 이상행동을 탐지합니다.

#### ConvLSTM 모델 구조:
![ConvLSTM 결과 예시](https://github.com/user-attachments/assets/31c31c2f-9cae-426f-8d24-9bd3d7badd39)


---

## 주요 기능
1. **실시간 행동 감지**:
   - 편의점 내부의 CCTV 영상을 분석하여 실시간으로 이상행동을 감지합니다.
2. **정확한 이상행동 탐지**:
   - UniPose와 ConvLSTM의 조합으로 높은 정확도를 자랑합니다.
3. **알림 시스템**:
   - 이상행동이 감지될 경우 관리자에게 즉시 알림을 보냅니다.

---

## 기대 효과
- **안전성 향상**: 실시간 이상행동 탐지로 편의점 직원과 고객의 안전을 보장.
- **운영 효율성 개선**: 관리자와 직원이 이상행동을 신속히 인지하고 대응할 수 있도록 지원.
- **스마트 환경 구축**: AI 기반 기술을 활용한 차세대 편의점 환경 조성.

---

## 개발 환경
- **모델 학습 프로그래밍 언어**: Python
- **앱 프로그래밍 언어**: Java
- **모델 학습 프레임워크**: TensorFlow, PyTorch
- **서버 프레임워크**: Django
- **스트리밍 툴**: OBS
- **추론 환경**: NVIDIA GPU
- **데이터**: AI HUB 편의점 CCTV 데이터

---

## 참여자
- **팀원 최승언**: UniPose, ConvLSTM 모델 설계 및 데이터 전처리 담당
- **팀원 김민재**: 서버, 앱 구조 설계, 구현

---
