import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Classe abstrata do setor, poosui 4 lados e os inimigos
public abstract class Setor {

  private final Posicao pos;
  private final boolean[] portas = new boolean[Direcao.values().length];
  private boolean visitado;
  private final List<Inimigo> inimigos = new ArrayList<Inimigo>();

  protected Setor(int x, int y) {
    this.pos = new Posicao(x, y);
  }

  public abstract char simbolo();
  public abstract String getTipo();

  public String atacar(Jogador j, Inimigo alvo, Random rnd) {
    
    alvo.receberDano(j.getAtk());
    return j.getNome() + " atacou " + alvo.getNome() + " (-" + j.getAtk() + " DEF).";
  }

  public boolean podeProcurar() {
    return true;
  }

  public Posicao getPos() {
    return pos;
  }
  public boolean isVisitado() {
    return visitado;
  }
  public void setVisitado() {
    visitado = true;
  }

  void setPorta(Direcao d, boolean aberta) {
    portas[d.ordinal()] = aberta;
  }

  public boolean temPorta(Direcao d) {
    return portas[d.ordinal()];
  }

  // Gera os virús
  public void visitar(Random rnd) {
    visitado = true;
    int qtd = 1 + rnd.nextInt(3);
    for (int i = 1; i <= qtd; i++) {
      int valor = 1 + rnd.nextInt(3);
      inimigos.add(new Inimigo("Vírus " + i, valor, valor, pos));
    }
  }

  // IA ajudou na sintaxe
  public List<Inimigo> inimigosVivos() {
    List<Inimigo> vivos = new ArrayList<Inimigo>();
    for (Inimigo i : inimigos) {
      if (i.estaVivo())
        vivos.add(i);
    }
    return vivos;
  }

  public void removerMortos() {
    inimigos.removeIf(i -> !i.estaVivo());
  }
}
