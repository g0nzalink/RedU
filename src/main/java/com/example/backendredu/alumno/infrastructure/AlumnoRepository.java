package com.example.backendredu.alumno.infrastructure;

import com.example.backendredu.alumno.domain.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlumnoRepository extends JpaRepository<Alumno, String> {
}

