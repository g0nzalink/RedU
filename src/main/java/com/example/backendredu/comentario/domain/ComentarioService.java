package com.example.backendredu.comentario.domain;

import com.example.backendredu.comentario.infrastructure.ComentarioRepository;
import com.example.backendredu.publicacion.domain.Publicacion;
import com.example.backendredu.publicacion.infrastructure.PublicacionRepository;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComentarioService {

    private final
    ComentarioRepository comentarioRepository;

    private final
    PublicacionRepository publicacionRepository;

    private final
    UsuarioRepository userRepository;

    public Comentario crearComentario(Comentario comentario, Long publicacionId){
        Publicacion pub = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new EntityNotFoundException("Publicación no encontrada"));

        Comentario c = new Comentario();
        c.setPublicacion(pub);
        //c.setAutor(autor); falta implementar el como se obtiene el autor
        c.setContenido(comentario.getContenido());

        pub.getListComentario().add(c);

        publicacionRepository.save(pub);

        return c;
    }

    public List<Comentario> listarComentarios(Long publicacionId) {
        return comentarioRepository.findByPublicacionId(publicacionId);
    }


}
