package com.iongroup.service.mapper;

import com.iongroup.data.issue.IssueEntity;
import com.iongroup.data.issue.status.IssueStatus;
import com.iongroup.data.issue.status.IssueStatusEntity;
import com.iongroup.data.user.UserEntity;
import com.iongroup.data.user.UserType;
import com.iongroup.data.user.type.UserTypeEntity;
import com.iongroup.service.dto.CreateIssueDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public abstract class IssueMapper {

    @PersistenceContext
    protected EntityManager entityManager;

    @Mapping(source = "posId", target = "pos.id")
    @Mapping(source = "issueTypeId", target = "type.id")
    @Mapping(source = "subTypeId", target = "subType.id")
    @Mapping(source = "problemDescription", target = "description")
    @Mapping(source = "status", target = "status", qualifiedByName = "mapToIssueStatusEntity")
    @Mapping(source = "assignedTo", target = "assignedTo", qualifiedByName = "mapToUserTypeEntity")
    @Mapping(source = "createdBy", target = "createdBy", qualifiedByName = "mapToUserEntity")
    public abstract IssueEntity mapToEntity(CreateIssueDto dto);

    @Mapping(source = "pos.id", target = "posId")
    @Mapping(source = "type.id", target = "issueTypeId")
    @Mapping(source = "subType.id", target = "subTypeId")
    @Mapping(source = "description", target = "problemDescription")
    @Mapping(source = "status.value.id", target = "status")
    @Mapping(source = "assignedTo.value.id", target = "assignedTo")
    @Mapping(source = "createdBy.id", target = "createdBy")
    public abstract CreateIssueDto mapToDto(IssueEntity entity);

    @Named("mapToUserEntity")
    protected UserEntity mapToUserEntity(Integer userId) {
        if (userId == null) {
            return null;
        }
        return entityManager.getReference(UserEntity.class, userId);
    }

    @Named("mapToUserTypeEntity")
    protected UserTypeEntity mapToUserTypeEntity(Integer userTypeId) {
        if (userTypeId == null) {
            return null;
        }
        return entityManager.getReference(UserTypeEntity.class, userTypeId);
    }

    @Named("mapToIssueStatusEntity")
    protected IssueStatusEntity mapToIssueStatusEntity(IssueStatus num) {
        if (num == null) {
            return null;
        }
        return entityManager.getReference(IssueStatusEntity.class, num.id);
    }

    @Named("mapToUserTypeEntity")
    protected UserTypeEntity mapToUserTypeEntity(UserType num) {
        if (num == null) {
            return null;
        }
        return entityManager.getReference(UserTypeEntity.class, num.id);
    }

    protected IssueStatus mapToIssueStatus(Integer id) {
        if (id == null) {
            return null;
        }

        for (IssueStatus status : IssueStatus.values()) {
            if (status.id == id) {
                return status;
            }
        }

        throw new IllegalArgumentException("Unknown IssueStatus id: " + id);
    }

}
