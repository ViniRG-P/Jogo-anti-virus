import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Collections;

// Tablureiro 5x5
public class Tabuleiro {

  public static final int TAM = 5;
  public static final Posicao CENTRO = new Posicao(3, 3);

  // Portas horizontais
  private static final String[] HORIZONTAIS = {"1101", "1011", "1111", "0111", "1110"};
  // Portas verticais
  private static final String[] VERTICAIS = {"10110", "11101", "01110", "10101"};

  private final Setor[][] setores = new Setor[TAM][TAM];
  private final Posicao fonte;

  public Tabuleiro(Random rnd) {

    String[] tipos = gerarTiposAleatorios(rnd);

    for (int y = 0; y < TAM; y++)
        for (int x = 0; x < TAM; x++)
            setores[y][x] = criar(tipos[y].charAt(x), x + 1, y + 1);

    for (int y = 0; y < TAM; y++){
      for (int x = 0; x < TAM - 1; x++){
        if (HORIZONTAIS[y].charAt(x) == '1') {
          setores[y][x].setPorta(Direcao.LESTE, true);
          setores[y][x + 1].setPorta(Direcao.OESTE, true);
        }
      }
    }
    for (int y = 0; y < TAM - 1; y++)
      for (int x = 0; x < TAM; x++)
        if (VERTICAIS[y].charAt(x) == '1') {
          setores[y][x].setPorta(Direcao.SUL, true);
          setores[y + 1][x].setPorta(Direcao.NORTE, true);
        }

    getSetor(CENTRO).setVisitado();

    int fx, fy;

    do {
      fx = 1 + rnd.nextInt(TAM);
      fy = 1 + rnd.nextInt(TAM);
    } while (fx == CENTRO.getX() && fy == CENTRO.getY());

    fonte = new Posicao(fx, fy);
  }

// 15 normais, 4 ocultos e 5 privados
private static String[] gerarTiposAleatorios(Random rnd) {

    List<Character> tipos = new ArrayList<>();

    for (int i = 0; i < 15; i++) {
        tipos.add('N');
    }

    for (int i = 0; i < 4; i++) {
        tipos.add('O');
    }

    for (int i = 0; i < 5; i++) {
        tipos.add('P');
    }

    Collections.shuffle(tipos, rnd);

    String[] matriz = new String[TAM];

    int indice = 0;

    for (int y = 0; y < TAM; y++) {

        StringBuilder linha = new StringBuilder();

        for (int x = 0; x < TAM; x++) {
           // O centro deve ser setor normal
            if (x == CENTRO.getX() - 1 && y == CENTRO.getY() - 1) {
                linha.append('N');
            } else {
                linha.append(tipos.get(indice));
                indice++;
            }
        }

        matriz[y] = linha.toString();
    }

    return matriz;
}


  private static Setor criar(char tipo, int x, int y) {

    switch (tipo) {
      case 'O':
        return new SetorOculto(x, y);
      case 'P':
        return new SetorPrivado(x, y);
      default:
        return new SetorNormal(x, y);
    }
  }

  public Setor getSetor(Posicao p) {
    return setores[p.getY() - 1][p.getX() - 1];
  }

  public Posicao getFonte() {
    return fonte;
  }

  public List<Setor> getSetores() {
    List<Setor> lista = new ArrayList<Setor>();

    for (Setor[] linha : setores)
      for (Setor s : linha) lista.add(s);

    return lista;
  }


  // Desenha o mapa
  // Usado IA
  public String desenhar(List<Jogador> jogadores) {

    StringBuilder sb = new StringBuilder();

    for (int y = 0; y < TAM; y++) {
      for (int x = 0; x < TAM; x++) {
        sb.append('+');
        if (y == 0)
          sb.append("---");
        else if (!conhecido(x, y - 1, x, y))
          sb.append("...");
        else
          sb.append(setores[y][x].temPorta(Direcao.NORTE) ? "   " : "---");
      }

      sb.append("+\n");
      for (int x = 0; x < TAM; x++) {
        if (x == 0)
          sb.append('|');
        else if (!conhecido(x - 1, y, x, y))
          sb.append(':');
        else
          sb.append(setores[y][x].temPorta(Direcao.OESTE) ? ' ' : '|');

        Setor s = setores[y][x];
        sb.append(s.isVisitado() ? s.simbolo() : '?');
        
        for (int i = 0; i < 2; i++) {
          boolean aqui = i < jogadores.size() && jogadores.get(i).estaVivo()
              && jogadores.get(i).getPos().equals(s.getPos());
          sb.append(aqui ? String.valueOf(i + 1) : " ");
        }
      }
      sb.append("|\n");
    }
    sb.append("+---".repeat(TAM)).append('+');
    return sb.toString();
  }

  private boolean conhecido(int x1, int y1, int x2, int y2) {
    return setores[y1][x1].isVisitado() || setores[y2][x2].isVisitado();
  }
}
