# Orury!

당신만의 클라이밍 커뮤니티, 오루리


## 멤버
|Backend|                                                            Backend                                                            |Backend|Backend|
|:---:|:-----------------------------------------------------------------------------------------------------------------------------:|:---:|:---:|
|<img src="https://github.com/Kernel360-4cell/algorithm-study/assets/44130863/eacb9aab-4a9b-4447-b516-9c5efe4484ce" width=100>| <img src="https://github.com/Kernel360/E2E1-Orury/assets/44130863/6fb7dacf-c5b6-4023-840f-96912da62910" width=100> |<img src="https://github.com/Kernel360-4cell/algorithm-study/assets/44130863/1ee6dd72-c060-4dab-996b-e9f9bc7048d2" width=100>|<img src="https://github.com/Kernel360-4cell/algorithm-study/assets/44130863/bef79d6c-5ec0-43c0-999c-906d42ad1e06" width=100>|
|[형준](https://github.com/kkkapuq)|                                                [찬욱](https://github.com/mooncw)                                                |[종민](https://github.com/ShineCorine)|[무룡](https://github.com/aqrms)|

## 기술 스택

![readme_stack](https://github.com/Kernel360/E2E1-Orury/assets/44130863/48959e1d-06d5-4b0a-be68-6d83ff143040)

## 서비스 흐름도
![image](https://github.com/Kernel360/E2E1-Orury/assets/44130863/ef0c31d3-623a-4ef8-b9a8-2a8fe0e5706b)

## CI/CD
![image](https://github.com/Kernel360/E2E1-Orury/assets/44130863/23132200-f87c-4137-ad4b-6227a6174caa)

## 사용법
### 1. apk를 다운받아서 사용하는 경우
제공해드린 APK 파일을 다운받아 여러분의 휴대폰에 설치하고 사용해주세요

### 2. IntelliJ에서 플러터를 직접 실행해야 하는 경우
intellij 실행 > plugins 에서 Dart와 Flutter를 설치합니다.

1. Android SDK 설치
2. Flutter SDK 설치

requirement

M1 Macbook의 경우 Rosetta2가 설치되어 있어야 합니다

Xcode가 설치되어 있어야 합니다.

[https://docs.flutter.dev/get-started/install](https://docs.flutter.dev/get-started/install) 에서 운영체제에 맞는 flutter 설치법을 확인하실 수 있습니다

1. `sudo softwareupdate --install-rosetta --agree-to-license`
2. SDK를 설치합니다
    1. 적절한 SDK를 다운받습니다.
        1. [intel mac 버전](https://storage.googleapis.com/flutter_infra_release/releases/stable/macos/flutter_macos_3.16.0-stable.zip)
        2. [Apple Silicon mac 버전](https://storage.googleapis.com/flutter_infra_release/releases/stable/macos/flutter_macos_arm64_3.16.0-stable.zip)
    2. 적당한 곳에 압축을 풀고(압축을 푼 곳 주소가 필요합니다)

       압축파일이 있는 곳으로 이동합니다 : `cd ~/development`
       압축을 풉니다: `unzip ~/Downloads/flutter_macos_arm64_3.16.0-stable.zip`

    3. 환경변수에 플러터 경로를 등록해줍니다

       `export PATH**=**"$PATH:`pwd`/flutter/bin"`

       만에 하나 계속 사용하실거면 .zshrc나 .bash_profile에 경로를 등록해야 합니다


3. 플러터 닥터를 실행해서 부족한 부분을 설정해줘야 합니다

`flutter doctor`

## 백엔드를 로컬에서 실행하는 방법

백엔드 프로젝트의 경로는 E2E1-Orury/backend/orury 입니다

1. 인텔리제이에서 해당 경로를 열어줍니다
2. backend/orury/src/main/java/com/kernel360/orury/config/JasyptConfig.java 파일에서
   @Configuration 어노테이션을 주석처리 합니다
3. backend/orury/src/main/resources/application.yml 파일을 다음과 같이 수정합니다


    ```
    ..
    
        url: ${LOCAL_DB_URL} # configuration에서 설정 필요
    #    url: ENC(4t9k8jNC5SIDd1hpsqRJiw5NgwtRAZ9l2cY42PcN3v6GNDC61KXk4X3nYwwUdW4+aC6g41zh14HyfWg+2fPmfuTXHg7aw+vTvL9Oomq1wEKM2r2ZHJbhmakdpmbeMVjVALC/MSk28Tp4a5sBhUFRWdpaFO2qQcpdUHUd9KsuJh0=)
        username: ${LOCAL_DB_USERNAME}
    #    username: ENC(2STroDy2TfWobkrTroFwbRcxLke05156ZFMrqwKmVssNcN4foSX4I6ax8YAgdW3R)
        password: ${LOCAL_DB_USER_PASSWORD}
    #    password: ENC(a8l7l/AYNz2x1HPYyu+urOBwVcBhQkeWgTi6Fvt+lQFq1QyHsL8yyi5+wr7lh26P)
    jwt:
      header: Authorization
      #HS512 알고리즘을 사용할 것이기 때문에 512bit, 즉 64byte 이상의 secret key를 사용해야 한다.
      #echo 'silvernine-tech-spring-boot-jwt-tutorial-secret-silvernine-tech-spring-boot-jwt-tutorial-secret'|base64
      secret: ${LOCAL_JWT_SECRET_KEY}
    #  secret: ENC(XNkGVBeK1sJZhLhri+zz7pJOhHCvfif265mvT8OUIbOGeQcOCtHNnG2s3qjsKNe2u+dLoNVQBzbF1bKUfDxi8Po5tL7jQbZMPA33Dg1QMQFQWV46IyrYnLykYXQQvpin/SNPXW04ECDoRLF3TNwcS22D8uWEwe8L2wtcauyHeO1z+J6lUQArPHy76O2pzC7FHlBjOTw3STd23e3dd1WBQtHAYVmOIvNuPreulzSaHXc=)
      access-validity: 1800
      refresh-validity: 3600
    ```
    
시스템 환경변수나 IntelliJ Configuration에서 
    
로컬 mysql 데이터베이스에 다음과 같은 환경 변수와 값을 등록해줍니다.

`LOCAL_DB_URL`, `LOCAL_DB_USERNAME`, `LOCAL_DB_USER_PASSWORD`, `LOCAL_JWT_SECRET_KEY`

또는 직접 등록해줘야 합니다. 

토큰 만료시간을 조절하기 위해 `access-validity`, `refresh-validity`의 값을 적절히 입력해줍니다.(단위는 초 입니다)

Api 확인을 위해서 스웨거에 접속하거나 postman 등을 써서 테스트해주시면 됩니다 
기본 포트는 8080 이고 swagger의 주소는 [다음](http://localhost:8080/swagger-ui/index.html)과 같습니다. 
