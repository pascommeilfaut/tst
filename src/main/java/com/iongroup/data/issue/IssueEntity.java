package com.iongroup.data.issue;

import com.iongroup.data.BaseEntity;
import com.iongroup.data.issue.status.IssueStatusEntity;
import com.iongroup.data.issue.type.IssueTypeEntity;
import com.iongroup.data.pos.PosEntity;
import com.iongroup.data.user.UserEntity;
import com.iongroup.data.user.type.UserTypeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "issues")
@Setter
@Getter
@NoArgsConstructor
public class IssueEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_pos", nullable = false)
    private PosEntity pos;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_type", nullable = false)
    private IssueTypeEntity type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_sub_type", nullable = false)
    private IssueTypeEntity subType;

    @Column(length = 255)
    private String problem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IssuePriority priority;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_status", nullable = false)
    private IssueStatusEntity status;

    @Column(length = 255)
    private String memo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_user_created", nullable = false, updatable = false)
    private UserEntity createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_assigned")
    private UserTypeEntity assignedTo;

    @Column(length = 10240)
    private String description;

    @Column(name = "assigned_date")
    private LocalDateTime assignedAt;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "modif_date", insertable = false)
    private LocalDateTime modifiedAt;

    @Column(length = 10240)
    private String solution;
}
