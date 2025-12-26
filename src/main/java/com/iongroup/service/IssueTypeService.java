package com.iongroup.service;

import com.iongroup.data.issue.IssueTypeRepo;
import com.iongroup.data.issue.type.IssueTypeEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IssueTypeService {

    private final IssueTypeRepo repo;

    @NonNull
    public Page<IssueTypeEntity> findAllFetched(Pageable pageable) {
        return repo.findAllFetched(pageable);
    }

    @NonNull
    public Page<IssueTypeEntity> findAllParents(Pageable pageable) {
        return repo.findAllParents(pageable);
    }

    @NonNull
    public Page<IssueTypeEntity> findAllSubTypes(Pageable pageable) {
        return repo.findAllSubTypes(pageable);
    }

}
