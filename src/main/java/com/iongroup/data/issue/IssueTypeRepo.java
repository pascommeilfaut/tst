package com.iongroup.data.issue;

import com.iongroup.data.issue.type.IssueTypeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IssueTypeRepo extends JpaRepository<IssueEntity, Integer> {

    @Query("""
            select i from IssueTypeEntity i
            left join fetch i.parent
        """)
    Page<IssueTypeEntity> findAllFetched(Pageable pageable);

    @Query("""
            select i from IssueTypeEntity i
            where i.parent is null
        """)
    Page<IssueTypeEntity> findAllParents(Pageable pageable);

    @Query("""
            select i from IssueTypeEntity i
            where i.parent is not null
        """)
    Page<IssueTypeEntity> findAllSubTypes(Pageable pageable);

}