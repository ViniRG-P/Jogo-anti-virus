public abstract class Personagem implements Combatente {
  private final String nome;
  private final int atk;
  private int def;
  private Posicao pos;

  public Personagem(String nome, int atk, int def, Posicao pos) {
    this.nome = nome;
    this.atk = atk;
    this.def = def;
    this.pos = pos;
  }

  public String getNome() {
    return this.nome;
  }
  public int getAtk() {
    return this.atk;
  }
  public int getDef() {
    return this.def;
  }
  public Posicao getPos() {
    return this.pos;
  }
  public void setPos(Posicao pos) {
    this.pos = pos;
  }

  public boolean estaVivo() {
    return this.def > 0;
  }
  public void receberDano(int dano) {
    this.def -= dano;
  }
  public void ganharDef(int valor) {
    this.def += valor;
  }

  // Tipo do personagem
  public abstract String descricao();

  public String toString() {
    return nome + " (" + descricao() + ") " + atk + "/" + def;
  }
}
