package com.iongroup.service;

import com.iongroup.data.issue.IssueEntity;
import com.iongroup.data.issue.IssueRepo;
import com.iongroup.data.issue.log.Action;
import com.iongroup.data.issue.log.IssueLogEntity;
import com.iongroup.data.issue.log.IssueLogRepo;
import com.iongroup.data.issue.status.IssueStatus;
import com.iongroup.service.dto.CreateIssueDto;
import com.iongroup.service.dto.UpdateIssueDto;
import com.iongroup.service.mapper.IssueMapper;
import com.iongroup.util.AuthUtils;
import com.iongroup.util.TimeUtils;
import com.iongroup.util.ValidationUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IssueService {

    private final IssueRepo repo;
    private final IssueLogRepo logRepo;
    private final IssueMapper issueMapper;

    @NonNull
    @Transactional
    public IssueEntity create(@NonNull CreateIssueDto issueParams) {
        ValidationUtils.validate(issueParams);

        IssueEntity issueEntity = issueMapper.mapToEntity(issueParams);

        issueEntity.setCreatedAt(TimeUtils.now());

        if (issueParams.getAssignedTo() != null) {
            issueEntity.setAssignedAt(TimeUtils.now());
        }

        IssueEntity savedIssue = repo.save(issueEntity);

        IssueLogEntity log = new IssueLogEntity();
        log.setIssue(savedIssue);
        log.setDateTime(TimeUtils.now());
        log.setAction(Action.CREATED);
        log.setActor(AuthUtils.getCurrentUser());
        log.setNotes("Issue created");
        logRepo.save(log);

        return savedIssue;
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
    public Optional<IssueEntity> findById(@NonNull Integer id) {
        return repo.findById(id);
    }

    @NonNull
    public List<IssueLogEntity> findLogs(@NonNull Integer issueId) {
        return logRepo.findByIssueId(issueId);
    }

    @Transactional
    @NonNull
    public IssueEntity update(@NonNull UpdateIssueDto issueParams, @NonNull Integer id) {
        ValidationUtils.validate(issueParams);

        IssueEntity issue = repo.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Issue with id %s does not exist".formatted(id)));

        IssueEntity updatedIssue = issueMapper.mapToEntity(issueParams);

        StringBuilder notes = new StringBuilder();

        if (issue.getStatus() != null && issueParams.getStatus() != null &&
                issue.getStatus().getValue() != issueParams.getStatus()) {
            notes.append("Status changed: ")
                    .append(issue.getStatus().getValue().getDisplayName())
                    .append(" -> ")
                    .append(issueParams.getStatus().getDisplayName())
                    .append(". ");
        }

        Integer oldAssignedTo = issue.getAssignedTo() != null ? issue.getAssignedTo().getId() : null;
        Integer newAssignedTo = issueParams.getAssignedTo();
        if (!Objects.equals(oldAssignedTo, newAssignedTo)) {
            notes.append("Assignment changed. ");
        }

        if (notes.isEmpty()) {
            notes.append("Details updated.");
        }

        updatedIssue.setId(issue.getId());
        updatedIssue.setCreatedAt(issue.getCreatedAt());
        updatedIssue.setCreatedBy(issue.getCreatedBy());
        updatedIssue.setModifiedAt(TimeUtils.now());

        if (issue.getAssignedTo() == null && updatedIssue.getAssignedTo() != null) {
            updatedIssue.setAssignedAt(TimeUtils.now());
        }

        IssueLogEntity log = new IssueLogEntity();
        log.setIssue(issue);
        log.setDateTime(TimeUtils.now());
        log.setAction(Action.UPDATED);
        log.setActor(AuthUtils.getCurrentUser());
        log.setNotes(notes.toString().trim());
        logRepo.save(log);

        return repo.save(updatedIssue);
    }
}