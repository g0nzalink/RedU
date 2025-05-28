package com.example.backendredu.club.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClubRequestDto {

    @NotBlank
    private String email;

    @NotBlank
    private String name;

    private String description;
}
