// Jogadores e virus
public interface Combatente {

  String getNome();
  int getAtk();
  int getDef();

  boolean estaVivo();

  void receberDano(int dano);
}
