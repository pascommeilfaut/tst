package com.iongroup.data.issue.log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IssueLogRepo extends JpaRepository<IssueLogEntity, Integer> {

    @Query("select l from IssueLogEntity l join fetch l.actor where l.issue.id = :issueId order by l.dateTime desc")
    List<IssueLogEntity> findByIssueId(@Param("issueId") Integer issueId);

}