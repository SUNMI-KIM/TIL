# @JsonManagedReference와 @JsonBackReference


```java
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    @Column(name = "POST_ID")
    private String id;

    @Enumerated(EnumType.STRING)
    private PostType postType; // 게시판 이름

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OrderBy("time DESC")
    private List<Comment> comments;

```

```java
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    @Column(name = "COMMENT_ID")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

```

위와 같이 `Post`와 `Comment` 엔티티가 존재하며, 두 클래스는 1:N 관계를 맺고 있다.

프로젝트를 진행하면서 Postman으로 `Post` 데이터를 조회했을 때, 응답이 제대로 표시되지 않고 스프링부트에서는 `StackOverflowError`가 발생했다.

응답 JSON을 확인해 보니, `Post` 직렬화 과정에서 `Comment`가 포함되고, 다시 그 `Comment` 안에서 `Post`가 직렬화되면서 무한 순환 참조가 일어난 것이 원인이었다.

## **@JsonManagedReference, @JsonBackReference**

`@JsonManagedReference`는 직렬화를 수행하는 어노테이션이고,

`@JsonBackReference`는 역참조를 직렬화하지 않도록 하여 순환을 방지하는 어노테이션이다.


일반적으로 1:N 관계에서는 **1쪽 필드에 `@JsonManagedReference`**, **N쪽 필드에 `@JsonBackReference`**를 적용한다.

이렇게 하면 1쪽에서는 직렬화가 이루어지고, N쪽에서는 직렬화가 차단되어 순환 참조 문제가 해결된다.

```java
public class Post {

		@JsonManagedReference // 추가
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OrderBy("time DESC")
    private List<Comment> comments;
```

```java
public class Comment {

		@JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;
```

## DTO 사용

순환 참조 문제를 근본적으로 해결하는 방법은 어노테이션에 의존하지 않고 **DTO를 생성하여 반환하는 것**이다.

이번 문제가 발생한 원인은 엔티티를 그대로 API 응답으로 내려주었기 때문이다.

따라서 엔티티를 DTO로 변환해 반환하면, 순환 참조를 원천적으로 방지할 수 있으며 응답 구조를 필요에 맞게 설계할 수도 있다.