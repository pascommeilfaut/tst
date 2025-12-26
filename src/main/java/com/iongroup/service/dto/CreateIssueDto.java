package com.iongroup.service.dto;

import com.iongroup.data.issue.IssuePriority;
import com.iongroup.data.issue.status.IssueStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateIssueDto {

    @NotNull private Integer posId;
    @NotNull private Integer issueTypeId;
    @NotNull private Integer subTypeId;
    @Size(max = 255) private String problem;
    @NotNull private IssuePriority priority;
    @NotNull private IssueStatus status;
    @Size (max = 10240) private String problemDescription;
    @Size (max = 10240) private String solution;
    private Integer assignedTo;
    @NotNull private Integer createdBy;
    @Size(max = 255) private String memo;

}
