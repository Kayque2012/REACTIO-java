package br.com.reactio.api.dto;

import br.com.reactio.domain.Afirmacao;

public record DesafioResponse(Long id, String enunciado, String tema, Integer ordem) {
  public static DesafioResponse de(Afirmacao a) {
    return new DesafioResponse(a.getId(), a.getEnunciado(), a.getTema(), a.getOrdem());
  }
}
