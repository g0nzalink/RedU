package com.example.backendredu.usuario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private String id;
    private String chat_id;
    private String user_id;
    private String content;
    private String inserted_at;
}