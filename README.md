# auth-service

> 사용자의 소중한 정보를 안전하게 관리하고, 서비스 이용을 위한 인증(Authentication) 및 인가(Authorization)를 담당하는 마이크로서비스입니다.

- - - - - -

## 핵심 기능

- **JWT 기반 인증 시스템**
  - `Access Token`: 단기 보안용 토큰 발급
  - `Refresh Token`: 로그인 유지(7일) 및 토큰 재발급 로직 지원
- **보안 강화 (Security)**
  - BCrypt 비밀번호 암호화 : 단방향 해시 알고리즘을 사용하여 사용자 비밀번호 암호화 저장
  - `Spring Boot Validation`: 회원가입 및 로그인 시 데이터 유효성 검증
- **세션 관리**
  - `Redis Blacklist`: 로그아웃 시 기존 토큰을 무효화하여 탈취된 토큰의 재사용 방지
  - `Redis`: Refresh Token 저장 및 세션 관리로 고성능 인증 처리



- - -

## API 라우팅 설계

| 컨트롤러명       | 요청 경로 (Path)         | 리턴값                 | 인증(JWT) 여부 |
|:------------|:---------------------|:--------------------|:----------:|
| **Login**   | `/api/auth/login`    | `TokenDto`   |     X      |
| **Logout**  | `/api/auth/logout`   | `String` (Success Message)         |     X      |
| **signUp**  | `/api/auth/signup`   | `UserResponse`    |     X      |
| **checkId** | `/api/auth/check-id` | `Map<String, Boolean>` |     X      |
| **getCurrentUser** | `/api/users/user`    | `UserResponse` |     **O**      |

---


## 기술 스택
- **Framework:** Java 21, Spring Boot, Spring Data MongoDB, Redis
- **Database:**
  - **MongoDB**: 사용자 계정 정보 저장 (NoSQL의 유연한 데이터 구조 활용)
  - **Redis**: Refresh Token 관리 및 로그아웃 블랙리스트 운영
- **Security:** JJWT 0.12.6, Spring Security
- **Build Tool:** Maven

---

## Roadmap & Updates
- [ ] Social Login (Google) 연동 테스트
- [ ] 회원 탈퇴 시 관련 데이터 연쇄 삭제 로직 구현
- [ ] 이메일 인증을 통한 비밀번호 찾기 기능 추가