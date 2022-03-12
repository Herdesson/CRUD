package org.example.SpringBootEssencies.Requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnimePostRequestBody {
    @NotEmpty(message = "the name anime is not empty")
    @NotNull(message = "the anime is not null")
    @Schema(description = "this is Anime name", example = "jujutsu kaisen", required = true)
    private String name;
}
