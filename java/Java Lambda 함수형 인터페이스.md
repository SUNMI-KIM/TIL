![](https://velog.velcdn.com/images/hariaus/post/2c8f3451-9168-4a02-898c-1c7729b08d6f/image.png)


# Lambda


람다 표현식이란 **함수형 인터페이스의 구현을 간결하게 표현하는 식**이다. 즉, 자바의 메소드를 간단한 함수 식으로 표현한 것이다. 

함수형 인터페이스를 구현한 **anonymous inner class**는 람다식을 사용해 더 간결하게 표현할 수 있다.

```jsx
String[] arr = {"Java", "C", "Python"};

Arrays.sort(arr, new Comparator<String>() {
    @Override
    public int compare(String o1, String o2) {
        return o1.length() - o2.length();
    }
});
```

# @FunctionalInterface


함수형 인터페이스란 추상 메서드가 오직 하나만 존재하는 인터페이스를 의미한다. 

이때 `@FunctionalInterface`를 명시함으로써 실수로 추상 메서드를 추가하는 것을 컴파일 타임에 방지하고 이 인터페이스가 람다용이라는 의도를 명확히 전달할 수 있다. 

추상 메서드가 하나라는 것은 `default method`나 `static method`가 여러 개 존재해도 위배되지 않는다. 

```java
@FunctionalInterface
interface StringLengthCalculator {
    int calculate(String str);
}
```

### 예시

```java
// 매개변수가 하나일 경우 소괄호 생략 가능
// 실행문이 한 줄이며 return문 하나일 경우 중괄호와 return 생략 가능
StringLengthCalculator stringlengthCalculator = str -> str.length();

// 생략할 수 없는 경우
.mapToInt((score) -> {
    if (score % 2 == 1) return score + 1;
    return score;
})
```

람다는 기본적으로 `매개변수 → 실행문`형태로 작성된다. 

람다를 처음 배울 때 헷갈렸던 부분은 `str -> str.length();`에서 `str`이 어떤 것인지 생각을 하게 되었는데, 함수의 매개변수가 들어간 것이었다. 

## 메서드 참조

람다의 실행문에서 기존 메서드 하나만을 호출하는 경우 `::` 연산자를 이용해 메서드 참조로 표현할 수 있다. 

```java
<클래스명 또는 객체> :: <메서드 명>
```

```java
Arrays.sort(nums, Integer::compare);

Arrays.sort(nums, (o1, o2) -> Integer.compare(o1, o2));
```

위 두 코드는 완전히 동일한 동작을 하며, 각각 메서드 참조와 람다식을 이용한 표현이다. 

# 표준 함수형 인터페이스 API


함수형 프로그래밍을 위해 람다가 도입되었고, 이 람다는 표준 함수형 인터페이스 API에서 주로 사용된다. 

- BI는 파라미터가 2개인 경우 접두사이다.

## Consumer 계열

`T → void`  

값을 받아서 소비만 하고 결과를 반환하지 않는다. `forEach`, 로깅, 출력에서 활용된다. 

- `P`는 `Integer, Long, Double`을 나타낸다.
- `p`는 `int, long, double`을 나타낸다.

| 인터페이스 | 입력 | 반환 | 메서드 |
| --- | --- | --- | --- |
| `Consumer<T>` | `T` | `void` | `accept(T t)` |
| `BiConsumer<T, U>` | `T`, `U` | `void` | `accept(T t, U u)` |
| `PConsumer` | `p` | `void` | `accept(p value)` |
| `ObjPConsumer<T>` | `T`, `p` | `void` | `accept(T t, p value)` |

## Supplier 계열

`() → T` 

값을 생성하여 제공한다. `orElseGet`, 객체 생성 등에 사용된다. 

- `P`는 `Boolean, Integer, Long, Double`을 나타낸다.
- `p`는 `boolean, int, long, double`을 나타낸다.

| 인터페이스 | 입력 | 반환 | 메서드 |
| --- | --- | --- | --- |
| `Supplier<T>` | 없음 | `T` | `get()` |
| `PSupplier` | 없음 | `p` | `getAsP()` |

## Function 계열

`T → R`

값을 받아 다른 값으로 변환하며, `map`의 핵심이다. 

- `P, Q`는 `Integer, Long, Double`를 나타낸다.
- `p, q`는 `int, long, double`을 나타낸다.

| 인터페이스 | 입력 | 반환 | 메서드 |
| --- | --- | --- | --- |
| `Function<T, R>` | `T` | `R` | `apply(T t)` |
| `BiFunction<T, U, R>` | `T`, `U` | `R` | `apply(T t, U u)` |
| `PFunction<R>` | `p` | `R` | `apply(p value)` |
| `PtoQFunction` | `p` | `q` | `applyAsQ(p value)` |
| `ToPFunction<T>` | `T` | `p` | `applyAsP(T t)` |
| `ToPBiFunction<T, U>` | `T`, `U` | `p` | `applyAsP(T t, U u)` |

## Operator 계열

`T → T`

Function의 특수 케이스로, 입력과 출력 타입이 동일하다. 

- `P, Q`는 `Integer, Long, Double`를 나타낸다.
- `p, q`는 `int, long, double`을 나타낸다.

`Function`의 특수 케이스, 입력과 출력 타입이 동일하다.

| 인터페이스 | 입력 | 반환 | 메서드 |
| --- | --- | --- | --- |
| `UnaryOperator<T>` | `T` | `T` | `apply(T t)` |
| `BinaryOperator<T>` | `T`, `T` | `T` | `apply(T t1, T t2)` |
| `PUnaryOperator` | `p` | `p` | `applyAsP(p value)` |
| `PBinaryOperator` | `p`, `p` | `p` | `applyAsP(p v1, p v2)` |

## Predicate 계열

`T → boolean`

조건 검사에 사용되며 `filter`의 핵심이다. 

- `P`는 `Integer, Long, Double`을 나타낸다.
- `p`는 `int, long, double`을 나타낸다.

| 인터페이스 | 입력 | 반환 | 메서드 |
| --- | --- | --- | --- |
| `Predicate<T>` | `T` | `boolean` | `test(T t)` |
| `BiPredicate<T, U>` | `T`, `U` | `boolean` | `test(T t, U u)` |
| `PPredicate` | `p` | `boolean` | `test(p value)` |