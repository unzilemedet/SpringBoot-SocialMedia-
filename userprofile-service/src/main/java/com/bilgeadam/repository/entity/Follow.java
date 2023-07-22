package com.bilgeadam.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Document
public class Follow extends Base {
    @Id
    private String id;
    private String userId;
    //userId' nin takip ettiği kişinin id'si
    private String followId;

    private String followName;
    private String followerName;

    //takip eden id           //takip edilen id        //önerilen takip
    //1                       //2, 5, 8                // 6 ve 9 u takip etmek ister misin
    //2                       //1, 6, 9
    //5                       //4, 6, 10
    //8                       //6, 12, 13
}
