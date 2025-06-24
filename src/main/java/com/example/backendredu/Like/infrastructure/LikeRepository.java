package com.example.backendredu.Like.infrastructure;

import com.example.backendredu.Like.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
