package com.example.backendredu.club.domain;

import com.example.backendredu.club.dto.ClubResponseDto;
import com.example.backendredu.club.infrastructure.ClubRepository;
import com.example.backendredu.pertenencia.infrastructure.PertenenciaRepository;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;

    private final ModelMapper modelMapper;

    private final PertenenciaRepository perteneceRepository;
    private final UsuarioRepository usuarioRepository;

    public ClubResponseDto getClub(String email){
        Club club = clubRepository.findById(email).orElseThrow(() -> new EntityNotFoundException("No existe un club con el correo " + email));
        return modelMapper.map(club, ClubResponseDto.class);
    }

    @Transactional
    public List<Club> allClubs() {
        return clubRepository.findAll().stream()
                .collect(Collectors.toList());
    }
}

