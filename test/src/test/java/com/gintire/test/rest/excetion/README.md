# Error handling for REST with Spring
> https://www.baeldung.com/exception-handling-for-rest-with-spring
## 목표 
REST API에서 스프링을 이용하여 Exception을 다루는 것에 대해서 알아봄

스프링 3.2 이전에는 아래 두가지의 방식으로 예외처리를 하였음
* HandlerExceptionResolver
* @ExceptionHandler annotation
두 가지 방법 모두 명백한 단점이 존재한다.

스프링 3.2 버전부터는 *@ControllerAdvice* annotation으로 위 두가지 방식의 한계를 해결하고, 전체 어플리케이션에서 통합 된 예외 처리를 가능하게함

스프링 5부터는 *ResponseStatusException* class를 제공한다. - REST API에서 기본적인 오류를 다루기 위한 빠른 방법

모든 것은 공통적인 점이 있다. 
*모두 우려에 대한 분리 (separation of concerns)를 잘 처리한다.*
=> 앱은 어떤 종류의 실패를 나타내기 위해 일반적으로 예외를 throw 할 수 있으며, 그런 다음 별도로 처리된다.

마지막으로 스프링 부트가 테이블에 제공하는 내용과 필요에 맞게 구성하는 방법을 알아본다.

### Solution 1 : 컨트롤러 레벨 @ExceptionHandler
첫 번째 해결 방법으로 @Controller 레벨에서 가능하다.
@ExceptionHandler를 메서드에 선언한다.

```
// 예외 처리
@ExceptionHandler({UserException.class})
    public void handleException() {
        //
        logger.info("Exception Handled");
}
```
@ExceptionHandler는 특정 컨트롤러에만 동작하는 anootated method 이다.  
모든 어플리케이션에 걸쳐서 동작하는 것이 아니다.  
물론, 모든 컨트롤러에 설정해줄 수 있지만, 일반적인 예외 처리 메카니즘에는 어울리지 않다.

모든 Controller에 Base Controller를 extend 할 수 있다.  
하지만, 여러 문제로 이러한 방법은 문제가 생길 수 있다.  
예를 들면, 컨트롤러는 다른 jar에 있거나 직접 수정할 수 없는 다른 기본 클래스에서 이미 확장되거나 직접 수정할 수 없을 수 있다.

### Solution2 : HandlerExceptionResolver
두 번쨰 방법은 HandlerExceptionResolver를 선언하는 것이다. 이렇게하면 응용 프로그램에서 발생한 모든 예외가 해결됩니다.  
또한 REST API에서 균일 한 예외 처리 메커니즘을 구현할 수 있습니다.  

Custom resolver를 연구하기전 기존 구현을 살펴본다.

#### ExceptionHandlerExceptionResolver
Spring 3.1에서 소개되었으며, DispatcherServlet에서 자동으로 사용 가능하다.
이것은 실제로 앞에서 제시 한 @ExceptionHandler 메커니즘이 작동하는 방식의 핵심 구성 요소입니다. 

#### DefaultHandlerExceptionResolver
Spring 3.0에서 소개됐으며, DispatcherServlet에서 기본적으로 사용가능하다.

표준 Spring 예외를 해당 HTTP 상태 코드, 즉 클라이언트 오류 4xx 또는 서버 오류 5xx 상태 코드로 반환하는데 사용한다.

다음은 처리하는 Spring 예외의 전체 목록과 상태코드에 매핑하는 방법이다.

응답의 상태 코드를 적절하게 설정하지만 한 가지 제한은 응답 본문에 아무것도 설정하지 않는다는 것입니다.
REST API의 경우 (상태 코드는 실제로 클라이언트에게 제공하기에 충분한 정보가 아닙니다.)  
응답에도 본문이 있어야 응용 프로그램이 실패에 대한 추가 정보를 제공 할 수 있습니다.

이는 ModelAndView를 통해 뷰 해상도를 구성하고 오류 콘텐츠를 렌더링하여 해결할 수 있지만 솔루션은 분명히 최적이 아닙니다.  
이것이 Spring 3.2가 이후 섹션에서 논의 할 더 나은 옵션을 도입 한 이유입니다.

#### ResponseStatusExceptionResolver
이 Resolver는 스프링 3.0에서 소개되었으며, DispatcherServlet에서 기본적으로 동작한다.  

주요 책임은 사용자 지정 예외에서 사용할 수 있는 @ResponseStatus 주석을 사용하고 이러한 예외를 HTTP 상태코드에 매핑하는 것이다.
```
@ResponseStatus(value = HttpStatus.NOT_FOUND )
public class MyResoueceNotFoundException extends RuntimeException {
    // ...
}
```
DefaultHandlerResolver와 동일하게 이 해석기는 응답의 본문을 처리하는 방식이 제한된다.  
응답에 상태코드를 매핑하지만 본문은 여전히 null이다.

#### SimpleMappingExceptionResolver 와 AnnotationMethodHandlerExceptionResolver
SimpleMappingExceptionResolver는 꽤 오랫동안 사용되어 왔습니다. 이전 Spring MVC 모델에서 나 왔으며 REST 서비스와는 관련이 없습니다.  
기본적으로 예외 클래스 이름을 매핑하여 이름을 보는 데 사용합니다.


#### Custom HandlerExceptionResolver
DefaultHandlerExceptionResolver와 ResponseStatusExceptionHandler 스프링 RESTful 서비스에서 에러를 다루는 좋은 메카니즘으로 오랜기간 사용되어 왔다.  
단점은, 이전에 이야기 했는 것과 같이, 응답 본문을 제어할 수 없다.

이상적으로는 클라이언트가 요청한 형식 (Accept 헤더를 통해)에 따라 JSON 또는 XML을 출력 할 수 있기를 원합니다.
이것만으로도 새로운 사용자 지정 예외 해결 프로그램을 만들 수 있습니다.

> RestResponseStatusExceptionResolver 참고

여기서 알아야할 점은 request 에 접근가능하여, 클라이언트에서 보내진 Accept header의 값을 알 수 있다.

예를 들어, 클라이언트가 application/json을 요청하면, 오류 조건이 발생하면, application/json으로 인코딩 된 응답 본문을 반환해야 한다.  

그리고 중요한 점은 반환값으로 ModelAndView를 가지는 것이다.  
이 방식은 REST 서비스의 에러 핸들링에 일관되고 쉽게 설정 가능한 메카니즘이다.

하지만, 이것은 제약사항을 가진다. low-level의 HttpServletResponse를 사용하며, ModelAndView를 사용하는 이전 MVC 모델에 적합하다. 그래도 여전히 개선의 여지는 있다.

### @ControllerAdvice
Spring 3.2부터 글로벌하게 사용가능한 @ExceptionHandler와 @ControllerAdvice 어노테이션을 지원합니다.  
이는 이전의 MVC 모델에서 벗어나 @ExceptionHandler 유형의 안전성 및 유연성과 함께 ResponseEntity를 사용하는 메커니즘을 가능하게 한다.

> RestResponseEntityExceptionHandler.java

@ControllerAdvice 주석을 사용하면 이전에 분산 된 여러 @ExceptionHandler를 단일 전역 오류 처리 구성 요소로 통합 할 수 있습니다.  
메카니즘은 간단하지만, 매우 유연하다
* 응답 본문과 상태코드를 완전히 제어할 수 있다.
* 함께 처리할 수 있도록 동일한 메서드에 대한 여러 예외 매핑을 제공
* 최신 RESTful ResposeEntity 응답을 잘 활용한다.

여기서 기억해야할 점은, @ExceptionHandler로 선언 된 예외를 메서드의 인수로 사용되는 예외와 일치시키는 것이다.  

이것들이 일치하지 않으면, 컴파일러는 이에 대해 문제를 일으키지않고, 스프링 또한 문제로 여기지 않는다.

하지만 실제 런타임에 예외가 발생하면 예외 처리 메카니즘에서 실패한다.
```
java.lang.IllegalStateException: No suitable resolver for argument [0] [type=...]
HandlerMethod details: ...
```

#### Solution 4: ResponseStatusException ( Spring 5 이상 )
Spring 5에서 ResponseStatusException 클래스를 소개하였다.

HttpStatus와 선택적으로 reason과 cause를 제공하는 인스턴스를 만들 수 있다.  

```
@GetMapping(value = "/{id}")
    public String findById(@PathVariable("id") Long id, HttpServletResponse httpServletResponse) {
        try {
            String username = userService.getUser("id").getName();
            
            return username;
        } catch (MyResourceNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found", exception);
        }
    }
``` 
ResponseStatusException을 사용하면서의 장점
* 프로토타이핑에 훌륭함 : 기본적인 솔루션을 빠르게 구현할 수  있다.
* 하나의 타입, 여러개의 상태 코드 : 하나의 예외 타입으로 여러개의 다른 응답을 생성할 수 있다.  
* * @ExceptionHandler에 비해 결합도를 낮춘다.
* 수많은 Custom exception class를 만들 필요가 없다.
* 예외는 프로그래밍 방식으로 생성 될 수 있으므로 예외 처리를 더 많이 제어 할 수 있습니다.

Tradeoff 
* 통합 된 예외 처리 방법 부재 : 전역 접근 방식을 제공하는 @ControllerAdvice와는 반대로 일부 애플리케이션 전체의 규칙을 적용하는 것이 더 어렵다.
* 코드 중복 : 여러 컨트롤러에 걸쳐 코드를 복제해야한다.

그러므로 위 두가지 방법을 잘 합쳐서 사용해야한다.  
예: 전역적으로 사용할 때는 @ControllerAdvice를 구현하고, 지역적으로 사용할 때는 ResponseStatusExceptions를 구현한다.

### MVC - Custom Error Page
