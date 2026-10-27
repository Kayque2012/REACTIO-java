# Roteiro de conferência - REACTIO

1. Execute `clean verify` e inicie a aplicação pelo README.
2. Abra o Swagger e mostre a separação Editorial / Quiz.
3. Consulte GET /api/quiz: nenhum item deve ter gabarito ou explicação.
4. Responda um desafio corretamente e incorretamente. Nos dois casos, confira explicação e fonte.
5. Selecione um caso DEPENDE e leia as condições da explicação.
6. Use o exemplo de cadastro no POST /api/afirmacoes. Confira 201 e Location.
7. Pesquise RADIAÇÃO, consulte o ID criado e altere seu enunciado pelo PUT.
8. Envie cadastro com ordem zero, campo em branco ou URL inválida; espere 400.
9. Reinicie na mesma pasta. Confira que o conteúdo criado/alterado permanece.
10. Exclua o item criado. Confira 204 e, na consulta seguinte, 404.
11. Confira GET /v3/api-docs e os parâmetros/respostas documentados.

Para publicação, copie o conteúdo do projeto para um repositório público próprio. Não publique data/, target/, credenciais ou logs. Confira o link em janela sem autenticação e envie pelo portal. Não simule commits de integrantes: cada pessoa deve realizar suas contribuições reais.
