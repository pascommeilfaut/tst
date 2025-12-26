package com.iongroup.data.issue.status;

import com.iongroup.data.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "statuses")
@Setter
@Getter
@NoArgsConstructor
public class IssueStatusEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "status", unique = true, nullable = false, updatable = false)
    private IssueStatus value;
}
