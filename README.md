# 🚪 Mini-Dooray (Team 5)

> **NHN Dooray! 협업 서비스를 벤치마킹하여 구현한 MSA 기반 업무 관리 시스템**  
> **개발 기간:** 2025.11.04 ~ 2025.11.11 (1주)  
> **팀원:** 4명 (Backend & Frontend)

<br>

## 📖 프로젝트 개요 (Overview)
**Mini-Dooray**는 팀 단위의 프로젝트 진행과 업무(Task) 관리를 효율적으로 돕는 웹 애플리케이션입니다.  
단일 모놀리식 구조가 아닌, **기능별로 서비스를 분리한 마이크로서비스 아키텍처(MSA)**를 도입하여 확장성과 유지보수성을 고려했습니다.

사용자는 웹 브라우저를 통해 **Gateway Server**로 접속하며, Gateway는 인증 처리 후 내부의 **Account API**와 **Task API** 서버와 통신하여 서비스를 제공합니다.

<br>


### 서비스 구성 (Service Components)

| 서비스명 | 역할 및 기능 | 기술 스택 | 담당자          |
|---|---|---|--------------|
| **Gateway Service** | 사용자 진입점, UI 렌더링(Thymeleaf), 보안/인증(Security), 트래픽 라우팅 | Spring Boot, Thymeleaf, Spring Security, Redis | **정예림**, 강병호 |
| **Account Service** | 회원 가입, 로그인 검증, 사용자 정보 관리 API 제공 | Spring Boot, JPA, MySQL | 강병호          |
| **Task Service** | 프로젝트, 태스크, 태그, 댓글 등 핵심 업무 데이터 관리 API 제공 | Spring Boot, JPA, MySQL | 이한음, 송도혁     |

<br>

## 🛠 전체 기술 스택 (Tech Stack)

### Common
![Java](https://img.shields.io/badge/Java-21-007396?style=flat-square&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.5.7-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=flat-square&logo=apachemaven&logoColor=white)

### Frontend & Gateway (My Contribution)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-View_Template-005F0F?style=flat-square&logo=thymeleaf&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-Auth-6DB33F?style=flat-square&logo=springsecurity&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-Session_Store-DC382D?style=flat-square&logo=redis&logoColor=white)

### Backend API (Account & Task)
![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-ORM-6DB33F?style=flat-square&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-Database-4479A1?style=flat-square&logo=mysql&logoColor=white)

<br>

## 👨‍💻 핵심 기여 내용 (My Key Contributions)

저는 본 프로젝트에서 시스템의 대문이자 보안을 책임지는 **Gateway Service** 개발을 전담했습니다.

### 1. 보안 및 인증 아키텍처 설계 (Security & Auth)
- **Redis 기반 분산 세션:** `SESSIONID`를 쿠키와 Redis에 매핑하여, 다중 서버 환경에서도 인증 상태가 유지되도록 설계했습니다.
- **Spring Security 커스터마이징:** `UserAuthenticationFilter`를 구현하여 매 요청마다 Redis 세션을 검증하고, 인증된 사용자만 비즈니스 로직에 접근하도록 인가 처리를 수행했습니다.

### 2. 마이크로서비스 통신 구현 (API Aggregation)
- **Service Layer 추상화:** 컨트롤러가 구체적인 통신 기술을 몰라도 되도록 `TaskApiService`, `AccountApiService`를 분리하여 비즈니스 로직의 결합도를 낮췄습니다.
- **RestTemplate 통신:** Account API(8082)와 Task API(8081)로 REST 요청을 보내고, 응답 데이터를 Thymeleaf 뷰에 적합한 포맷으로 가공하여 전달했습니다.

### 3. 사용자 경험(UX)을 고려한 예외 처리
- **Global Exception Handler:** API 서버와의 통신 단절이나 4xx/5xx 에러 발생 시, 사용자에게 불친절한 에러 페이지 대신 이전 페이지로 리다이렉트하거나 안내 메시지를 제공하는 전역 예외 처리 로직을 적용했습니다.

<br>
