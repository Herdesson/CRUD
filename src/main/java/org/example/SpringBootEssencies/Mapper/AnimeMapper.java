package org.example.SpringBootEssencies.Mapper;

import org.example.SpringBootEssencies.Dominio.Anime;
import org.example.SpringBootEssencies.Requests.AnimePostRequestBody;
import org.example.SpringBootEssencies.Requests.AnimePutRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class AnimeMapper {
    public static final AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);

    public abstract Anime toAnime(AnimePostRequestBody animePostRequestBody);
    public abstract Anime toAnime(AnimePutRequestBody animePutRequestBody);
}
