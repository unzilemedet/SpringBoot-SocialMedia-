package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.CreateNewPostRequestDto;
import com.bilgeadam.dto.response.UserProfileResponseDto;
import com.bilgeadam.repository.entity.Like;
import com.bilgeadam.repository.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,componentModel = "spring")
public interface IPostMapper {
    IPostMapper INSTANCE= Mappers.getMapper(IPostMapper.class);

    Post toPost(final CreateNewPostRequestDto dto);

    Like toLike(final UserProfileResponseDto userProfile);

}
