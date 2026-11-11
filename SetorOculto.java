import java.util.Random;

// O ataque tem 50% de chance de matar e 50% de chance de errar
public class SetorOculto extends Setor {

  public SetorOculto(int x, int y) {
    super(x, y);
  }
  
  public char simbolo() {
    return 'O';
  }
  
  public String getTipo() {
    return "oculto";
  }

  public String atacar(Jogador j, Inimigo alvo, Random rnd) {

    if (rnd.nextBoolean() == true) {
      alvo.receberDano(alvo.getDef());
      return j.getNome() + " eliminou um vírus no setor oculto.";
    }
    return j.getNome() + " atacou no setor oculto, mas errou o ataque.";
  }
}
