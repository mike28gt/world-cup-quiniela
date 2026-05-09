package com.quiniela.adapters.in.web;

import com.quiniela.ports.in.LoginUserUseCase;
import com.quiniela.ports.in.RegisterUserUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;

    public AuthController(RegisterUserUseCase registerUserUseCase,
                          LoginUserUseCase loginUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse registerUser(@Valid @RequestBody RegisterRequest request) {

        var command = new RegisterUserUseCase.Command(
                request.displayName(),
                request.email(),
                request.password(),
                request.confirmPassword()
        );

        var result  = registerUserUseCase.register(command);

        return new RegisterResponse(
                result.userId(),
                result.accessToken()
        );
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {

        var command = new LoginUserUseCase.Command(
                request.email(),
                request.password()
        );

        var result  = loginUserUseCase.login(command);

        return new LoginResponse(
                result.userId(),
                result.accessToken()
        );
    }

}
