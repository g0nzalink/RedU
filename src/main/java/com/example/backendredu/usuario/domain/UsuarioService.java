package com.example.backendredu.usuario.domain;

import com.example.backendredu.auth.JwtService;
import com.example.backendredu.exceptions.EmailAlreadyExistsException;
import com.example.backendredu.exceptions.UsernameAlreadyExistsException;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

	public void register(Role rol, String email, String username, String password) {
		if (userRepository.existsById(email)) {
			throw new EmailAlreadyExistsException(email);
		}

		if (userRepository.existsByUsername(username)) {
			throw new UsernameAlreadyExistsException("Nombre usuario ya registrado");
		}
		Usuario user = new Usuario();
		user.setEmail(email);
		user.setUsername(username);
		user.setPassword(encoder.encode(password));
		user.setUserType(rol);
		userRepository.save(user);
	}

	public String login(String email, String password) {
		Usuario user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
		if (!encoder.matches(password, user.getPassword())) {
			throw new BadCredentialsException("Contraseña incorrecta");
		}
		return jwtService.generateToken(user);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario user = userRepository.findById(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

		return new org.springframework.security.core.userdetails.User(
				user.getEmail(),
				user.getPassword(),
				List.of(new SimpleGrantedAuthority("ROLE_" + user.getUserType().name()))
		);
	}

	@Transactional
	public List<Usuario> allUsuarios() {
		return userRepository.findAll().stream()
				.collect(Collectors.toList());
	}
}
