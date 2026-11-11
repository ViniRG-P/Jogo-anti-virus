import java.util.Random;

// Jogadores
public abstract class Jogador extends Personagem {

  public Jogador(String nome, int atk, int def, Posicao pos) {
    super(nome, atk, def, pos);
  }

  public abstract boolean podeRecuperar();

  // Sorteia um numero de 1 a 6 e aplica o efeito
  public String procurar(Setor setor, Random aleat) {

    if (!setor.podeProcurar()) {
      return "Este setor é privado: não é possível procurar aqui.";
    }

    int num = 1 + aleat.nextInt(6);
    String msg = getNome() + " tirou " + num + " na procura: ";
    
    if (num <= 3) {
      return msg + "nada foi encontrado.";
    } else if (num == 4) {
      ganharDef(1);
      return msg + "+1 de DEF!";
    } else if (num == 5) {
      ganharDef(2);
      return msg + "+2 de DEF!";
    }

    // Usado IA para simplificar o loop
    for (Inimigo i : setor.inimigosVivos()) {
      i.receberDano(1);
    }
    setor.removerMortos();
    return msg + "todos os vírus perderam 1 de DEF!";
  }
}
