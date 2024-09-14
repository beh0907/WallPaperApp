# <div align=center>WallPaperApp</div>
[Pixabay API](https://pixabay.com/api/docs/)를 사용하여 kotlin 언어 기반의 월페이퍼 안드로이드 어플리케이션입니다.

# 특징
* 무한한 이미지 목록 제공 (API 제한 한도까지)
* 이미지를 선택하고 다운로드
* 다운로드 받은 이미지를 원하는대로 편집
* 원본 혹은 편집된 이미지를 타인에게 공유

# 기술스택 및 라이브러리
* 최소 SDK 26 / 타겟 SDK 34
* kotlin 언어 기반, 비동기 처리를 위한 coroutine + Flow
* 종속성 주입을 위한 [Dagger Hilt](https://dagger.dev/hilt/)
* JetPack
    * LifeCycle - Android의 수명 주기를 관찰하고 수명 주기 변경에 따라 UI 상태를 처리합니다.
    * ViewModel - UI와 DATA 관련된 처리 로직을 분리합니다.
    * ViewBinding - View(XML)과 코드(kotlin)간의 상호작용을 원활하게 처리합니다.
    * Paging3 - 무한 스크롤 목록을 처리하고 관리합니다. (API 페이징 처리)
    * Navigation - fragment 화면간의 이동을 처리하고 데이터 전달을 관리합니다.
    * Permissions - [TedPermission](https://github.com/ParkSangGwon/TedPermission)을 활용해 저장장치에 관한 권한을 요청하고 처리합니다.
    * Notifications - 이미지 파일의 다운로드 상태를 알리기 위해 알림을 표시합니다.
    * DataStore - SharedPreferences의 한계점을 개선한 라이브러리로 이미지의 검색 이력 정보를 관리합니다.
* Architecture
    * MVVM 패턴 적용 - Model + View + ViewModel
    * Repository 패턴 적용 - Data + Domain + Presentation Layer
* [Glide](https://github.com/bumptech/glide) - 효율적으로 이미지를 로드하고 적용합니다.
* [Retrofit2](https://github.com/square/retrofit) - REST API를 호출하여 서버로부터 JSON 타입의 데이터를 수신합니다.
* [Ketch](https://github.com/khushpanchal/Ketch) 이미지 파일의 HTTP 다운로드를 원활하게 처리하고 알림 상태를 제공합니다
* 이미지 편집
    * [PhotoEditor](https://github.com/burhanrashid52/PhotoEditor) - 이미지를 그리기, 필터링, 이모지 등의 효과를 반영하고 편집합니다
    * [Android-Image-Cropper](https://github.com/CanHub/Android-Image-Cropper) - 이미지를 사용자가 원하는 상태로 자르고 저장합니다.
* 로딩 상태
    * [Shimmer](https://github.com/facebookarchive/shimmer-android) - 이미지 로딩 상태를 표시하기 위해 Skeleton Effect를 적용합니다.
    * [Lottie Animation](https://github.com/airbnb/lottie-android) - 이미지 목록의 로딩 상태를 표시하기 위해 애니메이션 이미지를 적용합니다.

# 스크린샷
|메인화면(최신 월페이퍼)|인기 월페이퍼 화면|랜덤 월페이퍼 화면|
|---|---|---|
|![메인화면](https://github.com/user-attachments/assets/7c7c7c8b-8b2c-421c-a237-80534bedbc97)|![인기 이미지 화면](https://github.com/user-attachments/assets/d36539f0-34b1-44f6-807f-5dcea8e35d28)|![랜덤 이미지 화면](https://github.com/user-attachments/assets/67f103cf-4943-4645-8337-55c61c90685c)|

|카테고리 화면|카테고리 월페이퍼 화면|검색 화면|
|---|---|---|
|![카테고리 화면](https://github.com/user-attachments/assets/43884671-06a5-4b26-bda0-c61ea04899ea)|![카테고리 월페이퍼 화면](https://github.com/user-attachments/assets/f6f6cbee-44a3-4d42-8290-95f0df265b93)|![검색 화면](https://github.com/user-attachments/assets/d643ed0e-9f01-419d-b707-e18f9360625d)|

|다운로드 화면|나의 다운로드 목록 화면|편집 화면|
|---|---|---|
|![다운로드 화면](https://github.com/user-attachments/assets/8279e34c-4b9e-4d8a-9608-7927355c54b7)|![나의 다운로드 목록 화면](https://github.com/user-attachments/assets/c78b417f-0e52-450b-b111-f324c56c5349)|![편집 화면](https://github.com/user-attachments/assets/17013849-27f0-407f-896e-0b6df8b9c485)|