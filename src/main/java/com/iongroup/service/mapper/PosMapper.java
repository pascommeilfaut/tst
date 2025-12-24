package com.iongroup.service.mapper;

import com.iongroup.data.city.CityEntity;
import com.iongroup.data.pos.PosEntity;
import com.iongroup.data.pos.connection.ConnectionType;
import com.iongroup.data.pos.connection.ConnectionTypeEntity;
import com.iongroup.service.dto.SavePosDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public abstract class PosMapper {

    @PersistenceContext
    protected EntityManager em;

    @Mapping(source = "cityId", target = "city", qualifiedByName = "mapToCityEntity")
    public abstract PosEntity mapToEntity(SavePosDto dto);

    @Mapping(source = "connectionType.value", target = "connectionType")
    @Mapping(source = "city.id", target = "cityId")
    public abstract SavePosDto mapToDto(PosEntity entity);

    protected ConnectionTypeEntity mapToConnectionTypeEntity(ConnectionType num) {
        if (num == null) {
            return null;
        }
        return em.getReference(ConnectionTypeEntity.class, num.getId());
    }

    @Named("mapToCityEntity")
    protected CityEntity mapToCityEntity(Integer cityId) {
        if (cityId == null) {
            return null;
        }
        return em.getReference(CityEntity.class, cityId);
    }

}
