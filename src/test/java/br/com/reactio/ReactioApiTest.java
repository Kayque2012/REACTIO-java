package br.com.reactio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.reactio.domain.*;
import br.com.reactio.repository.AfirmacaoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@org.springframework.test.context.TestExecutionListeners(
    listeners =
        org.springframework.test.context.support.DependencyInjectionTestExecutionListener.class,
    mergeMode = org.springframework.test.context.TestExecutionListeners.MergeMode.REPLACE_DEFAULTS)
class ReactioApiTest {
  @Autowired MockMvc mvc;
  @Autowired ObjectMapper mapper;
  @Autowired AfirmacaoRepository repository;
  private Long id;

  @BeforeEach
  void preparar() {
    repository.deleteAll();
    id =
        repository
            .save(
                new Afirmacao(
                    "Toda radiação é artificial.",
                    "Cotidiano",
                    Classificacao.FALSO,
                    "Há fontes naturais.",
                    "EPA",
                    "https://www.epa.gov/radiation/radiation-sources-and-doses",
                    1))
            .getId();
  }

  private Map<String, Object> valido() {
    return new HashMap<>(
        Map.of(
            "enunciado",
            "Há fontes naturais de radiação.",
            "tema",
            "Cotidiano",
            "classificacao",
            "VERDADEIRO",
            "explicacao",
            "Há minerais naturais e raios cósmicos.",
            "referenciaTitulo",
            "EPA",
            "referenciaUrl",
            "https://www.epa.gov/radiation/radiation-sources-and-doses",
            "ordem",
            2));
  }

  @Test
  void crudCompleto() throws Exception {
    String criado =
        mvc.perform(
                post("/api/afirmacoes")
                    .contentType("application/json")
                    .content(mapper.writeValueAsString(valido())))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andReturn()
            .getResponse()
            .getContentAsString();
    long novo = mapper.readTree(criado).get("id").asLong();
    mvc.perform(get("/api/afirmacoes/" + novo))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.classificacao").value("VERDADEIRO"));
    var atualizado = valido();
    atualizado.put("enunciado", "Enunciado revisado.");
    mvc.perform(
            put("/api/afirmacoes/" + novo)
                .contentType("application/json")
                .content(mapper.writeValueAsString(atualizado)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.enunciado").value("Enunciado revisado."));
    assertThat(repository.findById(novo).orElseThrow().getEnunciado())
        .isEqualTo("Enunciado revisado.");
    mvc.perform(delete("/api/afirmacoes/" + novo))
        .andExpect(status().isNoContent())
        .andExpect(content().string(""));
    mvc.perform(get("/api/afirmacoes/" + novo)).andExpect(status().isNotFound());
  }

  @Test
  void buscaPorTrechoIgnoraMaiusculas() throws Exception {
    mvc.perform(get("/api/afirmacoes").param("texto", "  RADIAÇÃO "))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1));
    mvc.perform(get("/api/afirmacoes").param("texto", "inexistente"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));
    mvc.perform(get("/api/afirmacoes").param("texto", "   "))
        .andExpect(jsonPath("$.length()").value(1));
  }

  @Test
  void quizNaoExpoeGabaritoOuExplicacaoAntesDaResposta() throws Exception {
    mvc.perform(get("/api/quiz/" + id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.classificacao").doesNotExist())
        .andExpect(jsonPath("$.explicacao").doesNotExist())
        .andExpect(jsonPath("$.referenciaUrl").doesNotExist());
    mvc.perform(get("/api/quiz")).andExpect(jsonPath("$[0].classificacao").doesNotExist());
  }

  @Test
  void feedbackCorretoEIncorretoSempreTemFonte() throws Exception {
    for (String escolha : List.of("FALSO", "VERDADEIRO", "DEPENDE")) {
      mvc.perform(
              post("/api/quiz/" + id + "/respostas")
                  .contentType("application/json")
                  .content("{\"resposta\":\"" + escolha + "\"}"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.acertou").value(escolha.equals("FALSO")))
          .andExpect(jsonPath("$.classificacaoCorreta").value("FALSO"))
          .andExpect(jsonPath("$.explicacao").value("Há fontes naturais."))
          .andExpect(
              jsonPath("$.referenciaUrl")
                  .value("https://www.epa.gov/radiation/radiation-sources-and-doses"));
    }
    assertThat(repository.count()).isEqualTo(1);
  }

  @Test
  void dependePodeSerRespostaCorreta() throws Exception {
    var a =
        repository.save(
            new Afirmacao(
                "O resultado varia com o contexto.",
                "Contexto",
                Classificacao.DEPENDE,
                "Depende das condições.",
                "EPA",
                "https://www.epa.gov/",
                2));
    mvc.perform(
            post("/api/quiz/" + a.getId() + "/respostas")
                .contentType("application/json")
                .content("{\"resposta\":\"DEPENDE\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.acertou").value(true));
  }

  @Test
  void camposObrigatoriosETamanhosSaoValidados() throws Exception {
    mvc.perform(post("/api/afirmacoes").contentType("application/json").content("{}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.campos.enunciado").exists())
        .andExpect(jsonPath("$.campos.classificacao").exists());
    var dados = valido();
    dados.put("enunciado", " ");
    dados.put("explicacao", "x".repeat(1501));
    dados.put("ordem", 0);
    mvc.perform(
            post("/api/afirmacoes")
                .contentType("application/json")
                .content(mapper.writeValueAsString(dados)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.campos.enunciado").exists())
        .andExpect(jsonPath("$.campos.explicacao").exists())
        .andExpect(jsonPath("$.campos.ordem").exists());
  }

  @Test
  void referenciaPrecisaSerHttpsValido() throws Exception {
    for (String url : List.of("nao-e-url", "http://example.com", "javascript:alert(1)")) {
      var dados = valido();
      dados.put("referenciaUrl", url);
      mvc.perform(
              post("/api/afirmacoes")
                  .contentType("application/json")
                  .content(mapper.writeValueAsString(dados)))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.campos.referenciaUrl").exists());
    }
  }

  @Test
  void jsonInvalidoEnumDesconhecidoENumericoSaoRejeitados() throws Exception {
    for (String body :
        List.of(
            "{",
            "{\"resposta\":\"TALVEZ\"}",
            "{\"resposta\":1}",
            "{\"resposta\":null}",
            "{\"resposta\":\"FALSO\",\"extra\":true}")) {
      mvc.perform(
              post("/api/quiz/" + id + "/respostas").contentType("application/json").content(body))
          .andExpect(status().isBadRequest());
    }
  }

  @Test
  void parametrosInvalidosSao400() throws Exception {
    mvc.perform(get("/api/quiz/abc")).andExpect(status().isBadRequest());
    mvc.perform(get("/api/quiz/0")).andExpect(status().isBadRequest());
    mvc.perform(get("/api/quiz").param("texto", "x".repeat(101)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void operacoesComIdAusenteSao404() throws Exception {
    long ausente = Long.MAX_VALUE;
    mvc.perform(get("/api/quiz/" + ausente))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404));
    mvc.perform(
            put("/api/afirmacoes/" + ausente)
                .contentType("application/json")
                .content(mapper.writeValueAsString(valido())))
        .andExpect(status().isNotFound());
    mvc.perform(delete("/api/afirmacoes/" + ausente)).andExpect(status().isNotFound());
    mvc.perform(
            post("/api/quiz/" + ausente + "/respostas")
                .contentType("application/json")
                .content("{\"resposta\":\"FALSO\"}"))
        .andExpect(status().isNotFound());
  }

  @Test
  void listaOrdenadaPorPosicaoEId() throws Exception {
    var a = valido();
    a.put("ordem", 1);
    mvc.perform(
            post("/api/afirmacoes")
                .contentType("application/json")
                .content(mapper.writeValueAsString(a)))
        .andExpect(status().isCreated());
    mvc.perform(get("/api/quiz"))
        .andExpect(jsonPath("$[0].id").value(id))
        .andExpect(jsonPath("$[1].ordem").value(1));
  }

  @Test
  void openApiDocumentaTodosOsCaminhos() throws Exception {
    mvc.perform(get("/v3/api-docs"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.info.title").value("REACTIO API"))
        .andExpect(jsonPath("$.paths['/api/afirmacoes'].post.responses['201']").exists())
        .andExpect(jsonPath("$.paths['/api/quiz/{id}/respostas'].post.responses['400']").exists())
        .andExpect(jsonPath("$.paths['/api/quiz/{id}/respostas'].post.responses['404']").exists());
  }

  @Test
  void metodosETiposDeConteudoInvalidos() throws Exception {
    mvc.perform(patch("/api/afirmacoes/" + id)).andExpect(status().isMethodNotAllowed());
    mvc.perform(post("/api/afirmacoes").contentType("text/plain").content("texto"))
        .andExpect(status().isUnsupportedMediaType());
  }
}
