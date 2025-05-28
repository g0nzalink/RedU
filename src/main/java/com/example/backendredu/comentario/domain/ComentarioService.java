package com.example.backendredu.comentario.domain;

import com.example.backendredu.comentario.infrastructure.ComentarioRepository;
import com.example.backendredu.publicacion.domain.Publicacion;
import com.example.backendredu.comentario.dto.ComentarioRequestDto;
import com.example.backendredu.comentario.dto.ComentarioResponseDto;
import com.example.backendredu.publicacion.infrastructure.PublicacionRepository;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;

    private final PublicacionRepository publicacionRepository;

    private final UsuarioRepository userRepository;

    private final ModelMapper modelMapper;

    public ComentarioResponseDto crearComentario(ComentarioRequestDto newcomentario, Long publicacionId) {
        Publicacion pub = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new EntityNotFoundException("Publicación no encontrada"));

        // 1. Mapea y ajusta la publicación y la fecha
        Comentario comentario = modelMapper.map(newcomentario, Comentario.class);
        comentario.setPublicacion(pub);
        comentario.setFechaPublicacion(LocalDateTime.now());
        //falta implementar el guardar el autor

        // 2. Guarda SOLO el comentario
        Comentario saved = comentarioRepository.save(comentario);

        pub.getListComentario().add(saved);
        // 3. Devuelve el DTO mapeado del comentario guardado (con ID)
        return modelMapper.map(saved, ComentarioResponseDto.class);
    }

    @Transactional
    public List<ComentarioResponseDto> listarComentarios(Long publicacionId) {
        return comentarioRepository
                .findByPublicacionId(publicacionId)
                .stream()
                .map(c -> modelMapper.map(c, ComentarioResponseDto.class))
                .collect(Collectors.toList());
    }

}
