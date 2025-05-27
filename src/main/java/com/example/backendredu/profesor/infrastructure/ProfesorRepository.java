package com.example.backendredu.profesor.infrastructure;

import com.example.backendredu.profesor.domain.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfesorRepository extends JpaRepository<Profesor, String> {
}

