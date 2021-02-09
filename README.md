# Sweeper
Sweeper

---
울산대학교 IT융합전공  
2019년 2학기 분산모바일정보시스템 및 실험  
텀 프로젝트

---
 - Java Android
 - Android Studio
 - Google Material UI
 - Java JSON
 - Facebook Fresco
 - NewsAPI
--- 
## 개요
 - NewsAPI를 통해 구글 뉴스의 최신 기사를 JSON 객체로 다운로드
 - JSON 객체에서 필요한 내용을 파싱하여 각각의 NewsData 객체로 저장
 - 구글에서 제공하는 SwipeRefresh, SwipeDismiss 클래스를 사용하여  
   당겨서 새로고침, 밀어서 카드 삭제 기능을 구현
 - 카드 삭제시 스와이프 방향을 구분하여 관련 내용을 수헹할 수 있도록 SwipeDismiss 클래스에 내용 추가
 - Facebook Fresco 패키지를 사용하여 uri String에서 이미지를 다운로드, 뉴스 프리뷰 이미지로 사용 
 
## Reference
 - https://newsapi.org
 - Google SwipeRefreshLayout, SwipeDismissTouchListener
 - Facebook Fresco
 - Java JSON Library
 - Do it! 안드로이드 앱 프로그래밍
