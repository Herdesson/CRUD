package org.example.SpringBootEssencies.Repository;

import org.example.SpringBootEssencies.Dominio.Anime;
import org.example.SpringBootEssencies.Dominio.ClientUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteUserRepository extends JpaRepository<ClientUser, Long> {

    ClientUser findByUserName(String name);
}
