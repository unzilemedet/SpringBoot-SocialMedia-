package com.bilgeadam.service;

import com.bilgeadam.dto.request.*;
import com.bilgeadam.dto.response.ForgotPasswordMailResponseDto;
import com.bilgeadam.exception.AuthManagerException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.manager.IEmailManager;
import com.bilgeadam.manager.IUserProfileManager;
import com.bilgeadam.mapper.IAuthMapper;
import com.bilgeadam.rabbitmq.model.RegisterMailModel;
import com.bilgeadam.rabbitmq.producer.RegisterMailProducer;
import com.bilgeadam.rabbitmq.producer.RegisterProducer;
import com.bilgeadam.repository.IAuthRepository;
import com.bilgeadam.model.entity.Auth;
import com.bilgeadam.model.enums.EStatus;
import com.bilgeadam.utility.JwtTokenProvider;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.bilgeadam.utility.CodeGenerator.generateCode;

@Service
public class AuthService extends ServiceManager<Auth, Long> {
    private final IAuthRepository authRepository;
    private final IUserProfileManager userManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RegisterProducer registerProducer;
    private final RegisterMailProducer registerMailProducer;
    private final PasswordEncoder passwordEncoder;
    private final IEmailManager emailManager;

    public AuthService(JpaRepository<Auth, Long> repository,
                       IAuthRepository authRepository, IUserProfileManager userManager,
                       JwtTokenProvider jwtTokenProvider, RegisterProducer registerProducer,
                       RegisterMailProducer registerMailProducer, IEmailManager emailManager,
                       PasswordEncoder passwordEncoder) {
        super(repository);
        this.authRepository = authRepository;
        this.userManager = userManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.registerProducer = registerProducer;
        this.registerMailProducer = registerMailProducer;
        this.passwordEncoder = passwordEncoder;
        this.emailManager = emailManager;
    }

    public Auth register(RegisterRequestDto dto) {
        Auth auth = IAuthMapper.INSTANCE.fromRequestDtoToAuth(dto);
        if (dto.getPassword().equals(dto.getRepassword())) {
            //auth.setActivationCode(generateCode());
            auth.setPassword(passwordEncoder.encode(dto.getPassword()));
            save(auth);
            registerProducer.sendNewUser(IAuthMapper.INSTANCE.fromAuthToRegisterModel(auth));
            //activation link
            String token = jwtTokenProvider.createToken(auth.getId(), auth.getRoles()).get();
            registerMailProducer.sendActivationCode(RegisterMailModel.builder()
                    .name(auth.getName())
                    .surname(auth.getSurname())
                    .email(auth.getEmail())
                    .token(token).build());
        } else {
            throw new AuthManagerException(ErrorType.PASSWORD_ERROR);
        }
        //RegisterResponseDto responseDto = IAuthMapper.INSTANCE.fromAuthToResponseDto(auth);
        return auth;
    }

    public String login(LoginRequestDto dto) {
        Optional<Auth> auth = authRepository.findOptionalByEmail(dto.getEmail());
        if (auth.isEmpty() || !passwordEncoder.matches(dto.getPassword(), auth.get().getPassword())) {
            throw new AuthManagerException(ErrorType.LOGIN_ERROR);
        } else if (!auth.get().getStatus().equals(EStatus.ACTIVE)) {
            throw new AuthManagerException(ErrorType.ACTIVATE_CODE_ERROR);
        }
        return jwtTokenProvider.createToken(auth.get().getId(), auth.get().getRoles())
                .orElseThrow(() -> {
                    throw new AuthManagerException(ErrorType.TOKEN_NOT_CREATED);
                });
    }

    //OpenFeign Method-- From UserProfile
    public Boolean changePassword(FromUserProfilePasswordChangeRequestDto dto) {
        Optional<Auth> auth = authRepository.findById(dto.getAuthId());
        if (auth.isEmpty()) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        auth.get().setPassword(dto.getPassword());
        authRepository.save(auth.get());
        return true;
    }

    public Boolean forgotPasswordSendMailToken(String email) {
        Optional<Auth> auth = authRepository.findOptionalByEmail(email);
        if (auth.get().getStatus().equals(EStatus.ACTIVE)) {
            if (auth.get().getEmail().equals(email)) {
                String token = jwtTokenProvider.createToken(auth.get().getId(), auth.get().getRoles()).get();
                ForgotPasswordToMailSendLinkRequestDto forgotPassword = ForgotPasswordToMailSendLinkRequestDto.builder()
                        .email(email)
                        .token(token).build();
                emailManager.forgotPasswordMail(forgotPassword);
                return true;
            } else {
                throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
            }
        } else {
            if (auth.get().getStatus().equals(EStatus.DELETED)) {
                throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
            }
            throw new AuthManagerException(ErrorType.ACTIVATE_CODE_ERROR);
        }
    }

    //reset password
    public String forgotPassword(){
        //TODO: sayfa tasarımı yapıldıktan sonra şifre yenileme işlemi yapılacak
        return "Şifre Değiştirme Sayfası";
    }

    public Boolean activateStatus(String token) {
        System.out.println("Activate status service metoduna gelen token: " + token);
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isPresent()) {
            Optional<Auth> auth = findById(authId.get());
            auth.get().setStatus(EStatus.ACTIVE);
            update(auth.get());
            userManager.activateStatus("Bearer " + token);
            return true;
        }
        throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
    }

    public Boolean update(UpdateEmailOrUsernameRequestDto dto) {
        Optional<Auth> auth = authRepository.findById(dto.getAuthId());
        if (auth.isEmpty()) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        IAuthMapper.INSTANCE.updateUsernameOrEmail(dto, auth.get());
        update(auth.get());
        return true;
    }

    public Boolean delete(String email) {
        Optional<Auth> auth = authRepository.findOptionalByEmail(email);
        if (auth.isEmpty()) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        auth.get().setStatus(EStatus.DELETED);
        update(auth.get());
        userManager.delete(auth.get().getId());
        return true;
    }

    //user, admin  --> USER, ADMIN
    public List<Long> findByRole(String role) {
        return authRepository.findByRoles(role).stream().map(x -> x.getId()).collect(Collectors.toList());
    }
}
