# Stock Frontend

KOSPI와 KOSDAQ의 기간별 지수 흐름을 조회하고 차트와 상세 정보로 보여 주는 React 기반 웹 애플리케이션입니다.

## 주요 기능

- 시작일과 종료일을 선택해 두 시장의 지수 이력을 동시에 조회합니다.
- 전체, KOSPI, KOSDAQ 탭으로 차트에 표시할 시장을 전환합니다.
- 종가를 Recharts 선 그래프로 표시합니다.
- 차트의 특정 날짜를 클릭하면 시가·고가·저가·종가·등락률·거래량을 카드 형태로 표시합니다.
- 조회 중에는 로딩 스피너를, 결과가 없을 때는 빈 결과 안내를 표시합니다.
- 날짜 입력값이 역전되지 않도록 시작일/종료일 범위를 제한합니다.

## 기술 구성

- React 19, Vite 8
- Axios
- Recharts
- react-spinners
- ESLint

## 프로젝트 구조

```text
src/
├── api/
│   └── stockApi.js          # 백엔드 이력 조회 API 호출
├── components/
│   ├── DateSearch.jsx       # 조회 기간 선택
│   ├── MarketTabs.jsx       # 시장 탭
│   ├── StockChart.jsx       # 지수 선 그래프 및 날짜 클릭 처리
│   └── StockDetail.jsx      # 선택 날짜의 상세 카드
├── utils/
│   └── stockFormatter.js    # 날짜·숫자·등락률 포맷팅
├── App.jsx                  # 화면 상태 및 데이터 조합
├── App.css                  # 컴포넌트 스타일
├── index.css                # 전역 스타일
└── main.jsx                 # React 진입점
public/                      # 파비콘 및 SVG 아이콘
```

## 사전 요구 사항

- Node.js 및 npm
- `/api/index/history` 엔드포인트를 제공하는 백엔드 서버

이 앱은 아래 형식으로 백엔드에 요청합니다.

```http
GET {VITE_API_URL}/index/history?market=KOSPI&startDate=yyyyMMdd&endDate=yyyyMMdd
```

`VITE_API_URL`에는 `/api`까지 포함해야 합니다. 예를 들어 로컬 백엔드가 `http://localhost:8080`에서 실행 중이면 `http://localhost:8080/api`로 설정합니다.

## 환경 변수 설정

샘플 파일을 복사해 `.env`를 만듭니다.

```powershell
Copy-Item .env.sample .env
```

`.env` 예시:

```dotenv
VITE_API_URL=http://localhost:8080/api
```

배포 환경에서는 다음처럼 API 주소를 지정합니다.

```dotenv
VITE_API_URL=https://your-domain.com/api
```

`.env`는 Git에서 제외됩니다. Vite 환경 변수는 클라이언트 번들에 포함되므로 비밀 값은 넣지 마세요.

## 설치 및 실행

```powershell
npm install
npm run dev
```

Vite가 출력하는 로컬 주소로 접속합니다.

## 명령어

| 명령어 | 설명 |
| --- | --- |
| `npm run dev` | 개발 서버 실행 |
| `npm run build` | 프로덕션 정적 파일을 `dist`에 생성 |
| `npm run preview` | 빌드 결과를 로컬에서 미리 보기 |
| `npm run lint` | ESLint 검사 |

## 데이터 형식

백엔드의 기간 조회 API는 배열을 반환해야 하며, 화면은 아래 필드를 사용합니다.

```json
[
  {
    "market": "KOSPI",
    "tradeDate": "20260709",
    "openPrice": 3200.12,
    "highPrice": 3225.56,
    "lowPrice": 3188.4,
    "closePrice": 3210.34,
    "changeRate": 0.75,
    "volume": 123456789
  }
]
```

차트에는 `closePrice`를 사용합니다. 상세 카드에는 `openPrice`, `highPrice`, `lowPrice`, `closePrice`, `changeRate`, `volume`을 표시합니다. 등락률은 양수면 빨간색, 음수면 파란색으로 표현합니다.

## 동작 흐름

1. 사용자가 조회 기간을 선택하고 조회 버튼을 누릅니다.
2. 앱이 KOSPI와 KOSDAQ 이력을 병렬 요청합니다.
3. 선택된 탭에 맞춰 두 결과를 거래일 기준으로 합치거나 단일 시장 데이터로 차트를 구성합니다.
4. 차트에서 날짜를 클릭하면 해당 일자의 시장별 상세 정보를 표시합니다.

## 배포

`npm run build`로 생성되는 `dist` 디렉터리를 정적 호스팅 서비스에 배포합니다. 배포 전에 해당 환경의 `VITE_API_URL`을 설정하고, 백엔드 CORS 설정에 프론트엔드 배포 주소가 허용되어 있는지 확인해야 합니다.
