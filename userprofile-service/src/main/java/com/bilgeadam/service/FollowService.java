package com.bilgeadam.service;

import com.bilgeadam.dto.request.CreateFollowDto;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.UserProfileManagerException;
import com.bilgeadam.mapper.IUserProfileMapper;
import com.bilgeadam.repository.IFollowRepository;
import com.bilgeadam.repository.entity.Follow;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.utility.JwtTokenProvider;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class FollowService extends ServiceManager<Follow, String> {
    private final IFollowRepository followRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserProfileService userProfileService;

    public FollowService(IFollowRepository followRepository, JwtTokenProvider jwtTokenProvider, UserProfileService userProfileService) {
        super(followRepository);
        this.followRepository = followRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userProfileService = userProfileService;
    }

    public Boolean createFollow(CreateFollowDto dto){
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(dto.getToken());
        if (authId.isEmpty()){
            throw new UserProfileManagerException(ErrorType.INVALID_TOKEN);
        }

        Optional<UserProfile> userProfile = userProfileService.findByAuthId(authId.get()); //takip edecek kişi/takip eden
        Optional<UserProfile> followUser = userProfileService.findById(dto.getFollowId()); //takip edilecek kişi/takip edilen

        //Follow Repository' den daha önce takipleşen kişilerin varlığını bulmak için yazılan kod bloğu
        Optional<Follow> followDB = followRepository.findByUserIdAndFollowId
                (userProfile.get().getId(),
                        followUser.get().getId());
        if (followDB.isPresent()){
            throw new UserProfileManagerException(ErrorType.FOLLOW_ALREADY_EXIST);
        }

        if (userProfile.isPresent() && followUser.isPresent()){
            if (userProfile.get().getId().equals(followUser.get().getId())){
                throw new UserProfileManagerException(ErrorType.USER_NOT_FOLLOW);
            }else {
                Follow follow = IUserProfileMapper.INSTANCE.fromCreateFollowDtoToFollow(dto.getFollowId(), userProfile.get().getId());
                save(follow);

                userProfile.get().getFollows().add(followUser.get().getId()); //takip eden kişinin follows' una ekleme yapılıyor
                followUser.get().getFollowers().add(userProfile.get().getId()); //bir kişinin takipçilerine ekleme yapılıyor
                userProfileService.update(userProfile.get());
                userProfileService.update(followUser.get());
                return true;
            }
        }else {
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        }
    }
}
