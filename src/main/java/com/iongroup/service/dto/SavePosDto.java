package com.iongroup.service.dto;

import com.iongroup.data.pos.connection.ConnectionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class SavePosDto {

    @NotBlank @Size(max = 100) private String name;
    @Size(max = 50) private String telephone;
    @Size(max = 50) private String cellphone;
    @NotBlank @Size(max = 255) private String address;
    @NotNull private Integer cityId;
    @NotBlank @Size(max = 100) private String model;
    @NotBlank @Size(max = 100) private String brand;
    private ConnectionType connectionType;
    private LocalTime morningOpening;
    private LocalTime morningClosing;
    private LocalTime afternoonOpening;
    private LocalTime afternoonClosing;
    @Size(max = 20) private String daysClosed;
}
