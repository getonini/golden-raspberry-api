package com.goldenraspberryawards.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.goldenraspberryawards.model.Movie;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByWinnerTrue();
}
