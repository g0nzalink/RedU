package com.example.backendredu.club.infrastructure;

import com.example.backendredu.club.domain.Club;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<Club, String> {

}

