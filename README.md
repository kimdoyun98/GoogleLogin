# Google 로그인 구현 방법

## 1. Google Cloud Console 등록

### 1. Google Cloud 설정

#### 1-1. Google Cloud 프로젝트 생성
- Google Cloud Console 접속
- 새 프로젝트 생성

#### 1-2. OAuth 동의 화면 설정
- OAuth 동의 화면 생성
- 앱 이름 및 이메일 설정

#### 1-3. Android 앱 등록
- OAuth Client ID 생성
- 유형: Android

필요 정보:
- Package Name
- SHA-1
- SHA-256

#### 1-4. SHA 키 확인
- Debug SHA 키
```bash
./gradlew signingReport
```

또는

```bash
keytool -list -v \
-alias androiddebugkey \
-keystore ~/.android/debug.keystore
```

### 1-5. Web Client ID 생성
추가로 OAuth Client 생성:

- 유형: Web Application

생성 후:
- Web Client ID 저장
- 이후 `setServerClientId()` 에 사용

---

### 2. Google 로그인 라이브러리 추가

```kotlin
implementation("androidx.credentials:credentials:1.3.0")
implementation("androidx.credentials:credentials-play-services-auth:1.3.0")
implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")
```

---

## 로그인 처리 방법
```text
Google Login
    ↓
Google ID Token 발급
    ↓
Backend 서버 전달
    ↓
서버에서 Google Token 검증
    ↓
서비스 JWT(Access / Refresh Token) 발급
```

---

### 보안 방식

#### PKCE

Google 로그인은 OAuth2.0 기반의 PKCE 방식을 사용합니다.

PKCE는 Authorization Code 탈취 공격을 방지하기 위한 방식이며,  
`code_verifier`와 `code_challenge`를 사용해 요청한 클라이언트를 검증합니다.

Credential Manager 사용 시 PKCE는 Google SDK 내부에서 자동 처리됩니다.

---

#### Nonce

Replay Attack 방지를 위해 Nonce를 함께 사용했습니다.

```kotlin
.setNonce(generateSecureRandomNonce())
```

Nonce는 요청과 응답의 값 일치 여부를 검증하여  
이전에 발급된 ID Token 재사용 공격을 방지합니다.

---

### 서버 인증 구조

Google 로그인 성공 후 발급받은 `ID Token`을 서버로 전달합니다.

```kotlin
authApi.googleLogin(
    GoogleLoginRequest(idToken)
)
```

서버는 Google 공개키 기반으로 ID Token을 검증한 뒤  
서비스 전용 JWT를 발급합니다.

| Token | 역할 |
|---|---|
| Access Token | API 인증 |
| Refresh Token | 로그인 세션 유지 |

---

### 자동 로그인

앱 실행 시 Refresh Token 기반으로 자동 로그인을 처리합니다.

```text
앱 실행
    ↓
Refresh Token 확인
    ↓
Access Token 재발급 요청
    ↓
성공 → 자동 로그인
실패 → 로그인 화면 이동
```

---

### Access Token 재발급

API 요청은 Access Token 기반으로 처리하며,  
만료 시 Refresh Token을 사용해 재발급합니다.

```text
API 요청
    ↓
401 Unauthorized
    ↓
Refresh Token으로 재발급
    ↓
새 Access Token 저장
    ↓
기존 요청 재시도
```

OkHttp `Interceptor`와 `Authenticator` 기반으로 처리했습니다.

---

### 토큰 저장

로그인 상태 유지를 위해 토큰을 DataStore 기반으로 관리했습니다.

| 데이터 | 역할 |
|---|---|
| Access Token | API 요청 인증 |
| Refresh Token | 자동 로그인 유지 |

