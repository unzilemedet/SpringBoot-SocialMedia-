package com.bilgeadam.manager;

import com.bilgeadam.dto.response.UserProfileResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "http://localhost:8080/api/v1/user-profile", name = "post-userprofile")
public interface IUserProfileManager {

    @GetMapping("/find-by-userprofile-dto/{authId}")
    public ResponseEntity<UserProfileResponseDto> findByUserProfileDto(@PathVariable Long authId);
}
