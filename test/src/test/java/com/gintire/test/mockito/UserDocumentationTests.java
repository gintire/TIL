package com.gintire.test.mockito;

import com.gintire.test.application.UserService;
import com.gintire.test.domain.Gender;
import com.gintire.test.domain.User;
import com.gintire.test.interfaces.UserController;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.gintire.test.mockito.ApiDocumentUtils.getDocumentRequest;
import static com.gintire.test.mockito.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Project: spring-boot-test
 * Package: com.gintire.test.mockito
 * <p>
 *
 * @author: jin36
 * @version: 21.01
 * Date: 2021-01-19
 * Time: 오후 11:59
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureRestDocs(uriScheme = "http", uriHost = "localhost")
public class UserDocumentationTests {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void search() throws Exception {
        // given
        User user = new User(0, "james", 32, Gender.MALE);
        given(userService.getUser("james")).willReturn(user);

        // when
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/rest/api/v1/user/{name}", "james"));
        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(user.toString()))
                .andDo(print())
                .andDo(document("{class-name}/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("name").description("이름")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("아이디"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("age").type(JsonFieldType.NUMBER).description("나이"),
                                fieldWithPath("gender").type(JsonFieldType.STRING).description("성별")
                        )));


    }
}
