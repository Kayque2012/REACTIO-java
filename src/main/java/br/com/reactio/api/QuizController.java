package br.com.reactio.api;

import br.com.reactio.api.dto.*;
import br.com.reactio.api.error.ApiError;
import br.com.reactio.service.AfirmacaoService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz")
@Tag(
    name = "Quiz",
    description =
        "Desafios sem gabarito e feedback após responder. Tentativas não são persistidas.")
public class QuizController {
  private final AfirmacaoService service;

  public QuizController(AfirmacaoService service) {
    this.service = service;
  }

  @GetMapping
  @Operation(
      summary = "Listar desafios sem gabarito",
      description = "Busca parcial no enunciado. Ordem crescente e ID como desempate.")
  @ApiResponse(responseCode = "200", description = "Lista de desafios, possivelmente vazia")
  @ApiResponse(
      responseCode = "400",
      description = "Dados ou parâmetros inválidos",
      content = @Content(schema = @Schema(implementation = ApiError.class)))
  public List<DesafioResponse> listar(
      @Parameter(description = "Trecho do enunciado, até 100 caracteres", example = "radiação")
          @RequestParam(required = false)
          @Size(max = 100)
          String texto) {
    return service.desafios(texto);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Consultar desafio sem gabarito")
  @ApiResponse(responseCode = "200", description = "Enunciado, tema e ordem")
  @ApiResponse(
      responseCode = "400",
      description = "Dados ou parâmetros inválidos",
      content = @Content(schema = @Schema(implementation = ApiError.class)))
  @ApiResponse(
      responseCode = "404",
      description = "Afirmação inexistente",
      content = @Content(schema = @Schema(implementation = ApiError.class)))
  public DesafioResponse consultar(
      @Parameter(description = "ID positivo", example = "1") @PathVariable @Positive Long id) {
    return service.desafio(id);
  }

  @PostMapping("/{id}/respostas")
  @Operation(
      summary = "Responder e receber explicação e fonte",
      description = "Sempre devolve o feedback. Não grava histórico, XP ou progresso.")
  @ApiResponse(
      responseCode = "200",
      description = "Acerto, classificação correta, explicação e referência")
  @ApiResponse(
      responseCode = "400",
      description = "Dados ou parâmetros inválidos",
      content = @Content(schema = @Schema(implementation = ApiError.class)))
  @ApiResponse(
      responseCode = "404",
      description = "Afirmação inexistente",
      content = @Content(schema = @Schema(implementation = ApiError.class)))
  public FeedbackResponse responder(
      @Parameter(description = "ID positivo", example = "1") @PathVariable @Positive Long id,
      @Valid @RequestBody RespostaRequest r) {
    return service.responder(id, r);
  }
}
