package org.example.SpringBootEssencies.Client;

import lombok.extern.log4j.Log4j2;
import org.example.SpringBootEssencies.Dominio.Anime;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        ResponseEntity<Anime> forEntity = new RestTemplate().getForEntity("Http://localhost:8080/animes/{id}", Anime.class, 10);
        log.info(forEntity);

        Anime object = new RestTemplate().getForObject("Http://localhost:8080/animes/{id}", Anime.class,9);
        log.info(object);

        ResponseEntity<List<Anime>> animes = new RestTemplate().exchange("Http://localhost:8080/animes/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {});
        log.info(animes.getBody());

        //Post
        //Anime vinland = Anime.builder().name("vinland").build();
        //Anime vinlandSave = new RestTemplate().postForObject("Http://localhost:8080/animes/", vinland, Anime.class);
        //log.info("Saved anime",vinlandSave);
        //Anime vinland = Anime.builder().name("vinland").build();
        //ResponseEntity<Anime> vinlandSave = new RestTemplate().exchange("Http://localhost:8080/animes/"
        //        , HttpMethod.POST, new HttpEntity<>(vinland, createJsonHeader()),Anime.class);
        //log.info("Saved anime",vinlandSave);

        //put
        //Anime body = vinlandSave.getBody();
        //body.setName("DBZ");
        //ResponseEntity<Void> vinlandUpdate = new RestTemplate().exchange("Http://localhost:8080/animes/"
        //        , HttpMethod.PUT, new HttpEntity<>(body, createJsonHeader()), Void.class);
        //log.info(vinlandUpdate);

        //delete
        //ResponseEntity<Void> vinlandDelete = new RestTemplate().exchange("Http://localhost:8080/animes/{id}"
        //        , HttpMethod.DELETE, null, Void.class, 17);
        //log.info(vinlandDelete);
    }
    private static HttpHeaders createJsonHeader(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
