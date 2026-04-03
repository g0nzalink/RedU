package com.example.backendredu.usuario.domain;

import com.example.backendredu.auth.JwtService;
import com.example.backendredu.auth.LoginResponseDto;
import com.example.backendredu.exceptions.EmailAlreadyExistsException;
import com.example.backendredu.exceptions.UsernameAlreadyExistsException;
import com.example.backendredu.usuario.dto.UsuarioResponseDto;
import com.example.backendredu.usuario.dto.UsuarioUpdateDto;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements UserDetailsService {
	@Autowired
	private UsuarioRepository userRepository;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private ModelMapper modelMapper;
	
	public void register(Role rol, String email, String username, String password) {
		if (userRepository.existsById(email)) {
			throw new EmailAlreadyExistsException(email);
		}

		if (userRepository.existsByUsername(username)) {
			throw new UsernameAlreadyExistsException("Nombre de usuario ya registrado.");
		}
		Usuario user = new Usuario();
		user.setEmail(email);
		user.setUsername(username);
		user.setPassword(encoder.encode(password));
		user.setUserType(rol);
		user.setFotoPerfilUrl("https://res.cloudinary.com/dvrldorwz/image/upload/v1751791862/redu_padding_imbmse.png");
		userRepository.save(user);
	}

	public LoginResponseDto login(String email, String password) {
		Usuario user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Nombre de usuario no encontrado."));
		if (!encoder.matches(password, user.getPassword())) {
			throw new BadCredentialsException("Contraseña incorrecta.");
		}
		String token = jwtService.generateToken(user);
		return new LoginResponseDto(token, user.getEmail(), user.getUsername(), user.getUserType(), user.getFotoPerfilUrl());
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario user = userRepository.findById(email)
				.orElseThrow(() -> new UsernameNotFoundException("Nombre de usuario no encontrado."));

		return new User(
				user.getEmail(),
				user.getPassword(),
				List.of(new SimpleGrantedAuthority("ROLE_" + user.getUserType().name()))
		);
	}
	
	@Transactional
	public Usuario updateUsuario(String email, UsuarioUpdateDto dto) {
		Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no fue encontrado."));
		if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
			usuario.setUsername(dto.getUsername());
		}
		if (dto.getUsername() != null) {
			usuario.setDescription(dto.getDescripcion());
		}
		return usuarioRepository.save(usuario);
	}
	
	public List<UsuarioResponseDto> getAll() {
		List<Usuario> usuarios = usuarioRepository.findAll();
		List<UsuarioResponseDto> newUsuarios = new java.util.ArrayList<>(List.of());
		for (Usuario usuario : usuarios) {
			newUsuarios.add(modelMapper.map(usuario, UsuarioResponseDto.class));
		}
		return newUsuarios;
	}
}
