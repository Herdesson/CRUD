package org.example.SpringBootEssencies.Control;

import org.assertj.core.api.Assertions;
import org.example.SpringBootEssencies.Dominio.Anime;
import org.example.SpringBootEssencies.Requests.AnimePostRequestBody;
import org.example.SpringBootEssencies.Requests.AnimePutRequestBody;
import org.example.SpringBootEssencies.Service.AnimesServices;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AnimeControllerTest {

    @InjectMocks
    private AnimeController animeController;
    @Mock
    private AnimesServices animesServicesMock;

    @BeforeEach
    void setUp(){
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createAnimeValid()));
        BDDMockito.when(animesServicesMock.listAll(ArgumentMatchers.any()))
                .thenReturn(animePage);
        BDDMockito.when(animesServicesMock.listAllNoPageable())
                .thenReturn(List.of(AnimeCreator.createAnimeValid()));
        BDDMockito.when(animesServicesMock.findByIdException(ArgumentMatchers.anyLong()))
                .thenReturn(AnimeCreator.createAnimeValid());
        BDDMockito.when(animesServicesMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createAnimeValid()));
        BDDMockito.when(animesServicesMock.save(ArgumentMatchers.any(AnimePostRequestBody.class)))
                .thenReturn(AnimeCreator.createAnimeValid());
        BDDMockito.doNothing()
                .when(animesServicesMock).replace(ArgumentMatchers.any(AnimePutRequestBody.class));
        BDDMockito.doNothing()
                .when(animesServicesMock).delete(ArgumentMatchers.anyLong());
    }
    @Test
    @DisplayName("Test when list anime")
    void list_ReturnListAnime_WhenSuccessful(){
        String expectedAnime = AnimeCreator.createAnimeValid().getName();
        Page<Anime> animePage = animeController.list(null).getBody();
        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList())
                        .isNotEmpty().hasSize(1);
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedAnime);
    }
    @Test
    @DisplayName("Test when listAll anime")
    void listAll_ReturnListAnime_WhenSuccessful(){
        String expectedAnime = AnimeCreator.createAnimeValid().getName();
        List<Anime> animeList = animeController.listAll().getBody();
        Assertions.assertThat(animeList).isNotNull().isNotEmpty()
                        .hasSize(1);
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedAnime);
    }
    @Test
    @DisplayName("Test findById")
    void findById_ReturnListId_WhenSuccessful(){
        Long expectedId = AnimeCreator.createAnimeValid().getId();
        Anime anime = animeController.findById(1).getBody();
        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);
    }
    @Test
    @DisplayName("Test findByName")
    void findByName_ReturnListName_WhenSuccessful(){
        String expectedAnime = AnimeCreator.createAnimeValid().getName();
        List<Anime> animeList = animeController.findByName("Animes").getBody();
        Assertions.assertThat(animeList).isNotNull().isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedAnime);
    }
    @Test
    @DisplayName("Test findByName when anime is not found")
    void findByName_ReturnEmptyListName_WhenSuccessful(){
        BDDMockito.when(animesServicesMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());
        List<Anime> animeList = animeController.findByName("Animes").getBody();
        Assertions.assertThat(animeList).isNotNull().isEmpty();
    }
    @Test
    @DisplayName("Test save")
    void save_ReturnListAnime_WhenSuccessful(){
        Anime anime = animeController.save(AnimePostRequestBodyCreater.createAnimeToBeSavePostBody()).getBody();
        Assertions.assertThat(anime).isNotNull().isEqualTo(AnimeCreator.createAnimeValid());
    }
    @Test
    @DisplayName("Test replace")
    void replace_UpdateAnime_WhenSuccessful(){
        ResponseEntity<Void> entity = animeController.replace(AnimePutRequestBodyCreater.createAnimeToBeSavePutBody());
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
    @Test
    @DisplayName("Test Delete")
    void delete_RemoveAnime_WhenSuccessful(){
        ResponseEntity<Void> entity = animeController.delete(1);
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}