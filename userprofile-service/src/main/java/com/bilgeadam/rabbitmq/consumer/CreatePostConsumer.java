package com.bilgeadam.rabbitmq.consumer;

import com.bilgeadam.rabbitmq.model.CreatePostModel;
import com.bilgeadam.rabbitmq.model.UserProfileResponseModel;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreatePostConsumer {
    private final UserProfileService userProfileService;

    @RabbitListener(queues = ("${rabbitmq.queueCreatePost}"))
    public Object createPost(CreatePostModel model){
        Optional<UserProfile> userProfile = userProfileService.findByAuthId(model.getAuthId());
        UserProfileResponseModel userProfileResponseModel = UserProfileResponseModel.builder()
                .userId(userProfile.get().getId())
                .username(userProfile.get().getUsername())
                .avatar(userProfile.get().getAvatar())
                .build();
        return userProfileResponseModel;
    }
}
