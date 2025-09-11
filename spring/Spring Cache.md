# Spring Cache


백엔드 프로젝트 개발 중, 좋아요 수 조회 시 캐시를 적용하면 성능이 올라간다는 게시글들을 보고 나도 따라 도전하기로 했다. 

# Cache



데이터를 저장하여 나중에 해당 데이터에 대한 요청을 더 빠르게 처리할 수 있는 저장소이다.


- cache hit : 캐시 스토어(redis)에 데이터가 있을 경우 바로 가져온다.
- cache miss : 캐시 스토어(redis)에 데이터가 없을 경우, DB에서 데이터를 조회해야 한다.

캐시 기능을 도입하면 **성능을 크게 향상**시킬 수 있지만, 그로 인해 **데이터 정합성 문제**가 발생할 수 있다. 이 문제를 방지하려면 **캐시 쓰기 및 읽기 전략**을 적절히 도입해야 한다.

## 캐시 쓰기 전략


### Write-Back

캐시에 먼저 저장하고 일정 주기 스케줄링 작업(배치 작업)을 통해 데이터베이스에 저장하는 전략이다.

### Write-Through

저장할 데이터를 캐시에 먼저 저장 후 캐시에서 데이터베이스로 저장하는 전략이다. 

### Write-Around

모든 데이터를 데이터베이스에 저장하는 전략이다. 캐시엔 저장하지 않는다. 나중에 읽기를 시도할때 cache miss 라면 cache를 갱신한다. 

## 캐시 읽기 전략



### Read Through

캐시에서만 데이터를 읽어오는 전략이다. 

### Look Aside

데이터를 찾을때 우선 캐시에 저장된 데이터가 있는지 우선적으로 확인하는 전략이다.

💡 cache miss가 났을때 서버에서 DB를 바로 조회하면 Look Aside 패턴, 캐시에서 DB를 조회 후 다시 서버에 전달하는 방식이라면 Read Through 패턴이다.

## 프로젝트 캐시 전략


처음에는 Write-Around 전략을 사용하여 좋아요 수 조회와 쓰기 성능을 개선하려 했으나, 좋아요 수는 데이터 정합성이 매우 중요한 요소라고 판단했다.

따라서, 캐시와 데이터베이스 간의 일관성을 보장하는 것이 필요하다고 생각하여 쓰기 작업에서는 캐시를 사용하지 않기로 했다.

캐시를 통한 성능 개선은 조회에만 적용하기로 했다.

## Spring에서 Redis Cache 적용

### 프로젝트 환경

Java: 17

Spring Boot: 3.4.3                                                                                         

### 1. Redis 설정

- Redis 서버와 연결하기 위해 `RedisConnectionFactory`를 사용하고, 캐시 매니저(`CacheManager`)를 설정했다.
- 직렬화 방식과 캐시 TTL(Time-To-Live)도 설정했다.

```java
@Configuration
public class RedisCacheConfig {

    @Bean
    // 캐시 추상화를 담당하는 인터페이스 @Cacheable, @CachePut, @CacheEvict 같은 애너테이션과 연동됨.
    public CacheManager contentCacheManager(RedisConnectionFactory cf) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig() // 기본 redis 캐시 설정을 가져옴
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())) // redis에 key를 문자열로 저장
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())) // Value를 JSON 형태로 직렬화
                .disableCachingNullValues() // null 값 저장 x
                .entryTtl(Duration.ofMinutes(3L)); // 캐시 수명 30분

        // Spring CacheManager로 Bean 등록 → @Cacheable 등과 연동 가능
        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(cf).cacheDefaults(redisCacheConfiguration).build();
    }
}
```

- `@EnableCaching`을 적용하여 `@Cacheable`, `@CachePut`, `@CacheEvict` 등의 애너테이션을 사용할 수 있다.

```java
@EnableCaching // 여기
@EnableScheduling
@SpringBootApplication
public class Jeongo3Application {

    public static void main(String[] args) {
        SpringApplication.run(Jeongo3Application.class, args);
    }
}
```

### 2. 조회 로직에 캐쉬 적용

- 기존에는 `postLikeRepository`에서 바로 좋아요 수를 조회했지만, 캐시를 적용하기 위해 메서드를 분리했다.

```java
@Cacheable(value = "postLikeCount", key = "#postId")
    public int getLikeCount(String postId) {
        return postLikeRepository.countByPost_Id(postId);
    }
```

스프링이 내부적으로 `value:key` 형태로 Redis에 저장한다.

- 예: `postLikeCount::123` → `27`

![](https://velog.velcdn.com/images/hariaus/post/94c4b316-0610-4f11-882c-a407f2e7851c/image.png)

![](https://velog.velcdn.com/images/hariaus/post/3717aafd-8ec2-4c48-909f-bb7398365bb8/image.png)


### 3. 좋아요 수 변경 시 캐시 삭제

- `@CacheEvict(value = "postLikeCount", key = "#postId")`을 사용하여 좋아요 상태가 변경되면 캐시를 삭제하도록 했다.

```java
@CacheEvict(value = "postLikeCount", key = "#postId")
    public void savePostLike(String postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new MyException(POST_NOT_FOUND));
        if (postLikeRepository.existsByUserAndPost(user, post)) {
            throw new MyException(BAD_REQUEST);
        }
        postLikeRepository.save(new PostLike(user, post));
    }

    @CacheEvict(value = "postLikeCount", key = "#postId")
    public void deletePostLike(String postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new MyException(POST_NOT_FOUND));
        PostLike postLike = postLikeRepository.findByUserAndPost(user, post).orElseThrow(() -> new MyException(POSTLIKE_NOT_FOUND));
        if (!postLike.getUser().getId().equals(user.getId())) {
            throw new MyException(BAD_REQUEST);
        }
        postLikeRepository.delete(postLike);
    }
```

> 참고: `@CachePut`을 사용하면 캐시를 직접 갱신할 수도 있지만, 이번 프로젝트에서는 조회 성능 향상이 목적이므로 사용하지 않았다. 
> 

### 4. 캐시 적용 결과

- 캐시 미스 시(DB 조회) 소요 시간: **201ms**
- 캐시 히트 시: **37ms**

> 단일 조회 기준으로 약 5배 이상 성능 향상을 확인할 수 있었다. 
>