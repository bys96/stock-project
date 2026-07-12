# 📈 Stock Project

KRX(한국거래소) 지수 데이터를 조회하고 시각화하는 웹 프로젝트입니다.

프론트엔드는 React를 사용하여 차트와 UI를 제공하며, 백엔드는 Spring Boot를 통해 KRX API와 통신하고 데이터를 제공합니다.

---

## 🚀 Tech Stack

### Frontend

* React
* Vite
* Recharts
* Bootstrap Icons
* CSS

### Backend

* Java 17
* Spring Boot
* Spring Web
* Spring WebFlux (WebClient)
* Spring Data JPA
* MySQL

---

## 📂 Project Structure

```text
stock-project
├── stock-frontend   # React
└── stock-api        # Spring Boot
```

---

## ✨ Features

* KOSPI / KOSDAQ 지수 조회
* 기간별 데이터 검색
* 라인 차트 및 단일 날짜 차트 제공
* KOSPI / KOSDAQ 탭 전환
* 지수 상세 정보 조회
* Spring Boot REST API 제공
* MySQL 데이터 저장 및 조회
* CORS 설정
* 환경변수를 이용한 민감 정보 관리

---

## 🔐 Environment Variables

민감한 정보는 Github에 포함되지 않습니다.

### Frontend

`.env.sample`

```env
VITE_API_URL=http://localhost:8080/api
```

실행 시

```text
.env.sample
      ↓
.env
```

로 복사하여 사용합니다.

---

### Backend

`application-secret.properties.sample`

```properties
frontend.urls=http://localhost:5173

krx.api.key=YOUR_KRX_API_KEY

spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

실행 시

```text
application-secret.properties.sample
                ↓
application-secret.properties
```

로 복사한 후 실제 값을 입력합니다.

---

## ⚙️ Getting Started

### Frontend

```bash
cd stock-frontend
npm install
npm run dev
```

기본 실행 주소

```
http://localhost:5173
```

---

### Backend

```bash
cd stock-api
```

`application-secret.properties`를 생성한 후

```bash
./gradlew bootRun
```

또는 IntelliJ에서 실행합니다.

기본 실행 주소

```
http://localhost:8080
```

---

## 📌 Notes

* 실제 API Key 및 DB 정보는 Github에 포함되지 않습니다.
* 환경변수를 통해 개발 환경과 배포 환경을 분리할 수 있습니다.
* 프론트엔드는 Spring Boot REST API를 통해 데이터를 조회합니다.
