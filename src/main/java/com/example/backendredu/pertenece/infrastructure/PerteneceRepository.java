package com.example.backendredu.pertenece.infrastructure;

import com.example.backendredu.pertenece.domain.Pertenece;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerteneceRepository extends JpaRepository<Long, Pertenece> { }

