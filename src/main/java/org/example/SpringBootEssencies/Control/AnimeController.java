package org.example.SpringBootEssencies.Control;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.example.SpringBootEssencies.Dominio.Anime;
import org.example.SpringBootEssencies.Requests.AnimePostRequestBody;
import org.example.SpringBootEssencies.Requests.AnimePutRequestBody;
import org.example.SpringBootEssencies.Service.AnimesServices;
import org.example.SpringBootEssencies.Util.DataUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("animes")
//cria getters e setters, equals e hashcode
@Log4j2
//cria um construtor
@RequiredArgsConstructor
public class AnimeController {
    private final AnimesServices animesServices;
    @GetMapping
    @Operation(summary = "list all pages", description = "size full list animes", tags = {"anime"})

    public ResponseEntity<Page<Anime>> list(@ParameterObject Pageable pageable){
        return ResponseEntity.ok(animesServices.listAll(pageable));
    }
    @GetMapping(path = "/all")
    public ResponseEntity<List<Anime>> listAll(){
        return ResponseEntity.ok(animesServices.listAllNoPageable());
    }
    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> findById(@PathVariable long id){
        return ResponseEntity.ok(animesServices.findByIdException(id));
    }
    @GetMapping(path = "by-id/{id}")
    public ResponseEntity<Anime> findByIdAuth(@PathVariable long id, @AuthenticationPrincipal UserDetails userDetails){
        log.info(userDetails);
        return ResponseEntity.ok(animesServices.findByIdException(id));
    }
    @GetMapping(path = "/find")
    public ResponseEntity<List<Anime>> findByName(@RequestParam String name){
        return ResponseEntity.ok(animesServices.findByName(name));
    }
    @PostMapping
    public ResponseEntity<Anime> save(@RequestBody @Valid AnimePostRequestBody animePostRequestBody){
        return new ResponseEntity<>(animesServices.save(animePostRequestBody), HttpStatus.CREATED);
    }
    @DeleteMapping(path = "/admin/{id}")
    @ApiResponses( value = {@ApiResponse(responseCode = "204", description = "Successful"),
            @ApiResponse(responseCode = "400", description = "when anime is not exists")
    })
    public ResponseEntity<Void> delete(@PathVariable long id){
        animesServices.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody @Valid AnimePutRequestBody animePutRequestBody){
        animesServices.replace(animePutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
