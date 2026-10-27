package br.com.reactio.api.dto;

import br.com.reactio.domain.Classificacao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

public record AfirmacaoRequest(
    @NotBlank @Size(max = 400) @Schema(example = "Toda radiação tem origem artificial.")
        String enunciado,
    @NotBlank @Size(max = 80) @Schema(example = "Radiação no cotidiano") String tema,
    @NotNull @Schema(example = "FALSO") Classificacao classificacao,
    @NotBlank
        @Size(max = 1500)
        @Schema(example = "Existem fontes naturais, como os raios cósmicos e minerais do solo.")
        String explicacao,
    @NotBlank @Size(max = 200) @Schema(example = "EPA - Radiation Sources and Doses")
        String referenciaTitulo,
    @NotBlank
        @Size(max = 500)
        @URL(protocol = "https")
        @Schema(example = "https://www.epa.gov/radiation/radiation-sources-and-doses")
        String referenciaUrl,
    @NotNull
        @Positive
        @Schema(example = "10", description = "Posição na trilha; empates são ordenados pelo ID.")
        Integer ordem) {}
