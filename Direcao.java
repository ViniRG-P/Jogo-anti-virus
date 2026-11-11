// Usado IA para o uso de enum
public enum Direcao {

    NORTE(0, -1),
    SUL(0, 1),
    LESTE(1, 0),
    OESTE(-1, 0);

    private final int dx;
    private final int dy;

    Direcao(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }
}
