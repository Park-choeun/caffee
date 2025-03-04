<div align="center">

<img src="https://drive.google.com/uc?export=view&id=1SNB7PeRHeTW1z4tWEJZNYSD7O6XR1Ms8" alt="CAFFEE" width="550px" />


가봤던 혹은 가고 싶은 카페를 기록하는 나만의 카페 다이어리 앱 서비스

<img src="https://img.shields.io/badge/Java-007396?style=flat&logo=java&logoColor=white" />  
<img src="https://img.shields.io/badge/Google Place API-4285F4?style=flat&logo=googlemaps&logoColor=white" /> 
<img src="https://img.shields.io/badge/Android Studio-3DDC84?style=flat&logo=androidstudio&logoColor=white" /> 

</div>


## ☕CAFFEE 소개


+ 개발 기간: 2021/09/01 ~ 2021/12/24
+ 개발 배경 및 목적
  * 사람들은 대부분 외출 시에 카페를 간다. 심지어 타지역에 위치한 카페를 일부러 찾아가기도 한다. SNS를 보면 대형 카페, 인테리어가 예쁜 카페, 맛있는 카페 등 다양한 카페가 광고되고 우리는 SNS의 저장 기능을 통해 가고 싶은 카페들을 저장한다. 하지만 이는 SNS에 접속한 후, 저장된 목록으로 들어가 그 속에서 카페 관련 게시물을 찾아야할 뿐더러 기재된 카페주소를 복사해 별도의 지도 앱에서 검색해야 하는 번거로움이 있다. 
    </br>→ 따라서 이와 같은 번거로움을 줄이고자 나만의 카페 다이어리 ‘CAFFEE’를 구현했다.
    
+ 주요 기능
  * 카페 지도</br>: 현재위치 표시 및 별도의 버튼으로 현재위치로 돌아가기 기능</br>: 현재위치 반경 300m내의 카페만을 검색·표시</br>: 사용자 입력 기반 위치 검색
  * 나만의 카페 리스트(스크랩)</br>: 가봤던 카페(MyList)와 가고 싶은 카페(WishList)를 분리해 저장</br>: MyList에 저장 시, 카페에 대한 간단한 리뷰·메모와 별점(RatingBar) 작성 가능</br>: 저장된 장소 조회 시, 주소와 전화번호 및 위치 출력(위치는 지도로 출력)
  * 모든 화면에서 상단 메뉴를 통하여, 다른 화면으로 이동 및 앱 종료
    
+ 서비스 메뉴얼: https://drive.google.com/file/d/1D4IfwyTmlwJvMiep3MpxHgE4H5uhhIhJ/view


## ⚙ 개발 환경


+ Languages: Java
+ API: Google Place API, Android Location API(LocationListener), SQLite(SQLiteOpenHelper, SQLiteDatabase)
+ Tools: Android Studio
