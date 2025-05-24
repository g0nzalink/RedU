package com.example.backendredu.directiva.infrastructure;

import com.example.backendredu.directiva.domain.Directiva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectivaRepository extends JpaRepository<Long, Directiva> { }

