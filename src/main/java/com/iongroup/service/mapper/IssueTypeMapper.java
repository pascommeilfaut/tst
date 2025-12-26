package com.iongroup.service.mapper;

import com.iongroup.data.issue.type.IssueTypeEntity;
import com.iongroup.service.dto.IssueTypeDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class IssueTypeMapper {

    public abstract IssueTypeEntity mapToEntity(IssueTypeDto dto);
    public abstract IssueTypeDto mapToDto(IssueTypeEntity entity);

}
