package com.example.backendredu.usuario.domain;

import com.example.backendredu.auth.JwtService;
import com.example.backendredu.publicacion.domain.Publicacion;
import com.example.backendredu.publicacion.infrastructure.PublicacionRepository;
import com.example.backendredu.usuario.infrastructure.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService implements UserDetailsService {
	@Autowired
	private UsuarioRepository userRepository;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private JwtService jwtService;

	public void register(Rol rol, String email, String username, String password) {
		Usuario user = new Usuario();
		user.setEmail(email);
		user.setUsername(username);
		user.setPassword(encoder.encode(password));
		user.setUserType(rol);
		userRepository.save(user);
	}

	public String login(String username, String password) {
		Usuario user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
		if (!encoder.matches(password, user.getPassword())) {
			throw new BadCredentialsException("Contraseña incorrecta");
		}
		return jwtService.generateToken(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

		return new org.springframework.security.core.userdetails.User(
				user.getUsername(),
				user.getPassword(),
				List.of(new SimpleGrantedAuthority("ROLE_" + user.getUserType().name()))
		);
	}
}
