![](https://velog.velcdn.com/images/hariaus/post/670531ab-338e-4d31-a878-98d92d12cd75/image.png)

자바를 다운로드하려고 보면, 버전 옆에 LTS라고 표시된 경우가 있다. 

LTS Long-Term Support의 약자로, 장기간 보안 업데이트와 버그 수정을 지원하는 버전을 의미한다. 

따라서 Java 개발을 할때는 LTS 버전을 선택하는 것이 유지보수 측면에서 유리하다. 

Java는 하위 호환성이 높은 언어로, Java 5나 Java 8에서 컴파일 된 코드는 대부분 Java 8~17과 같은 상위 JVM 환경에서도 실행할 수 있다. 반대로 Java 17에서 추가된 문법이나 기능을 Java 8 환경에서 사용하면 컴파일되지 않는다. 

이러한 특성을 하위 호환성이 높다고 하며, Java 8의 기본 기능을 먼저 학습한 후 필요에 따라 상위 버전의 기능을 익히는 것을 추천한다. 

## JDK(Java Development kit)

<aside>

**JDK는 자바 개발키트의 약자로 개발자들이 자바로 개발하는 데 사용되는 SDK(Software development kit) 키트이다.**

</aside>

자바는 널리 사용되는 프로그래밍 언어이기 때문에 JDK 또한 여러 배포판이 존재한다. 각자의 개발 및 운영 환경에 맞는 JDK를 선택해 사용하면 된다.

나의 경우 AWS 환경에서 배포할 때는 **Amazon Corretto**를 사용하고 있다.  

## Java 8


Java 8의 경우 대규모 릴리즈라고 불린다. 이후 업데이트의 경우 Java 8을 개선한 버전이라고 할 수 있다.

Java 8은 자바 역사에서 대규모 릴리즈로 평가된다. 이후 버전들은 전반적으로 Java 8에서 도입된 개념과 기능을 확장하고 개선하는 방향으로 발전해왔다. 

### Lambda 람다 표현식

```java
String[] strArray = {"apple", "banana", "kiwi", "strawberry"};

Arrays.sort(strArray, new Comparator<String>() {
    @Override
    public int compare(String s1, String s2) {
        return s2.length() - s1.length();
    }
});
```

배열을 문자열의 길이에 따라 정렬하는 경우, 위와 같이 `compare(String s1, String s2)`를 작성하기 위해 쓸데없는 코드를 더 작성해야 했다. 

```java
Arrays.sort(strArray, (s1, s2) -> s2.length() - s1.length());
```

Java 8 이후, 위와 같이 인터페이스의 구현체를 바로 정의할 수 있게 되면서 간결하고 직관적인 코드를 작성할 수 있게 되었다. 

### Stream

기존 반복문의 경우, 반복, 조건, 변환 로직이 한 곳에 섞이면서 비즈니스 로직의 의도를 파악하기 어려운 문제가 있었다. 

```java
List<String> result = new ArrayList<>();

for (String s : list) {
    if (s.length() > 3) {
        result.add(s.toUpperCase());
    }
}
```

Stream API가 도입되면서 어떻게 처리할지를 하나하나 작성하지 않아도, 무엇을 하고 싶은지를 중심으로 코드를 작성할 수 있게 되었다. 또한 병렬 처리 역시 쉽게 적용할 수 있게 되었다. 

```java
List<String> result = list.stream()
    .filter(s -> s.length() > 3)
    .map(String::toUpperCase)
    .collect(Collectors.toList());
```

## Java 11


### 로컬 변수 타입 추론

```java
var list = new ArrayList<String>();
```

Java 11부터는 로컬 변수에 한해 `var` 키워드를 사용하여 컴파일러가 타입을 추론할 수 있게 되었다. 

### 새로운 HTTP 클라이언트 API

Java 11에서는 Java 9 에서 실험적으로 도입되었던 HTTP Client API가 정식 기능으로 제공되었다. 

### String API 개선

```java
str.isBlank();
str.strip();
str.lines();
str.repeat(2);
```

### `Optional.isEmpty()`

기존의 `isPresent()` 메서드는 값이 존재하는지를 확인했다. Java 11부터는 `isEmpty()` 메서드를 통해 값이 없는 경우를 보다 직관적으로 표현할 수 있다. 

## Java 17


### Sealed Class

Sealed Class는 상속 가능한 클래스를 제한하여 클래스 계층 구조를 명확히 하고, 설계 의도를 코드로 표현할 수 있도록 한다. 

```java
public sealed interface Payment
    permits Card, Account {}
```

### 패턴 매칭 for switch **(Preview)**

기존의 if-else 문 대신 switch 문을 통해 가독성 좋은 조건 분기를 만들 수 있다. 

```java
switch (obj) {
    case Integer i -> ...
    case String s -> ...
}
```

### Record Patterns **(Preview)**

불변 데이터 객체를 간편하게 만들 수 있다. (Getter, Setter, 생성자 등 제공)

`public record Point(int x, int y) {}`

## Java 21


### 패턴 매칭 for switch

Java 17에서 미리보기로 제공되었던 switch 패턴 매칭이 Java 21에서 정식 기능으로 포함되었다. 

### 경량 가상 스레드

OS 스레드를 직접 사용하지 않는 가상 스레드가 도입되어, 높은 동시성을 보다 효율적으로 처리할 수 있게 되었다. 

### Foreign Function & Memory API

JNI 없이 자바 코드에서 C/C++ 등의 외부 언어 함수와 메모리에 접근할 수 있는 API가 정식 기능으로 표준화되었다. 

## Java 25



### **Scoped Values**

Java 25에서는 Scoped Values가 정식 기능으로 포함되었다. 이는 가상 스레드 환경에서 기존 `ThreadLocal`이 가지던 문제를 해결하기 위해 등장한 기능이다. 

### Structured Concurrency

동시에 실행되는 여러 작업을 하나의 논리적인 단위로 묶어 관리함으로써, 동시성 프로그래밍의 가독성과 안정성을 높인다. 

### **32비트 x86 포트 제거**

오래된 32비트 하드웨어 및 운영체제에 대한 지원이 공식적으로 중단되었다.