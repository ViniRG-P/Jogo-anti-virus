// Setor sem restrições
public class SetorNormal extends Setor {

  public SetorNormal(int x, int y) {
    super(x, y);
  }

  public char simbolo() {
    return 'N';
  }

  public String getTipo() {
    return "normal";
  }
}
