package com.iongroup.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class IssueTypeDto {
    @NotNull private Integer id;
    private IssueTypeDto parent;
    @NotNull private Integer level;
    @NotBlank @Size(max = 100) private String name;
    @NotNull private LocalDateTime createdAt;
}
