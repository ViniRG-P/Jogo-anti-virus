// 2 de ATK e 6 de DEF
// Sem habilidade especial
public class JogadorSimples extends Jogador {
  public JogadorSimples(String nome, Posicao pos) {
    super(nome, 2, 6, pos);
  }

  public boolean podeRecuperar() {
    return false;
  }

  public String descricao() {
    return "simples";
  }
}
