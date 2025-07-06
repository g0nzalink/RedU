package com.example.backendredu.usuario.domain;

import com.example.backendredu.usuario.dto.ChatDto;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupabaseService {

    private final RestTemplate restTemplate;
    private final UsuarioRepository usuarioRepository;

    @Value("${SUPABASE_SERVICE_ROLE_KEY}")
    private String supabaseServiceKey;

    private static final String SUPABASE_URL = "https://qfqcubmkxczcbuadlkmt.supabase.co/rest/v1/chats";

    public List<ChatDto> obtenerChatsDelUsuario(String userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseServiceKey);
        headers.set("Authorization", "Bearer " + supabaseServiceKey);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            // 1. Obtener los chat_id donde el usuario participa
            String chatUsersUrl = "https://qfqcubmkxczcbuadlkmt.supabase.co/rest/v1/chat_users?select=chat_id&user_id=eq." + userId;
            ResponseEntity<Map[]> response = restTemplate.exchange(chatUsersUrl, HttpMethod.GET, request, Map[].class);

            if (response.getBody() == null || response.getBody().length == 0) {
                return List.of();
            }

            List<String> chatIds = Arrays.stream(response.getBody())
                    .map(entry -> (String) entry.get("chat_id"))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (chatIds.isEmpty()) {
                return List.of();
            }

            // 2. Obtener información de los chats (CORREGIDO)
            String chatsUrl = SUPABASE_URL + "?select=id,name,created_at&id=in.(" + String.join(",", chatIds) + ")";
            ResponseEntity<ChatDto[]> chatResponse = restTemplate.exchange(chatsUrl, HttpMethod.GET, request, ChatDto[].class);

            if (chatResponse.getBody() == null) {
                return List.of();
            }

            return Arrays.asList(chatResponse.getBody());

        } catch (Exception e) {
            System.err.println("❌ Error al obtener chats del usuario: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }


    public ChatDto buscarOCrearChatDirecto(String user1, String user2) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseServiceKey);
        headers.set("Authorization", "Bearer " + supabaseServiceKey);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        // 1. Buscar si ya existe un chat entre ambos usuarios
        String filter = String.format("user_id=in.(\"%s\",\"%s\")", user1, user2);
        String chatUsersUrl = "https://qfqcubmkxczcbuadlkmt.supabase.co/rest/v1/chat_users?select=chat_id,user_id&" + filter;

        ResponseEntity<Map[]> response = restTemplate.exchange(chatUsersUrl, HttpMethod.GET, request, Map[].class);
        Map[] body = response.getBody();

        if (body != null && body.length > 0) {
            Map<String, List<String>> chatMap = new HashMap<>();

            for (Map item : body) {
                String chatId = (String) item.get("chat_id");
                String uid = (String) item.get("user_id");
                chatMap.computeIfAbsent(chatId, k -> new ArrayList<>()).add(uid);
            }

            for (Map.Entry<String, List<String>> entry : chatMap.entrySet()) {
                List<String> users = entry.getValue();
                if (users.contains(user1) && users.contains(user2) && users.size() == 2) {
                    // Chat ya existe
                    String chatId = entry.getKey();
                    String chatInfoUrl = SUPABASE_URL + "?select=id,name,created_at&id=eq." + chatId;
                    ResponseEntity<ChatDto[]> chatResponse = restTemplate.exchange(chatInfoUrl, HttpMethod.GET, request, ChatDto[].class);
                    ChatDto[] existingChats = chatResponse.getBody();

                    if (existingChats != null && existingChats.length > 0) {
                        return existingChats[0];
                    }
                }
            }
        }

        // 2. Crear nuevo chat con el nombre del receptor
        Usuario receptor = usuarioRepository.findById(user2)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario receptor no encontrado"));

        String nombreDelChat = receptor.getUsername(); // Mostrar el nombre del otro usuario como título

        Map<String, Object> newChat = new HashMap<>();
        newChat.put("name", nombreDelChat);
        newChat.put("owner_id", user1);

        // ⚠️ AGREGADO: Necesario para que Supabase devuelva datos tras el POST
        headers.set("Prefer", "return=representation");

        HttpEntity<Map<String, Object>> createChatRequest = new HttpEntity<>(newChat, headers);
        ResponseEntity<ChatDto[]> newChatResponse = restTemplate.exchange(
                SUPABASE_URL + "?select=id,name,created_at",
                HttpMethod.POST,
                createChatRequest,
                ChatDto[].class
        );

        ChatDto[] createdChats = newChatResponse.getBody();

        if (createdChats == null || createdChats.length == 0) {
            throw new RuntimeException("Error al crear el chat directo: Supabase no devolvió datos");
        }

        ChatDto createdChat = createdChats[0];

        // 3. Insertar usuarios en tabla chat_users
        String chatUsersInsertUrl = "https://qfqcubmkxczcbuadlkmt.supabase.co/rest/v1/chat_users";
        List<Map<String, Object>> chatUsers = List.of(
                Map.of("chat_id", createdChat.getId(), "user_id", user1),
                Map.of("chat_id", createdChat.getId(), "user_id", user2)
        );

        HttpEntity<List<Map<String, Object>>> insertUsersRequest = new HttpEntity<>(chatUsers, headers);
        restTemplate.postForEntity(chatUsersInsertUrl, insertUsersRequest, String.class);

        return createdChat;
    }
}
