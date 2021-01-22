# Error handling for REST with Spring
> https://www.baeldung.com/exception-handling-for-rest-with-spring

## 목표
스프링 시큐리티에서 접근 거부를 관리하는 방법에 대해 연구

## MVC - Custom Error page
먼저 솔루션의 MVC 스타일을 살펴보고 액세스 거부에 대한 오류 페이지를 사용자 지정하는 방법을 살펴 본다.

XML
```
<http>
    <intercept-url pattern="/admin/*" access="hasAnyRole('ROLE_ADMIN')"/>   
    ... 
    <access-denied-handler error-page="/my-error-page" />
</http>
```

Java configuration
```
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/admin/*").hasAnyRole("ROLE_ADMIN")
        ...
        .and()
        .exceptionHandling().accessDeniedPage("/my-error-page");
}
```
/my-error-page로 리다이렉팅됨

### Custom AccessDeniedHandler
커스텀 AccessDeniedHandler
```
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle
      (HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) 
      throws IOException, ServletException {
        response.sendRedirect("/my-error-page");
    }
}
```

XML configure 를 사용하여 설정
```
<http>
    <intercept-url pattern="/admin/*" access="hasAnyRole('ROLE_ADMIN')"/> 
    ...
    <access-denied-handler ref="customAccessDeniedHandler" />
</http>
```
Java configuration 사용
```
@Autowired
private CustomAccessDeniedHandler accessDeniedHandler;

@Override
protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/admin/*").hasAnyRole("ROLE_ADMIN")
        //...
        .and()
        .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
}
```
### REST and Method-Level Security
메서드 수준의 보안인 @PreAuthorize, @PostAuthorize 및 @Secure Access Denied를 처리하는 방법을 살펴 본다.
```
@ControllerAdvice
public class RestResponseEntityExceptionHandler 
  extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDeniedException(
      Exception ex, WebRequest request) {
        return new ResponseEntity<Object>(
          "Access denied message here", new HttpHeaders(), HttpStatus.FORBIDDEN);
    }
    
    //...
}
```
### Spring Boot Support
Spring Boot는 합리적인 방식으로 오류를 처리하기위한 ErrorController 구현을 제공한다.  
간단히 말해서 브라우저 용 폴백 오류 페이지 (일명 Whitelabel 오류 페이지)와 RESTful 비 HTML 요청에 대한 JSON 응답을 제공한다.
```
{
    "timestamp": "2019-01-17T16:12:45.977+0000",
    "status": 500,
    "error": "Internal Server Error",
    "message": "Error processing the request!",
    "path": "/my-endpoint-with-exceptions"
}
```
Spring boot는 속성을 사용하여 이러한 기능을 구성할 수 있다.
* server.error.whitelabel.enabled: Whitelabel 오류 페이지를 비활성화하고 서블릿 컨테이너에 의존하여 HTML 오류 메시지를 제공하는 데 사용할 수 있습니다.
* server.error.include-stacktrace: 항상 값을 사용합니다. HTML 및 JSON 기본 응답 모두에 스택 추적을 포함합니다.  

이러한 속성 외에도 Whitelabel 페이지를 재정 의하여 / error에 대한 자체 뷰-리졸버 매핑을 제공 할 수 있습니다.
  
컨텍스트에 ErrorAttributes 빈을 포함하여 응답에 표시 할 속성을 맞춤 설정할 수도 있습니다. Spring Boot에서 제공하는 DefaultErrorAttributes 클래스를 확장하여 작업을 더 쉽게 할 수 있습니다.
```
@Component
public class MyCustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(
      WebRequest webRequest, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = 
          super.getErrorAttributes(webRequest, includeStackTrace);
        errorAttributes.put("locale", webRequest.getLocale()
            .toString());
        errorAttributes.remove("error");

        //...

        return errorAttributes;
    }
}
```
더 나아가서 애플리케이션이 특정 콘텐츠 유형에 대한 오류를 처리하는 방법을 정의 (또는 재정의)하려면 ErrorController Bean을 등록 할 수 있습니다.  
다시 말하지만 Spring Boot에서 제공하는 기본 BasicErrorController를 사용하여 도움을 줄 수 있습니다.  

예를 들어 애플리케이션이 XML 엔드 포인트에서 트리거 된 오류를 처리하는 방법을 사용자 정의하고 싶다고 가정 해보십시오.  
우리는 @RequestMapping을 사용하여 공개 메서드를 정의하고 application/xml 미디어 유형을 생성한다고 명시하기만 하면 된다.
```
@Component
public class MyErrorController extends BasicErrorController {

    public MyErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes, new ErrorProperties());
    }

    @RequestMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Map<String, Object>> xmlError(HttpServletRequest request) {
        
    // ...

    }
}
```
