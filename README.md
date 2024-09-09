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
    * navigation - fragment 화면간의 이동을 처리하고 데이터 전달을 관리합니다.
    * Download Manager - 이미지 파일의 HTTP 다운로드를 백그라운드에서 원활하게 처리합니다.
    * Permissions - (TedPermission)[https://github.com/ParkSangGwon/TedPermission]을 활용해 저장장치에 관한 권한을 요청하고 처리합니다.
    * Notifications - 이미지 파일의 다운로드 상태를 알리기 위해 알림을 표시합니다.
* Architecture
    * MVVM 패턴 적용 - Model + View + ViewModel
    * Repository 패턴 적용 - Data + Domain + Presentation Layer
* [Glide](https://github.com/bumptech/glide) - 효율적으로 이미지를 로드하고 적용합니다.
* [Retrofit2](https://github.com/square/retrofit) - REST API를 호출하여 서버로부터 JSON 타입의 데이터를 수신합니다.
* 이미지 편집
    * [PhotoEditor](https://github.com/burhanrashid52/PhotoEditor) - 이미지를 그리기, 필터링, 이모지 등의 효과를 반영하고 편집합니다
    * [Android-Image-Cropper](https://github.com/CanHub/Android-Image-Cropper) - 이미지를 원하는 사용자가 원하는 상태로 자르고 저장합니다.
* 로딩 상태
    * [Shimmer](https://github.com/facebookarchive/shimmer-android) - 이미지 로딩 상태를 표시하기 위해 Skeleton Effect를 적용합니다.
    * [Android-SpinKit](https://github.com/ybq/Android-SpinKit) - 이미지 목록의 로딩 상태를 표시하기 위해 Pulse Effect를 적용합니다.

# 스크린샷
