package com.iongroup.service;

import com.iongroup.data.user.UserEntity;
import com.iongroup.data.user.UserRepo;
import com.iongroup.service.dto.EditUserDto;
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

        if (userParams.getRawPassword() == null || userParams.getRawPassword().isBlank()) {
            throw new IllegalArgumentException("Password must not be null or blank");
        }

        UserEntity userEntity = userMapper.mapToEntityFromSaveDto(userParams);
        userEntity.setPassword(passwordEncoder.encode(userParams.getRawPassword()));

        return repo.save(userEntity);
    }

    @Transactional
    @NonNull
    public UserEntity update(@NonNull EditUserDto userParams) {
        ValidationUtils.validate(userParams);

        UserEntity user = repo.findById(userParams.getId())
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userParams.getId() + " does not exist"));

        // Use Mapper to update fields (except password)
        userMapper.updateEntityFromEditDto(userParams, user);

        // Handle Password Update Separately
        if (userParams.getRawPassword() != null && !userParams.getRawPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userParams.getRawPassword()));
        }

        return repo.save(user);
    }

    @NonNull
    public Optional<UserEntity> findById(@NonNull Integer id) {
        return repo.findById(id);
    }

    @Transactional(readOnly = true)
    @NonNull
    public Optional<SaveUserDto> findSaveUserDtoById(@NonNull Integer id) {
        return repo.findById(id).map(userMapper::mapToSaveDto);
    }

    @Transactional(readOnly = true)
    @NonNull
    public Optional<EditUserDto> findEditUserDtoById(@NonNull Integer id) {
        return repo.findById(id).map(userMapper::mapToEditDto);
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