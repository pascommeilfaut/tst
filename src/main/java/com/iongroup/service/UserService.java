package com.iongroup.service;

import com.iongroup.data.user.UserEntity;
import com.iongroup.data.user.UserRepo;
import com.iongroup.service.dto.SaveUserDto;
import com.iongroup.service.mapper.UserMapper;
import com.iongroup.util.ValidationUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepo repo;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @NonNull
    public UserEntity create(@NonNull SaveUserDto userParams) {
        ValidationUtils.validate(userParams);

        String rawPassword = userParams.getRawPassword();
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Password must not be null or blank");
        }

        UserEntity userEntity = userMapper.mapToEntityFromSaveDto(userParams);
        userEntity.setPassword(passwordEncoder.encode(rawPassword));

        return repo.save(userEntity);
    }

    @Transactional
    @NonNull
    public UserEntity update(@NonNull SaveUserDto userParams, @NonNull Integer id) {
        ValidationUtils.validate(userParams);

        UserEntity user = findById(id).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("User with id %s does not exist".formatted(id));
        }

        UserEntity updatedUser = userMapper.mapToEntityFromSaveDto(userParams);
        updatedUser.setId(user.getId());
        String rawNewPassword = userParams.getRawPassword();
        String newPassword = (rawNewPassword == null || rawNewPassword.isBlank())
                ? user.getPassword()
                : passwordEncoder.encode(rawNewPassword);
        updatedUser.setPassword(newPassword);

        return repo.save(updatedUser);
    }

    @NonNull
    public Optional<UserEntity> findById(@NonNull Integer id) {
        return repo.findById(id);
    }

    @Transactional(readOnly = true)
    @NonNull
    public Optional<SaveUserDto> findSaveUserDtoById(@NonNull Integer id) {
        UserEntity entity = findById(id).orElse(null);
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(userMapper.mapToSaveDto(entity));
    }

    @NonNull
    public Optional<UserEntity> findByLogin(@NonNull String login) {
        return repo.findByLogin(login);
    }

    @NonNull
    public Page<UserEntity> findByFilter(String searchTerm, Pageable pageable) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return repo.findAllFetched(pageable);
        } else {
            return repo.search(searchTerm, pageable);
        }
    }

    @NonNull
    public Page<UserEntity> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

}
