package com.bilgeadam.controller;

import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.bilgeadam.constant.ApiUrls.*;

@RestController
@RequestMapping(USER)
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService userProfileService;

    @GetMapping(FIND_ALL)
    public ResponseEntity<Iterable<UserProfile>> findAll(){
        return ResponseEntity.ok(userProfileService.findAll());
    }

    @GetMapping("/find-all-pageable")
    public ResponseEntity<Iterable<UserProfile>> findAll(int page, int size, String  sortParameter, String sortType){
        return ResponseEntity.ok(userProfileService.findAll(page, size,sortParameter,sortType));
    }

    @DeleteMapping("/delete-all")
    public void deleteAll(){
        userProfileService.deleteAll();
    }

}
