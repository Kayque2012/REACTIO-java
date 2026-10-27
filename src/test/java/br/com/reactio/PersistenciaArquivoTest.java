package br.com.reactio;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.reactio.domain.*;
import br.com.reactio.repository.AfirmacaoRepository;
import java.nio.file.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;

class PersistenciaArquivoTest {
  @Test
  void cadastroEAlteracaoSobrevivemAoFecharEReabrirAplicacao() throws Exception {
    Path dir = Files.createTempDirectory(Path.of("target"), "persistencia-");
    String url =
        "--spring.datasource.url=jdbc:h2:file:"
            + dir.toAbsolutePath().toString().replace('\\', '/')
            + "/reactio";
    Long id;
    try (var app =
        new SpringApplicationBuilder(ReactioApplication.class)
            .web(WebApplicationType.NONE)
            .run(url, "--reactio.seed.enabled=false", "--spring.jpa.hibernate.ddl-auto=update")) {
      var repo = app.getBean(AfirmacaoRepository.class);
      var a =
          repo.saveAndFlush(
              new Afirmacao(
                  "Antes da revisão.",
                  "Cotidiano",
                  Classificacao.FALSO,
                  "Explicação.",
                  "EPA",
                  "https://www.epa.gov/",
                  1));
      id = a.getId();
      a.atualizar(
          "Depois da revisão.",
          "Cotidiano",
          Classificacao.FALSO,
          "Explicação.",
          "EPA",
          "https://www.epa.gov/",
          1);
      repo.saveAndFlush(a);
    }
    try (var app =
        new SpringApplicationBuilder(ReactioApplication.class)
            .web(WebApplicationType.NONE)
            .run(url, "--reactio.seed.enabled=false", "--spring.jpa.hibernate.ddl-auto=update")) {
      var repo = app.getBean(AfirmacaoRepository.class);
      assertThat(repo.findById(id).orElseThrow().getEnunciado()).isEqualTo("Depois da revisão.");
      assertThat(repo.count()).isEqualTo(1);
    }
  }
}
