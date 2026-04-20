![](https://velog.velcdn.com/images/hariaus/post/295046e1-95d2-4417-bdbb-8ea3b1977095/image.png)


# Servlet이란?


Servlet은 자바 기반의 웹 애플리케이션 프로그래밍 기술로, 클라이언트의 요청을 처리하고 그 결과를 반환하는 Java Web Component이다. 

WAS(Web Application Server)안에 서블릿 컨테이너(Servlet Container)가 들어있다. 즉, 자바 언어로 만들어져 웹에서 들어오는 요청을 받아 처리하며, 개발자가 통신 인프라(소켓 통신, HTTP 파싱 등)에 신경 쓰지 않고 비즈니스 로직 구현에 집중할 수 있도록 돕는다.  

![](https://velog.velcdn.com/images/hariaus/post/2092983c-70c5-4ecf-8893-f1de77559a78/image.png)


## Servlet의 특징


### 객체 지향과 독립성

Java의 OOP 기반으로 작성되어 유지보수성 및 재사용성이 우수하며 플랫폼에 독립적이다. 

### 싱글통 & 멀티스레딩

서블릿 객체는 메모리에 단 하나만 생성되며, 요청이 들어올 때마다 새로운 스레드가 생성되어 해당 객체를 공유한다. 이를 통해 다수의 동시 요청을 효율적으로 처리할 수 있다. 

### 확장성

필터(Filter)를 통한 공통 관심사(보안, 로깅 등)의 전/후 처리, 리스너(Listener)를 이용한 이벤트 기반 처리가 가능하며 Spring 같은 프레임워크와 통합이 용이하다.

### 비즈니스 로직과 표현 로직의 중복

서블릿 내부에 자바 코드와 HTML 생성 코드가 섞여 있어, 화면 UI가 복잡해질수록 가독성과 유지보수성이 크게 떨어진다는 단점이 있다. 이를 해결하기 위해 JSP, 나아가 Spring MVC와 같은 기술이 등장했다. 

## Servlet 코드 작성



```java
// 1. URL 매핑 (어떤 주소로 들어올 때 이 서블릿이 일할지 결정)
@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    // 2. HTTP GET 요청(브라우저 주소창 입력)이 들어오면 이 메서드가 실행됩니다.
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // 3. 응답(Response) 세팅: "내가 보낼 데이터는 HTML이고, 한글(UTF-8)을 쓸 거야!"
        response.setContentType("text/html;charset=UTF-8");

        // 4. 브라우저에 글씨를 쓰기 위한 펜(PrintWriter)을 가져옵니다.
        PrintWriter out = response.getWriter();

        // 5. HTML 코드를 작성해서 브라우저로 쏩니다. (요리 완성 및 서빙)
        out.println("<html>");
        out.println("<head><title>나의 첫 서블릿</title></head>");
        out.println("<body>");
        out.println("<h1>안녕하세요! 서블릿(Servlet)이 직접 그린 페이지입니다.</h1>");
        out.println("</body>");
        out.println("</html>");
    }
}
```

위와 같이 `HttpServletRequest`를 통해 클라이언트의 파라미터, 바디, 헤더 정보를 읽을 수 있고 `HttpServletResponse`를 통해 응답을 전송한다. 

요청 URL과 서블릿을 매핑하는 방법에는 전통적인 방식인 web.xml을 이용하는 방법과, Servlet 3.0 이후 도입된 `@WebServlet` 애너테이션을 활용하는 방법이 있다. 

## Servlet Life Cycle (생명주기) 관리


![](https://velog.velcdn.com/images/hariaus/post/821ade96-9889-4056-a14a-172fd003c068/image.png)


개발자는 서블릿 클래스를 작성하지만, 객체를 직접 `new` 키워드로 생성하거나 메서드를 직접 호출하지 않는다. 이 역할을 서블릿 컨테이너(Tomcat 등)가 전담한다. 컨테이너는 라이프사이클 단계에 맞춰 특정 메서드(Hook)를 자동으로 호출하며, 개발자는 이 메서드들을 오버라이딩하여 자원 관리와 성능 최적화를 구현한다. 

### init()

서블릿 객체가 생성된 후 가장 먼저, 그리고 단 한 번만 호출된다. 서블릿이 요청을 처리하기 전에 필요한 자원(DB 연결, 설정 파일 로드 등)을 초기화하는데 사용된다. 이 메서드가 완전히 종료되기 전에는 어떠한 클라이언트 요청도 처리할 수 없다. 

### service()

클라이언트의 요청이 올 때마다 실행되는 핵심 메서드이다. 요청의 HTTP 메서드(GET, POST, PUT, DELETE)를 분석하여 그에 맞는 `doGet()`, `doPost()` 등의 메서드로 요청을 분기한다. 

### destroy()

컨테이너가 종료되거나, 서블릿 객체가 가비지 컬렉션(GC)의 대상이 되어 메모리에서 해제될 때 호출된다. `init()` 에서 할당했던 자원(DB 커넥션 반납 등)을 안전하게 정리하는 역할을 한다. 진행 중인 요청(스레드)이 모두 처리된 후 안전하게 동작한다. 

## HttpServletRequest, HttpServletResponse



두 인터페이스는 HTTP 프로토콜의 요청(Request)과 응답(Response) 메시지를 개발자가 다루기 쉽게 Java 객체로 추상화한 것이다. 

이 객체들은 클라이언트의 요청이 들어올 때 컨테이너에 의해 새롭게 생성되고, 응답이 완료되면 즉시 소멸된다. 만약 요청/응답 객체가 메모리에 계속 남아있다면 수많은 요청을 견디지 못하고 서버는 결국 OOM(Out of Memory) 에러를 발생시킬 것이다. 

HTTP의 무상태성(Stateless) 덕분에 서버는 응답을 마친 후 클라이언트의 상태 정보를 유지할 필요가 없어 관련 데이터를 즉시 폐기할 수 있고, 이는 곧 웹 서버가 수많은 동시 요청을 버텨내는 뛰어난 확장성을 가지는 핵심 이유가 된다. 

## Front Controller Pattern



기존 방식은 사용자의 요청(URL)과 이를 처리하는 Servlet이 1:1로 매칭되는 구조이다. 이로 인해 새로운 요청이 추가될 때마다 매번 새로운 Servlet 클래스를 생성해야 했고, 여러 Servlet에서 공통으로 처리해야 하는 로직(보안, 인가, 로깅, 인코딩)이 중복될 수 있다. 이는 개발 생산성 저하와 유지보수의 어려움으로 이어진다.

## Front Controller



Front Controller는 전면에서 모든 요청을 받아들이는 Servlet이다. 

Front Controller 패턴은 클라이언트의 모든 요청을 가장 앞단에서 받아들이는 단일 진입점 역할을 하는 서블릿을 둔다. (Spring 프레임워크는 DispatcherServlet을 둔다.)

![](https://velog.velcdn.com/images/hariaus/post/f0c25df9-6ef6-4f56-a0df-1b9c97a09813/image.png)


### Front Controller Pattern 의 장점

- 단일 진입점 : 모든 요청을 Front Controller 에서 접수하므로 요청 처리의 일관성을 보장한다.
- 공통 처리 : 모든 작업이 Front Controller를 거쳐간다. 따라서 권한 체크나 인코딩 같은 필터 작업을 한 곳에서 일괄 처리할 수 있다.
- 유연한 확장성 : 새로운 요청 처리를 추가할 때 기존의 구조를 크게 변경하지 않고도 쉽게 확장 가능하다.
- 코드 간결성 : 여러 개의 Servlet을 만드는 번거로움이 줄고 코드의 가독성도 향상시킨다.

### ControllerHelper

```java
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public interface ControllerHelper {

    // 공통 전처리 메서드: 로깅 및 action 파라미터 반환
    default String preProcessing(HttpServletRequest req, HttpServletResponse resp) {
        // 1. 요청 정보 로깅
        System.out.printf("##요청 경로: %s, 요청 방식: %s%n", req.getRequestURI(), req.getMethod());
        req.getParameterMap().forEach((k, v) -> 
            System.out.printf("name: %s, value: %s%n", k, Arrays.toString(v))
        );
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        // 2. action 파라미터 확인 및 추출
        String action = req.getParameter("action");
        if (action == null || action.isBlank()) {
            action = "index"; // 파라미터가 없으면 기본값 "index" 반환
        }
        
        return action;
    }

    // 공통 응답 메서드: 간단한 HTML을 생성하여 응답
    default void responseHtml(String title, String content, HttpServletResponse response) throws IOException {
        String html = "<html><body><h1>%s</h1>%s</body></html>".formatted(title, content);
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(html);
    }
}
```

`ControllerHelper`는 요청이 들어오면 그 요청 URL에 대해 전처리를 진행한다. 사용자는 요청을 보낼 때, `action` 파라미터를 통해 특정 작업을 요청할 수 있다. 

여러 컨트롤러에서 이 공통 로직을 재사용할 수 있도록 인터페이스의 `defalut` 메서드로 구현한다. 

### MainController

```java
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/main")
public class MainController extends HttpServlet implements ControllerHelper {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 인터페이스의 default 메서드를 내장 메서드처럼 호출
        String action = preProcessing(req, resp);
        
        // action에 따른 분기 처리 (GET 요청)
        switch (action) {
            case "gugu" -> gugu(req, resp);
            case "index" -> {
                try {
                    responseHtml("메인 화면", "인덱스 페이지입니다.", resp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            default -> {
                try {
                    responseHtml("오류", "지원하지 않는 GET 요청입니다.", resp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 한글 깨짐 방지 등 POST 전용 처리가 필요하다면 preProcessing 전에 작성 가능
        String action = preProcessing(req, resp);
        
        // action에 따른 분기 처리 (POST 요청)
        switch (action) {
            case "login" -> login(req, resp);
            default -> {
                try {
                    responseHtml("오류", "지원하지 않는 POST 요청입니다.", resp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 분기되어 실행될 실제 비즈니스 로직 메서드들
    protected void gugu(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 구구단 처리 로직 작성
        // 예: 파라미터로 단을 받아서 처리 후 결과 응답
        responseHtml("구구단 처리", "구구단 결과가 나오는 화면입니다.", response);
    }

    protected void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 실제 login 처리 로직 작성
        // 예: DB 확인 후 세션 생성
        responseHtml("로그인 처리", "로그인 검증이 완료된 화면입니다.", response);
    }
}
```

`/main`으로 요청이 들어오면 전처리를 통해 어떤 요청인지 구분하고 `action`에 따라 각 메서드를 호출하여 비즈니스 로직이 실행되도록 한다. 

## Servlet 공유 자원 문제



![](https://velog.velcdn.com/images/hariaus/post/ee0f29bc-411b-4f56-b890-81721131d39a/image.png)


데이터베이스 Connection이나 파일 시스템 같은 자원들은 생성 비용이 크기 때문에, 웹 애플리케이션에서는 이를 공유 자원으로 만들어 효율성을 높인다. 하지만 여러 스레드가 동시에 공유 자원에 접근할 때 동시성 문제(Race Condition)가 발생할 수 있으므로 안전한 제어가 필요하다. 

## 공유 자원 동기화 기법



### 뮤텍스 (Mutex)

공유 구역에 들어갈 수 있는 통로가 단 하나이다. 예를 들면 자리가 하나뿐인 화장실과 열쇠 하나이다. `synchronized` 블록으로 구현 가능하다. 

### 세마포어 (Semaphore)

공유 구역에 들어갈 수 있는 통로가 여러 개이다. 예를 들면 자리가 여러 개인 유료 주차장과 주차권이다. 데이터베이스 Connection pool이 이에 해당한다.

## Servlet의 멀티스레드



웹 애플리케이션 서버는 효율성을 위해 요청이 올 때마다 서블릿 객체를 새로 생성하지 않고, 하나의 서블릿 인스턴스를 생성하여 여러 스레드가 이를 공유한다. 

![](https://velog.velcdn.com/images/hariaus/post/f373c475-3088-4bb3-82ef-64982121d45c/image.png)


서블릿 인스턴스가 여러 스레드에 의해 공유되므로, 서블릿 클래스 내부에 상태를 저장하는 인스턴스 변수를 선언하면 Thread Safe 하지 않으므로 A 스레드가 변경한 값을 B 스레드가 덮어쓸 수 있기 있다. 

동시성 문제를 피하겠다고 `synchronized`를 걸어버리면, 한 번에 하나의 요청만 처리하게 되어 웹 서버의 성능이 매우 떨어진다. 

따라서 서블릿 개발 시에는 절대 멤버 변수를 통해 클라이언트의 상태 정보를 관리해서는 안된다.