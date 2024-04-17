CHAT REDIS
---

실시간으로 채팅을 할 수 있는 채팅 프로젝트입니다.
WebSocket을 사용해 실시간으로 채팅을 주고 받을 수 있으며 개인, 그룹 채팅이 가능합니다.
메시지의 수신자에게는 FCM 알림이 발송됩니다.

- 프로젝트 명칭 : ChatRedis (Redis를 이용한 채팅 서비스입니다.)
- 개발 인원 : 1명
- 개발 기간 : 2024.02.24~
- 📄 [API 명세](https://www.notion.so/CHAT-REDIS-API-1f790348c2ed459180cef26e5e1a6944?pvs=21)

---

### 빌드 방법

```docker
docker-compose build && docker-compose up -d
```

http://localhost:8080/swagger-ui/index.html에서 API 명세를 확인할 수 있습니다.

---

### 사용 Skills

- Java17, SpringBoot, Gradle
- MySQL, Redis
- Junit5, Mockito
- Github, Docker
- FCM, Swagger

---

### 주요 기능

- 실시간 채팅 기능을 구현하기 위해 WebSocket과 Redis Pub/Sub 기능을 사용했습니다.
- WebSocket을 사용하기 전 JWT를 검증하도록 구현했습니다.
    - 로그인 된 사용자의 정보를 가져와서 인증/인가 처리를 하려고 했으나  STOMP의 Pub/Sub 과정에선 사용자 인증이나 보안을 Spring Security와 별개로 추가적인 작업이 필요하다는 것을 확인, JWT를 검증하는 과정을 추가했습니다.
- 채팅 기능을 호출하기 위한 인증/인가 검증 로직을 작성하였습니다.
- 채팅 메시지를 다양한 조건으로 필터링하여 조회할 수 있도록 QueryDSL을 사용했습니다.
    - 삭제한 메시지 제외, 채팅방을 다시 들어온 경우 이전 메시지 제외등 다양한 조건이 적용됩니다.

- 그룹 채팅
    - 커서 기반 페이징으로 채팅 메시지 목록을 조회할 수 있습니다.
    - 채팅 메시지를 삭제할 수 있습니다. (본인만 조회 불가능)
        - 실제로는 삭제되지 않고 @ElementCollection ****을 사용해 삭제된 메시지의 id를 기록하는 테이블을 작성했습니다.
    - 채팅방에 초대되기 이전 메시지는 조회할 수 없습니다.
    - 채팅방에 초대하거나 초대 코드를 사용해 입장 가능합니다.
    - 초대코드는 Redis에서 관리합니다.
        - 유효기간이 정해져있고 유실되어도 재발급하면 되는 코드이므로 빠른 입출력과 DB부하 감소를 위해 Redis에서 관리하도록 했습니다.
- 개인 채팅
    - 커서 기반 페이징으로 채팅 메시지 목록을 조회할 수 있습니다.
    - 채팅 메시지를 삭제할 수 있습니다. (본인만 조회 불가)
    - 채팅방을 나간 경우 이전 메시지를 조회할 수 없습니다.
- 알림 기능
    - 채팅 메시지가 전송되면 수신자 모두에게 Web Push 알림이 발송됩니다.
    - 알림 기능에 사용되는 FCM 토큰은 Redis에서 관리합니다.

---

### ERD

---

![ChatRedis](https://github.com/hankyu0301/ChatRedis/assets/77604789/9c6659d8-36a3-4401-b314-6f7e0af2986c)
