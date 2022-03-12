package org.example.SpringBootEssencies.Integration;

import org.assertj.core.api.Assertions;
import org.example.SpringBootEssencies.Dominio.Anime;
import org.example.SpringBootEssencies.Dominio.ClientUser;
import org.example.SpringBootEssencies.Repository.AnimeRepository;
import org.example.SpringBootEssencies.Repository.ClienteUserRepository;
import org.example.SpringBootEssencies.Requests.AnimePostRequestBody;
import org.example.SpringBootEssencies.Util.AnimeCreator;
import org.example.SpringBootEssencies.Util.AnimePostRequestBodyCreater;
import org.example.SpringBootEssencies.Wappers.PageableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AnimeControllerIT {
    @Autowired
    @Qualifier(value = "testRestTemplateUserCreator")
    private TestRestTemplate testRestTemplate;
    @Autowired
    @Qualifier(value = "testRestTemplateAdminCreator")
    private TestRestTemplate testRestTemplateAdmin;
    @Autowired
    private AnimeRepository animeRepository;
    @Autowired
    private ClienteUserRepository userRepository;
    private static final ClientUser usuario = ClientUser.builder()
            .name("herdesson")
            .passowrd("{bcrypt}$2a$10$EKpZyDpMTkllvjkYYGTuIO1FC9G1C6EuAI2rBENhMGXBN5lgVahPK")
            .userName("flavio")
            .authorities("ROLE_USER")
            .build();
    private static final ClientUser admin = ClientUser.builder()
            .name("flavio")
            .passowrd("{bcrypt}$2a$10$EKpZyDpMTkllvjkYYGTuIO1FC9G1C6EuAI2rBENhMGXBN5lgVahPK")
            .userName("flavio")
            .authorities("ROLE_USER,ROLE_ADMIN")
            .build();

    @TestConfiguration
    @Lazy
    static class Config{
        @Bean(name = "testRestTemplateUserCreator")
        public TestRestTemplate testRestTemplateUserCreator(@Value("${local.server.port}") int port){
            RestTemplateBuilder newUser = new RestTemplateBuilder()
                    .rootUri("http://localhost:"+port)
                    .basicAuthentication("herdesson", "flavio");
            return new TestRestTemplate(newUser);
        }
        @Bean(name = "testRestTemplateAdminCreator")
        public TestRestTemplate testRestTemplateAdminCreator(@Value("${local.server.port}") int port){
            RestTemplateBuilder newUserAdmin = new RestTemplateBuilder()
                    .rootUri("http://localhost:"+port)
                    .basicAuthentication("flavio", "herdesson");
            return new TestRestTemplate(newUserAdmin);
        }
    }
    @Test
    @DisplayName("Test when list anime")
    void list_ReturnListAnime_WhenSuccessful(){
        Anime saveAnime = animeRepository.save(AnimeCreator.createAnimeToBeSave());
        userRepository.save(usuario);
        String expectedAnime = saveAnime.getName();
        PageableResponse<Anime> animeList = testRestTemplate.exchange("/animes", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {
                }).getBody();
        Assertions.assertThat(animeList).isNotNull();
        Assertions.assertThat(animeList.toList())
                .isNotEmpty().hasSize(1);
        Assertions.assertThat(animeList.toList().get(0).getName()).isEqualTo(expectedAnime);
    }
    @Test
    @DisplayName("listAll returns list of anime when successful")
    void listAll_ReturnsListOfAnimes_WhenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSave());
        userRepository.save(usuario);

        String expectedName = savedAnime.getName();

        List<Anime> animes = testRestTemplate.exchange("/animes/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }
    @Test
    @DisplayName("Test findById")
    void findById_ReturnListId_WhenSuccessful(){
        Anime saveAnime = animeRepository.save(AnimeCreator.createAnimeToBeSave());
        userRepository.save(usuario);
        Long expectedId = saveAnime.getId();
        Anime anime = testRestTemplate.getForObject("/animes/{id}", Anime.class,expectedId);
        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);
    }
    @Test
    @DisplayName("Test findByName")
    void findByName_ReturnListName_WhenSuccessful(){
        Anime saveAnime = animeRepository.save(AnimeCreator.createAnimeToBeSave());
        userRepository.save(usuario);
        String expectedAnime = saveAnime.getName();
        String url = String.format("/animes/find?name=%s", expectedAnime);
        List<Anime> animeList = testRestTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();
        Assertions.assertThat(animeList).isNotNull().isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedAnime);
    }
    @Test
    @DisplayName("Test findByName when anime is not found")
    void findByName_ReturnEmptyListName_WhenSuccessful(){
        userRepository.save(usuario);
        List<Anime> animeList = testRestTemplate.exchange("/animes/find?name=dbz", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();
        Assertions.assertThat(animeList).isNotNull().isEmpty();
    }
    @Test
    @DisplayName("Test save")
    void save_ReturnListAnime_WhenSuccessful(){
        AnimePostRequestBody animeToBeSavePostBody = AnimePostRequestBodyCreater.createAnimeToBeSavePostBody();
        userRepository.save(usuario);
        ResponseEntity<Anime> animeEntity = testRestTemplate.postForEntity("/animes", animeToBeSavePostBody, Anime.class);
        Assertions.assertThat(animeEntity).isNotNull();
        Assertions.assertThat(animeEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(animeEntity.getBody().getId()).isNotNull();
    }
    @Test
    @DisplayName("Test replace")
    void replace_UpdateAnime_WhenSuccessful(){
        Anime saveAnime = animeRepository.save(AnimeCreator.createAnimeToBeSave());
        userRepository.save(usuario);
        saveAnime.setName("cdz");
        ResponseEntity<Void> animeEntity = testRestTemplate.exchange("/animes",
                HttpMethod.PUT, new HttpEntity<>(saveAnime),Void.class);
        Assertions.assertThat(animeEntity).isNotNull();
        Assertions.assertThat(animeEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
    @Test
    @DisplayName("Test Delete")
    void delete_RemoveAnime_WhenSuccessful(){
        Anime saveAnime = animeRepository.save(AnimeCreator.createAnimeToBeSave());
        userRepository.save(admin);
        ResponseEntity<Void> animeEntity = testRestTemplateAdmin.exchange("/animes/admin/{id}",
                HttpMethod.DELETE, null, Void.class, saveAnime.getId());
        Assertions.assertThat(animeEntity).isNotNull();
        Assertions.assertThat(animeEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
    @Test
    @DisplayName("Test Delete when is not admin")
    void delete_Return_WhenIsNotAdmin(){
        Anime saveAnime = animeRepository.save(AnimeCreator.createAnimeToBeSave());
        userRepository.save(usuario);
        ResponseEntity<Void> animeEntity = testRestTemplate.exchange("/animes/admin/{id}",
                HttpMethod.DELETE, null, Void.class, saveAnime.getId());
        Assertions.assertThat(animeEntity).isNotNull();
        Assertions.assertThat(animeEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}
