package com.iongroup.data.issue;

import com.iongroup.data.issue.status.IssueStatus;
import com.iongroup.data.issue.status.IssueStatusEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface IssueRepo extends JpaRepository<IssueEntity, Integer> {

    @Query("""
            select i from IssueEntity i
            join fetch i.type
            left join fetch i.assignedTo
            left join fetch i.createdBy
            join fetch i.pos
            join fetch i.status
            join fetch i.subType
        """)
    Page<IssueEntity> findAllFetched(Pageable pageable);

    @Query("""
            select i from IssueEntity i
            join fetch i.type
            where i.type.parent is null
        """)
    Page<IssueEntity> findAllParents(Pageable pageable);

    @Query("""
            select i from IssueEntity i
            join fetch i.type
            join fetch i.type.parent
        """)
    Page<IssueEntity> findAllSubTypes(Pageable pageable);

    @Query("""
            select i from IssueEntity i
            join fetch i.type
            left join fetch i.assignedTo
            left join fetch i.createdBy
            join fetch i.pos
            join fetch i.status
            join fetch i.subType
            where lower(i.pos.name) like lower(concat('%', :searchTerm, '%'))
                or lower(i.type.name) like lower(concat('%', :searchTerm, '%'))
                or lower(i.status.value) like lower(concat('%', :searchTerm, '%'))
                or lower(i.assignedTo.value) like lower(concat('%', :searchTerm, '%'))
                or lower(i.memo) like lower(concat('%', :searchTerm, '%'))
            """)
    Page<IssueEntity> search(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("""
            select i from IssueEntity i
            join fetch i.type
            left join fetch i.assignedTo
            left join fetch i.createdBy
            join fetch i.pos
            join fetch i.status
            join fetch i.subType
            where i.status.value = :status
    """)
    Page<IssueEntity> findIssueEntitiesByStatus(IssueStatus status, Pageable pageable);

    @Query("select i.status, count(i) from IssueEntity i group by i.status")
    List<Object[]> countIssuesByStatusRaw();

    default Map<IssueStatus, Long> countIssuesByStatus() {
        return countIssuesByStatusRaw().stream()
                .collect(Collectors.toMap(
                        row -> ((IssueStatusEntity) row[0]).getValue(),
                        row -> (Long) row[1]
                ));
    }

    @Query("select count(i) from IssueEntity i where i.pos.id = :posId")
    Long countIssuesByPosId(Integer posId);

}
