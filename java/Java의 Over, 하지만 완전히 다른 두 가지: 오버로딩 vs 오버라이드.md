![](https://velog.velcdn.com/images/hariaus/post/02289536-b9d1-4671-a5df-a1d501c5820c/image.png)

면접을 한 번도 가본 적이 없지만, 오버로딩과 오버라이드의 차이점은 꼭 알아야 한다는 말을 많이 들었다. 하나는 상속을 통한 메서드 재정의와 관련이 있고, 다른 하나는 매개변수의 개수나 타입을 다르게 정의하는 것이다. 하지만 이를 말해보라고 하면 자주 까먹어서 제대로 설명하지 못한다. 오늘은 이 두 개념을 정리해보도록 하겠다. 

## Override (오버라이드)


`@Override` 어노테이션을 사용하여 부모 클래스의 메서드를 자식 클래스에서 재정의하는 것이다. 부모 클래스에서 정의된 메서드의 기능을 자식 클래스에서 다르게 구현하려고 할 때 사용한다. 

```java
@Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }
```

- 위 코드는 `Object` 클래스의 `toString` 메서드를 오버라이드하여 `Person` 객체에 맞게 문자열로 출력할 수 있도록 재정의한 예시이다.

## Overload (오버로딩)



같은 이름의 메서드를 매개변수의 개수나 타입을 달리하여 여러 개 정의하는 것이다. 오버로딩은 메서드 이름은 동일하지만, 매개변수나 리턴 타입이 다르게 구현된다. 

```java
// 기본 생성자
    public Person() {
        this.name = "Unknown";
        this.age = 0;
    }

    // name만 받는 생성자
    public Person(String name) {
        this.name = name;
        this.age = 0;
    }

    // name과 age를 모두 받는 생성자
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
```

```java
class Calculator {
    
    // 두 정수를 더하는 메서드
    public int add(int a, int b) {
        return a + b;
    }

    // 세 정수를 더하는 메서드
    public int add(int a, int b, int c) {
        return a + b + c;
    }

    // 두 실수를 더하는 메서드
    public double add(double a, double b) {
        return a + b;
    }
    
    // 문자열을 더하는 메서드
    public String add(String a, String b) {
        return a + b;
    }
}
```

- `add` 메서드를 오버로딩하여, 매개변수의 개수와 타입에 따라 다르게 처리하도록 정의했다. 동일한 메서드 이름으로 다양한 방식의 덧셈을 구현할 수 있다.