package br.com.reactio.api.error;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.*;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.*;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
  private static final org.slf4j.Logger LOG =
      org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);

  private ResponseEntity<ApiError> erro(
      int status, String mensagem, Map<String, String> campos, HttpServletRequest r) {
    return ResponseEntity.status(status)
        .body(new ApiError(Instant.now(), status, mensagem, r.getRequestURI(), campos));
  }

  @ExceptionHandler(RecursoNaoEncontradoException.class)
  ResponseEntity<ApiError> ausente(RecursoNaoEncontradoException e, HttpServletRequest r) {
    return erro(404, e.getMessage(), Map.of(), r);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<ApiError> validacao(MethodArgumentNotValidException e, HttpServletRequest r) {
    Map<String, String> campos = new TreeMap<>();
    e.getBindingResult()
        .getFieldErrors()
        .forEach(f -> campos.putIfAbsent(f.getField(), f.getDefaultMessage()));
    return erro(400, "Dados inválidos. Confira os campos.", campos, r);
  }

  @ExceptionHandler({
    HttpMessageNotReadableException.class,
    MethodArgumentTypeMismatchException.class,
    HandlerMethodValidationException.class
  })
  ResponseEntity<ApiError> formato(Exception e, HttpServletRequest r) {
    return erro(
        400,
        "Formato inválido. Confira JSON, identificador, tamanho do texto e classificação"
            + " (VERDADEIRO, FALSO ou DEPENDE).",
        Map.of(),
        r);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  ResponseEntity<ApiError> rota(Exception e, HttpServletRequest r) {
    return erro(404, "Rota não encontrada.", Map.of(), r);
  }

  @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
  ResponseEntity<ApiError> metodo(Exception e, HttpServletRequest r) {
    return erro(405, "Método HTTP não permitido nesta rota.", Map.of(), r);
  }

  @ExceptionHandler(org.springframework.web.HttpMediaTypeNotSupportedException.class)
  ResponseEntity<ApiError> midia(Exception e, HttpServletRequest r) {
    return erro(415, "Use Content-Type application/json.", Map.of(), r);
  }

  @ExceptionHandler(Exception.class)
  ResponseEntity<ApiError> inesperado(Exception e, HttpServletRequest r) {
    LOG.error("Erro inesperado em {}", r.getRequestURI(), e);
    return erro(500, "Não foi possível concluir a operação.", Map.of(), r);
  }
}
