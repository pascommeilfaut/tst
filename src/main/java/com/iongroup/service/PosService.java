package com.iongroup.service;

import com.iongroup.data.pos.PosEntity;
import com.iongroup.data.pos.PosRepo;
import com.iongroup.service.dto.SavePosDto;
import com.iongroup.service.mapper.PosMapper;
import com.iongroup.util.TimeUtils;
import com.iongroup.util.ValidationUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PosService {

    private final PosRepo repo;
    private final PosMapper savePosDtoMapper;

    @NonNull
    public PosEntity create(@NonNull SavePosDto posParams) {
        ValidationUtils.validate(posParams);

        PosEntity pos = savePosDtoMapper.mapToEntity(posParams);
        pos.setCreatedAt(TimeUtils.now());

        return repo.save(pos);
    }

    @Transactional
    @NonNull
    public PosEntity update(@NonNull SavePosDto posParams, @NonNull Integer id) {
        ValidationUtils.validate(posParams);

        PosEntity pos = findById(id).orElseThrow(
                () -> new IllegalArgumentException("POS with id %s does not exist".formatted(id)));
        PosEntity updatedPos = savePosDtoMapper.mapToEntity(posParams);
        updatedPos.setId(pos.getId());
        updatedPos.setCreatedAt(pos.getCreatedAt());

        return repo.save(updatedPos);
    }

    @NonNull
    public Optional<PosEntity> findById(@NonNull Integer id) {
        return repo.findById(id);
    }

    @Transactional(readOnly = true)
    @NonNull
    public Optional<SavePosDto> findSavePosDtoById(@NonNull Integer id) {
        PosEntity entity = findById(id).orElse(null);
        if (entity == null) {
            return Optional.empty();
        }

        return Optional.of(savePosDtoMapper.mapToDto(entity));
    }

    @NonNull
    public Page<PosEntity> findByFilter(String searchTerm, Pageable pageable) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return repo.emptySearch(pageable);
        } else {
            return repo.search(searchTerm, pageable);
        }
    }
}
