package br.com.reactio.api.error;

public class RecursoNaoEncontradoException extends RuntimeException {
  public RecursoNaoEncontradoException(Long id) {
    super("Afirmação " + id + " não encontrada.");
  }
}
