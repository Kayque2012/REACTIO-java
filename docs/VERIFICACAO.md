# Revisão final - REACTIO / Sprint 1

Verificação realizada em 27/10/2026, em Windows com Java 17 e Maven Wrapper 3.9.11.

## Java

- Compilação e empacotamento: BUILD SUCCESS.
- Suíte final: **15 testes, 0 falhas, 0 erros, 0 ignorados**.
- Execução original com Maven Wrapper, antes da remoção solicitada do script Windows. Para repetir a verificação, use `mvn clean verify` com Maven disponível no terminal ou execute `verify` pela janela Maven do IntelliJ.
- CRUD completo, busca por trecho, ordenação, validação, erros, quiz e OpenAPI: aprovados.
- H2 em arquivo: um teste criou e alterou um registro, fechou o contexto Spring e o reabriu; o registro e a alteração permaneceram.
- HTTP real: GET /api/quiz retornou nove registros sem gabarito; POST editorial retornou 201; resposta ao quiz trouxe feedback e fonte; Swagger UI retornou 200.
- O contrato de cinco caminhos e oito operações foi exportado da aplicação para `docs/openapi.json`.
- Código formatado e configurações/dados revisados.

Os relatórios resumidos de teste estão em `docs/evidencias/`. O teste de persistência fecha a aplicação normalmente. Uma tentativa separada com encerramento forçado do processo no ambiente restrito não reabriu o banco de forma utilizável; não se considera aprovado esse cenário de falha. Para uso normal, encerrar com Ctrl+C e executar novamente na mesma pasta. Não há alegação de tolerância a queda ou recuperação de desastre.

## Compliance

PDF único, sete páginas. Conferidos os oito critérios: problema, público, objetivo, descrição, três funcionalidades, diferencial, duas referências comparadas e quadro das sete disciplinas. Todas as páginas foram renderizadas e inspecionadas visualmente. Conteúdo e afirmações alinhados ao recorte Java. Público de todas as faixas etárias, com uso mediado quando necessário. Fontes e limites identificados.

## Identificação

Turma 2TDSPB, informada pelo grupo.

## Pendências externas
- Publicar o projeto em repositório público próprio no GitHub e validar o acesso sem login.
- Enviar PDF de Compliance e link público de Java nas entregas correspondentes do portal.
- Alinhar contribuições propostas de .NET, Banco de Dados, Mobile, DevOps e Redes com os responsáveis.

Não foram feitos implantação pública, envio ao portal, testes com usuários ou validação de acessibilidade do aplicativo. Os endpoints editoriais não têm autenticação; o padrão de rede é local.
