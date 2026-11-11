
public final class Posicao {
  private final int x, y;

  public Posicao(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }
  public int getY() {
    return y;
  }

  public Posicao mover(Direcao d) {
    return new Posicao(x + d.getDx(), y + d.getDy());
  }

  public boolean equals(Object objeto) {
    return objeto instanceof Posicao p && p.x == x && p.y == y;
  }
  public int hashCode() {
    return 31 * x + y;
  }
  public String toString() {
    return "[" + x + "," + y + "]";
  }
}
