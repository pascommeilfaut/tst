package com.iongroup.service.dto;

import com.iongroup.data.issue.IssuePriority;
import com.iongroup.data.issue.status.IssueStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
public class CreateIssueDto {

    @NotNull private Integer posId;
    @NotNull private Integer issueTypeId;
    @NotNull private Integer subTypeId;
    @Length(max = 255) private String problem;
    @NotNull private IssuePriority priority;
    @NotNull private IssueStatus status;
    @Length (max = 10240) private String problemDescription;
    @Length (max = 10240) private String solution;
    private Integer assignedTo;
    @NotNull private Integer createdBy;
    @Length(max = 255) private String memo;

}
