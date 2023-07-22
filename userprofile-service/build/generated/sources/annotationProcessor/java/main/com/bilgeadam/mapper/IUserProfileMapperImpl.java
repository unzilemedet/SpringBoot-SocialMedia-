package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.NewCreateUserRequestDto;
import com.bilgeadam.dto.request.ToAuthPasswordChangeDto;
import com.bilgeadam.dto.request.UpdateEmailOrUsernameRequestDto;
import com.bilgeadam.dto.request.UserProfileUpdateRequestDto;
import com.bilgeadam.dto.response.UserProfileResponseDto;
import com.bilgeadam.rabbitmq.model.RegisterElasticModel;
import com.bilgeadam.rabbitmq.model.RegisterModel;
import com.bilgeadam.repository.entity.Follow;
import com.bilgeadam.repository.entity.UserProfile;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-04T14:55:40+0300",
    comments = "version: 1.5.3.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.5.1.jar, environment: Java 17.0.6 (Amazon.com Inc.)"
)
@Component
public class IUserProfileMapperImpl implements IUserProfileMapper {

    @Override
    public UserProfile fromDtoToUserProfile(NewCreateUserRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        UserProfile.UserProfileBuilder<?, ?> userProfile = UserProfile.builder();

        userProfile.authId( dto.getAuthId() );
        userProfile.username( dto.getUsername() );
        userProfile.email( dto.getEmail() );
        userProfile.password( dto.getPassword() );

        return userProfile.build();
    }

    @Override
    public UserProfile fromRegisterModelToUserProfile(RegisterModel registerModel) {
        if ( registerModel == null ) {
            return null;
        }

        UserProfile.UserProfileBuilder<?, ?> userProfile = UserProfile.builder();

        userProfile.authId( registerModel.getAuthId() );
        userProfile.username( registerModel.getUsername() );
        userProfile.name( registerModel.getName() );
        userProfile.surname( registerModel.getSurname() );
        userProfile.email( registerModel.getEmail() );
        userProfile.password( registerModel.getPassword() );

        return userProfile.build();
    }

    @Override
    public RegisterElasticModel fromUserProfileToElasticModel(UserProfile userProfile) {
        if ( userProfile == null ) {
            return null;
        }

        RegisterElasticModel.RegisterElasticModelBuilder registerElasticModel = RegisterElasticModel.builder();

        registerElasticModel.id( userProfile.getId() );
        registerElasticModel.authId( userProfile.getAuthId() );
        registerElasticModel.username( userProfile.getUsername() );
        registerElasticModel.email( userProfile.getEmail() );

        return registerElasticModel.build();
    }

    @Override
    public UserProfile updateFromDtoToUserProfile(UserProfileUpdateRequestDto dto, UserProfile userProfile) {
        if ( dto == null ) {
            return userProfile;
        }

        if ( dto.getUsername() != null ) {
            userProfile.setUsername( dto.getUsername() );
        }
        if ( dto.getEmail() != null ) {
            userProfile.setEmail( dto.getEmail() );
        }
        if ( dto.getPhone() != null ) {
            userProfile.setPhone( dto.getPhone() );
        }
        if ( dto.getAvatar() != null ) {
            userProfile.setAvatar( dto.getAvatar() );
        }
        if ( dto.getInfo() != null ) {
            userProfile.setInfo( dto.getInfo() );
        }
        if ( dto.getAddress() != null ) {
            userProfile.setAddress( dto.getAddress() );
        }

        return userProfile;
    }

    @Override
    public UpdateEmailOrUsernameRequestDto toUpdateUsernameEmail(UserProfile userProfile) {
        if ( userProfile == null ) {
            return null;
        }

        UpdateEmailOrUsernameRequestDto.UpdateEmailOrUsernameRequestDtoBuilder updateEmailOrUsernameRequestDto = UpdateEmailOrUsernameRequestDto.builder();

        updateEmailOrUsernameRequestDto.authId( userProfile.getAuthId() );
        updateEmailOrUsernameRequestDto.username( userProfile.getUsername() );
        updateEmailOrUsernameRequestDto.email( userProfile.getEmail() );

        return updateEmailOrUsernameRequestDto.build();
    }

    @Override
    public UpdateEmailOrUsernameRequestDto toUpdateUsernameEmail(UserProfileUpdateRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        UpdateEmailOrUsernameRequestDto.UpdateEmailOrUsernameRequestDtoBuilder updateEmailOrUsernameRequestDto = UpdateEmailOrUsernameRequestDto.builder();

        updateEmailOrUsernameRequestDto.username( dto.getUsername() );
        updateEmailOrUsernameRequestDto.email( dto.getEmail() );

        return updateEmailOrUsernameRequestDto.build();
    }

    @Override
    public Follow fromCreateFollowDtoToFollow(String followId, String userId) {
        if ( followId == null && userId == null ) {
            return null;
        }

        Follow.FollowBuilder<?, ?> follow = Follow.builder();

        follow.followId( followId );
        follow.userId( userId );

        return follow.build();
    }

    @Override
    public ToAuthPasswordChangeDto fromUserProfileToAuthPasswordChangeDto(UserProfile userProfile) {
        if ( userProfile == null ) {
            return null;
        }

        ToAuthPasswordChangeDto.ToAuthPasswordChangeDtoBuilder toAuthPasswordChangeDto = ToAuthPasswordChangeDto.builder();

        toAuthPasswordChangeDto.authId( userProfile.getAuthId() );
        toAuthPasswordChangeDto.password( userProfile.getPassword() );

        return toAuthPasswordChangeDto.build();
    }

    @Override
    public UserProfileResponseDto fromUserProfileToResponseDto(UserProfile userProfile) {
        if ( userProfile == null ) {
            return null;
        }

        UserProfileResponseDto.UserProfileResponseDtoBuilder userProfileResponseDto = UserProfileResponseDto.builder();

        userProfileResponseDto.userId( userProfile.getId() );
        userProfileResponseDto.username( userProfile.getUsername() );
        userProfileResponseDto.avatar( userProfile.getAvatar() );

        return userProfileResponseDto.build();
    }
}
