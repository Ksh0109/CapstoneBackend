# 2026년도 캡스톤 BE part

구독 관리 서비스 Spring Boot 서버입니다.

## 사전에 설치해야 하는 것
1. Java 17 (JDK 17) 이상
2. MySQL (포트는 3306)

## 실행 가이드
### 1단계 : DB 세팅 ( 최초 1회만 )
MySQL 터미널이나 WorkBench에서 아래 SQL문을 실행해주세요.
PW는 1234로 설정해뒀습니다. 
만약 문제가 생기면 src/main/resources/application.yml 에서 확인해주세요.
이 부분은 추후에 변경할 예정입니다.
```sql
1. 데이터베이스 생성
CREATE DATABASE subscription_db;

2. 서버가 켜질 때까지 대기!
먼저 아래 '2단계: 서버 실행'을 해서 서버를 한 번 켰다 꺼주세요. 
(서버가 켜지면 테이블이 자동으로 생성됩니다.)

3. 임시 테스트 유저 생성 
USE subscription_db;
INSERT INTO users (email, name, password, is_notify_enabled, created_at) 
VALUES ('test@abc.com', '테스트유저', '1234', 1, NOW());
```

### 2단계 : 서버 실행
터미널을 키고, 프로젝트 폴더로 들어가셔서 다음 명령어를 입력해주세요
<br>Mac / Linux : 
```
./gradlew bootRun
```
<br>Windows :
```
gradlew.bootRun
```

### 3단계 : 접속 확인
서버가 켜지면 아래 주소로 접속 가능합니다
<br> 서버 상태 확인 : http://localhost:8080/api/status
<br> Swagger API 명세서 : http://localhost:8080/swagger-ui/index.html
<br>각종 추가 기능들은 현재 개발중입니다.
<br>문제가 생기면 연락주세요
