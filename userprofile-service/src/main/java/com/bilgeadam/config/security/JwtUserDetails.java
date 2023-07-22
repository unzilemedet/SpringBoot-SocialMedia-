package com.bilgeadam.config.security;

import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtUserDetails implements UserDetailsService {

    private final UserProfileService userProfileService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public UserDetails loadUserByUserRole(String role, Long authId) throws UsernameNotFoundException {
        //gelen role --> [Role(id=1, role=USER), Role(id=2, role=ADMIN)]
        String[] roleParts = role.split(",");
        List<String> roles = new ArrayList<>();
        for (String part : roleParts) {
            String pureRole = part.trim().replaceAll("role=", "").replaceAll("[^a-zA-Z]", "");
            if (!role.isEmpty()) {
                roles.add(pureRole);
                roles.remove("Roleid");
            }
        }
        List<GrantedAuthority> authorityList = new ArrayList<>();
        roles.forEach(x -> {
            System.out.println(x);
            authorityList.add(new SimpleGrantedAuthority(x));
        });
        Optional<UserProfile> userProfile = userProfileService.findByAuthId(authId);
        return User.builder()
                .username(userProfile.get().getUsername())
                .password("")
                .accountLocked(false)
                .accountExpired(false)
                .authorities(authorityList)
                .build();
    }
}
