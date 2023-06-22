package batalhanaval;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Tabuleiro {

    /*Haverá 2 tabuleiros
        1 - Jogador
        2 - Computador/Outro jogador
    */
    /*
        Tabuleiro
        0 - Vazio
        1 - Navio
        2 - Errou
        3 - Acertou       
    */
    
    //variáveis do tabuleiro
    static final int VAZIO = 0;
    static final int NAVIO = 1;
    static final int ERROU_TIRO = 2;
    static final int ACERTOU_TIRO = 3;
    
    static final int POSICAO_X = 0;
    static final int POSICAO_Y = 1;
    
    static public String nomeJogador1, nomeJogador2;
    static public int tamanhoX, tamanhoY, qtdDeNavios, maxDeNavios;
    static public int tabuleiroJogador1[][], tabuleiroJogador2[][];
    static public Scanner input = new Scanner(System.in);
    static int naviosJogador1, naviosJogador2;

    public static void obterNomeDosJogadores() {
        System.out.println("Digite o nome do Jogador1: ");
        nomeJogador1 = input.next();
        System.out.println("Digite o nome do Jogador2: ");
        nomeJogador2 = input.next();
    }

    public static void obterTamanhoDosTabuleiros() {

        //Scanner input = null;
        for (;;) {
            boolean tudoOk = false; //vai repetir enquanto for false
            try {
                System.out.println("Digite a Altura do tabuleiro");
                tamanhoX = input.nextInt();
                System.out.println("Digite o Comprimento do tabuleiro");
                tamanhoY = input.nextInt();
                tudoOk = true;

            } catch (InputMismatchException erro) {
                System.out.println("Digite um valor numérico");
            }
            if (tudoOk) {
                break;
            }
        }

    }

    public static void iniciandoTamanhoDosTabuleiros() {
        tabuleiroJogador1 = retornarNovoTabuleiroVazio();
        tabuleiroJogador2 = retornarNovoTabuleiroVazio();
    }

    public static int[][] retornarNovoTabuleiroVazio() {
        return new int[tamanhoX][tamanhoY];
    }

    public static void calcularQtdMaxDeNaviosNoJogo() {
        //qtd de navios tem que ser proporcional a matriz do tabuleiro
        maxDeNavios = (tamanhoX * tamanhoY) / 3;
    }

    public static void calcularQtdMaxDeNavios() {
        System.out.println("Digite a quantidade de navios que deseja");
        System.out.println("Max: " + maxDeNavios + " navios!");
        qtdDeNavios = input.nextInt();
        if (qtdDeNavios < 1 && qtdDeNavios > maxDeNavios) {
            qtdDeNavios = maxDeNavios;
        }

    }

    public static int[][] retornarNovoTabuleiroComOsNavios() {
        int novoTabuleiro[][] = retornarNovoTabuleiroVazio();
        int qtdRestanteDeNavios = qtdDeNavios;
        int x = 0, y = 0;
        Random numeroAleatorio = new Random();
        do {
            x = 0;
            y = 0;
            for (int[] linha : novoTabuleiro) {
                for (int coluna : linha) {
                    if (numeroAleatorio.nextInt(100) <= 10) {
                        if (coluna == VAZIO) {
                            novoTabuleiro[x][y] = NAVIO; //quando há um navio na posição
                            qtdRestanteDeNavios--;
                            break;
                        }
                        if (qtdRestanteDeNavios < 0) {
                            break;
                        }
                    }
                    y++;
                }
                y = 0; //quando passar pela linha
                x++;
                if (qtdRestanteDeNavios <= 0) {
                    break;
                }
            }
        } while (qtdRestanteDeNavios > 0);
        return novoTabuleiro;
    }

    public static void inserirOsNaviosNosTabuleirosDosJogadores() {
        tabuleiroJogador1 = retornarNovoTabuleiroComOsNavios();
        tabuleiroJogador2 = retornarNovoTabuleiroComOsNavios();
    }

    public static void exibirNumerosDasColunasDosTabuleiros() {
        int numerosDaColuna = 1;
        String numerosDoTabuleiro = "   ";

        for (int i = 0; i < tamanhoY; i++) {
            numerosDoTabuleiro += (numerosDaColuna++) + " ";
        }
        System.out.println(numerosDoTabuleiro);
    }
    
    public static void exibirTabuleiro(String nomeDoJogador, int[][] tabuleiro, boolean seuTabuleiro) {
        System.out.println("|-------- " + nomeDoJogador + " --------|");
        exibirNumerosDasColunasDosTabuleiros();
        String linhaDoTabuleiro = "";
        char letraDaLinha = 65;
        for (int[] linha : tabuleiro) {
            linhaDoTabuleiro = (letraDaLinha++) + " |";

            for (int coluna : linha) {
                switch (coluna) {
                    case VAZIO:
                        linhaDoTabuleiro += " |";
                        break;
                    case NAVIO:
                        if (seuTabuleiro) {
                            linhaDoTabuleiro += "N|";
                            break;
                        } else {
                            linhaDoTabuleiro += " |";
                            break;
                        }
                    case ERROU_TIRO:
                        linhaDoTabuleiro += "X|";
                        break;
                    case ACERTOU_TIRO:
                        linhaDoTabuleiro += "D|";
                        break;
                }
            }
            System.out.println(linhaDoTabuleiro);
        }
    }

    public static void exibirTabuleiroDosJogadores() {
        exibirTabuleiro(nomeJogador1, tabuleiroJogador1, true);
        exibirTabuleiro(nomeJogador2, tabuleiroJogador2, false);
        //false impede de ver o jogo do adversário(jogador 2)
    }

    public static String receberValorDigitadoPeloJogador() {
        System.out.println("Digite a posição do seu tiro: ");
        return input.next();
    }

    public static boolean validarTiroDoJogador(String tiroDoJogador) {
        int qtdDeNumeros = (tamanhoY > 10) ? 2 : 1;
        //se for verdadeiro '?' será 2
        //se for false ':' será 1
        String expressaoVerificacao = "^[A-Za-z]{1}[0-9]{"
                + qtdDeNumeros + "}$";

        return tiroDoJogador.matches(expressaoVerificacao);
    }

    public static int[] retornaPosicoesDigitadasPelosJogadores(String tiroDoJogador) {
        String tiro = tiroDoJogador.toLowerCase();
        int[] retorno = new int[2];
        retorno[POSICAO_X] = tiro.charAt(0) - 97;
        retorno[POSICAO_Y] = Integer.parseInt(tiro.substring(1)) - 1;
        return retorno;

    }

    public static void inserirValoresDaAçãoNoTabuleiro(int[] posicoes, int numeroDoJoagdor) {
        if (numeroDoJoagdor == 1) {
            if (tabuleiroJogador2[posicoes[POSICAO_X]][posicoes[POSICAO_Y]] == NAVIO) {
                tabuleiroJogador2[posicoes[POSICAO_X]][posicoes[POSICAO_Y]] = ACERTOU_TIRO;
                naviosJogador2--;
                System.out.println("Você acertou o navil inimigo!");
            } else {
                tabuleiroJogador2[posicoes[POSICAO_X]][posicoes[POSICAO_Y]] = ERROU_TIRO;
                System.out.println("Você errou o tiro!");
            }
        } else {
            if (tabuleiroJogador1[posicoes[POSICAO_X]][posicoes[POSICAO_Y]] == NAVIO) {
                tabuleiroJogador1[posicoes[POSICAO_X]][posicoes[POSICAO_Y]] = ACERTOU_TIRO;
                naviosJogador1--;
                System.out.println("Você acertou o navil inimigo!");
            } else {
                tabuleiroJogador1[posicoes[POSICAO_X]][posicoes[POSICAO_Y]] = ERROU_TIRO;
                System.out.println("Você errou o tiro!");
            }
        }

    }

    public static boolean acaoDoJogador() {
        boolean acaoValida = true;
        String tiroDoJogador = receberValorDigitadoPeloJogador();
        if (validarTiroDoJogador(tiroDoJogador)) {
            int[] posicoes = retornaPosicoesDigitadasPelosJogadores(tiroDoJogador);
            if (validarPosicoesInseridaPeloJogador(posicoes)) {
                inserirValoresDaAçãoNoTabuleiro(posicoes, 1);
            } else {
                acaoValida = false;
            }
        } else {
            System.out.println("Posição Inválida");
            acaoValida = false;
        }
        return acaoValida;
    }

    public static boolean validarPosicoesInseridaPeloJogador(int[] posicoes) {
        boolean retorno = true;
        if (posicoes[0] > tamanhoX - 1) {
            //ERRO
            retorno = false;
            System.out.println("A posição das letras não pode ser maior que "
                    + (char) (tamanhoX + 64));
        }

        if (posicoes[1] > tamanhoY) {
            //ERRO
            retorno = false;
            System.out.println("A posição numerica não pode ser maior que "
                    + tamanhoY);
        }
        return retorno;
    }
    
    public static void instanciarContadoresDosNaviosDosJogadores(){
        naviosJogador1 = qtdDeNavios;
        naviosJogador2 = qtdDeNavios;
    }
    
    public static void acaoDoComputador(){      
        int[] posicoes = retornarJogadaDoComputador();
        inserirValoresDaAçãoNoTabuleiro(posicoes, 2);
    }
    
    public static int[] retornarJogadaDoComputador(){
        int[] posicoes = new int[2];
        posicoes[POSICAO_X] = retornarJogadaAleatoriaDoComputador(tamanhoX);
        posicoes[POSICAO_Y] = retornarJogadaAleatoriaDoComputador(tamanhoY);
        return posicoes;
    }
    
    public static int retornarJogadaAleatoriaDoComputador(int limite){
        Random jogadaDoComputador = new Random();
        int numeroGerado = jogadaDoComputador.nextInt(limite);
        return (numeroGerado == limite) ? --numeroGerado : numeroGerado;
    }

    public static void main(String[] args) {
        //Tabuleiro t = new Tabuleiro();
        obterNomeDosJogadores();
        obterTamanhoDosTabuleiros();
        calcularQtdMaxDeNaviosNoJogo();
        iniciandoTamanhoDosTabuleiros();
        calcularQtdMaxDeNavios();
        instanciarContadoresDosNaviosDosJogadores();
        inserirOsNaviosNosTabuleirosDosJogadores();
        boolean jogoAtivo = true;
        do {
            exibirTabuleiroDosJogadores();
            if (acaoDoJogador()) {
                if(naviosJogador2 <= 0){
                    System.out.println(nomeJogador1+ " Venceu o Jogo!");
                    break;
                }

                acaoDoComputador();

                if(naviosJogador1 <= 0){
                    System.out.println(nomeJogador2+ " Venceu o Jogo!");
                    break;
                }
            }

        } while (jogoAtivo);
        exibirTabuleiroDosJogadores();
        input.close();
    }
}
