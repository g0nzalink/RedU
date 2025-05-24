package com.example.backendredu.alumno.infrastructure;

import com.example.backendredu.alumno.domain.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface AlumnoRepository extends JpaRepository<Alumno, String> {
}

