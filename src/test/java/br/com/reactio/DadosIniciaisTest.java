package br.com.reactio;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.reactio.config.DadosIniciais;
import br.com.reactio.domain.Classificacao;
import br.com.reactio.repository.AfirmacaoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
    properties = {
      "spring.datasource.url=jdbc:h2:mem:seed-test;DB_CLOSE_DELAY=-1",
      "spring.jpa.hibernate.ddl-auto=create-drop",
      "reactio.seed.enabled=true"
    })
@org.springframework.test.context.TestExecutionListeners(
    listeners =
        org.springframework.test.context.support.DependencyInjectionTestExecutionListener.class,
    mergeMode = org.springframework.test.context.TestExecutionListeners.MergeMode.REPLACE_DEFAULTS)
class DadosIniciaisTest {
  @Autowired AfirmacaoRepository repository;
  @Autowired DadosIniciais seeder;

  @Test
  void cargaTemTresClassificacoesEFonteENaoDuplica() throws Exception {
    assertThat(repository.count()).isEqualTo(9);
    assertThat(repository.findAll())
        .extracting(a -> a.getClassificacao())
        .contains(Classificacao.VERDADEIRO, Classificacao.FALSO, Classificacao.DEPENDE);
    assertThat(repository.findAll())
        .allSatisfy(
            a -> {
              assertThat(a.getExplicacao()).isNotBlank();
              assertThat(a.getReferenciaUrl()).startsWith("https://");
            });
    seeder.run(new DefaultApplicationArguments(new String[0]));
    assertThat(repository.count()).isEqualTo(9);
  }
}
