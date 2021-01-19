package com.gintire.test.junit5;

import com.gintire.test.domain.Calculator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.function.ThrowingConsumer;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Parameter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

/**
 * Project: spring-boot-test
 * Package: com.gintire.springboottest.junit5
 * <p>
 * User: gintire
 * Date: 2021-01-06
 * Time: 오전 1:11
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */

public class AnotationsTest {
    /**
     * @Test
     */
    @Test
    @DisplayName("test anotation test")
    void testTest() {
        Assertions.assertEquals(1, 1);
    }

    /**
     * @param inputs
     * @ParameterizedTest
     */
    @ParameterizedTest
    @ValueSource(ints = {3, 4, 5, 8, 14})
    void parameterizedTest(int inputs) {
        int input = inputs;
        assertEquals(inputs, input);
    }

    /**
     * @param testInfo DisplayName : display name of the @RepeatedTest method
     *                 {currentRepetition} : the current repetition count
     *                 {totalRepetition} : the total number of repetitions
     * @RepeatedTest
     */
    @RepeatedTest(10)
    @DisplayName("Repeated Test")
    void reapeactedTestEmpty(TestInfo testInfo) {
        // ...
    }

    @RepeatedTest(value = 1, name = "{displayName} {currentRepetition}/{totalRepetitions}")
    @DisplayName("Repeated Test")
    void reapeactedTest(TestInfo testInfo) {
        assertEquals("Repeated Test 1/1", testInfo.getDisplayName());
    }

    /**
     * @TestFactory 동적으로 생성되는 테스트를 생성할 수 있다.
     * DynamicTest is a test case generated at runtime.
     * It is composed of a display name and an Executable.
     * Executable is a @FunctionalInterface which means that the implementations of dynamic tests can be provided as lambda expressions or method references.
     * @TestFactory method must return a single DynamicNode or a Stream, Collection, Iterable, Iterator, or array of DynamicNode instances.
     */
    private Calculator calculator = new Calculator();

    // This will result in a JUnitException!
    @TestFactory
    @Disabled("Not Allowed")
    @DisplayName("Dynamic tests with invalid return type")
    List<String> dynamicTestsWithInvalidReturnType() {
        return Arrays.asList("Hello");
    }

    @TestFactory
    Collection<DynamicTest> dynamicTestsFromCollection() {
        return Arrays.asList(
                dynamicTest("1st dynamic test", () -> assertTrue(isPalindrome("madam"))),
                dynamicTest("2nd dynamic test", () -> assertEquals(4, calculator.multiply(2, 2)))
        );
    }

    @TestFactory
    DynamicTest[] dynamicTestsFromArray() {
        return new DynamicTest[]{
                dynamicTest("7th dynamic test", () -> assertTrue(isPalindrome("madam"))),
                dynamicTest("8th dynamic test", () -> assertEquals(4, calculator.multiply(2, 2)))
        };
    }

    @TestFactory
    Stream<DynamicTest> dynamicTestsFromStream() {
        return Stream.of("racecar", "radar", "mom", "dad")
                .map(text -> dynamicTest(text, () -> assertTrue(isPalindrome(text))));
    }

    @TestFactory
    Stream<DynamicTest> dynamicTestsFromIntStream() {
        // Generates tests for the first 10 even integers.
        return IntStream.iterate(0, n -> n + 2).limit(10)
                .mapToObj(n -> dynamicTest("test" + n, () -> assertTrue(n % 2 == 0)));
    }

    @TestFactory
    Stream<DynamicTest> generateRandomNumberOfTestsFromIterator() {

        // Generates random positive integers between 0 and 100 until
        // a number evenly divisible by 7 is encountered.
        Iterator<Integer> inputGenerator = new Iterator<Integer>() {

            Random random = new Random();
            int current;

            @Override
            public boolean hasNext() {
                current = random.nextInt(100);
                return current % 7 != 0;
            }

            @Override
            public Integer next() {
                return current;
            }
        };

        // Generates display names like: input:5, input:37, input:85, etc.
        Function<Integer, String> displayNameGenerator = (input) -> "input:" + input;

        // Executes tests based on the current input value.
        ThrowingConsumer<Integer> testExecutor = (input) -> assertTrue(input % 7 != 0);

        // Returns a stream of dynamic tests.
        return DynamicTest.stream(inputGenerator, displayNameGenerator, testExecutor);
    }


    @TestFactory
    Stream<DynamicNode> dynamicTestsWithContainers() {
        return Stream.of("A", "B", "C")
                .map(input -> dynamicContainer("Container " + input, Stream.of(
                        dynamicTest("not null", () -> assertNotNull(input)),
                        dynamicContainer("properties", Stream.of(
                                dynamicTest("length > 0", () -> assertTrue(input.length() > 0)),
                                dynamicTest("not empty", () -> assertFalse(input.isEmpty()))
                        ))
                )));
    }

    @TestFactory
    DynamicNode dynamicNodeSingleTest() {
        return dynamicTest("'pop' is a palindrome", () -> assertTrue(isPalindrome("pop")));
    }

    @TestFactory
    DynamicNode dynamicNodeSingleContainer() {
        return dynamicContainer("palindromes",
                Stream.of("racecar", "radar", "mom", "dad")
                        .map(text -> dynamicTest(text, () -> assertTrue(isPalindrome(text)))
                        ));
    }

    private boolean isPalindrome(String string) {
        int p = 0;
        int q = string.length() - 1;
        while (p < q) {
            if (string.charAt(p) != string.charAt(q)) {
                return false;
            }
            p += 1;
            q -= 1;
        }
        return true;
    }

    /**
     * @TestTemplate
     */

    final List<String> fruits = Arrays.asList("apple", "banana", "lemon");

    @TestTemplate
    @ExtendWith(MyTestTemplateInvocationContextProvider.class)
    void testTemplate(String fruit) {
        assertTrue(fruits.contains(fruit));
    }
    /**
     * @TestInstance
     */

    /**
     * @DisplayName
     * Declares a custom display name for the test class or test method. Such annotations are not inherited.
     */

    @Test
    @DisplayName("Displayname")
    void testDisplayName() {}

    /**
     * @DisplayNameGeneration
     * Declares a custom display name generator for the test class. Such annotations are inherited.
     */
    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class A_year_is_not_supported {
        @Test
        void if_it_is_zero() {}

        @DisplayName("A negative value for year is not supported by the leap year computation.")
        @ParameterizedTest(name = "For example, year {0} is not supported.")
        @ValueSource(ints = { -1, -4 })
        void if_it_is_negative(int year) {
        }
    }

    /**
     * @BeforeEach
     * Denotes that the annotated method should be executed before each @Test, @RepeatedTest, @ParameterizedTest, or @TestFactory method in the current class; analogous to JUnit 4’s @Before.
     * Such methods are inherited unless they are overridden.
     *
     * @AfterEach
     * Denotes that the annotated method should be executed after each @Test, @RepeatedTest, @ParameterizedTest, or @TestFactory method in the current class; analogous to JUnit 4’s @After.
     * Such methods are inherited unless they are overridden.
     *
     * @BeforeAll
     * Denotes that the annotated method should be executed before all @Test, @RepeatedTest, @ParameterizedTest, and @TestFactory methods in the current class; analogous to JUnit 4’s @BeforeClass.
     * Such methods are inherited (unless they are hidden or overridden) and must be static (unless the "per-class" test instance lifecycle is used).
     *
     * @@AfterAll
     * Denotes that the annotated method should be executed after all @Test, @RepeatedTest, @ParameterizedTest, and @TestFactory methods in the current class; analogous to JUnit 4’s @AfterClass.
     * Such methods are inherited (unless they are hidden or overridden) and must be static (unless the "per-class" test instance lifecycle is used).
     */

    /**
     * @Nested
     * Denotes that the annotated class is a non-static nested test class. @BeforeAll and @AfterAll methods cannot be used directly in a @Nested test class unless the "per-class" test instance lifecycle is used.
     * Such annotations are not inherited.
     */

    /**
     * @Tag
     *
     * pom.xml에
     * maven-surefire-plugin 플러그인 추가
     * 혹은 mvn test -DexcludedGroups="development" 로 빌드드
    */
    @Test
    @Tag("production")
    void productionTest() {}

    @Test
    @DisplayName("Enabled when it is built")
    @Disabled
    @Tag("development")
    void developmentTest() {
        //fail("It is a Development test");
    }

    /**
     * @Timeout
     *
     * Used to fail a test, test factory, test template, or lifecycle method if its execution exceeds a given duration.
     * Such annotations are inherited.
     */

    /**
     * @ExtendWith
     * Used to register extensions declaratively. Such annotations are inherited.
     */
    @ExtendWith(RandomParametersExtension.class)
    @Test
    void test(@RandomParametersExtension.Random int i) {
        assertNotEquals(1, i);
    }

    /**
     * @RegisterExtension
     *
     */

    @RegisterExtension
    static WebServerExtension server = WebServerExtension.builder()
            .enableSecurity(false)
            .build();
    @Test
    @Timeout(10)
    void getProductList() {
        WebClient webClient = new WebClient();
        String serverUrl = server.getServerUrl();
        // Use WebClient to connect to web server using serverUrl and verify response
        assertEquals(200, webClient.get(serverUrl + "/products").getResponseStatus());
    }

    /**
     * @TempDir
     * Used to supply a temporary directory via field injection or parameter injection in a lifecycle method or test method;
     * located in the org.junit.jupiter.api.io package.
     */

    @Test
    void writeItemsToFile(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("test.txt");
        new ListWriter(file).write("a", "b", "c");

        assertEquals(java.util.Collections.singletonList("a,b,c"), Files.readAllLines(file));
    }

}
/**
 * @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderedTestDemo {
    @Test
    @Order(3)
    void nullValues(){}
    @Test
    @Order(2)
    void emptyValues(){}
    @Test
    @Order(1)
    void validValues(){}
}

class RandomParametersExtension implements ParameterResolver {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    public @interface Random {
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.isAnnotated(Random.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return getRandomValue(parameterContext.getParameter(), extensionContext);
    }

    private Object getRandomValue(Parameter parameter, ExtensionContext extensionContext) {
        Class<?> type = parameter.getType();
        java.util.Random random = extensionContext.getRoot().getStore(ExtensionContext.Namespace.GLOBAL)//
                .getOrComputeIfAbsent(java.util.Random.class);
        if (int.class.equals(type)) {
            return random.nextInt();
        }
        if (double.class.equals(type)) {
            return random.nextDouble();
        }
        throw new ParameterResolutionException("No random generator implemented for " + type);
    }

}

class WebServerExtension implements BeforeAllCallback {

    @Override
    public void beforeAll(ExtensionContext context) {
        /* no-op for demo */
    }

    public String getServerUrl() {
        return "https://example.org:8181";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        public Builder enableSecurity(boolean b) {
            return this;
        }

        public WebServerExtension build() {
            return new WebServerExtension();
        }

    }

}
class WebResponse {

    public int getResponseStatus() {
        return 200;
    }

}
class WebClient {

    public WebResponse get(String string) {
        return new WebResponse();
    }

}

class ListWriter {

    private final Path file;

    public ListWriter(Path file) {
        this.file = file;
    }

    public void write(String... items) throws IOException {
        Files.write(file, java.util.Collections.singletonList(String.join(",", items)));
    }

}
