package br.com.reactio.service;

import br.com.reactio.api.dto.*;
import br.com.reactio.api.error.RecursoNaoEncontradoException;
import br.com.reactio.domain.*;
import br.com.reactio.repository.AfirmacaoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AfirmacaoService {
  private final AfirmacaoRepository repository;

  public AfirmacaoService(AfirmacaoRepository repository) {
    this.repository = repository;
  }

  private List<Afirmacao> selecionar(String texto) {
    return texto == null || texto.isBlank()
        ? repository.findAllByOrderByOrdemAscIdAsc()
        : repository.findByEnunciadoContainingIgnoreCaseOrderByOrdemAscIdAsc(texto.strip());
  }

  public List<AfirmacaoResponse> listar(String texto) {
    return selecionar(texto).stream().map(AfirmacaoResponse::de).toList();
  }

  public List<DesafioResponse> desafios(String texto) {
    return selecionar(texto).stream().map(DesafioResponse::de).toList();
  }

  private Afirmacao encontrar(Long id) {
    return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException(id));
  }

  public AfirmacaoResponse consultar(Long id) {
    return AfirmacaoResponse.de(encontrar(id));
  }

  public DesafioResponse desafio(Long id) {
    return DesafioResponse.de(encontrar(id));
  }

  @Transactional
  public AfirmacaoResponse criar(AfirmacaoRequest r) {
    return AfirmacaoResponse.de(
        repository.save(
            new Afirmacao(
                r.enunciado(),
                r.tema(),
                r.classificacao(),
                r.explicacao(),
                r.referenciaTitulo(),
                r.referenciaUrl(),
                r.ordem())));
  }

  @Transactional
  public AfirmacaoResponse atualizar(Long id, AfirmacaoRequest r) {
    Afirmacao a = encontrar(id);
    a.atualizar(
        r.enunciado(),
        r.tema(),
        r.classificacao(),
        r.explicacao(),
        r.referenciaTitulo(),
        r.referenciaUrl(),
        r.ordem());
    return AfirmacaoResponse.de(repository.save(a));
  }

  @Transactional
  public void excluir(Long id) {
    repository.delete(encontrar(id));
  }

  public FeedbackResponse responder(Long id, RespostaRequest r) {
    Afirmacao a = encontrar(id);
    return new FeedbackResponse(
        a.getId(),
        a.getClassificacao() == r.resposta(),
        r.resposta(),
        a.getClassificacao(),
        a.getExplicacao(),
        a.getReferenciaTitulo(),
        a.getReferenciaUrl());
  }
}
