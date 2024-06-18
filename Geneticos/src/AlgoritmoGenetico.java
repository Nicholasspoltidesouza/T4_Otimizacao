import java.util.*;
import java.io.*;

public class AlgoritmoGenetico {
    static final int TAMANHO_POPULACAO = 10;
    static final int NUMERO_GERACOES = 50000;
    static final double TAXA_MUTACAO = 0.1;
    static final int NUM_INDIVIDUOS_EXEMPLO = 5; // Número estratégico de indivíduos de exemplo
    static final double TAXA_ELITISMO = 0.1; // 10% da população

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Uso: java AlgoritmoGenetico <arquivo_de_cidades>");
            System.exit(1);
        }

        String nomeArquivoCidades = args[0];
        Cidade[] cidades = Utils.lerCidadesDoArquivo(nomeArquivoCidades);

        // Definindo alguns indivíduos de exemplo diretamente no código
        double[][] individuosExemplo = gerarIndividuosExemplo(cidades.length);

        int numIndividuosExemplo = individuosExemplo.length;

        double[][] populacao = new double[TAMANHO_POPULACAO][cidades.length];
        for (int i = 0; i < numIndividuosExemplo; i++) {
            populacao[i] = individuosExemplo[i];
        }

        // Completa a população inicial com indivíduos aleatórios se necessário
        for (int i = numIndividuosExemplo; i < TAMANHO_POPULACAO; i++) {
            populacao[i] = Utils.gerarRotaAleatoria(cidades.length);
        }

        double[] melhorRota = populacao[0];
        double melhorDistancia = Utils.calcularDistanciaTotal(melhorRota, cidades);

        System.out.println("Procurando uma nova distância...");
        for (int geracao = 0; geracao < NUMERO_GERACOES; geracao++) {
            boolean encontrouMelhorDistancia = false;
            for (double[] rota : populacao) {
                double distancia = Utils.calcularDistanciaTotal(rota, cidades);
                if (distancia < melhorDistancia) {
                    melhorRota = rota.clone();
                    melhorDistancia = distancia;
                    encontrouMelhorDistancia = true;
                }
            }

            if (encontrouMelhorDistancia) {
                // System.out.println("Geração " + geracao + " - Melhor distância: " + melhorDistancia);
                // System.out.print("Caminho da menor rota: ");
                // for (double cidadeIndex : melhorRota) {
                //     Cidade cidade = cidades[(int) cidadeIndex];
                //     System.out.print(cidade.getNome() + " (" + cidade.getX() + ", " + cidade.getY() + ") -> ");
                // }
                System.out.println();
                System.out.println("Distância: " + melhorDistancia);
                System.out.println("Procurando uma nova distância...");
            }

            // Criação da nova população com elitismo
            double[][] novaPopulacao = new double[TAMANHO_POPULACAO][cidades.length];
            int numElitismo = (int) (TAMANHO_POPULACAO * TAXA_ELITISMO);
            Arrays.sort(populacao, Comparator.comparingDouble(o -> Utils.calcularDistanciaTotal(o, cidades)));
            for (int i = 0; i < numElitismo; i++) {
                novaPopulacao[i] = populacao[i];
            }

            for (int i = numElitismo; i < TAMANHO_POPULACAO; i++) {
                double[] pai1 = Utils.selecionarRota(populacao, cidades);
                double[] pai2 = Utils.selecionarRota(populacao, cidades);
                double[] filho = Utils.cruzarRotas(pai1, pai2);
                if (Math.random() < TAXA_MUTACAO) {
                    Utils.mutarRota(filho);
                }
                novaPopulacao[i] = filho;
            }
            populacao = novaPopulacao;
        }

        // Imprime a melhor rota encontrada ao final da execução
        System.out.println("Melhor rota encontrada: " + melhorDistancia);
        System.out.print("Caminho da menor rota: ");
        for (double cidadeIndex : melhorRota) {
            Cidade cidade = cidades[(int) cidadeIndex];
            System.out.print(cidade.getNome() + " (" + cidade.getX() + ", " + cidade.getY() + ") -> ");
        }
        System.out.println();
        System.out.println("Distância: " + melhorDistancia);
    }

    // Função para gerar indivíduos de exemplo que cobrem todas as cidades
    private static double[][] gerarIndividuosExemplo(int numCidades) {
        double[][] individuosExemplo = new double[NUM_INDIVIDUOS_EXEMPLO][numCidades];
        for (int i = 0; i < NUM_INDIVIDUOS_EXEMPLO; i++) {
            List<Double> individuo = new ArrayList<>();
            for (int j = 0; j < numCidades; j++) {
                individuo.add((double) j);
            }
            Collections.shuffle(individuo);
            for (int j = 0; j < numCidades; j++) {
                individuosExemplo[i][j] = individuo.get(j);
            }
        }
        return individuosExemplo;
    }
}