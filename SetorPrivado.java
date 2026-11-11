// Não pode usar o procurar
public class SetorPrivado extends Setor {

  public SetorPrivado(int x, int y) {
    super(x, y);
  }

  public char simbolo() {
    return 'P';
  }

  public String getTipo() {
    return "privado";
  }

  public boolean podeProcurar() {
    return false;
  }
}
