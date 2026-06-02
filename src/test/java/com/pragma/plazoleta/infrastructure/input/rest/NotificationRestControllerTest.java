package com.pragma.plazoleta.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.plazoleta.application.dto.request.notification.SendSmsRequestDto;
import com.pragma.plazoleta.application.handler.INotificationHandler;
import com.pragma.plazoleta.infrastructure.configuration.SecurityConfiguration;
import com.pragma.plazoleta.infrastructure.configuration.security.CustomAccessDeniedHandler;
import com.pragma.plazoleta.infrastructure.configuration.security.CustomAuthenticationEntryPoint;
import com.pragma.plazoleta.infrastructure.configuration.security.CustomAuthenticationFilter;
import com.pragma.plazoleta.infrastructure.configuration.security.token.ITokenValidationPort;
import com.pragma.plazoleta.infrastructure.configuration.security.token.dto.AuthenticatedUser;
import com.pragma.plazoleta.infrastructure.exceptionhandler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = NotificationRestController.class)
@Import({
        GlobalExceptionHandler.class,
        SecurityConfiguration.class,
        CustomAuthenticationFilter.class,
        CustomAuthenticationEntryPoint.class,
        CustomAccessDeniedHandler.class
})
class NotificationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ITokenValidationPort tokenValidationPort;

    @MockBean
    private INotificationHandler notificationHandler;

    private ObjectMapper objectMapper;
    private SendSmsRequestDto validRequest;

    private UsernamePasswordAuthenticationToken employeeAuthentication;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        validRequest = SendSmsRequestDto.builder()
                .to("+51938475869")
                .message("Your order #12 is ready. PIN: 482910")
                .build();

        var employeePrincipal = new AuthenticatedUser(
                7L,
                "employee@plazoleta.com",
                "EMPLOYEE"
        );
        employeeAuthentication = new UsernamePasswordAuthenticationToken(
                employeePrincipal,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))
        );
    }

    @Test
    @DisplayName("Should return 204 No Content when EMPLOYEE dispatches a valid SMS in send sms")
    void shouldReturn204NoContentWhenEmployeeDispatchesAValidSmsInSendSms() throws Exception {
        mockMvc.perform(post("/api/v1/notifications/sms")
                        .with(authentication(employeeAuthentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isNoContent());

        verify(notificationHandler).sendSms(any(SendSmsRequestDto.class));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when phone is missing in send sms")
    void shouldReturn400BadRequestWhenPhoneIsMissingInSendSms() throws Exception {
        validRequest.setTo(null);

        mockMvc.perform(post("/api/v1/notifications/sms")
                        .with(authentication(employeeAuthentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());

        verify(notificationHandler, never()).sendSms(any());
    }

    @Test
    @DisplayName("Should return 400 Bad Request when phone contains letters in send sms")
    void shouldReturn400BadRequestWhenPhoneContainsLettersInSendSms() throws Exception {
        validRequest.setTo("+51ABC666666");

        mockMvc.perform(post("/api/v1/notifications/sms")
                        .with(authentication(employeeAuthentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());

        verify(notificationHandler, never()).sendSms(any());
    }
}
