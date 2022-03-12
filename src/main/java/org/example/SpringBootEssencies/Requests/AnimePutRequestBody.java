package org.example.SpringBootEssencies.Requests;

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
public class AnimePutRequestBody {
    private Long id;
    @NotEmpty(message = "the name anime is empty")
    @NotNull(message = "the anime is null")
    public String name;
}
