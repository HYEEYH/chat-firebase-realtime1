
![header](https://capsule-render.vercel.app/api?type=waving&color=auto&height=200&section=header&text=Firebase%20ChattingApp&fontSize=70)

# chat-firebase-realtime1
채팅앱 - 파이어베이스-실시간데이터베이스기반(번역기능O)
<br/><br/>

* google 의 Firebase 를 이용한 채팅기능을 가진 앱으로 실시간데이터베이스 기반의 채팅 앱 입니다. <br/>
<a href= "https://drive.google.com/file/d/10OPgREisny6R08xASe2_5mxemA2di6JJ/view?usp=drive_link">[포트폴리오 보러 가기]</a><br/>
<a href= "https://drive.google.com/file/d/17uiTZSeLXYdTnOmmm3UxsVfYJjixShIg/view?usp=drive_link">[실시간데이터베이스 기반 앱 시연영상 보러 가기]</a><br/>
<a href= "https://drive.google.com/file/d/19BAwG2ERAnkJpk-cW4apO-H1Rnya0cHt/view?usp=drive_link">[파이어스토어 기반 앱 시연영상 보러 가기]</a><br/>
<img src="https://github.com/HYEEYH/aws-rekognition-app2/assets/130967557/c8146fce-acc5-47fd-8110-e8f9649767de"  width="700" height="442" /><br/><br/>

## 사용 툴
<div align=center>
<img src="https://img.shields.io/badge/Android Studio-3DDC84?style=flat&logo=androidstudio&logoColor=white"/>
<img src="https://img.shields.io/badge/Amazon AWS-232F3E?style=flat-square&logo=amazonaws&logoColor=white"/>
<img src="https://img.shields.io/badge/firebase-FFCA28?style=flat-square&logo=firebase&logoColor=white">
</div>

## 사용한 기술
### Front-ends
#### Android
- 어댑터를 만들어 상대방 글과 나의 글이 각각 왼쪽정렬, 오른쪽정렬 되도록 구현
- SharedPreferences를 이용하여 닉네임과 프로필 이미지를 디바이스에 저장
- 디바이스에 저장된 값(닉네임과 프로필 이미지)가 있는지 확인
- 글라이드에 텍스트를 배치
##### Firebase
- Firebase 콘솔에서 프로젝트를 만들고 안드로이드와 연동
##### Realtime Database
채팅 리스트를 보여주는 어댑터 구현
실시간데이터베이스에 데이터를 저장하고 받아오기 구현
라디오선택버튼을 통해 어떤 언어로 번역할 지 입력받음
유저가 작성한 글을 가져와서 입력받은 언어로 번역(네이버 파파고 오픈API 이용)
Config파일을 만들어 API key를 노출되지 않도록 숨김

### Open API
네이버 Papage 번역 API


