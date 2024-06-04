package com.intouch.aligooligo.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static reactor.core.publisher.Mono.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intouch.aligooligo.domain.auth.controller.AuthController;
import com.intouch.aligooligo.domain.auth.dto.SignInResponse;
import com.intouch.aligooligo.domain.auth.dto.TokenInfo;
import com.intouch.aligooligo.domain.auth.service.AuthService;
import com.intouch.aligooligo.domain.user.entity.Role;
import com.intouch.aligooligo.global.Jwt.JwtTokenProvider;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @MockBean
    private AuthService authService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();
    private TokenInfo tokenInfo;

    @BeforeEach
    public void initMockMvc() {
        tokenInfo = new TokenInfo("accessToken", "refreshToken");
    }
    @Test
    @WithMockUser
    @DisplayName("토큰 재발급 컨트롤러 테스트")
    void reissueTokenTest() throws Exception {
        String refreshToken = "refreshToken";
        given(jwtTokenProvider.resolveRefreshToken(any(HttpServletRequest.class))).willReturn(refreshToken);
        given(authService.reIssueToken(any(String.class))).willReturn(tokenInfo);

        MockHttpServletRequestBuilder requestBuilder = post("/api/auth/reissue")
                .header("RefreshToken", "refreshToken")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON);
        ResultActions resultActions = mockMvc.perform(requestBuilder);

        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("유저 로그인 컨트롤러 테스트")
    void signInKakao() throws Exception {
        //given
        String code = "authorizeCode";
        String kakaoToken = "kakaoAccessToken";
        tokenInfo = new TokenInfo("accessToken", "refreshToken");
        given(authService.getKakaoAccessToken(code)).willReturn(kakaoToken);
        given(authService.getKakaoUserInfo(kakaoToken)).willReturn(new SignInResponse(tokenInfo, true));
        //given(authService.kakaoLogin(code)).willReturn(new SignInResponse(tokenInfo, true));

        //when
        MockHttpServletRequestBuilder requestBuilder = post("/api/auth/kakao")
                .param("code", code)
                .contentType(MediaType.APPLICATION_JSON);
        ResultActions resultActions = mockMvc.perform(requestBuilder);

        resultActions.andExpect(status().isOk());
    }


}
