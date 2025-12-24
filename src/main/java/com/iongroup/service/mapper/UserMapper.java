package com.iongroup.service.mapper;

import com.iongroup.data.user.UserEntity;
import com.iongroup.data.user.UserType;
import com.iongroup.data.user.type.UserTypeEntity;
import com.iongroup.service.dto.UserDto;
import com.iongroup.service.dto.SaveUserDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @PersistenceContext
    protected EntityManager entityManager;

    @Mapping(source = "type", target = "type.value")
    public abstract UserEntity mapToEntity(UserDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(source = "rawPassword", target = "password", ignore = true)
    @Mapping(source = "type", target = "type", qualifiedByName = "mapToUserTypeEntity")
    public abstract UserEntity mapToEntityFromSaveDto(SaveUserDto dto);

    @Mapping(source = "type.value", target = "type")
    public abstract UserDto mapToDto(UserEntity entity);

    @Mapping(source = "password", target = "rawPassword", ignore = true)
    @Mapping(source = "type.value", target = "type")
    public abstract SaveUserDto mapToSaveDto(UserEntity entity);

    @Named("mapToUserTypeEntity")
    protected UserTypeEntity mapToUserTypeEntity(UserType userType) {
        if (userType == null) {
            return null;
        }
        return entityManager.getReference(UserTypeEntity.class, userType.id);
    }

}
