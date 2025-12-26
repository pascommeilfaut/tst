package com.iongroup.data.issue.log;

import com.iongroup.data.BaseEntity;
import com.iongroup.data.issue.IssueEntity;
import com.iongroup.data.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
@Setter
@Getter
@NoArgsConstructor
public class IssueLogEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "issue_id", nullable = false, updatable = false)
    private IssueEntity issue;

    @Column(name = "date_time", nullable = false, updatable = false)
    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Action action;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private UserEntity actor;

    @Column(length = 10240)
    private String notes;
}
