package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.*;
import com.bilgeadam.dto.response.UserProfileResponseDto;
import com.bilgeadam.rabbitmq.model.RegisterElasticModel;
import com.bilgeadam.rabbitmq.model.RegisterModel;
import com.bilgeadam.repository.entity.Follow;
import com.bilgeadam.repository.entity.UserProfile;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserProfileMapper {

    IUserProfileMapper INSTANCE = Mappers.getMapper(IUserProfileMapper.class);

    UserProfile fromDtoToUserProfile(final NewCreateUserRequestDto dto);

    UserProfile fromRegisterModelToUserProfile(final RegisterModel registerModel);

    RegisterElasticModel fromUserProfileToElasticModel(final UserProfile userProfile);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserProfile updateFromDtoToUserProfile(UserProfileUpdateRequestDto dto, @MappingTarget UserProfile userProfile);

    UpdateEmailOrUsernameRequestDto toUpdateUsernameEmail(final UserProfile userProfile);

    UpdateEmailOrUsernameRequestDto toUpdateUsernameEmail(final UserProfileUpdateRequestDto dto);

    Follow fromCreateFollowDtoToFollow(final String followId, final String userId);

    ToAuthPasswordChangeDto fromUserProfileToAuthPasswordChangeDto(final UserProfile userProfile);

    @Mapping(source = "id", target = "userId")
    UserProfileResponseDto fromUserProfileToResponseDto(final UserProfile userProfile);
}
