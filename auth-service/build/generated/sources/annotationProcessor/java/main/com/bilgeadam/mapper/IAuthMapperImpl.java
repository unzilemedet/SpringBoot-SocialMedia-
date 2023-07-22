package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.RegisterRequestDto;
import com.bilgeadam.dto.request.UpdateEmailOrUsernameRequestDto;
import com.bilgeadam.dto.response.RegisterResponseDto;
import com.bilgeadam.model.entity.Auth;
import com.bilgeadam.model.entity.Role;
import com.bilgeadam.rabbitmq.model.RegisterMailModel;
import com.bilgeadam.rabbitmq.model.RegisterModel;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-05T01:39:34+0300",
    comments = "version: 1.5.3.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.5.1.jar, environment: Java 17.0.6 (Amazon.com Inc.)"
)
@Component
public class IAuthMapperImpl implements IAuthMapper {

    @Override
    public Auth fromRequestDtoToAuth(RegisterRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Auth.AuthBuilder<?, ?> auth = Auth.builder();

        auth.username( dto.getUsername() );
        auth.name( dto.getName() );
        auth.surname( dto.getSurname() );
        auth.email( dto.getEmail() );
        auth.password( dto.getPassword() );
        List<Role> list = dto.getRoles();
        if ( list != null ) {
            auth.roles( new ArrayList<Role>( list ) );
        }

        return auth.build();
    }

    @Override
    public RegisterModel fromAuthToRegisterModel(Auth auth) {
        if ( auth == null ) {
            return null;
        }

        RegisterModel.RegisterModelBuilder registerModel = RegisterModel.builder();

        registerModel.authId( auth.getId() );
        registerModel.username( auth.getUsername() );
        registerModel.name( auth.getName() );
        registerModel.surname( auth.getSurname() );
        registerModel.email( auth.getEmail() );
        registerModel.password( auth.getPassword() );

        return registerModel.build();
    }

    @Override
    public RegisterMailModel fromAuthToRegisterMailModel(Auth auth) {
        if ( auth == null ) {
            return null;
        }

        RegisterMailModel.RegisterMailModelBuilder registerMailModel = RegisterMailModel.builder();

        registerMailModel.name( auth.getName() );
        registerMailModel.surname( auth.getSurname() );
        registerMailModel.email( auth.getEmail() );

        return registerMailModel.build();
    }

    @Override
    public RegisterResponseDto fromAuthToResponseDto(Auth auth) {
        if ( auth == null ) {
            return null;
        }

        RegisterResponseDto.RegisterResponseDtoBuilder registerResponseDto = RegisterResponseDto.builder();

        registerResponseDto.id( auth.getId() );
        registerResponseDto.username( auth.getUsername() );
        registerResponseDto.activationCode( auth.getActivationCode() );

        return registerResponseDto.build();
    }

    @Override
    public void updateUsernameOrEmail(UpdateEmailOrUsernameRequestDto dto, Auth auth) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getUsername() != null ) {
            auth.setUsername( dto.getUsername() );
        }
        if ( dto.getEmail() != null ) {
            auth.setEmail( dto.getEmail() );
        }
    }
}
