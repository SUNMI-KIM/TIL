![](https://velog.velcdn.com/images/hariaus/post/46fac13e-fb4b-4293-9205-c19988eebb20/image.png)

객체가 스스로 행동하게 해야 한다는 객체지향 원칙에 따라 객체에 `equals()` 메서드를 구현하고 있었다. 

나는 `equals()`를 구현했을 뿐인데, 컴파일 시 다음과 같은 경고가 발생했다.

```java
equals() overridden but hashCode() not overridden
```

이는 `equals()` 가 `true`를 반환하는 두 객체는 반드시 동일한 `hashCode()` 값을 가져야 한다는 자바의 규칙 때문이다.

이번 글에서는 `equals()`와 `hashCode()`의 역할을 살펴보고, 왜 이 두 메서드는 항상 함께 오버라이드되어야 하는지 정리해보려고 한다. 

## equals 란?

자바에서 `String` 값의 비교가 필요할 때는 `==` 연산자가 아니라 `equals()` 메서드를 사용한다. 

- `==` : 두 참조 변수가 같은 객체를 가르키는지(주소값) 비교
- `equals()` : 두 객체의 논리적인 값이 같은지 비교

String 클래스는 내부적으로 equals()가 재정의되어 있기 때문에, 문자열의 내용 자체를 비교할 수 있다. 

하지만 우리가 직접 구현한 객체의 경우, `equals()`를 오버라이드하지 않으면 `Object` 클래스의 기본 구현을 사용하게 되고, 이 경우 주소값 비교가 이루어진다. 

### equals 재정의

우리가 만든 객체를 값 기준으로 비교하고 싶다면, `equals()`를 재정의하여 값을 비교하도록 변경해야 한다.

```java
public boolean equals(Object o) {
        if (this == o) return true; 
        if (!(o instanceof Person)) return false; 
        Custom custom = (Custom) o; 
        return Objects.equals(this.field, 객체.field); 
    }
```

위와 같이 재정의하면 `Custom` 객체는 `field` 값이 같을 경우 같은 객체로 판별된다. 

## HashCode 란?

hashCode()는 객체를 식별하기 위해 사용하는 정수 값(int)을 반환하는 메서드이다. 기본 구현에서는 객체의 메모리 주소를 기반으로 값이 생성된다. 

`equals()`와 `hashCode()`는 모두 객체의 동일성을 판단하는데 사용되지만, 용도와 사용 시점은 다르다. 

### HashCode를 재정의하지 않으면 생기는 문제

해시 기반 자료구조(`HashMap`, `HashSet`, `Hashtable`)에서는 객체를 빠르게 저장하고 조회하기 위해 다음과 같은 과정을 거친다. 

![](https://velog.velcdn.com/images/hariaus/post/48bd72be-28bf-4e9e-bcbe-41a6d0f0a4aa/image.png)


1. `hashCode()`를 호출하여 해시 값을 얻는다. 
2. 다르다면 다른 객체로 판별한 후 같은 값을 가진 객체끼리 `equals()`를 호출하여 반환 값을 비교한다. 
3. `equals()`까지 같다면 같은 객체로 판별된다. 

이때 `hashCode()`는 정수 값 하나로 비교하므로 모든 field 값을 비교하는  `equals()`에 비해 빠르다. `equals()`를 호출하기 전, 1차 필터로 사용된다. 

### Collection은 같은 객체로 인식한다

같은 값을 가진 객체더라도, `hashCode()`를 재정의 하지 않으면 `Collection` 입장에선 같은 객체로 인식하지 않는다. 

→ 따라서 위와 같은 문제를 방지하기 위해 `equals()`와 `hashCode()`를 둘 다 재정의 해야한다.
