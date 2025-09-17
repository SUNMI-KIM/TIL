![](https://velog.velcdn.com/images/hariaus/post/31a41bc5-5631-4658-9a87-c78e9b70caa7/image.png)


에브리타임 서비스를 이용하면서, 게시글이 수십만 개 이상 축적되어 있음에도 불구하고 **검색 기능과 페이지네이션이 매우 빠르게 동작하는 것**을 경험했다. 이를 보며 나의 프로젝트에도 비슷한 수준의 빠른 검색 기능을 적용하고 싶었고, 그 과정에서 **Elastic Search**라는 서비스를 알게 되어 직접 비교해 보기로 했다.

먼저 기존 프로젝트의 검색 구조와 코드를 살펴보고, 성능을 측정해 보았다.

## 검색 기능 개발 환경 및 구성


### 개발 환경

Java: 17

Spring Boot: 3.4.3

---

### PostController

```java
@GetMapping("/search")
    public ResponseEntity<Response<List<PostMapping>>> searchPost(@RequestParam String keyword) {
        List<PostMapping> postList = postService.searchPost(keyword);
        Response response = Response.builder().message("검색된 게시물").data(postList).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
```

### PostService

```java
public List<PostMapping> searchPost(String keyword) {
        return postRepository.findByTitleContainingOrContentContaining(keyword, keyword);
    }
```

### PostRepository

```java
List<PostMapping> findByTitleContainingOrContentContaining(String title, String content);
```

여기서 `Containing`은 SQL의 `LIKE`와 동일한 역할을 한다.

---

### Postman 데이터 삽입

Postman의 **Run 기능**을 사용해 약 30,000개의 게시글을 추가했다.

데이터는 ChatGPT가 생성해 준 CSV를 활용했으며, 전체 2,000셀(title+content) 중 15셀만 `"테스트 입니다"` 문구를 포함하고, 나머지는 모두 20자 이내의 랜덤 문자열로 구성되어 있다.

---

### 검색 결과

![](https://velog.velcdn.com/images/hariaus/post/e5e41379-c704-4703-aca1-fb25d0e5485b/image.png)


실제 검색 결과를 조회한 시간은 **약 726ms**로, Send 버튼을 누른 뒤 눈에 띌 정도의 로딩 화면이 나타난 후 결과가 출력되었다.

---

### LIKE Query 한계

현재 프로젝트에서는 JPA의 `Containing`을 사용해 SQL의 `LIKE` 기반 검색을 구현했다. 

하지만 데이터가 쌓일수록 검색 속도가 느려지고, 정확한 키워드 매칭이나 복잡한 조건 검색에는 한계가 있다. 

실제로 30000건의 게시글 검색에서도 약 726ms가 소요되어, 사용자가 체감할 수 있을 정도로 지연이 발생했다. 

→ 이러한 한계를 극복하고 더 빠른 검색을 구현하기 위해, Elastic Search를 도입하려고 한다.