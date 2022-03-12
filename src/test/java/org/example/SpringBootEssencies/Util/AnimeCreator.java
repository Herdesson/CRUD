package org.example.SpringBootEssencies.Util;

import org.example.SpringBootEssencies.Dominio.Anime;

public class AnimeCreator {

    public static Anime createAnimeToBeSave(){
        return Anime.builder().name("Jujutsu Kaisen").build();
    }

    public static Anime createAnimeValid(){
        return Anime.builder().name("Cavaleiro dos Zodiacos")
                .id(1L).build();
    }
    public static Anime createAnimeUpdated(){
        return Anime.builder().name("DrStone")
                .id(1L).build();
    }
}


