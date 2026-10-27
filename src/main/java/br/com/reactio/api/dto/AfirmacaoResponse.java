package br.com.reactio.api.dto;

import br.com.reactio.domain.*;

public record AfirmacaoResponse(
    Long id,
    String enunciado,
    String tema,
    Classificacao classificacao,
    String explicacao,
    String referenciaTitulo,
    String referenciaUrl,
    Integer ordem) {
  public static AfirmacaoResponse de(Afirmacao a) {
    return new AfirmacaoResponse(
        a.getId(),
        a.getEnunciado(),
        a.getTema(),
        a.getClassificacao(),
        a.getExplicacao(),
        a.getReferenciaTitulo(),
        a.getReferenciaUrl(),
        a.getOrdem());
  }
}
