# Spring 예외처리


프로젝트를 처음 프론트와 같이 진행했을 때, 예외 처리를 해두지 않아서 무조건 **500 Internal Server Error**만 응답했다. 실제로는 서버 문제보다는 클라이언트에서 잘못된 요청을 보내는 경우가 많았는데, 적절한 예외 처리가 없으니 매번 어떤 예외인지 일일이 설명해야 했다.

이 과정을 겪으면서, 예외를 명확하게 분류해서 던져주면 서버 입장에서는 불필요한 부하를 줄일 수 있고, 클라이언트도 에러를 상황에 맞게 처리할 수 있다는 걸 깨달았다. 그래서 예외 처리 방식에 대해 공부하게 되었다.

## @ExceptionHandler


`@ExceptionHandler` 를 사용하면 특정 예외를 지정해서 핸들링할 수 있다. 

```java
@RestController
public class UserController {
		// ...
		
		@ExceptionHandler(NullPointerException.class) // 예외 지정
    public ResponseEntity<ExceptionDto> exception() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // 에러코드 지정
                .body(new ExceptionDto(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.getMessage()));
                // 반환 값 지정
    }
}
```

## @RestControllerAdvice


`@RestControllerAdvice`를 사용하면 컨트롤러 단에서 발생하는 예외를 한 곳에서 처리할 수 있다. 

```java
@RestControllerAdvice // Controller에서 발생하는 예외 핸들링 가능
public class ExceptionHandler {

		// 예외 지정
    @org.springframework.web.bind.annotation.ExceptionHandler(MyException.class)
    public ResponseEntity<ExceptionDto> MyExceptionHandler(MyException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(new ExceptionDto(e.getErrorCode(), e.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ExceptionDto> MissingParamHandler(MissingServletRequestParameterException e) {
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(new ExceptionDto(errorCode, "입력 인자 값이 없습니다."));
    }
```

# JWT Exception Handler



내 프로젝트에서는 세션 대신 JWT를 사용했다. 인증·인가 과정에서 발생하는 예외는 컨트롤러가 아니라 **필터(Filter)** 단에서 발생하기 때문에, `@RestControllerAdvice`만으로는 잡히지 않았다. 이럴 때는 `AuthenticationEntryPoint`와 `AccessDeniedHandler`를 사용하면 된다.

## SecurityConfig


```java
.exceptionHandling()
       .authenticationEntryPoint(customAuthenticationEntryPoint) 
       .accessDeniedHandler(new CustomAccessDeniedHandler(objectMapper()));
```

`Configuration`에 위와 같이 등록해주어야 사용 가능하다. 

## AuthenticationEntryPoint


인증이 실패했을때의 **401(Unauthorized)** 에러를 처리한다. 필터에서 들어온 에러에 따라 응답 값을 정하게 된다. 

```java
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String exception = (String) request.getAttribute("exception");
        response.setContentType("application/json;charset=UTF-8");

        ExceptionDto exceptionDto = new ExceptionDto();

        if (exception == null) {
            exceptionDto.setErrorCode(ErrorCode.NOT_FOUND);
            exceptionDto.setMessage(ErrorCode.NOT_FOUND.getMessage());
            
            // string 끼리의 비교가 아닌 enum 끼리의 비교가 좋다고 한다. 
        } else if (exception.equals(ErrorCode.UNKNOWN_ERROR.getMessage())) {
            exceptionDto.setErrorCode(ErrorCode.UNKNOWN_ERROR);
            exceptionDto.setMessage(ErrorCode.UNKNOWN_ERROR.getMessage());
        } else if (exception.equals(ErrorCode.WRONG_TOKEN.getMessage())) {
            exceptionDto.setErrorCode(ErrorCode.WRONG_TOKEN);
            exceptionDto.setMessage(ErrorCode.WRONG_TOKEN.getMessage());
        } else if (exception.equals(ErrorCode.EXPIRED_TOKEN.getMessage())) {
            exceptionDto.setErrorCode(ErrorCode.EXPIRED_TOKEN);
            exceptionDto.setMessage(ErrorCode.EXPIRED_TOKEN.getMessage());
        }
        setResponse(response, exceptionDto);
    }

    private void setResponse(HttpServletResponse response, ExceptionDto exceptionDto) throws IOException {
        String result = objectMapper.writeValueAsString(exceptionDto);
        response.setStatus(exceptionDto.getErrorCode().getHttpStatus().value());
        response.getWriter().write(result);
    }
}
```

## AccessDeniedHandler



인가 실패 시 **403 Forbidden** 에러를 처리한다. 예를 들어 토큰은 정상적이지만 `USER` 권한만 가진 사용자가 `ADMIN` 리소스에 접근할 때 발생한다.

```java
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ExceptionDto exceptionDto = new ExceptionDto(ErrorCode.ACCESS_DENIED, ErrorCode.ACCESS_DENIED.getMessage());
        setResponse(response, exceptionDto);
    }

    private void setResponse(HttpServletResponse response, ExceptionDto exceptionDto) throws IOException {
        String result = objectMapper.writeValueAsString(exceptionDto);
        response.setStatus(403);
        response.getWriter().write(result);
    }
}
```

# JWT 예외처리 + @RestControllerAdvice

이 방식은 EntryPoint와 AccessDeniedHandler에서 직접 응답을 만들다 보니 코드가 길어지고, **SRP**나 **DIP** 관점에서도 아쉬움이 있었다. 그래서 `AuthenticationEntryPoint`에서 발생한 예외를 **HandlerExceptionResolver**로 위임하고, 최종 응답은 `@RestControllerAdvice`에서 처리하도록 분리했다. 이렇게 하면 예외 처리를 한 곳에서 관리할 수 있고 코드도 훨씬 깔끔해진다.

## AuthenticationEntryPoint


```java
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver resolver;

    public CustomAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (request.getAttribute("exception") == null)
            request.setAttribute("exception", new CustomException(ErrorCode.INVALID_TOKEN_ERROR));

        resolver.resolveException(request, response, null, (Exception)request.getAttribute("exception"));
    }
}
```

`handlerExceptionResolver`는 Spring MVC의 예외처리를 담당하는데, 인증 예외가 발생 후 처리를 `handlerExceptionResolver`에 위임한다. 처리기는 `@RestControllerAdvice`에 다시 위임하여 예외를 처리할 수 있도록 한다.

## JwtExceptionHandler



```java
@RestControllerAdvice
public class JwtExceptionHandler {

    @ExceptionHandler({SignatureException.class, MalformedJwtException.class})
    public ResponseEntity<ErrorResponse> JwtInvalidException() {
        ErrorCode errorCode = ErrorCode.INVALID_TOKEN_ERROR;
        return ResponseEntity.status(errorCode.getStatus()).body(makeErrorResponse(errorCode));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> JwtExpiredException() {
        ErrorCode errorCode = ErrorCode.EXPIRED_TOKEN_ERROR;
        return ResponseEntity.status(errorCode.getStatus()).body(makeErrorResponse(errorCode));
    }
```