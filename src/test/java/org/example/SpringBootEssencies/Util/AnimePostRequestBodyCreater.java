package org.example.SpringBootEssencies.Util;

import org.example.SpringBootEssencies.Dominio.Anime;
import org.example.SpringBootEssencies.Requests.AnimePostRequestBody;

public class AnimePostRequestBodyCreater {
    public static AnimePostRequestBody createAnimeToBeSavePostBody(){
        return AnimePostRequestBody.builder()
                .name(AnimeCreator.createAnimeToBeSave().getName())
                .build();
    }
}
