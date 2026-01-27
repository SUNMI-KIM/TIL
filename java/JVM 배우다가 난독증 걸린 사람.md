![](https://velog.velcdn.com/images/hariaus/post/02289536-b9d1-4671-a5df-a1d501c5820c/image.png)

SSAFY에서 추상화 개념을 배우며 JVM의 메모리 구조를 간단하게 접하게 되었다. 이 참에 JVM에 대해 한 번 제대로 정리해두자는 생각이 들어 글을 작성하게 되었다. 

JVM은 개념 자체가 방대하다 보니 한 번에 이해하기가 쉽지 않았고, 유튜브 강의를 참고하며 전체 흐름 위주로 정리했다. 

> https://www.youtube.com/watch?v=GU254H0N93Y&t=652s
> 

## JVM(Java Virtual Machine)


<aside>

JVM(Java Virtual Machine)은 자바 가상 머신의 약자로, 프로그램을 실행하기 위해 **물리적 머신과 유사한 환경을 소프트웨어로 구현한 것**이다.

</aside>

JVM은 자바 바이트코드(.class)를 OS에 특화된 기계어로 변환하여 실행한다. Java와 OS 사이에서 중개자 역할을 수행함으로써, Java 프로그램이 OS에 구애받지 않고 독립적으로 실행될 수 있도록 한다. 

### JVM의 동작 방식

1. JVM은 실행 시 OS로부터 메모리를 할당받는다. 
2. 자바 소스 코드(.java)를 작성하고 컴파일을 하면 자바 바이트코드가(.class) 생성된다. 
3. Class Loader는 동적 로딩을 통해 필요한 클래스들을 로딩 및 링크하여 Runtime Data Area의 Method Area(Meta-Space)에 올린다.
4. Runtime Data Area에 로딩된 바이트코드는 Execution Engine에 의해 해석된다. 이때 인터프리터가 바이트코드를 한 줄씩 실행하며, 반복적으로 실행되는 코드는 JIT 컴파일러가 기계어로 변환하여 성능을 최적화한다.
5. JVM은 실행 중 Heap 영역을 관리하기 위해 Garbage Collector를 동작시키며, 멀티 스레드 환경에서는 동기화 메커니즘을 통해 안전한 실행을 보장한다. 

## JVM의 구조



![](https://velog.velcdn.com/images/hariaus/post/0c7cd5c5-a092-4140-b482-b6b690297b48/image.png)


---

### 스레드 공유 영역

다음 영역들은 모든 스레드가 공유하므로 멀티 스레드 환경에서 동기화에 주의해야 한다.

**Method Area (Meta Space)**

클래스에 대한 메타데이터를 저장하는 영역이다. 객체를 생성하기 위한 설계도 역할을 하며, 클래스 필드 정보, 메서드 정보, 상수 풀을 관리한다. 

**Heap**

런타임에 생성되는 모든 객체와 배열이 저장되는 영역이다. Garbage Collector의 관리 대상이다. 

---

### 스레드 전용 영역

다음 영역들은 스레드가 생성될 때마다 함께 생성되며, 서로 다른 스레드가 침범할 수 없는 영역이다. 우리가 지역 변수의 동시성 문제를 따로 고려하지 않는 이유도 여기에 있다. 

**Stack**

메서드를 실행하기 위한 정보들이 저장되는 공간이다. 메서드 호출 시 마다 메서드 프레임이 적층된다. 메서드 종료 시 프레임이 제거되며 메모리를 반납한다. 지역 변수, 파라미터, 참조 변수를 저장한다. 

**PC Registers**

각 스레드가 현재 실행 중인 JVM 명령어의 주소를 저장하는 영역이다. 멀티 스레드 환경에서 스레드 전환 시, 이전에 실행하던 위치를 기억하기 위해 사용된다. 

**Native Method Stacks**

C나 C++로 작성된 Native Method를 실행할 때 사용되는 스택 영역이다. 

---

![](https://velog.velcdn.com/images/hariaus/post/4c337626-7d66-40b8-b395-1111677fd8f4/image.png)


```java
public class UserTest {

    public static void main(String[] args) {
        User u1 = new User("Alice");
        u1.login();

        User u2 = new User("Bob");
        u2.login();

        System.out.println(u1.getName());
        System.out.println(u2.getName());
    }
}
```

![](https://velog.velcdn.com/images/hariaus/post/05d7c0a9-0d09-4ce2-a79d-90846d302c55/image.png)


## 코드 실행 시 JVM 메모리 구조 동작 과정


### 1. Metaspace (클래스 정보 로딩)

프로그램이 실행되면 JVM은 가장 먼저 객체의 클래스 파일을 읽어 Metaspace에 저장한다. 필드 구성, 메서드 정보, 상수 풀이 저장된다. 

이는 객체를 생성하기 위한 설계도이며, 프로그램 종료 또는 클래스 언로드 시까지 유지된다. 

### 2. Stack (참조 변수 생성)

`main` 메서드가 실행되면서 Stack 영역에 **메서드 프레임**이 생성된다. Stack에는 객체 자체가 아닌 Heap 객체의 주소값이 저장된다. 

### 3. Heap (실제 객체 데이터)

`new` 예약어를 통해 객체를 생성할 때 마다 Heap 영역에 새로운 객체가 생성된다. 

# 변수



### 인스턴스 멤버 변수

```java
public class Person {
		String name;
		int age = 10;
}
```

- 객체가 생성될 때 Heap에 객체별로 생성된다.
- 타입별 default 초기화 후 명시적 초기화가 이루어진다.
- 객체가 GC 대상이 되면(Stack이나 `static` 변수 등에서 더 이상 참조되지 않는 객체) 함께 소멸한다.
- 프로그래머가 명시적으로 소멸시킬 수 없다.

### 클래스 멤버 변수 (static)

```java
public class Person {
		static String name; // 클래스 멤버 변수
		int age = 10;
}
```

- 클래스 로딩 시 생성된다.
- 변수의 실제 값은 Heap에 저장된다.
- 객체 생성과 무관하며 모든 객체가 공유한다.
- 클래스 언로드 시 함께 제거된다.

### 지역 변수 & 파라미터 변수

```java
void call(String to) {              // 파라미터 변수 
		String beep = "띠";             // 로컬 변수
		
		for (int i = 0; i < 3; i++) {   // 로컬 변수
				System.out.println(beep);
		}
}
```

- 선언된 시점에 Stack의 메서드 프레임 내부에 생성된다.
- 사용 전 반드시 명시적 초기화가 필요하다.
- 선언된 블록을 벗어나면 소멸된다.

# 함수



### 가변인자

```java
List.of(Object ... args)
printf(String.format, Object ... args)
```

메소드 선언 시 동일 타입의 인자가 몇 개 들어올지 알 수 없을 때 사용한다. 호출 시 전달된 인자 개수에 따라 자동으로 배열을 생성하여 처리한다. 

### 메서드 호출 스택

---

![](https://velog.velcdn.com/images/hariaus/post/fadc98a5-a3cc-458d-85b4-0f1b8e032fe1/image.png)



메서드가 호출될 때마다 Stack에 프레임이 쌓이고, 메서드 종료 시 프레임은 제거되며 메모리를 반납한다. 

### Call By Value vs Call by Reference

자바의 경우 Call By Value 방식이다. 값 자체를 복사하여 전달하는 방식이다. 참조형 변수의 경우 주소 값이 복사되어 전달된다. 

### 메서드 오버로딩(overloading)

<aside>

동일한 기능을 수행하는 메서드를 여러 형태로 정의하는 것이다. 

</aside>

메서드 이름은 같지만 매개변수의 개수, 순서, 타입이 다른 경우 메서드 오버로딩이라고 한다.