package com.gintire.test.junit5;

import org.junit.jupiter.api.extension.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * Project: spring-boot-test
 * Package: com.gintire.springboottest.junit5
 * <p>
 * User: jin36
 * Date: 2021-01-06
 * Time: 오전 2:21
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 *
 * This class is used AnotationsTest::testTemplate
 */
public class MyTestTemplateInvocationContextProvider
        implements TestTemplateInvocationContextProvider {

    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        return true;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(
            ExtensionContext context) {

        return Stream.of(invocationContext("apple"), invocationContext("banana"));
    }

    private TestTemplateInvocationContext invocationContext(String parameter) {
        return new TestTemplateInvocationContext() {
            @Override
            public String getDisplayName(int invocationIndex) {
                return parameter;
            }

            @Override
            public List<Extension> getAdditionalExtensions() {
                return Collections.singletonList(new ParameterResolver() {
                    @Override
                    public boolean supportsParameter(ParameterContext parameterContext,
                                                     ExtensionContext extensionContext) {
                        return parameterContext.getParameter().getType().equals(String.class);
                    }

                    @Override
                    public Object resolveParameter(ParameterContext parameterContext,
                                                   ExtensionContext extensionContext) {
                        return parameter;
                    }
                });
            }
        };
    }
}
