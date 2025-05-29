package com.example.backendredu.club;

import com.example.backendredu.auth.JwtService;
import com.example.backendredu.club.application.ClubController;
import com.example.backendredu.club.domain.Club;
import com.example.backendredu.club.domain.ClubService;
import com.example.backendredu.club.dto.ClubResponseDto;
import com.example.backendredu.club.infrastructure.ClubRepository;
import com.example.backendredu.pertenencia.domain.Pertenencia;
import com.example.backendredu.pertenencia.domain.PertenenciaService;
import com.example.backendredu.pertenencia.infrastructure.PertenenciaRepository;
import com.example.backendredu.usuario.domain.Usuario;
import com.example.backendredu.usuario.dto.UsuarioResponseDto;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = ClubController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ClubControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ClubService clubService;
	
	@MockBean
	private PertenenciaService pertenenciaService;
	
	@MockBean
	private ModelMapper modelMapper;
	
	@MockBean
	private JwtService jwtService;
	
	@MockBean
	private UsuarioRepository usuarioRepository;
	
	@MockBean
	private ClubRepository clubRepository;
	
	@MockBean
	private PertenenciaRepository pertenenciaRepository;
	
	private Club club;
	private ClubResponseDto clubDto;
	
	@BeforeEach
	void setUp() {
		club = new Club();
		club.setEmail("club@example.com");
		club.setNombre("Club de Ciencia");
		club.setDescripcion("Exploración científica");
		
		clubDto = new ClubResponseDto("club@example.com", "Club de Ciencia", "Exploración científica");
	}
	
	@Test
	void testGetClub() throws Exception {
		when(clubService.getClub("club@example.com")).thenReturn(clubDto);
		
		mockMvc.perform(get("/club/club@example.com"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("club@example.com"))
				.andExpect(jsonPath("$.nombre").value("Club de Ciencia"));
	}
	
	@Test
	void testListarClubs() throws Exception {
		when(clubService.allClubs()).thenReturn(List.of(club));
		
		mockMvc.perform(get("/club"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].email").value("club@example.com"));
	}
	
	@Test
	@WithMockUser(roles = {"ALUMNO"})
	void testSeguirClub() throws Exception {
		Pertenencia pertenencia = new Pertenencia();
		pertenencia.setId(1L);
		
		when(pertenenciaService.crearPertenencia(anyString(), anyString()))
				.thenReturn(pertenencia);
		
		mockMvc.perform(post("/club/123/seguir")
						.principal(() -> "user@example.com"))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "http://localhost/club/1"));
	}
	
	@Test
	@WithMockUser(roles = {"ALUMNO"})
	void testDejarDeSeguirClub() throws Exception {
		doNothing().when(pertenenciaService).borrarPertenencia("user@example.com", "clubId");
		
		mockMvc.perform(delete("/club/clubId/dejarSeguir")
						.principal(() -> "user@example.com"))
				.andExpect(status().isNoContent());
	}
	
	@Test
	void testGetSeguidores() throws Exception {
		Usuario user = new Usuario();
		user.setEmail("seguidor@example.com");
		
		UsuarioResponseDto dto = new UsuarioResponseDto();
		dto.setEmail("seguidor@example.com");
		
		when(pertenenciaService.obtenerSeguidores("club@example.com"))
				.thenReturn(List.of(user));
		when(modelMapper.map(user, UsuarioResponseDto.class)).thenReturn(dto);
		
		mockMvc.perform(get("/club/club@example.com/seguidores"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].email").value("seguidor@example.com"));
	}
	
	@Test
	@WithMockUser(roles = {"ADMINISTRADOR"})
	void testDesignarDirectiva() throws Exception {
		Pertenencia p = new Pertenencia();
		p.setId(1L);
		
		when(pertenenciaService.designarDirectiva("user@example.com", "clubId")).thenReturn(p);
		
		mockMvc.perform(post("/club/clubId/designarDirectiva/user@example.com"))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "/club/clubId/directiva/user@example.com"));
	}
	
	@Test
	@WithMockUser(roles = {"DIRECTIVA"})
	void testNewMiembro() throws Exception {
		Pertenencia p = new Pertenencia();
		p.setId(2L);
		
		when(pertenenciaService.crearPertenenciaMiembro("user@example.com", "nuevo@example.com", "clubId"))
				.thenReturn(p);
		
		mockMvc.perform(post("/club/clubId/miembro/nuevo@example.com")
						.principal(() -> "user@example.com"))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "/club/clubId/miembro/nuevo@example.com"));
	}
	
	@Test
	@WithMockUser(roles = {"ADMINISTRADOR"})
	void testEliminarDirectiva() throws Exception {
		doNothing().when(pertenenciaService).eliminarRelacionDirectiva("user@example.com", "clubId");
		
		mockMvc.perform(delete("/club/clubId/directiva/user@example.com"))
				.andExpect(status().isNoContent());
	}
	
	@Test
	@WithMockUser(roles = {"DIRECTIVA"})
	void testEliminarMiembro() throws Exception {
		doNothing().when(pertenenciaService).eliminarRelacionMiembro("directiva@example.com", "miembro@example.com", "clubId");
		
		mockMvc.perform(delete("/club/clubId/miembro/miembro@example.com")
						.principal(() -> "directiva@example.com"))
				.andExpect(status().isNoContent());
	}
}