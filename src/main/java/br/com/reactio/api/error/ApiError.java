package br.com.reactio.api.error;

import java.time.Instant;
import java.util.Map;

public record ApiError(
    Instant timestamp, int status, String mensagem, String caminho, Map<String, String> campos) {}
