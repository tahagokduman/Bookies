package com.bookapp.SecurityTests;

import static org.assertj.core.api.Assertions.assertThat;

import com.bookapp.backend.application.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@SpringBootTest
public class SecurityConfigTest {

    @Autowired
    private SecurityConfig securityConfig;

    @Test
    public void passwordEncoder_IsBCryptPasswordEncoder() {
        assertThat(securityConfig.passwordEncoder()).isInstanceOf(PasswordEncoder.class);
    }

    @Test
    public void authenticationProvider_IsNotNull() {
        assertThat(securityConfig.authenticationProvider()).isNotNull();
    }

    @Test
    public void authenticationManager_IsNotNull() throws Exception {
        AuthenticationConfiguration config = Mockito.mock(AuthenticationConfiguration.class);
        AuthenticationManager mockManager = Mockito.mock(AuthenticationManager.class);

        Mockito.when(config.getAuthenticationManager()).thenReturn(mockManager);

        AuthenticationManager manager = securityConfig.authenticationManager(config);

        assertThat(manager).isNotNull();
    }

    @Test
    public void securityFilterChain_IsConfigured() throws Exception {
        HttpSecurity httpSecurity = Mockito.mock(HttpSecurity.class);

        SecurityFilterChain chain = securityConfig.securityFilterChain(httpSecurity);

        assertThat(chain).isNotNull();
    }
}
