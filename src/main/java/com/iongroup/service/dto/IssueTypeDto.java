package com.iongroup.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Getter
@Setter
public class IssueTypeDto {
    @NotNull private Integer id;
    private IssueTypeDto parent;
    @NotNull private Integer level;
    @NotBlank @Length(max = 100) private String name;
    @NotNull private LocalDateTime createdAt;
}
