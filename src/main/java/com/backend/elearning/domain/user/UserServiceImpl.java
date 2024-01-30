package com.backend.elearning.domain.user;

import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.domain.media.MediaService;
import com.backend.elearning.exception.DuplicateException;
import com.backend.elearning.exception.NotFoundException;
import com.backend.elearning.utils.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final MediaService mediaService;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, MediaService mediaService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mediaService = mediaService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public PageableData<UserVm> getUsers(int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<User> userPage = userRepository.findAll(pageable);
        List<User> users = userPage.getContent();
        List<UserVm> userVms = users.stream().map(user -> {
            String urlPhoto = mediaService.getUrlById(user.getPhotoId());
            return UserVm.fromModel(user, urlPhoto);
        }).toList();
        return new PageableData<>(
                pageNum,
                pageSize,
                (int) userPage.getTotalElements(),
                userPage.getTotalPages(),
                userVms
        );
    }

    @Override
    public UserGetDetailVm getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.USER_EMAIL_DUPLICATED, userId));
        String urlPhoto = mediaService.getUrlById(user.getPhotoId());
        return new UserGetDetailVm(
                userId,
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getGender().name(),
                user.isActive(),
                urlPhoto,
                user.getDateOfBirth().toString(),
                user.getRole().name()
        );
    }

    @Override
    @Transactional
    public UserVm create(UserPostVm userPostVm) {
        if (userRepository.countByExistedEmail(userPostVm.email(), null) > 0) {
            throw new DuplicateException(Constants.ERROR_CODE.USER_EMAIL_DUPLICATED, userPostVm.email());
        }
        User user = User.builder()
                .email(userPostVm.email())
                .firstName(userPostVm.firstName())
                .lastName(userPostVm.lastName())
                .active(userPostVm.active())
                .password(passwordEncoder.encode(userPostVm.password()))
                .dateOfBirth(LocalDate.of(userPostVm.year(), userPostVm.month(), userPostVm.day()))
                .photoId(userPostVm.photoId())
                .role(ERole.valueOf(userPostVm.role()))
                .gender(EGender.valueOf(userPostVm.gender()))
                .build();
        User savedUser = userRepository.saveAndFlush(user);
        String urlPhoto = mediaService.getUrlById(savedUser.getPhotoId());
        return UserVm.fromModel(user, urlPhoto);
    }

    @Override
    @Transactional
    public void update(UserPutVm userPutVm, Long userId) {
        if (userRepository.countByExistedEmail(userPutVm.email(), userId) > 0) {
            throw new DuplicateException(Constants.ERROR_CODE.USER_EMAIL_DUPLICATED, userPutVm.email());
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.USER_NOT_FOUND, userId));
        user.setEmail(userPutVm.email());
        user.setFirstName(userPutVm.firstName());
        user.setLastName(userPutVm.lastName());
        user.setActive(userPutVm.active());
        user.setRole(ERole.valueOf(userPutVm.role()));
        user.setDateOfBirth(LocalDate.of(userPutVm.year(), userPutVm.month(), userPutVm.day()));
        if (!userPutVm.photoId().isEmpty() && !userPutVm.photoId().isBlank()) {
            user.setPhotoId(userPutVm.photoId());
        }
        if (!userPutVm.password().isEmpty() && !userPutVm.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(userPutVm.password()));
        }
        // update
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(Long userId) {

    }

}
