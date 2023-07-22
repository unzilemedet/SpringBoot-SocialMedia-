package com.bilgeadam.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Document
public class Post extends Base{
    @Id
    private String id;
    private String userId;
    private String username;
    private String avatar;
    private String content;
    private List<String> mediaUrls;
    private List<String> likes = new ArrayList<>();
    private List<String> dislikes;
    private List<String> comments;
}
