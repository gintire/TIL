# Junit5

## 기본적인 사용법
```
@Test
void test() {
    Assertions.assertEquals(1,1);
}
```

* Disable 처리
```
@Test
@Disabled
```

* Display를 위한 test name 정해주기
```
@Test
@DisplayName("Blah Blah")
```

* Multiple Assertions
```
assertAll()
```
* Assumptions
```
Assumptions.assumeTrue(조건);
```
* Data Driven Test
```
@parameterizedTest
@ValueSource(ints = {3, 4, 5, 6})
void test(int expectedNumbers) {}
```
* check Exceptions
```
assertThrows(IllegalArgumentException.calss, () -> {});
```

* Grouping tests with @Nested
```
@Nested
```

### JUni5의 어노테이션
> @Test  
> @ParameterizedTest  
> @RepeatedTest  
> @TestFactory  
> @TestTemplate  
> @TestMethodOrder  
> @TestInstance  
> @DisplayName  
> @DisplayNameGeneration  
> @BeforeEach  
> @AfterEach  
> @BeforeAll  
> @AfterAll  
> @Nested  
> @Tag  
> @Disabled  
> @Timeout  
> @ExtendWith  
> @RegisterExtension  
> @TempDir


### Assertions
> https://junit.org/junit5/docs/current/user-guide/#writing-tests-assertions  

JUnit Jupiter comes with many of the assertion methods that JUnit 4 has and adds a few that lend themselves well to being used with Java 8 lambdas. All JUnit Jupiter assertions are static methods in the org.junit.jupiter.api.Assertions class.

Assertions의 JUnit4와의 가장 큰 차이점은 Java8의 람다를 사용할 수 있다라는 것입니다. 따라서 테스트 코드를 Functional 하게 작성할 수 있다라는 것이 가장 큰 차이점입니다.

### Assumptions
> https://junit.org/junit5/docs/current/user-guide/#writing-tests-assertions

JUnit Jupiter comes with a subset of the assumption methods that JUnit 4 provides and adds a few that lend themselves well to being used with Java 8 lambda expressions and method references. All JUnit Jupiter assumptions are static methods in the org.junit.jupiter.api.Assumptions class.  

Assumptions 구문은 테스트 메서드가 특정한 조건에 대해서 실행하고자 할 때 사용하는 구문이다.

### Parallel Execution
> https://junit.org/junit5/docs/current/user-guide/#writing-tests-parallel-execution

기본적으로 Junit Jupiter test는 싱글 스레드에서 순차적으로 실행된다. 병렬 실행으로 실행 속도를 증가시킬수 있다.

`junit.jupiter.execution.parallel.enabled` 를 `true`로 설정 하면 된다. - 예를 들어 `junit-platform.properties`에 설정한다.

병렬 설정 모드에는 다음 두 설정을 할 수 있다.  
`junit.jupiter.execution.parallel.mode.default`  
* `SAME_THREAD`  
Force execution in the same thread used by the parent. For example, when used on a test method, the test method will be executed in the same thread as any @BeforeAll or @AfterAll methods of the containing test class.
* `CONCURRENT`  
Execute concurrently unless a resource lock forces execution in the same thread.

`@Execution(CONCURRENT)` 를 각 클래스에 설정하여 개별 클래스로 설정도 가능하다.

[![병렬실행과 싱글 쓰레드 관계!](https://junit.org/junit5/docs/current/user-guide/images/writing-tests_execution_mode.svg)](https://junit.org/junit5/docs/current/user-guide/images/writing-tests_execution_mode.svg)

#### 설정
`junit.jupiter.execution.parallel.config.strategy`로 설정이 가능하다.
* `dynamic`  
Computes the desired parallelism based on the number of available processors/cores multiplied by the `junit.jupiter.execution.parallel.config.dynamic.factor` configuration parameter (defaults to 1).
* `fixed`  
Uses the mandatory `junit.jupiter.execution.parallel.config.fixed.parallelism` configuration parameter as the desired parallelism.
* `custom`  
Allows you to specify a custom `ParallelExecutionConfigurationStrategy` implementation via the mandatory `junit.jupiter.execution.parallel.config.custom.class` configuration parameter to determine the desired configuration.

> ParallelExecutionConfigurationStrategy 클래스를 확인하면, Custom 설정에 대해 이해할 수 있다.


