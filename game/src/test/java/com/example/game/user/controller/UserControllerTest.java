package com.example.game.user.controller;

import com.example.game.config.SecurityConfig;
import com.example.game.config.jwt.JwtAuthenticationFilter;
import com.example.game.config.jwt.JwtAuthorizationFilter;
import com.example.game.user.dto.RequestUsernameChange;
import com.example.game.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthorizationFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
        }
)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @DisplayName("유저 닉네임을 변경한다.")
    @Test
    void usernameChangeTest() throws Exception {
        // given
        RequestUsernameChange request = new RequestUsernameChange("username");
        // when // then
        mockMvc.perform(
                        post("/user/name")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("success"))
                .andExpect(jsonPath("$.error").isEmpty());
    }

    @DisplayName("유저 닉네임을 변경 시 닉네임은 2~14글자이고 숫자와 영문만 사용 가능하다.")
    @CsvSource({"a", "012345678901234", "aaaaa#", "한글닉네임"})
    @ParameterizedTest
    void usernameChangeWithWrongUsernameTest(String username) throws Exception {
        // given
        RequestUsernameChange request = new RequestUsernameChange(username);
        // when // then
        mockMvc.perform(
                        post("/user/name")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error.message").value("Validation failed"))
                .andExpect(jsonPath("$.error.detail").value("닉네임은 특수문자를 제외한 2~14자리여야 합니다."));
    }

}
