package br.com.reactio;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
    info =
        @Info(
            title = "REACTIO API",
            version = "1.0.0",
            description =
                "Sprint 1. CRUD editorial e quiz. Editorial sem autenticação: demonstração"
                    + " acadêmica local."))
public class ReactioApplication {
  public static void main(String[] args) {
    SpringApplication.run(ReactioApplication.class, args);
  }
}
