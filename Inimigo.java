// Virus
public class Inimigo extends Personagem {

  public Inimigo(String nome, int forca,int defesa, Posicao pos) {
    super(nome, forca, defesa, pos);
  }

  public String descricao() {
    return "vírus";
  }
}
