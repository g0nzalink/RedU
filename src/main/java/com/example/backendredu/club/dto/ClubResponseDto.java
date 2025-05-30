package com.example.backendredu.club.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClubResponseDto {

    private String email;
    private String nombre;
    private String descripcion;

}
