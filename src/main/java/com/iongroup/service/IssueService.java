package com.iongroup.service;

import com.iongroup.data.issue.IssueEntity;
import com.iongroup.data.issue.IssueRepo;
import com.iongroup.data.issue.status.IssueStatus;
import com.iongroup.service.dto.CreateIssueDto;
import com.iongroup.service.mapper.IssueMapper;
import com.iongroup.util.TimeUtils;
import com.iongroup.util.ValidationUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IssueService {

    private final IssueRepo repo;
    private final IssueMapper issueMapper;

    @NonNull
    public IssueEntity create(@NonNull CreateIssueDto issueParams) {
        ValidationUtils.validate(issueParams);

        IssueEntity issueEntity = issueMapper.mapToEntity(issueParams);

        issueEntity.setCreatedAt(TimeUtils.now());

        if (issueParams.getAssignedTo() != null) {
            issueEntity.setAssignedAt(TimeUtils.now());
        }

        return repo.save(issueEntity);
    }

    @NonNull
    public Page<IssueEntity> findByFilter(String searchTerm, Pageable pageable) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return repo.findAllFetched(pageable);
        } else {
            return repo.search(searchTerm, pageable);
        }
    }

    @NonNull
    public Page<IssueEntity> findByStatus(IssueStatus issueStatus, Pageable pageable) {
        if (issueStatus == null) {
            return repo.findAllFetched(pageable);
        } else {
            return repo.findIssueEntitiesByStatus(issueStatus, pageable);
        }
    }

    @NonNull
    public Page<IssueEntity> findAllParents(Pageable pageable) {
        return repo.findAllParents(pageable);
    }

    @NonNull
    public Page<IssueEntity> findAllSubTypes(Pageable pageable) {
        return repo.findAllSubTypes(pageable);
    }

    @NonNull
    public Map<IssueStatus, Long> countIssuesByStatus() {
        return repo.countIssuesByStatus();
    }

    @NonNull
    public Long countIssuesByPosId(Integer posId) {
        return repo.countIssuesByPosId(posId);
    }

    @Transactional(readOnly = true)
    @NonNull
    public Optional<CreateIssueDto> findSaveIssueDtoById(@NonNull Integer id) {
        IssueEntity entity = repo.findById(id).orElse(null);

        if (entity == null) {
            return Optional.empty();
        }

        return Optional.of(issueMapper.mapToDto(entity));
    }

    @Transactional
    @NonNull
    public IssueEntity update(@NonNull CreateIssueDto issueParams, @NonNull Integer id) {
        ValidationUtils.validate(issueParams);

        IssueEntity issue = repo.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Issue with id %s does not exist".formatted(id)));
        IssueEntity updatedIssue = issueMapper.mapToEntity(issueParams);
        updatedIssue.setId(issue.getId());
        updatedIssue.setCreatedAt(issue.getCreatedAt());
        updatedIssue.setModifiedAt(TimeUtils.now());

        if (issue.getAssignedTo() == null && updatedIssue.getAssignedTo() != null) {
            updatedIssue.setAssignedAt(TimeUtils.now());
        }

        return repo.save(updatedIssue);
    }

}
