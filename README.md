# ✈️ K-Traveler

# 프로젝트 소개

<img src="https://github.com/First-Time-Korea/.github/assets/65807803/12068b05-f1c5-4045-a547-f8fd25b16b53" width="500px" />

외래 관광객을 위한 한국 관광지 추천 서비스

> 본 사이트는 `여행 테마 테스트`를 통해 여행객의 `여행 선호도`를 파악하고, 분류된 테마에 따라 `맞춤형 관광지`를 추천합니다.
> 추천된 관광지를 가이드로 하여금, 외래 관광객이 한국 여행에 대한 계획을 수월하게 세울 수 있도록 돕습니다.

<br>

# 기획 의도

외래 관광객은 국내 여행을 떠나는 한국인과 달리, 한국에 대한 사전 정보가 없습니다.

그들은 계획을 세우기 앞서, 어떤 것부터 알아봐야할 지 막막해 합니다.

그럼에도 직접 여행 계획을 세우기 위해 여행 사이트를 방문하는 여행객은, 본인만의 특색있는 여행을 떠나고 싶은 여행객이라 판단했습니다.

여행 테마 테스트를 통해 여행객의 여행 선호도를 파악하고, 분류된 테마에 따라, 맞춤형 관광지를 추천합니다.

추천된 관광지를 가이드로 하여금, 외래 관광객이 한국 여행에 대한 계획을 수월하게 세울 수 있도록 돕게 하고자 기획 되었습니다.


<br>

---

# 문서

| 기획/디자인                                                                                                                                                                                        | ERD                                                  | 작업록                                                                                                                    | 문서                                                                                                                          | 리팩토링                                                                                                   |
|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------|
| [🎨기획/디자인](https://www.figma.com/design/q4wSnsEX1NBRHRxGLdanwh/%EC%96%B4%EC%84%9C%EC%99%80-%ED%95%9C%EA%B5%AD%EC%9D%80-%EC%B2%98%EC%9D%8C%EC%9D%B4%EC%A7%80?node-id=0-1&t=E7wMpTYvwAUo8BNz-1) | [⚡ERD](https://www.erdcloud.com/d/GxTNeS8AxmjZa7Wbn) | [📝작업록](https://crystalline-larch-eb2.notion.site/9cddd4769e0a4bcaaa84832dc2808008?v=310fe8c0f78045308abf3b1d9aa5af41) | [📜문서](https://crystalline-larch-eb2.notion.site/75f807b5af9e4148ad50d2cd3a6aa62d?v=ac2be13221bf4aacab7ed677b84aa869&pvs=4) | [🛠️리팩토링](https://github.com/First-Time-Korea/K-Traveler-BE/wiki/%EB%A6%AC%ED%8C%A9%ED%86%A0%EB%A7%81) |

<br>

---

# 프로젝트 소개 및 주요 기능

[![Project Overview](https://img.youtube.com/vi/8EsWTENBV0U/0.jpg)](https://www.youtube.com/watch?v=8EsWTENBV0U)


---

# 기술 스택

| Category                         | Details                                 |
|----------------------------------|-----------------------------------------|
| **Server**                       | Tomcat                                  |
| **Language**                     | Java 17, JavaScript                     |
| **Framework**                    | SpringBoot 3.2, MyBatis 3.0, Vue.js 3.4 |
| **IDE**                          | Intellij, VS Code, MySQL Workbench      |
| **Infra**                        | -                                       |
| **Database**                     | MySQL (Community Edition), JDBC         |
| **Authentication/Authorization** | JWT                                     |
| **CI/CD**                        | -                                       |
| **Team Collaboration Tool**      | Git, Github, Discord, Notion            |

<br>

---

# 역할 분담

<table>
  <tr>
      <td align="center">
        <a href="https://github.com/seminss">
          <img src="https://avatars.githubusercontent.com/seminss" width="120px;" alt="seminss">
        </a>
      </td>
      <td align="center">
        <a href="https://github.com/jiwon718">
          <img src="https://avatars.githubusercontent.com/jiwon718" width="120px;" alt="jiwon718">
        </a>
      </td>
  </tr>
  <tr>
    <td align="center">
      <a href="https://github.com/seminss">
        김세민
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/jiwon718">
        이지원
      </a>
    </td>
  </tr>
</table>

## 🐰 김세민 (FE & BE)
- K-Culture 관련 명소 데이터 수집 및 DB 저장
- Chat GPT를 이용하여 국문 데이터 영문으로 번역
- 회원 관련 화면 및 Rest API 구현
- 국내 여행지 검색 화면 및 Rest API 구현
- 여행 계획 화면 및 Rest API 구현

## 🦊 이지원 (FE & BE)
- 영문 국내 여행지 데이터 수집 및 DB 저장
- 여행 테마 테스트 화면 및 Rest API 구현
- 여행 후기 게시판 화면 및 Rest API 구현

---

# 기술적 경험

[GPT API를 이용 영문 데이터베이스 구축](https://seminss.tistory.com/14)

[스토어드 프로시저 적용기](https://seminss.tistory.com/15)

[여행 후기 게시판 관련 CRUD API 구현](https://jione-i.tistory.com/8)

---

# 아키텍처

## BE

<img src="https://github.com/First-Time-Korea/.github/assets/65807803/4d0b180f-697a-4f81-bba4-6cb880391946"/>

```
├─java
│  └─com
│      └─ssafy
│          └─firskorea
│              ├─common
│              │  ├─consts
│              │  ├─dto
│              │  └─exception
│              ├─config
│              ├─controller
│              ├─domain
│              │  ├─attraction
│              │  │  ├─constant
│              │  │  ├─dto
│              │  │  │  ├─prompt
│              │  │  │  ├─request
│              │  │  │  └─response
│              │  │  │      └─category
│              │  │  ├─mapper
│              │  │  └─service
│              │  ├─board
│              │  │  ├─dto
│              │  │  │  ├─request
│              │  │  │  └─response
│              │  │  ├─mapper
│              │  │  └─service
│              │  ├─member
│              │  │  ├─dto
│              │  │  │  └─request
│              │  │  ├─mapper
│              │  │  └─service
│              │  └─plan
│              │      ├─dto
│              │      │  ├─request
│              │      │  └─response
│              │      ├─mapper
│              │      └─service
│              ├─intercepter
│              └─util
└─resources
    └─mapper

```
