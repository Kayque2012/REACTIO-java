package br.com.reactio.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "afirmacoes")
public class Afirmacao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 400)
  private String enunciado;

  @Column(nullable = false, length = 80)
  private String tema;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 16)
  private Classificacao classificacao;

  @Column(nullable = false, length = 1500)
  private String explicacao;

  @Column(nullable = false, length = 200)
  private String referenciaTitulo;

  @Column(nullable = false, length = 500)
  private String referenciaUrl;

  @Column(nullable = false)
  private Integer ordem;

  protected Afirmacao() {}

  public Afirmacao(
      String enunciado,
      String tema,
      Classificacao classificacao,
      String explicacao,
      String referenciaTitulo,
      String referenciaUrl,
      Integer ordem) {
    atualizar(enunciado, tema, classificacao, explicacao, referenciaTitulo, referenciaUrl, ordem);
  }

  public void atualizar(
      String enunciado,
      String tema,
      Classificacao classificacao,
      String explicacao,
      String referenciaTitulo,
      String referenciaUrl,
      Integer ordem) {
    this.enunciado = enunciado.strip();
    this.tema = tema.strip();
    this.classificacao = classificacao;
    this.explicacao = explicacao.strip();
    this.referenciaTitulo = referenciaTitulo.strip();
    this.referenciaUrl = referenciaUrl.strip();
    this.ordem = ordem;
  }

  public Long getId() {
    return id;
  }

  public String getEnunciado() {
    return enunciado;
  }

  public String getTema() {
    return tema;
  }

  public Classificacao getClassificacao() {
    return classificacao;
  }

  public String getExplicacao() {
    return explicacao;
  }

  public String getReferenciaTitulo() {
    return referenciaTitulo;
  }

  public String getReferenciaUrl() {
    return referenciaUrl;
  }

  public Integer getOrdem() {
    return ordem;
  }
}
