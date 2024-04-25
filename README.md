CHAT REDIS
---
## WebSocket과 Redis 사용한 사용자 간 채팅 서비스

**Github :  📄 [Github Link](https://github.com/hankyu0301/ChatRedis)**

**API 명세서 : 📄 [API 명세서](https://www.notion.so/CHAT-REDIS-API-1f790348c2ed459180cef26e5e1a6944?pvs=21)**

**개발 인원 : 1명**

**개발 기간 : 2024.03.07~2024.03.26**

---

### 빌드 방법

```docker
docker-compose build && docker-compose up -d
```

[**http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) 에서 API 명세를 확인할 수 있습니다.**

---

### **Tech Stack**

- **Java**
- **Spring Boot, Spring Security**
- **Spring Data JPA, Spring Data Redis, QueryDSL, MySQL**
- **JWT, Docker, Swagger**

---

### **Features**

### 채팅 기능

- **WebSocket을 연결하는 과정에서 JWT를 검증할 수 있습니다.**
    - **WebSocket으로 보낸 요청은 일반적으로 HTTP 프로토콜이 아닌 WebSocket 프로토콜을 통해 전송됩니다. 별도의 Interceptor를 작성하여 WebSocket 사용자의 인증/인가를 처리했습니다.**
- **채팅 메시지 발행 시 JWT의 정보를 읽어와 userId를 주입하는 AOP를 작성했습니다.**
    - **누군가가 채팅 메시지 작성자를 조작할 수 있으므로, 해당 데이터는 JWT를 읽어 사용  했습니다.**
    
    ```java
    @Aspect
    @Component
    @RequiredArgsConstructor
    @Slf4j
    public class AssignUserIdAspect {
    
        private final JwtTokenizer jwtTokenizer;
    
        @Around(value = "@annotation(com.example.chatredis.global.aop.AssignUserId) && args(jwt, req)", argNames = "joinPoint,jwt,req")
        public void assignUserId(ProceedingJoinPoint joinPoint, String jwt, Object req) throws Throwable {
            Long userId = extractUserIdFromJwt(jwt);
    
            invokeSetUserIdMethod(req, userId);
    
            joinPoint.proceed();
        }
    
        private Long extractUserIdFromJwt(String jwt) {
            Map<String, Object> claims = jwtTokenizer.verifyJws(jwt.replace("Bearer ", ""));
            return Long.valueOf((String) claims.get("userId"));
        }
    	  // 2. setUserId() 메소드가 존재한다면 jwt를 읽어 주입합니다.
        private void invokeSetUserIdMethod(Object req, Long userId) {
            Class<?> reqClass = req.getClass();
            Optional<Method> setUserIdMethod = getSetUserIdMethod(reqClass);
    
            if (setUserIdMethod.isPresent()) {
                try {
                    setUserIdMethod.get().invoke(req, userId);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException("setUserId 메소드 실행에 실패했습니다.", e);
                }
            } else {
                throw new IllegalArgumentException("setUserId 메소드를 찾을 수 없습니다.");
            }
        }
    		// 1. 채팅 메시지를 보내는 사용자 요청의 클래스를 가져오고 'setUserId()' 메서드를 가져옵니다.
        private Optional<Method> getSetUserIdMethod(Class<?> clazz) {
            try {
                return Optional.of(clazz.getMethod("setUserId", Long.class));
            } catch (NoSuchMethodException e) {
                return Optional.empty();
            }
        }
    }
    ```
    
- **채팅 메시지를 다양한 조건으로 필터링하여 조회할 수 있도록 QueryDSL을 사용했습니다.**
    - **삭제한 메시지 제외, 채팅방을 다시 들어온 경우 이전 메시지 제외등 다양한 조건이 적용됩니다.**
- **채팅방 코드를 생성해 상대를 초대할 수 있습니다.**
    - **유효기간이 정해져있고 유실되어도 재발급하면 되는 코드이므로 빠른 입출력과 DB부하 감소를 위해 Redis에서 관리하도록 했습니다.**
- **채팅 메시지를 삭제할 수 있습니다. (본인만 조회 불가)**
    - **실제로는 삭제되지 않고 @ElementCollection 을 사용해 삭제된 메시지의 id를 기록하는 테이블을 작성했습니다.**
- **채팅 메시지 발행시 알림을 발송하는 과정을 비동기 이벤트를 생성해 결합도를 줄이도록 구현**
    - **알림을 발송하는 로직때문에 메시지 발행이 실패되서는 안되기 때문에 트랜잭션이 커밋된 이후 리스너에서 이벤트를 처리 하도록 TransactionPhase.AFTER_COMMIT 을 사용했습니다.**

### 회원 기능

- **Spring Security를 사용한 JWT 로그인/로그아웃을 구현**
    - **로그아웃 시 사용자의 AccessToken을 받아 남은 유효기간만큼 Redis에 저장하여 로그아웃 된 사용자의 AccessToken 재사용을 방지 했습니다.**

### ETC

- **실시간 채팅 기능을 구현하기 위해 WebSocket과 Redis Pub/Sub 구조를 사용했습니다.**
- **Swagger를 이용하여 API문서를 생성 하였습니다.**
- **docker-compose를 작성해 어디서든 도커 환경을 통한 빌드를 할 수 있습니다.**
---

### ERD

---

![ChatRedis](https://github.com/hankyu0301/ChatRedis/assets/77604789/9c6659d8-36a3-4401-b314-6f7e0af2986c)
