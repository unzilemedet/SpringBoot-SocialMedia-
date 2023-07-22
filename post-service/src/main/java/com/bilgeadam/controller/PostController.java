package com.bilgeadam.controller;

import com.bilgeadam.dto.request.CreateCommentDto;
import com.bilgeadam.dto.request.CreateNewPostRequestDto;
import com.bilgeadam.dto.request.UpdatePostRequestDto;
import com.bilgeadam.repository.entity.Post;
import com.bilgeadam.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constant.ApiUrls.*;

@RestController
@RequestMapping(POST)
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping(CREATE + "/{token}")
    public ResponseEntity<Post> createPost(@PathVariable String token, @RequestBody CreateNewPostRequestDto dto){
        return ResponseEntity.ok(postService.createPost(token, dto));
    }
    @PostMapping(CREATE + "/-with-rabbitmq" + "/{token}")
    public ResponseEntity<Post> createPostWithRabbitMq(@PathVariable String token, @RequestBody CreateNewPostRequestDto dto){
        return ResponseEntity.ok(postService.createPostWithRabbitMq(token, dto));
    }

    @PutMapping(UPDATE)
    public ResponseEntity<Post> updatePost(String token,String postId, UpdatePostRequestDto dto){
        return ResponseEntity.ok(postService.updatePost(token, postId, dto));
    }

    @GetMapping(FIND_ALL)
    public ResponseEntity<List<Post>> findAll(){
        return ResponseEntity.ok(postService.findAll());
    }

    @PostMapping("/like-post")
    public ResponseEntity<Boolean> likePost(String token, String postId){
        return ResponseEntity.ok(postService.likePost(token, postId));
    }

    @DeleteMapping(DELETE_BY_ID)
    public ResponseEntity<Boolean> deletePost(String token, String postId){
        return ResponseEntity.ok(postService.deletePost(token, postId));
    }

    @PostMapping("/create-comment/{token}")
    public ResponseEntity<Boolean> createComment(@PathVariable String token, @RequestBody CreateCommentDto dto){
        return ResponseEntity.ok(postService.createComment(token, dto));
    }
}
