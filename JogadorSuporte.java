import java.util.ArrayList;
import java.util.List;

// 1 de ATK e 7 de DEF
// Exige um jogadorSimples
// Recupera DEF
public class JogadorSuporte extends Jogador {
  private static final int ATK = 1, DEF = 7, CURA = 2;
  private final JogadorSimples parceiro;

  public JogadorSuporte(String nome, Posicao pos, JogadorSimples parceiro) {
    super(nome, ATK, DEF, pos);
    // IA para fazer esses erros
    if (parceiro == null) {
      throw new IllegalArgumentException("O jogador de suporte não pode existir sem um jogador simples.");
    }
    if (ATK >= parceiro.getAtk()) {
      throw new IllegalArgumentException("O ATK do suporte deve ser menor que o do jogador simples.");
    }
    this.parceiro = parceiro;
  }

  public boolean podeRecuperar() {
    return true;
  }

  public String descricao() {
    return "suporte";
  }


  public List<Jogador> alvosPossiveis() {

    List<Jogador> alvos = new ArrayList<Jogador>();
    if(this.estaVivo()){
      alvos.add(this);
    }

    if (parceiro.estaVivo() && parceiro.getPos().equals(getPos())) {
      alvos.add(parceiro);
    }
    return alvos;
  }

  public String recuperar(Jogador alvo) {

    if (!alvosPossiveis().contains(alvo)) {
      return "Alvo inválido para recuperação";
    }

    alvo.ganharDef(CURA);
    return getNome() + " recuperou " + CURA + " de DEF de " + alvo.getNome() + ".";
  }
}
