import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

// Controla o jogo
public class Jogo {
  private static final int MAX_CICLOS = 25;

  private enum Acao {
    ATACAR("Atacar um vírus"),
    PROCURAR("Procurar itens"),
    MOVER("Mover para outro setor"),
    RECUPERAR("Recuperar defesa (habilidade do suporte)"),
    PASSAR("Encerrar turno");

    final String rotulo;
    Acao(String rotulo) {
      this.rotulo = rotulo;
    }
  }

  private final Scanner entrada = new Scanner(System.in);
  private final Random rnd = new Random();
  private final Tabuleiro tabuleiro = new Tabuleiro(rnd);
  private final List<Jogador> jogadores = new ArrayList<Jogador>();
  private final JogadorSimples p1;
  private final JogadorSuporte p2;
  private Jogador vencedor;

  public Jogo() {
    p1 = new JogadorSimples("P1", Tabuleiro.CENTRO);
    p2 = new JogadorSuporte("P2", Tabuleiro.CENTRO, p1);
    jogadores.add(p1);
    jogadores.add(p2);
  }

  public void iniciar() {
    System.out.println("=== Inicio de jogo ===");
    System.out.println("Encontre a fonte de infecção em até " + MAX_CICLOS + " ciclos.");
    System.out.println("Legenda dos setores: N=normal O=oculto P=privado ?=não visitado");

    // Turno 0: P1 e depois P2 saem do setor inicial.
    System.out.println(Cores.colorir(Cores.ANSI_CYAN,"\n--- Turno 0: escolham uma porta do setor inicial ---"));
    for (Jogador j : jogadores) {
      System.out.println(tabuleiro.desenhar(jogadores));
      moverJogador(j);
      if (vencedor != null)
        break;
    }

    int ciclo = 1;
    while (vencedor == null && ciclo <= MAX_CICLOS && existeVivo()) {
      System.out.println(Cores.colorir(Cores.ANSI_CYAN, "\n---------- CICLO " + ciclo + "/" + MAX_CICLOS + " ----------"));
      for (Jogador j : jogadores) {
        if (!j.estaVivo())
          continue;
        turnoJogador(j);
        if (vencedor != null)
          break;
      }
      if (vencedor == null)
        turnoInimigos();
      ciclo++;
    }

    System.out.println();
    if (vencedor != null) {
      System.out.println("Vitória! " + vencedor.getNome() + " encontrou a fonte da infecção " + tabuleiro.getFonte() + "!");
    } else if (!existeVivo()) {
      System.out.println("Derrota: todos os jogadores foram eliminados.");
    } else {
      System.out.println("Derrota: o limite de " + MAX_CICLOS + " ciclos foi excedido. A fonte estava em " + tabuleiro.getFonte() + ".");
    }
  }

  private boolean existeVivo() {

    for (Jogador j : jogadores)
      if (j.estaVivo())
        return true;
    return false;
  }

  // Turno dos jogadores
  private void turnoJogador(Jogador j) {

    System.out.println("\nTurno de " + j.getNome());
    int acoes = 2;

    while (acoes > 0 && vencedor == null && j.estaVivo()) {
      Setor s = tabuleiro.getSetor(j.getPos());
      mostrarSituacao(j, s);

      List<Acao> opcoes = new ArrayList<Acao>();
      opcoes.add(s.inimigosVivos().isEmpty() ? Acao.MOVER : Acao.ATACAR);

      if (s.podeProcurar())
        opcoes.add(Acao.PROCURAR);
      if (j.podeRecuperar())
        opcoes.add(Acao.RECUPERAR);
      opcoes.add(Acao.PASSAR);

      System.out.println("Ações restantes: " + acoes);

      for (int i = 0; i < opcoes.size(); i++) {
        System.out.println((i + 1) + ") " + opcoes.get(i).rotulo);
      }
      Acao escolhida = opcoes.get(lerInt(1, opcoes.size()) - 1);

      switch (escolhida) {
        case ATACAR:
          atacar(j, s);
          acoes--;
          break;
        case PROCURAR:
          System.out.println(j.procurar(s, rnd));
          acoes--;
          break;
        case MOVER:
          moverJogador(j);
          acoes--;
          break;
        case RECUPERAR:
          recuperar((JogadorSuporte) j);
          acoes--;
          break;
        default:
          acoes = 0;
      }
    }
  }
  // Imprime o estado dos jogadores, inimigos, setor atual e do tabuleiro 
  private void mostrarSituacao(Jogador j, Setor s) {

    System.out.println("\n" + tabuleiro.desenhar(jogadores));

    for (Jogador x : jogadores) {
      System.out.println(x + (x.estaVivo() ? "" : " [ELIMINADO]") + " em " + x.getPos());
    }
    if (s instanceof SetorOculto) {
      System.out.println("Setor " + s.getPos() + " (oculto) - localização dos vírus não identificada.");
    } else {
      System.out.println("Setor " + s.getPos() + " (" + s.getTipo() + ") - vírus: "
          + (s.inimigosVivos().isEmpty() ? "nenhum" : s.inimigosVivos()));
    }
  }

  private void atacar(Jogador j, Setor s) {

    List<Inimigo> alvos = s.inimigosVivos();
    Inimigo alvo;

    if (s instanceof SetorOculto) {
      // O jogador não escolhe nem visualiza o vírus no setor oculto.
      alvo = alvos.get(rnd.nextInt(alvos.size()));
    } else {
      System.out.println("Escolha o alvo:");

      for (int i = 0; i < alvos.size(); i++) {
        System.out.println((i + 1) + ") " + alvos.get(i));
      }

      alvo = alvos.get(lerInt(1, alvos.size()) - 1);
    }

    System.out.println(s.atacar(j, alvo, rnd));

    if (!alvo.estaVivo()) {
      if (s instanceof SetorOculto)
        System.out.println("Um vírus foi eliminado.");
      else
        System.out.println(alvo.getNome() + " foi eliminado!");
    }

    s.removerMortos();
  }

  private void recuperar(JogadorSuporte sup) {

    List<Jogador> alvos = sup.alvosPossiveis();
    System.out.println("Quem recupera defesa?");

    for (int i = 0; i < alvos.size(); i++) {
      System.out.println((i + 1) + ") " + alvos.get(i));
    }

    System.out.println(sup.recuperar(alvos.get(lerInt(1, alvos.size()) - 1)));
  }

  private void moverJogador(Jogador j) {

    Setor atual = tabuleiro.getSetor(j.getPos());

    if (!atual.inimigosVivos().isEmpty()) {
      System.out.println("Ainda existem vírus no setor!");
      return;
    }

    List<Direcao> portas = new ArrayList<>();

    for (Direcao d : Direcao.values())
      if (atual.temPorta(d))
        portas.add(d);

    System.out.println(j.getNome() + " está em " + j.getPos() + ". Portas disponíveis:");

    for (int i = 0; i < portas.size(); i++) {
      System.out.println((i + 1) + ") " + portas.get(i));
    }

    Direcao d = portas.get(lerInt(1, portas.size()) - 1);

    j.setPos(j.getPos().mover(d));
    System.out.println(j.getNome() + " moveu-se para " + j.getPos() + ".");

    if (j.getPos().equals(tabuleiro.getFonte())) {
      vencedor = j;
      return;
    }

    Setor novo = tabuleiro.getSetor(j.getPos());

    if (!novo.isVisitado()) {
      novo.visitar(rnd);
      if (novo instanceof SetorOculto) {
        System.out.println("Novo setor oculto! A localização dos vírus não será revelada.");
      } else {
        System.out.println("Setor novo (" + novo.getTipo() + ")! "
            + novo.inimigosVivos().size() + " vírus surgiram: " + novo.inimigosVivos());
      }
    }
  }

  // Turno dos vírus
private void turnoInimigos() {

    System.out.println("\nTurno dos vírus");

    for (Setor s : tabuleiro.getSetores()) {
      for (Inimigo v : s.inimigosVivos()) {
        for (Jogador j : jogadores) {
          if (j.estaVivo() && j.getPos().equals(s.getPos())) {
              processarAtaque(v, j, rnd, s instanceof SetorOculto);
          }
        }
      }
    }
  }

  private void processarAtaque(Inimigo v, Jogador j, Random rnd, boolean setorOculto) {
    int d6 = 1 + rnd.nextInt(6);

    if (d6 % 2 == 0) {
        j.receberDano(v.getAtk());

        if (setorOculto) {
          System.out.printf("Um vírus oculto atacou %s, nova DEF: %d.\n",j.getNome(), j.getDef());
        } 
        else {
          System.out.printf("%s (%d/%d) atacou %s , DEF agora %d.\n", v.getNome(), v.getAtk(), v.getDef(), j.getNome(), j.getDef());
        }

        if (!j.estaVivo()) {
            System.out.println(j.getNome() + " foi ELIMINADO!");
        }
    } 
    else {
        if (setorOculto) {
            System.out.printf("Um vírus oculto tentou atacar %s, mas falhou.\n",j.getNome());
        } else {
          System.out.printf("%s tentou atacar %s, mas falhou.\n", v.getNome(), j.getNome());
        }
    }
  }

  // Leitura de entrada do usuário
  private int lerInt(int min, int max) {

    while (true) {
        System.out.print("> ");

        if (!entrada.hasNextLine()) {
            System.out.println("\nEntrada encerrada.");
            System.exit(0);
        }

        if (entrada.hasNextInt()) {
            int v = entrada.nextInt();
            entrada.nextLine(); 
            
            if (v >= min && v <= max) {
                return v;
            }
        } 
        else {
            entrada.nextLine();
        }

        System.out.println("Opção inválida. Digite um número de " + min + " a " + max + ".");
    }
  }
}
