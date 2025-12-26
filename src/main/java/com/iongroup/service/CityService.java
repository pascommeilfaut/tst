package com.iongroup.service;

import com.iongroup.data.city.CityEntity;
import com.iongroup.data.city.CityRepo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CityService {

    private final CityRepo repo;

    @NonNull
    public Page<CityEntity> findAll(@NonNull Pageable pageable) {
        return repo.findAll(pageable);
    }

    @NonNull
    public Optional<CityEntity> findById(@NonNull Integer id) {
        return repo.findById(id);
    }
}
