package br.com.reactio.config;

import br.com.reactio.api.dto.AfirmacaoRequest;
import br.com.reactio.domain.Afirmacao;
import br.com.reactio.repository.AfirmacaoRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validator;
import java.util.List;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@ConditionalOnProperty(name = "reactio.seed.enabled", havingValue = "true", matchIfMissing = true)
public class DadosIniciais implements ApplicationRunner {
  private final AfirmacaoRepository repository;
  private final ObjectMapper mapper;
  private final Validator validator;

  public DadosIniciais(AfirmacaoRepository repository, ObjectMapper mapper, Validator validator) {
    this.repository = repository;
    this.mapper = mapper;
    this.validator = validator;
  }

  @Override
  @Transactional
  public void run(ApplicationArguments args) throws Exception {
    if (repository.count() != 0) return;
    try (var input = new ClassPathResource("dados-iniciais.json").getInputStream()) {
      List<AfirmacaoRequest> dados = mapper.readValue(input, new TypeReference<>() {});
      for (AfirmacaoRequest r : dados) {
        if (!validator.validate(r).isEmpty())
          throw new IllegalStateException("Dados iniciais inválidos.");
        repository.save(
            new Afirmacao(
                r.enunciado(),
                r.tema(),
                r.classificacao(),
                r.explicacao(),
                r.referenciaTitulo(),
                r.referenciaUrl(),
                r.ordem()));
      }
    }
  }
}
