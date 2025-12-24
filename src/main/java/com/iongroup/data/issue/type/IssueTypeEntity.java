package com.iongroup.data.issue.type;

import com.iongroup.data.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "issue_types")
@Setter
@Getter
@NoArgsConstructor
public class IssueTypeEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_issue")
    private IssueTypeEntity parent;

    @Column(name = "issue_level", nullable = false)
    private Integer level;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "insert_date", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
