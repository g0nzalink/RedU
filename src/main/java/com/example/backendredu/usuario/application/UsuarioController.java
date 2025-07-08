package com.example.backendredu.usuario.application;

import com.example.backendredu.cloudinary.CloudinaryService;
import com.example.backendredu.usuario.domain.SupabaseService;
import com.example.backendredu.usuario.domain.Usuario;
import com.example.backendredu.usuario.domain.UsuarioService;
import com.example.backendredu.usuario.dto.ChatDto;
import com.example.backendredu.usuario.dto.DirectChatRequest;
import com.example.backendredu.usuario.dto.UsuarioResponseDto;
import com.example.backendredu.usuario.dto.UsuarioUpdateDto;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UsuarioController {
    
    private final CloudinaryService cloudinaryService;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final ModelMapper modelMapper;
    private final SupabaseService supabaseService;
    
    @PostMapping("usuario/{email}/profile-photo")
    @PreAuthorize("hasAnyRole('ALUMNO', 'PROFESOR', 'DIRECTIVA', 'ADMINISTRADOR')")
    public ResponseEntity<String> uploadProfilePhoto(
            @PathVariable String email,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (!email.equals(userDetails.getUsername())) { return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No puedes cambiar la foto de otro usuario."); }
            String url = cloudinaryService.uploadImage(file, "usuarios", email);
            Usuario usuario = usuarioRepository.findById(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Nombre de usuario no encontrado."));
            usuario.setFotoPerfilUrl(url);
            usuarioRepository.save(usuario);
            return ResponseEntity.ok(url);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al subir imagen de perfil: " + e.getMessage());
        }
    }
    
    @PatchMapping("usuario/{email}/update")
    public ResponseEntity<UsuarioResponseDto> actualizarUsuario(
            @PathVariable String email,
            @RequestBody UsuarioUpdateDto dto
    ) {
        Usuario actualizado = usuarioService.updateUsuario(email, dto);
        UsuarioResponseDto response = modelMapper.map(actualizado, UsuarioResponseDto.class);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/usuario")
    public List<UsuarioResponseDto> getAllUsers() {
        return usuarioService.getAll();
    }

    @GetMapping("/chat")
    @PreAuthorize("hasAnyRole('ALUMNO', 'PROFESOR', 'DIRECTIVA', 'ADMINISTRADOR')")
    public ResponseEntity<?> obtenerChats(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        List<ChatDto> chats = supabaseService.obtenerChatsDelUsuario(username);
        return ResponseEntity.ok(chats);
    }

    @PostMapping("/chat/direct")
    @PreAuthorize("hasAnyRole('ALUMNO','PROFESOR','DIRECTIVA','ADMINISTRADOR')")
    public ResponseEntity<?> crearChatDirecto(
            @RequestBody DirectChatRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        System.out.println("✅ Entré a crearChatDirecto");
        String actual = userDetails.getUsername();
        String receptor = request.getReceiverUsername();
        ChatDto chat = supabaseService.buscarOCrearChatDirecto(actual, receptor);
        return ResponseEntity.ok(chat);

    }
}
