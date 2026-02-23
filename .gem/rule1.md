# 애플리케이션 제약(컨벤션, 기술스택정의, 코드패턴 정의의)

1. 시큐리티에서 CORS 필터를 적용
2. 회원가입된 유저 정보보기 기능이 필요

- UserController에 /api/users/{id} 형태로 만들기
- 메서드는 AuthController 참고해서 패턴 동일하게 만들기
  -DTO는 @Data를 써 -추가된 소스코드에는 꼭 주석을 친절하고 쉽게 달아줘
- 코드수정이 일어나면, 대체된 필요없는 코드는 삭제해줘.
  ex) SecurityConfig에 CORS필터를 직접 적용했는데, filterConfig에 CORS부분과 filter 폴더에 CorsFilter 코드를
  삭제하지 않았어!
  - 분석은 한글로해줘
