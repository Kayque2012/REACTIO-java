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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/afirmacoes")
@Tag(
    name = "Editorial",
    description = "CRUD com gabarito. Sem autenticação nesta demonstração local.")
public class AfirmacaoController {
  private final AfirmacaoService service;

  public AfirmacaoController(AfirmacaoService service) {
    this.service = service;
  }

  @GetMapping
  @Operation(
      summary = "Listar ou pesquisar conteúdo editorial",
      description =
          "Busca parcial no enunciado, sem distinguir maiúsculas. Texto vazio lista tudo; ordem"
              + " crescente e ID como desempate.")
  @ApiResponse(responseCode = "200", description = "Lista de afirmações, possivelmente vazia")
  @ApiResponse(
      responseCode = "400",
      description = "Dados ou parâmetros inválidos",
      content = @Content(schema = @Schema(implementation = ApiError.class)))
  public List<AfirmacaoResponse> listar(
      @Parameter(description = "Trecho do enunciado, até 100 caracteres", example = "radiação")
          @RequestParam(required = false)
          @Size(max = 100)
          String texto) {
    return service.listar(texto);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Consultar conteúdo editorial completo")
  @ApiResponse(responseCode = "200", description = "Afirmação com gabarito e fonte")
  @ApiResponse(
      responseCode = "400",
      description = "Dados ou parâmetros inválidos",
      content = @Content(schema = @Schema(implementation = ApiError.class)))
  @ApiResponse(
      responseCode = "404",
      description = "Afirmação inexistente",
      content = @Content(schema = @Schema(implementation = ApiError.class)))
  public AfirmacaoResponse consultar(
      @Parameter(description = "ID positivo", example = "1") @PathVariable @Positive Long id) {
    return service.consultar(id);
  }

  @PostMapping
  @Operation(summary = "Cadastrar afirmação com explicação e referência")
  @ApiResponse(responseCode = "201", description = "Criada; Location aponta para a consulta")
  @ApiResponse(
      responseCode = "400",
      description = "Dados ou parâmetros inválidos",
      content = @Content(schema = @Schema(implementation = ApiError.class)))
  public ResponseEntity<AfirmacaoResponse> criar(@Valid @RequestBody AfirmacaoRequest r) {
    AfirmacaoResponse a = service.criar(r);
    return ResponseEntity.created(java.net.URI.create("/api/afirmacoes/" + a.id())).body(a);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Substituir os dados de uma afirmação")
  @ApiResponse(responseCode = "200", description = "Afirmação atualizada")
  @ApiResponse(
      responseCode = "400",
      description = "Dados ou parâmetros inválidos",
      content = @Content(schema = @Schema(implementation = ApiError.class)))
  @ApiResponse(
      responseCode = "404",
      description = "Afirmação inexistente",
      content = @Content(schema = @Schema(implementation = ApiError.class)))
  public AfirmacaoResponse atualizar(
      @Parameter(description = "ID positivo", example = "1") @PathVariable @Positive Long id,
      @Valid @RequestBody AfirmacaoRequest r) {
    return service.atualizar(id, r);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Excluir afirmação")
  @ApiResponse(responseCode = "204", description = "Excluída", content = @Content)
  @ApiResponse(
      responseCode = "400",
      description = "Dados ou parâmetros inválidos",
      content = @Content(schema = @Schema(implementation = ApiError.class)))
  @ApiResponse(
      responseCode = "404",
      description = "Afirmação inexistente",
      content = @Content(schema = @Schema(implementation = ApiError.class)))
  public ResponseEntity<Void> excluir(
      @Parameter(description = "ID positivo", example = "1") @PathVariable @Positive Long id) {
    service.excluir(id);
    return ResponseEntity.noContent().build();
  }
}
