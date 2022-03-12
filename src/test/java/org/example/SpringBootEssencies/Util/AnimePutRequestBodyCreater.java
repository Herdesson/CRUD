package org.example.SpringBootEssencies.Util;


import org.example.SpringBootEssencies.Requests.AnimePutRequestBody;

public class AnimePutRequestBodyCreater {
    public static AnimePutRequestBody createAnimeToBeSavePutBody(){
        return AnimePutRequestBody.builder()
                .name(AnimeCreator.createAnimeUpdated().getName())
                .id(AnimeCreator.createAnimeUpdated().getId())
                .build();
    }
}
