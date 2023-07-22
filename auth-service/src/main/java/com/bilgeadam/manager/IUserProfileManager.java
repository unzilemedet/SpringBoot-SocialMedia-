package com.bilgeadam.manager;

import com.bilgeadam.dto.request.ToUserProfileForgotPasswordRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.bilgeadam.constant.ApiUrls.DELETE_BY_ID;
import static com.bilgeadam.constant.ApiUrls.FORGOT_PASSWORD;

@FeignClient(url = "http://localhost:8080/api/v1/user-profile", name = "auth-userprofile")
public interface IUserProfileManager {
    @PostMapping("/activate-status")
    public ResponseEntity<Boolean> activateStatus(@RequestHeader(value = "Authorization") String token);

    @DeleteMapping(DELETE_BY_ID + "/{authId}")
    public ResponseEntity<Boolean> delete(@PathVariable Long authId);

    @PutMapping(FORGOT_PASSWORD)
    public ResponseEntity<Boolean> forgotPassword(@RequestBody ToUserProfileForgotPasswordRequestDto dto);
}
