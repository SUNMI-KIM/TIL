

프로젝트를 진행하면서 사용자의 감정을 수치로 표현하고, 그에 맞는 피드백을 주어야하는 기능이 필요했다. 이 부분을 구현하기 위해 Chat Gpt API를 적용해보았다.  

## Chat Gpt 개발 환경 및 구성 - Chat Gpt Key 발급


1. 아래의 사이트에 접속한다. 
    
    [https://platform.openai.com/api-keys](https://platform.openai.com/api-keys)
    
2. 아래의  `Create new secret key` 버튼을 누른다. 
    
![](https://velog.velcdn.com/images/hariaus/post/19c4f751-b109-44cc-a501-9e45b1c13a9f/image.png)

    
3. 필요한 정보를 입력한 뒤 `Create secret key`를 누른다. 

![](https://velog.velcdn.com/images/hariaus/post/6d22ecac-47ab-489c-89f3-aedb2700e383/image.png)


1. 키가 생성되면 복사해 두고, 안전한 곳에 보관한다. 

![](https://velog.velcdn.com/images/hariaus/post/efce6cd6-8d5c-49cf-a5e5-30169a8ee81b/image.png)


## Chat Gpt 개발 환경 및 구성 - 개발 및 구성


### 개발 환경

Java: 17

Spring Boot: 3.4.3                                                                                         

### 프로젝트 구조

![](https://velog.velcdn.com/images/hariaus/post/9da53f86-351f-4998-b7f8-9b6ef19fe68d/image.png)


### application.yml

```java
openai:
  model: gpt-4o
  url: "https://api.openai.com/v1/chat/completions"
  secret-key: ${GPT_KEY}
```

공통적으로 사용할 설정을 `application.yml`에 추가해 두었다. `secret-key`값에는 발급받은 키를 환경 변수로 주입한다. 

### ChatGptConfig

```java
@Getter
@Configuration
public class ChatGptConfig {
    @Value("${openai.secret-key}")
    private String secretKey;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.url}")
    private String url;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    @Bean
    public HttpHeaders httpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
```

설정 값들을 불러와 Bean으로 등록하고, API 요청을 위해 `RestTemplate`을 사용했다. `HttpHeaders`에는 `secretKey`를 담아 `Service`에서 그대로 활용할 수 있도록 했다. 

### ChatGptRequest, ChatGptResponse

```java
@Data
@AllArgsConstructor
public class ChatGptRequest {
    private String model;
    private List<Message> messages;

    @Data
    @AllArgsConstructor
    public static class Message {
        private String role;
        private String content;
    }
}
```

```java
@Data
public class ChatGptResponse {
    private List<Choice> choices;

    @Data
    public static class Choice {
        private Message message;
    }

    @Data
    public static class Message {
        private String role;
        private String content;
    }
}
```

요청과 응답을 처리하기 위한 DTO 클래스이다.

### ChatGptService

```java
@Service
@RequiredArgsConstructor
public class ChatGptService {

    private final RestTemplate restTemplate;
    private final ChatGptConfig chatGptConfig;
    private final HttpHeaders baseHeaders;

    public String getChatGptAnswer(Map<String, Long> percentageMap) {
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(baseHeaders);

        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("다음은 한 사람의 감정 분석 결과입니다.\n\n");
        percentageMap.forEach((emotion, percentage) -> {
            promptBuilder.append(String.format("- %s: %d%%\n", emotion, percentage));
        });
        promptBuilder.append("\n이 데이터를 바탕으로, 이 사람의 현재 감정 상태를 분석하고 적절한 피드백을 주세요.  \n" +
                "- 슬픔, 분노, 불안 등 부정적인 감정이 높을 경우: 위로와 함께 전문가 상담, 휴식, 산책 등 실질적인 조치를 권장해주세요.  \n" +
                "- 행복, 평온 등 긍정적인 감정이 높을 경우: 긍정적인 감정을 유지하도록 격려해주세요.\n" +
                "\n" +
                "**조건:**\n" +
                "- 1줄\n" +
                "- 지나치게 포괄적이지 말고, 구체적이고 현실적인 행동을 제시해주세요.\n" +
                "- 상담사가 말하듯 따뜻하고 진심 어린 어투로 작성해주세요.");

        ChatGptRequest.Message message = new ChatGptRequest.Message("user", promptBuilder.toString());
        ChatGptRequest request = new ChatGptRequest(chatGptConfig.getModel(), Collections.singletonList(message));

        HttpEntity<ChatGptRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<ChatGptResponse> response = restTemplate.exchange(
                chatGptConfig.getUrl(), // 보낼 URL
                HttpMethod.POST, // POST 방식
                httpEntity, // httpEntity가 Body
                ChatGptResponse.class // ChatGptResponse 형식으로 응답 받겠다. 
        );

        return response.getBody()
                .getChoices()
                .get(0)
                .getMessage()
                .getContent();
    }
}
```

감정 분석 결과가 `{”기쁨”: 30, “슬픔”: 20 …}` 형태로 들어오면, 이를 문자열로 변환해 프롬프트에 포함시켜 요청을 보낸다. 응답으로 받은 메시지를 그대로 반환한다.