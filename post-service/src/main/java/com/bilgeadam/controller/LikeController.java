package com.bilgeadam.controller;

import com.bilgeadam.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.bilgeadam.constant.ApiUrls.*;

@RestController
@RequestMapping(LIKE)
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;


}
