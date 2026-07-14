# Stock Project

KRX(한국거래소) 일별 지수 데이터를 수집·저장하고, KOSPI와 KOSDAQ의 추이를 웹 차트로 조회하는 풀스택 프로젝트입니다.

이 문서는 프로젝트의 큰 그림을 설명합니다. 각 애플리케이션의 설정, API 명세, 세부 실행 방법은 하위 README를 참고하세요.

- [Frontend 상세 문서](stock-frontend/Readme.md)
- [Backend 상세 문서](stock-api/Readme.md)

## 구성

```text
stock-project/
├── stock-frontend/  # React/Vite 웹 애플리케이션
├── stock-api/       # Spring Boot REST API 및 KRX 데이터 동기화
└── README.md        # 프로젝트 개요 (현재 문서)
```

| 영역 | 역할 | 주요 기술 |
| --- | --- | --- |
| `stock-frontend` | 기간·시장 선택, 차트 및 상세 정보 표시 | React, Vite, Axios, Recharts |
| `stock-api` | KRX 데이터 수집, DB 저장, 조회 REST API 제공 | Java 17, Spring Boot, JPA, WebClient |
| Database | 수집한 일별 지수 데이터 영속화 | MySQL 또는 PostgreSQL |
| KRX Data API | KOSPI/KOSDAQ 원천 지수 데이터 제공 | KRX API |

## 전체 흐름

```text
사용자
  │ 기간과 시장을 선택
  ▼
React Frontend (stock-frontend)
  │ GET /api/index/history
  ▼
Spring Boot API (stock-api)
  │ 조회
  ▼
MySQL / PostgreSQL  ◀── KRX Data API
                          │ 시작 시·매일 18:00 동기화
```

1. 백엔드는 시작 시 비동기로 KRX API에서 누락된 KOSPI/KOSDAQ 데이터를 수집합니다.
2. 데이터가 하나도 없으면 최근 1년부터 적재하고, 이후에는 DB의 마지막 거래일 다음 날부터 오늘까지 수집합니다.
3. 같은 작업이 매일 18:00에도 실행됩니다. `(market, trade_date)` 조합은 중복 저장되지 않습니다.
4. 프론트엔드는 사용자가 고른 기간에 대해 두 시장의 이력을 병렬 요청합니다.
5. 응답의 종가를 선 그래프로 표시하며, 그래프의 날짜를 클릭하면 해당 일자의 세부 지표를 보여 줍니다.

## 주요 기능

- KOSPI와 KOSDAQ 일별 지수 데이터 자동 수집
- 기간별 지수 이력 조회
- 전체/KOSPI/KOSDAQ 차트 전환
- 선택한 날짜의 시가·고가·저가·종가·등락률·거래량 확인
- 프론트엔드 주소를 제한하는 CORS 설정
- 로컬·운영 환경을 분리할 수 있는 프로필 및 환경 변수 설정

## 빠른 시작

### 1. 준비

- Java 17
- Node.js 및 npm
- MySQL 또는 PostgreSQL
- KRX API 인증 키

### 2. 백엔드 설정 및 실행

```powershell
cd stock-api
Copy-Item src/main/resources/application-local.properties.sample src/main/resources/application-local.properties
```

`application-local.properties`에 DB 접속 정보, KRX API 키, 프론트엔드 주소를 설정한 뒤 실행합니다.

```powershell
$env:SPRING_PROFILES_ACTIVE = "local"
.\gradlew.bat bootRun
```

API는 기본적으로 `http://localhost:8080`에서 실행됩니다. 첫 동기화에는 데이터 수집 시간이 걸릴 수 있습니다.

### 3. 프론트엔드 설정 및 실행

새 터미널에서 다음을 실행합니다.

```powershell
cd stock-frontend
Copy-Item .env.sample .env
npm install
npm run dev
```

`.env`의 API 주소가 로컬 백엔드를 가리키는지 확인합니다.

```dotenv
VITE_API_URL=http://localhost:8080/api
```

Vite가 출력한 로컬 주소(일반적으로 `http://localhost:5173`)로 접속합니다.

## 환경 변수와 보안

| 위치 | 파일 | 관리 대상 |
| --- | --- | --- |
| 프론트엔드 | `stock-frontend/.env` | `VITE_API_URL` |
| 백엔드 로컬 | `stock-api/src/main/resources/application-local.properties` | DB 정보, `krx.api.key`, `frontend.urls` |
| 백엔드 운영 | `stock-api/src/main/resources/application-prod.properties` 또는 환경 변수 | DB 정보, `krx.api.key`, `frontend.urls` |

실제 설정 파일은 Git에서 제외됩니다. 특히 KRX 인증 키와 DB 비밀번호는 커밋하지 마세요. 또한 `VITE_`로 시작하는 값은 브라우저에 노출되므로 비밀 정보를 넣으면 안 됩니다.

## 배포 개요

1. DB와 KRX API 키를 준비합니다.
2. 백엔드에 운영용 프로필과 DB/CORS 설정을 주입해 배포합니다. `stock-api`에는 Dockerfile이 포함되어 있습니다.
3. 프론트엔드는 운영 API 주소로 `VITE_API_URL`을 설정한 뒤 `npm run build`를 실행합니다.
4. 생성된 `stock-frontend/dist`를 정적 호스팅에 배포합니다.
5. 백엔드의 `frontend.urls`에 실제 프론트엔드 배포 주소를 등록합니다.

자세한 설정 값과 API 엔드포인트는 각 하위 프로젝트 README를 참고하세요.
