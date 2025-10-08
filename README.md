<a href="#" target="_blank">
[사진첨부예정]
</a>

<br/>
<br/>

# 0. Getting Started (시작하기)
```bash
$ npm start
```
[접속하기]
<br/>
<br/>

# 1. Project Overview (프로젝트 개요)
![Static Badge](https://img.shields.io/badge/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8_%EC%9D%B4%EB%A6%84-Mega_%EA%B2%8C%EC%8B%9C%ED%8C%90-blue)<br />
![Static Badge](https://img.shields.io/badge/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8%20%EC%84%A4%EB%AA%85-MSA%EB%A5%BC%20%EC%9D%B4%EC%9A%A9%ED%95%98%EC%97%AC%20%EB%8C%80%EA%B7%9C%EB%AA%A8%20%ED%8A%B8%EB%9E%98%ED%94%BD%20%EC%B2%98%EB%A6%AC%EA%B0%80%20%EA%B0%80%EB%8A%A5%ED%95%9C%20%EA%B2%8C%EC%8B%9C%ED%8C%90%20%EC%84%9C%EB%B9%84%EC%8A%A4-darkgreen)<br />
![Static Badge](https://img.shields.io/badge/%EA%B0%9C%EB%B0%9C_%EB%82%A0%EC%A7%9C-2025.08.02_~_2025.10.08-orange)<br />


<br/>
<br/>

# 2. Team Members (팀원 및 팀 소개)
|                                                        이서영                                                        |
|:-----------------------------------------------------------------------------------------------------------------:|
| <img src="https://github.com/user-attachments/assets/bb84a010-1904-4020-9488-a3aa8176c1f8" alt="이서영" width="150"> |
|                                                      BE & FE                                                      |
|                                        [GitHub](https://github.com/Pneum4)                                        |

<br/>
<br/>

# 3. Key Features (주요 기능)
- **회원가입**:
  - 회원가입 시 DB에 유저정보가 등록됩니다.
    <br/>
    <br/>
- **로그인**:
  - 사용자 인증 정보를 통해 로그인합니다.
    <br/>
    <br/>
- **게시판**:
  - 게시판의 게시글 목록을 조회합니다.
    <br/>
    <br/>
- **게시글**:
  - 게시글 생성, 수정, 삭제 서비스를 제공합니다.
  - 페이지 단위로 게시글을 조회합니다.
  - 무한 스크롤을 이용하여 게시글을 조회합니다.
    <br/>
    <br/>
- **댓글달기**:
    - 무한 Depth로 대댓글을 작성 할 수 있습니다.
    <br/>
    <br/>
- **좋아요**:
  - 게시글에 좋아요 / 좋아요 취소 기능을 제공합니다.
    <br/>
    <br/>
- **조회수**:
  - 게시글의 조회수를 조회합니다.
    <br/>
    <br/>
<br/>
<br/>

# 4. Tasks & Responsibilities (작업 및 역할 분담)
| 이름  | 프로필                                                                                                             | 역할                                                                                        |
|-----|-----------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------|
| 이서영 | <img src="https://github.com/user-attachments/assets/bb84a010-1904-4020-9488-a3aa8176c1f8" alt="이서영" width="100"> | <ul><li>인프런 대규모 트래픽 강의 학습</li><li>Spring Boot 서버 어플리케이션 제작</li><li>클라이언트 페이지 제작</li></ul> |


<br/>
<br/>

# 5. Technology Stack (기술 스택)
## 💬 Language
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaScript](https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E)

## 🎨 FrontEnd
![React](https://img.shields.io/badge/react-%2320232a.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB)
![HTML5](https://img.shields.io/badge/html5-%23E34F26.svg?style=for-the-badge&logo=html5&logoColor=white)
![Styled Components](https://img.shields.io/badge/styled--components-DB7093?style=for-the-badge&logo=styled-components&logoColor=white)

## ⚙️ BackEnd
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)

## 🗄️ Database
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)

## 🚀 Distribution & Deployment
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)
![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-000?style=for-the-badge&logo=apachekafka)

# 6. Project Structure (프로젝트 구조)
```plaintext
project/
├── public/
│   ├── index.html           # HTML 템플릿 파일
│   └── favicon.ico          # 아이콘 파일
├── src/
│   ├── assets/              # 이미지, 폰트 등 정적 파일
│   ├── components/          # 재사용 가능한 UI 컴포넌트
│   ├── hooks/               # 커스텀 훅 모음
│   ├── pages/               # 각 페이지별 컴포넌트
│   ├── App.js               # 메인 애플리케이션 컴포넌트
│   ├── index.js             # 엔트리 포인트 파일
│   ├── index.css            # 전역 css 파일
│   ├── firebaseConfig.js    # firebase 인스턴스 초기화 파일
│   package-lock.json    # 정확한 종속성 버전이 기록된 파일로, 일관된 빌드를 보장
│   package.json         # 프로젝트 종속성 및 스크립트 정의
├── .gitignore               # Git 무시 파일 목록
└── README.md                # 프로젝트 개요 및 사용법
```

<br/>
<br/>

<br/>

# 7. 커밋 Convention
## 커밋 이모지
```
== 코드 관련
📝	코드 작성
🔥	코드 제거
🔨	코드 리팩토링
💄	UI / style 변경

== 문서&파일
📰	새 파일 생성
🔥	파일 제거
📚	문서 작성

== 버그
🐛	버그 리포트
🚑	버그를 고칠 때

== 기타
🐎	성능 향상
✨	새로운 기능 구현
💡	새로운 아이디어
🚀	배포
```

<br/>

## 커밋 예시
```
== ex1
✨Feat: "회원 가입 기능 구현"

SMS, 이메일 중복확인 API 개발

== ex2
📚chore: styled-components 라이브러리 설치

UI개발을 위한 라이브러리 styled-components 설치
```

<br/>
<br/>

