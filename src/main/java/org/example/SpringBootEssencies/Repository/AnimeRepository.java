package org.example.SpringBootEssencies.Repository;

import org.example.SpringBootEssencies.Dominio.Anime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimeRepository extends JpaRepository<Anime, Long> {

    List<Anime> findByName(String name);
}
