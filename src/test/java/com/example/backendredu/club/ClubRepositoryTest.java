package com.example.backendredu.club;

import com.example.backendredu.club.domain.Club;
import com.example.backendredu.club.infrastructure.ClubRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
@ExtendWith(SpringExtension.class)
public class ClubRepositoryTest {
	
	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15.3")
			.withDatabaseName("testdb")
			.withUsername("test")
			.withPassword("test");
	
	@Autowired
	private ClubRepository clubRepository;
	
	private Club club1;
	private Club club2;
	
	@BeforeEach
	void setUp() {
		club1 = new Club();
		club1.setEmail("club1@utec.edu.pe");
		club1.setNombre("Club Uno");
		club1.setDescripcion("Primer club");
		
		club2 = new Club();
		club2.setEmail("club2@utec.edu.pe");
		club2.setNombre("Club Dos");
		club2.setDescripcion("Segundo club");
		
		clubRepository.saveAll(List.of(club1, club2));
	}
	
	@Test
	void findByIdWithExistingClubShouldReturnExistingClub() {
		Optional<Club> found = clubRepository.findById("club1@utec.edu.pe");
		
		assertThat(found).isPresent();
		assertThat(found.get().getNombre()).isEqualTo("Club Uno");
	}
	
	@Test
	void findByIdWithoutExistingClubShouldReturnError() {
		Optional<Club> found = clubRepository.findById("noexiste@utec.edu.pe");
		
		assertThat(found).isNotPresent();
	}
	
	@Test
	void findAllShouldReturnAllClubs() {
		List<Club> clubs = clubRepository.findAll();
		
		assertThat(clubs).hasSize(2);
		assertThat(clubs).extracting(Club::getEmail)
				.containsExactlyInAnyOrder("club1@utec.edu.pe", "club2@utec.edu.pe");
	}
	
	@Test
	void saveShouldAddNewClub() {
		Club newClub = new Club();
		newClub.setEmail("nuevo@utec.edu.pe");
		newClub.setNombre("Nuevo Club");
		newClub.setDescripcion("Nuevo en la lista");
		
		clubRepository.save(newClub);
		
		Optional<Club> found = clubRepository.findById("nuevo@utec.edu.pe");
		assertThat(found).isPresent();
		assertThat(found.get().getNombre()).isEqualTo("Nuevo Club");
	}

	@Test
	void deleteByIdShouldDeleteClub() {
		clubRepository.deleteById("club1@utec.edu.pe");
		
		Optional<Club> found = clubRepository.findById("club1@utec.edu.pe");
		assertThat(found).isNotPresent();
	}
}