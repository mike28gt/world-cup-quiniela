package com.quiniela.config;

import com.quiniela.application.LoginUserService;
import com.quiniela.application.RegisterUserService;
import com.quiniela.ports.in.LoginUserUseCase;
import com.quiniela.ports.in.RegisterUserUseCase;
import com.quiniela.ports.out.PasswordHasher;
import com.quiniela.ports.out.TokenIssuer;
import com.quiniela.ports.out.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public RegisterUserUseCase registerUserUseCase(
            UserRepository userRepository,
            PasswordHasher passwordHasher,
            TokenIssuer tokenIssuer
    ) {

        return new RegisterUserService(
                userRepository,
                passwordHasher,
                tokenIssuer
        );
    }

    @Bean
    public LoginUserUseCase loginUserUseCase(
            UserRepository userRepository,
            PasswordHasher passwordHasher,
            TokenIssuer tokenIssuer
    ) {

        return new LoginUserService(
                userRepository,
                passwordHasher,
                tokenIssuer
        );
    }
}
