package br.com.reactio.api.dto;

import br.com.reactio.domain.Classificacao;

public record FeedbackResponse(
    Long afirmacaoId,
    boolean acertou,
    Classificacao respostaEnviada,
    Classificacao classificacaoCorreta,
    String explicacao,
    String referenciaTitulo,
    String referenciaUrl) {}
