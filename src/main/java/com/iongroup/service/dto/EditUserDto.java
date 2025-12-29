package com.iongroup.service.dto;

import com.iongroup.data.user.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditUserDto {

    @NotNull private Integer id;
    @NotBlank private String name;
    @Email private String email;
    @NotBlank private String login;
    private String rawPassword;
    private String telephone;
    @NotNull private UserType type;

}