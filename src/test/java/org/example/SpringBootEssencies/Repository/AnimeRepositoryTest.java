package org.example.SpringBootEssencies.Repository;

import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.example.SpringBootEssencies.Dominio.Anime;
import org.example.SpringBootEssencies.Util.AnimeCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;


@DataJpaTest
@DisplayName("testes para anime repository")
class AnimeRepositoryTest {
    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("save persist anime")
    void saveTestSuccessful() {
        Anime createAnime = AnimeCreator.createAnimeToBeSave();
        Anime save = this.animeRepository.save(createAnime);
        Assertions.assertThat(save).isNotNull();
        Assertions.assertThat(save.getName()).isNotNull();
        Assertions.assertThat(save.getName()).isEqualTo(createAnime.getName());
    }

    @Test
    @DisplayName("update anime")
    void updateTestSuccessful() {
        Anime anime = AnimeCreator.createAnimeToBeSave();
        Anime save = this.animeRepository.save(anime);
        save.setName("DBZ");
        Anime update = this.animeRepository.save(save);
        Assertions.assertThat(update).isNotNull();
        Assertions.assertThat(update.getName()).isNotNull();
        Assertions.assertThat(update.getName()).isEqualTo(save.getName());
    }

    @Test
    @DisplayName("delete anime")
    void deleteTestSuccessful() {
        Anime anime = AnimeCreator.createAnimeToBeSave();
        Anime save = this.animeRepository.save(anime);
        this.animeRepository.delete(save);
        Optional<Anime> optional = this.animeRepository.findById(save.getId());
        Assertions.assertThat(optional).isEmpty();
    }

    @Test
    @DisplayName("find by name anime")
    void findByNameTestSuccessful() {
        Anime anime = AnimeCreator.createAnimeToBeSave();
        Anime save = this.animeRepository.save(anime);
        String name = save.getName();
        List<Anime> animes = this.animeRepository.findByName(name);
        Assertions.assertThat(animes).isNotEmpty()
                .contains(save);
    }

    @Test
    @DisplayName("find by name return empty list anime")
    void findByNameReturnEmptyTestIsNotFound() {
        List<Anime> animes = this.animeRepository.findByName("name");
        Assertions.assertThat(animes).isEmpty();
    }

    @Test
    @DisplayName("save throw anime")
    void saveThrowTestSuccessful() {
        Anime anime = new Anime();
        Assertions.assertThatThrownBy(() -> this.animeRepository.save(anime))
                .isInstanceOf(ConstraintViolationException.class);

    }
}
    