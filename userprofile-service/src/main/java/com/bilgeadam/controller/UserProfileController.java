package com.bilgeadam.controller;

import com.bilgeadam.dto.request.NewCreateUserRequestDto;
import com.bilgeadam.dto.request.PasswordChangeDto;
import com.bilgeadam.dto.request.UserProfileUpdateRequestDto;
import com.bilgeadam.dto.response.UserProfileChangePasswordResponseDto;
import com.bilgeadam.dto.response.UserProfileResponseDto;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.service.UserProfileService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.bilgeadam.constant.ApiUrls.*;

@RestController
@RequestMapping(USER_PROFILE)
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService userProfileService;

    @PutMapping(PASS_CHANGE)
    public ResponseEntity<Boolean> changePassword(PasswordChangeDto dto){
        return ResponseEntity.ok(userProfileService.changePassword(dto));
    }

    @Hidden
    @PutMapping(FORGOT_PASSWORD)
    public ResponseEntity<Boolean> forgotPassword(@RequestBody UserProfileChangePasswordResponseDto dto){
        return ResponseEntity.ok(userProfileService.forgotPassword(dto));
    }

    @GetMapping(FIND_ALL)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<List<UserProfile>> findAll(){
        return ResponseEntity.ok(userProfileService.findAll());
    }

    @Hidden
    @PostMapping(ACTIVATE_STATUS)
    public ResponseEntity<Boolean> activateStatus(@RequestHeader(value = "Authorization") String token){
        return ResponseEntity.ok(userProfileService.activateStatus(token));
    }

    @PutMapping(UPDATE)
    public ResponseEntity<UserProfile> update(@RequestBody UserProfileUpdateRequestDto dto){
        return ResponseEntity.ok(userProfileService.update(dto));
    }

    @Hidden
    @DeleteMapping(DELETE_BY_ID + "/{authId}")
    public ResponseEntity<Boolean> delete(@PathVariable Long authId){
        return ResponseEntity.ok(userProfileService.delete(authId));
    }

    @GetMapping("/find-by-username/{username}")
    public ResponseEntity<UserProfile> findByUsername(@PathVariable String username){
        return ResponseEntity.ok(userProfileService.findByUsername(username));
    }

    @Hidden
    @GetMapping("/find-by-role/{role}")
    public ResponseEntity<List<UserProfile>> findByRole(@RequestHeader(value = "Authorization")String token, @PathVariable String role){
        return ResponseEntity.ok(userProfileService.findByRole(role, token));
    }

    @GetMapping("/find-by-auth-id/{authId}")
    public ResponseEntity<Optional<UserProfile>> findByAuthId(@PathVariable Long authId){
        return ResponseEntity.ok(userProfileService.findByAuthId(authId));
    }

    @GetMapping("/find-by-userprofile-dto/{authId}")
    public ResponseEntity<UserProfileResponseDto> findByUserProfileDto(@PathVariable Long authId){
        return ResponseEntity.ok(userProfileService.findByUserProfileDto(authId));
    }
}
