package br.com.reactio.api.dto;

import br.com.reactio.domain.Classificacao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record RespostaRequest(@NotNull @Schema(example = "FALSO") Classificacao resposta) {}
