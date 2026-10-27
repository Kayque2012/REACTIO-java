package br.com.reactio.repository;

import br.com.reactio.domain.Afirmacao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AfirmacaoRepository extends JpaRepository<Afirmacao, Long> {
  List<Afirmacao> findAllByOrderByOrdemAscIdAsc();

  List<Afirmacao> findByEnunciadoContainingIgnoreCaseOrderByOrdemAscIdAsc(String texto);
}
