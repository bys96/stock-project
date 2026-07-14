# Stock API

한국거래소(KRX) 일별 지수 데이터를 수집해 데이터베이스에 저장하고, KOSPI·KOSDAQ 지수를 조회하는 Spring Boot API입니다.

## 주요 기능

- KRX 데이터 API에서 KOSPI와 KOSDAQ의 일별 지수 데이터를 조회합니다.
- 애플리케이션 시작 시 최근 1년부터 누락된 거래일 데이터를 비동기로 적재합니다.
- 매일 18:00에 누락 데이터를 다시 동기화합니다.
- 시장·거래일별 단건 조회와 기간별 시계열 조회 API를 제공합니다.
- `(market, trade_date)` 복합 유니크 제약으로 중복 저장을 방지합니다.

## 기술 구성

- Java 17, Spring Boot 4.1.0, Gradle 9.5.1
- Spring Web / WebFlux `WebClient`
- Spring Data JPA
- MySQL 또는 PostgreSQL
- Lombok, Jackson

## 프로젝트 구조

```text
src/main/java/io/github/bys96/stock_api
├── client/       # KRX API 클라이언트
├── config/       # CORS 및 WebClient 설정
├── controller/   # REST API
├── dto/          # KRX 응답 DTO
├── entity/       # stock_index 엔티티
├── repository/   # JPA Repository
├── runner/       # 시작 시 동기화 실행
├── scheduler/    # 매일 18:00 동기화
└── service/      # 지수 조회·수집·저장 로직
```

## 사전 준비

1. Java 17을 설치합니다.
2. MySQL 또는 PostgreSQL 데이터베이스를 준비합니다.
3. KRX 데이터 API 인증 키를 발급받습니다.
4. 실행 환경에 맞는 설정 파일을 샘플 파일에서 생성합니다.

```powershell
Copy-Item src/main/resources/application-local.properties.sample src/main/resources/application-local.properties
```

`application-local.properties`에 아래 값을 채웁니다. 이 파일은 `.gitignore`에 포함되어 있어 저장소에 커밋되지 않습니다.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/stock_db
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
frontend.urls=http://localhost:3000
krx.api.key=YOUR_KRX_API_KEY
```

PostgreSQL을 사용할 경우 `application-prod.properties.sample`의 JDBC URL 형식을 참고할 수 있습니다.

기본 프로필은 `prod`로 설정되어 있습니다.

로컬 개발 환경에서는 `application.properties`의 활성 프로필을 `local`로 변경합니다.

```properties
spring.profiles.active=local
```

## 실행

Windows PowerShell:

```powershell
.\gradlew.bat bootRun
```

테스트 및 JAR 빌드:

```powershell
.\gradlew.bat test
.\gradlew.bat bootJar
java -jar build/libs/app.jar
```

기본 포트는 `8080`입니다.

## API

### 특정 일자 지수 조회

```http
GET /api/index?market=KOSPI&tradeDate=20260709
```

| 파라미터 | 필수 | 설명 |
| --- | --- | --- |
| `market` | 예 | `KOSPI` 또는 `KOSDAQ` |
| `tradeDate` | 예 | 거래일 (`yyyyMMdd`) |

### 기간별 지수 조회

```http
GET /api/index/history?market=KOSDAQ&startDate=20260701&endDate=20260709
```

| 파라미터 | 필수 | 설명 |
| --- | --- | --- |
| `market` | 예 | `KOSPI` 또는 `KOSDAQ` |
| `startDate` | 예 | 시작 거래일 (`yyyyMMdd`) |
| `endDate` | 예 | 종료 거래일 (`yyyyMMdd`) |

### 전체 이력 조회

```http
GET /api/index/history/all
```

### 누락 데이터 수동 동기화

```http
POST /api/index/sync
```

동기화는 DB의 가장 최근 거래일 다음 날부터 오늘까지 KOSPI와 KOSDAQ 데이터를 수집합니다. 저장된 데이터가 전혀 없으면 최근 1년부터 수집합니다. 비거래일 또는 KRX 응답에 대상 지수가 없는 날짜는 저장하지 않습니다.

## 응답 데이터

조회 API는 `stock_index` 데이터를 JSON으로 반환합니다.

| 필드 | 설명 |
| --- | --- |
| `id` | 내부 식별자 |
| `market` | 시장 (`KOSPI`, `KOSDAQ`) |
| `tradeDate` | 거래일 (`yyyyMMdd`) |
| `openPrice`, `highPrice`, `lowPrice`, `closePrice` | 시가·고가·저가·종가 지수 |
| `changePrice`, `changeRate` | 전일 대비 변동값·등락률 |
| `volume`, `tradeValue`, `marketCap` | 거래량·거래대금·시가총액 |
| `createdAt`, `updatedAt` | 저장·수정 시각 |

## Docker

프로젝트 루트의 `Dockerfile`은 Gradle로 JAR을 빌드한 뒤 8080 포트로 실행합니다.

```powershell
docker build -t stock-api .
docker run --rm -p 8080:8080 --env SPRING_PROFILES_ACTIVE=prod stock-api
```

컨테이너 실행 시에는 데이터베이스 접속 정보, CORS 허용 주소, KRX 인증 키가 포함된 `application-prod.properties`를 이미지에 제공하거나 Spring 환경 변수로 주입해야 합니다.
