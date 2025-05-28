package com.example.backendredu.club;

import com.example.backendredu.club.domain.Club;
import com.example.backendredu.club.domain.ClubService;
import com.example.backendredu.club.dto.ClubResponseDto;
import com.example.backendredu.club.infrastructure.ClubRepository;
import com.example.backendredu.pertenencia.infrastructure.PertenenciaRepository;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClubServiceTest {
	
	private ClubRepository clubRepository;
	private PertenenciaRepository pertenenciaRepository;
	private UsuarioRepository usuarioRepository;
	private ModelMapper modelMapper;
	private ClubService clubService;
	
	@BeforeEach
	void setUp() {
		clubRepository = mock(ClubRepository.class);
		pertenenciaRepository = mock(PertenenciaRepository.class);
		usuarioRepository = mock(UsuarioRepository.class);
		modelMapper = new ModelMapper();
		clubService = new ClubService(clubRepository, modelMapper, pertenenciaRepository, usuarioRepository);
	}
	
	@Test
	void getClub_shouldReturnMappedClub_whenClubExists() {
		Club club = new Club();
		club.setEmail("club1@utec.edu.pe");
		club.setNombre("Club de Ciencia");
		club.setDescripcion("Divulgación científica");
		
		when(clubRepository.findById("club1@utec.edu.pe")).thenReturn(Optional.of(club));
		ClubResponseDto result = clubService.getClub("club1@utec.edu.pe");
		
		assertThat(result)
				.isNotNull()
				.extracting("email", "nombre", "descripcion")
				.containsExactly("club1@utec.edu.pe", "Club de Ciencia", "Divulgación científica");
	}
	
	@Test
	void getClub_shouldThrowException_whenClubDoesNotExist() {
		when(clubRepository.findById("inexistente@utec.edu.pe")).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> clubService.getClub("inexistente@utec.edu.pe"))
				.isInstanceOf(EntityNotFoundException.class)
				.hasMessageContaining("No existe un club con el correo");
	}
	
	@Test
	void allClubs_shouldReturnListOfClubs() {
		Club c1 = new Club();
		c1.setEmail("club1@utec.edu.pe");
		
		Club c2 = new Club();
		c2.setEmail("club2@utec.edu.pe");
		
		when(clubRepository.findAll()).thenReturn(List.of(c1, c2));
		List<Club> result = clubService.allClubs();
		
		assertThat(result)
				.hasSize(2)
				.extracting(Club::getEmail)
				.containsExactly("club1@utec.edu.pe", "club2@utec.edu.pe");
	}
}