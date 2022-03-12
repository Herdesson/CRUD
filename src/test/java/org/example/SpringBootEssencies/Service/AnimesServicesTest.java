package org.example.SpringBootEssencies.Service;

import org.assertj.core.api.Assertions;
import org.example.SpringBootEssencies.Dominio.Anime;
import org.example.SpringBootEssencies.Exception.BadRequestException;
import org.example.SpringBootEssencies.Repository.AnimeRepository;
import org.example.SpringBootEssencies.Util.AnimeCreator;
import org.example.SpringBootEssencies.Util.AnimePostRequestBodyCreater;
import org.example.SpringBootEssencies.Util.AnimePutRequestBodyCreater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class AnimesServicesTest {
    @InjectMocks
    private AnimesServices animeServices;
    @Mock
    private AnimeRepository animeRepositoryMock;

    @BeforeEach
    void setUp(){
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createAnimeValid()));
        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(animePage);
        BDDMockito.when(animeRepositoryMock.findAll())
                .thenReturn(List.of(AnimeCreator.createAnimeValid()));
        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(AnimeCreator.createAnimeValid()));
        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createAnimeValid()));
        BDDMockito.when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class)))
                .thenReturn(AnimeCreator.createAnimeValid());
        BDDMockito.doNothing()
                .when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));
    }
    @Test
    @DisplayName("Test when list anime")
    void listAll_ReturnListAnime_WhenSuccessful(){
        String expectedAnime = AnimeCreator.createAnimeValid().getName();
        Page<Anime> animePage = animeServices.listAll(PageRequest.of(1,2));
        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList())
                .isNotEmpty().hasSize(1);
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedAnime);
    }
    @Test
    @DisplayName("Test when listAll no Pageable anime")
    void listAllNoPageable_ReturnListAnime_WhenSuccessful(){
        String expectedAnime = AnimeCreator.createAnimeValid().getName();
        List<Anime> animeList = animeServices.listAllNoPageable();
        Assertions.assertThat(animeList).isNotNull().isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedAnime);
    }
    @Test
    @DisplayName("Test findById Exception")
    void findByIdException_ReturnListId_WhenSuccessful(){
        Long expectedId = AnimeCreator.createAnimeValid().getId();
        Anime anime = animeServices.findByIdException(1);
        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);
    }
    @Test
    @DisplayName("Test findById Exception Throws is not found")
    void findByIdExceptionThrows_ReturnListId_WhenSuccessful(){
        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> animeServices.findByIdException(1));
    }
    @Test
    @DisplayName("Test findByName")
    void findByName_ReturnListName_WhenSuccessful(){
        String expectedAnime = AnimeCreator.createAnimeValid().getName();
        List<Anime> animeList = animeServices.findByName("Animes");
        Assertions.assertThat(animeList).isNotNull().isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedAnime);
    }
    @Test
    @DisplayName("Test findByName when anime is not found")
    void findByName_ReturnEmptyListName_WhenSuccessful(){
        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());
        List<Anime> animeList = animeServices.findByName("Animes");
        Assertions.assertThat(animeList).isNotNull().isEmpty();
    }
    @Test
    @DisplayName("Test save")
    void save_ReturnListAnime_WhenSuccessful(){
        Anime anime = animeServices.save(AnimePostRequestBodyCreater.createAnimeToBeSavePostBody());
        Assertions.assertThat(anime).isNotNull().isEqualTo(AnimeCreator.createAnimeValid());
    }
    @Test
    @DisplayName("Test replace")
    void replace_UpdateAnime_WhenSuccessful(){
        Assertions.assertThatCode(() -> animeServices.replace(AnimePutRequestBodyCreater.createAnimeToBeSavePutBody()));
    }
    @Test
    @DisplayName("Test Delete")
    void delete_RemoveAnime_WhenSuccessful(){
        Assertions.assertThatCode(() -> animeServices.delete(1))
                .doesNotThrowAnyException();

    }
}