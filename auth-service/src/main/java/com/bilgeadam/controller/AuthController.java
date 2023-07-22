package com.bilgeadam.controller;

import static com.bilgeadam.constant.ApiUrls.*;

import com.bilgeadam.dto.request.*;
import com.bilgeadam.model.entity.Auth;
import com.bilgeadam.service.AuthService;
import com.bilgeadam.utility.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(AUTH)
public class AuthController {
    private final AuthService authService;
    private final JwtTokenProvider tokenProvider;

    @PostMapping(REGISTER)
    public ResponseEntity<Auth> register(@RequestBody @Valid RegisterRequestDto dto){
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping(LOGIN)
    public ResponseEntity<String> login(@RequestBody LoginRequestDto dto){
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping(FORGOT_PASSWORD_SEND_MAIL_TOKEN)
    public ResponseEntity<Boolean> forgotPasswordSendMailToken(String email){
        return ResponseEntity.ok(authService.forgotPasswordSendMailToken(email));
    }

    @GetMapping(FORGOT_PASSWORD)
    public ResponseEntity<String> forgotPassword(){
        return ResponseEntity.ok(authService.forgotPassword());
    }

    @GetMapping(ACTIVATE_STATUS)
    public ResponseEntity<Boolean> activateStatus(@RequestParam String token){
        System.out.println("AuthController activateStatus' e gelen token: " + token);
        return ResponseEntity.ok(authService.activateStatus(token));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping(FIND_ALL)
    public ResponseEntity<List<Auth>> findAll(){
        return ResponseEntity.ok(authService.findAll());
    }

    //User Delete
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(DELETE_BY_ID)
    public ResponseEntity<Boolean> delete(String email){
        return ResponseEntity.ok(authService.delete(email));
    }

    @Hidden
    @PutMapping("/update-username-email")
    public ResponseEntity<Boolean> update(@RequestBody UpdateEmailOrUsernameRequestDto dto){
        return ResponseEntity.ok(authService.update(dto));
    }
    @Hidden
    @PutMapping(PASSWORD_CHANGE)
    public ResponseEntity<Boolean> passwordChange(@RequestBody FromUserProfilePasswordChangeRequestDto dto){
        return ResponseEntity.ok(authService.changePassword(dto));
    }
}
