# TRuler
농작업 자세 분석 안드로이드 앱

## 개요
농작업 시 작업자세를 분석하여 부상의 위험도가 얼마나 되는지를 분석하고 안전한 자세를 안내하는 앱. 기본적인 동작 방식은 사진을 찍고 손으로 직접 관절 부분을 클릭하면 각도를 계산하여 점수를 매겨 위험도를 측정한다.

이론적인 내용들은 Document 폴더 참조

## 앱 정보
1. Legacy 버전 (Native Android)
    - Target Android API Level: 24 (Android 7.0 Nougat) 
    - Min Android API Level: 15 (Android 4.0.3 Ice Cream Sandwitch) 

2. Xamarin 버전
    - Xamarine Forms 사용 (단, 일부 Effect가 안드로이드만 구현되어 다른 플랫폼은 동작 안 됨)
    - Target Android API Level: 28 (Android 9 Pie) 
    - Min Android API Level: 20 (Android 4.4 KitKat) 


### 버전 별 Root 폴더
    - Legacy 버전: './'
    - Xamarin 버전: './TRulerX/'
