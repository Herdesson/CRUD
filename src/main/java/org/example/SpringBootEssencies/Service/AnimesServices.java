package org.example.SpringBootEssencies.Service;

import lombok.RequiredArgsConstructor;
import org.example.SpringBootEssencies.Dominio.Anime;
import org.example.SpringBootEssencies.Exception.BadRequestException;
import org.example.SpringBootEssencies.Mapper.AnimeMapper;
import org.example.SpringBootEssencies.Repository.AnimeRepository;
import org.example.SpringBootEssencies.Requests.AnimePostRequestBody;
import org.example.SpringBootEssencies.Requests.AnimePutRequestBody;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimesServices {

    private final AnimeRepository animeRepository;

    public Page<Anime> listAll(Pageable pageable){
        return animeRepository.findAll(pageable);
    }
    public List<Anime> listAllNoPageable() {
        return animeRepository.findAll();
    }
    public List<Anime> findByName(String name){
        return animeRepository.findByName(name);
    }
    public Anime findByIdException(long id){
        return  animeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Anime Not Found"));
    }
    @Transactional
    public Anime save(AnimePostRequestBody animePostRequestBody){
        return animeRepository.save(AnimeMapper.INSTANCE.toAnime(animePostRequestBody));
    }

    public void delete(long id) {
        animeRepository.delete(findByIdException(id));
    }

    public void replace(AnimePutRequestBody animePutRequestBody) {
        Anime animeSave = findByIdException(animePutRequestBody.getId());
        Anime anime = AnimeMapper.INSTANCE.toAnime(animePutRequestBody);
        anime.setId(animeSave.getId());
        animeRepository.save(anime);

    }


}
