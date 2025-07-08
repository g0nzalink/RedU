package com.example.backendredu.usuario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto {
    private String id;
    private String name;
    private String display_name;
    private String created_at;
}
