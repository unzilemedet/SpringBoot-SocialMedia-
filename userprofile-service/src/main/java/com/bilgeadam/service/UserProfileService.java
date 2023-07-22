package com.bilgeadam.service;

import com.bilgeadam.dto.request.NewCreateUserRequestDto;
import com.bilgeadam.dto.request.PasswordChangeDto;
import com.bilgeadam.dto.request.UpdateEmailOrUsernameRequestDto;
import com.bilgeadam.dto.request.UserProfileUpdateRequestDto;
import com.bilgeadam.dto.response.UserProfileChangePasswordResponseDto;
import com.bilgeadam.dto.response.UserProfileResponseDto;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.UserProfileManagerException;
import com.bilgeadam.manager.IAuthManager;
import com.bilgeadam.mapper.IUserProfileMapper;
import com.bilgeadam.rabbitmq.model.RegisterModel;
import com.bilgeadam.rabbitmq.producer.RegisterElasticProducer;
import com.bilgeadam.repository.IUserProfileRepository;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.repository.enums.EStatus;
import com.bilgeadam.utility.JwtTokenProvider;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserProfileService extends ServiceManager<UserProfile, String> {
    private final IUserProfileRepository userProfileRepository;
    private final IAuthManager authManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CacheManager cacheManager;
    private final RegisterElasticProducer registerElasticProducer;
    private final PasswordEncoder passwordEncoder;
    public UserProfileService(IUserProfileRepository userProfileRepository, IAuthManager authManager, JwtTokenProvider jwtTokenProvider, CacheManager cacheManager, RegisterElasticProducer registerElasticProducer, PasswordEncoder passwordEncoder) {
        super(userProfileRepository);
        this.userProfileRepository = userProfileRepository;
        this.authManager = authManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.cacheManager = cacheManager;
        this.registerElasticProducer = registerElasticProducer;
        this.passwordEncoder = passwordEncoder;
    }

    @Cacheable(value = "findAll")
    public List<UserProfile> findAll() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return userProfileRepository.findAll();
    }

    @CacheEvict(value = "find-by-username", key = "#model.username.toLowerCase()")
    public Boolean createUser(RegisterModel model) {
        try {
            UserProfile userProfile = save(IUserProfileMapper.INSTANCE.fromRegisterModelToUserProfile(model));
            registerElasticProducer.sendNewUser(IUserProfileMapper.INSTANCE.fromUserProfileToElasticModel(userProfile));
            cacheManager.getCache("findAll").clear();
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Beklenmeyen bir hata oluştu.");
        }
    }

    @CacheEvict(value = "find-by-username", allEntries = true)
    public Boolean changePassword(PasswordChangeDto dto){
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(dto.getToken());
        if (authId.isEmpty()){
            throw new UserProfileManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(authId.get());
        if (userProfile.isPresent()){
            if (passwordEncoder.matches(dto.getOldPassword(), userProfile.get().getPassword())){
                String newPass = dto.getNewPassword();
                userProfile.get().setPassword(passwordEncoder.encode(newPass));
                cacheManager.getCache("findAll").clear();
                userProfileRepository.save(userProfile.get());
                authManager.changePassword(IUserProfileMapper.INSTANCE.fromUserProfileToAuthPasswordChangeDto(userProfile.get()));
                return true;
            }else {
                throw new UserProfileManagerException(ErrorType.PASSWORD_ERROR);
            }
        }else {
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        }
    }

    public Boolean forgotPassword(UserProfileChangePasswordResponseDto dto){
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(dto.getAuthId());
        if (userProfile.isEmpty()){
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        }
        userProfile.get().setPassword(dto.getPassword());
        update(userProfile.get());
        return true;
    }

    public Boolean activateStatus(String token) {
        System.out.println("Service' e gelen token --> " + token);
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token.substring(7));
        System.out.println("Service' de substring edilmiş token --> " + token.substring(7));
        if (authId.isEmpty()) {
            throw new UserProfileManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(authId.get());
        if (userProfile.isEmpty()) {
            throw new RuntimeException("Auth id bulunamadı");
        }
        userProfile.get().setStatus(EStatus.ACTIVE);
        update(userProfile.get());
        return true;
    }

    //cache delete yerine cache'i update etmeye yaramaktadır
    //oluşan değişiklikler sonucunda cache'in update edilmesini sağlar
    @CachePut(value = "find-by-username", key = "#dto.username.toLowerCase()")
    public UserProfile update(UserProfileUpdateRequestDto dto){
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(dto.getToken());
        if (authId.isEmpty()){
            throw new UserProfileManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(authId.get());
        if (userProfile.isEmpty()){
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        }
        //cache delete
        //cacheManager.getCache("find-by-username").evict(userProfile.get().getUsername().toLowerCase());

        /*
        ----UserProfile Çözümü(Cenk)----
        update(IUserProfileMapper.INSTANCE.updateFromDtoToUserProfile(dto, userProfile.get()));
        authManager.updateUsernameOrEmail(IUserProfileMapper.INSTANCE.toUpdateUsernameEmail(userProfile.get()));
        */
        UpdateEmailOrUsernameRequestDto updateEmailOrUsernameRequestDto = IUserProfileMapper.INSTANCE.toUpdateUsernameEmail(dto);
        updateEmailOrUsernameRequestDto.setAuthId(authId.get());
        authManager.updateUsernameOrEmail(updateEmailOrUsernameRequestDto);
        update(IUserProfileMapper.INSTANCE.updateFromDtoToUserProfile(dto, userProfile.get()));

        //Bertan Çözüm
        //cacheManager.getCache("find-by-username").put(userProfile.get().getUsername().toString().toLowerCase(), userProfile.get());
        return userProfile.get();
    }


    public Boolean delete(Long authId){
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(authId);
        if (userProfile.isEmpty()) {
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        }
        userProfile.get().setStatus(EStatus.DELETED);
        update(userProfile.get());
        return true;
    }

    @Cacheable(value = "find-by-username", key = "#username.toLowerCase()") //find-by-username(cache) --> ardaagdemir, ab123, asda, asdss
    public UserProfile findByUsername(String username){
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByUsernameIgnoreCase(username);
        if (userProfile.isEmpty()){
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        }
        return userProfile.get();
    }

    @Cacheable(value = "find-by-role", key = "#role.toUpperCase()")  //USER, ADMIN
    public List<UserProfile> findByRole(String role, String token) {
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //auth manager
        List<Long> authIds = authManager.findByRole(token,role).getBody();
        return authIds.stream().map(
                        x -> userProfileRepository.findOptionalByAuthId(x)
                                .orElseThrow(() -> {
                                    throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
                                }))
                .collect(Collectors.toList());
    }

    //for FollowService
    public Optional<UserProfile> findByAuthId(Long authId) {
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(authId);
        if (userProfile.isEmpty()) {
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        }
        return userProfile; //id, username, email , password
    }

    //for post-service
    public UserProfileResponseDto findByUserProfileDto(Long authId) {
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(authId);
        if (userProfile.isEmpty()) {
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        }
        UserProfileResponseDto userProfileResponseDto = IUserProfileMapper.INSTANCE.fromUserProfileToResponseDto(userProfile.get());
        return userProfileResponseDto; //return userId, username, avatar
    }
}
