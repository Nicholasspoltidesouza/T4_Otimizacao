import java.util.*;
import java.io.*;

public class AlgoritmoGenetico {
    static final int TAMANHO_POPULACAO = 100;
    static final int NUMERO_GERACOES = 500;
    static final double TAXA_MUTACAO = 0.01;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Uso: java AlgoritmoGenetico <arquivo>");
            System.exit(1);
        }

        String nomeArquivo = args[0];
        List<Cidade> cidades = lerCidadesDoArquivo(nomeArquivo);

        List<Rota> populacao = new ArrayList<>();
        for (int i = 0; i < TAMANHO_POPULACAO; i++) {
            populacao.add(new Rota(cidades));
        }

        Rota melhorRota = populacao.get(0);
        for (int geracao = 0; geracao < NUMERO_GERACOES; geracao++) {
            for (Rota rota : populacao) {
                if (rota.distancia < melhorRota.distancia) {
                    melhorRota = rota;
                }
            }
            System.out.println("Geração " + geracao + " - Melhor distância: " + melhorRota.distancia);

            List<Rota> novaPopulacao = new ArrayList<>();
            for (int i = 0; i < TAMANHO_POPULACAO; i++) {
                Rota pai1 = selecionarRota(populacao);
                Rota pai2 = selecionarRota(populacao);
                Rota filho = cruzarRotas(pai1, pai2);
                if (Math.random() < TAXA_MUTACAO) {
                    mutarRota(filho);
                }
                novaPopulacao.add(filho);
            }
            populacao = novaPopulacao;
        }

        System.out.println("Melhor rota encontrada: " + melhorRota.distancia);
    }

    static List<Cidade> lerCidadesDoArquivo(String nomeArquivo) {
        List<Cidade> cidades = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(nomeArquivo))) {
            while (scanner.hasNextLine()) {
                String[] partes = scanner.nextLine().split(" ");
                double x = Double.parseDouble(partes[0]);
                double y = Double.parseDouble(partes[1]);
                String nome = partes[2];
                cidades.add(new Cidade(x, y, nome));
            }
        } catch (FileNotFoundException e) {
            System.err.println("Arquivo não encontrado: " + nomeArquivo);
            System.exit(1);
        }
        return cidades;
    }

    static Rota selecionarRota(List<Rota> populacao) {
        Collections.sort(populacao, Comparator.comparingDouble(rota -> rota.distancia));
        double somaDistancias = populacao.stream().mapToDouble(rota -> rota.distancia).sum();
        double valorSorteado = Math.random() * somaDistancias;
        double acumulado = 0;
        for (Rota rota : populacao) {
            acumulado += rota.distancia;
            if (acumulado >= valorSorteado) {
                return rota;
            }
        }
        return populacao.get(0);
    }

    static Rota cruzarRotas(Rota pai1, Rota pai2) {
        List<Cidade> filhoCidades = new ArrayList<>(pai1.cidades);
        int pontoCorte = new Random().nextInt(pai1.cidades.size());
        for (int i = pontoCorte; i < pai1.cidades.size(); i++) {
            filhoCidades.set(i, pai2.cidades.get(i));
        }
        return new Rota(filhoCidades);
    }

    static void mutarRota(Rota rota) {
        int index1 = new Random().nextInt(rota.cidades.size());
        int index2 = new Random().nextInt(rota.cidades.size());
        Collections.swap(rota.cidades, index1, index2);
        rota.distancia = rota.calcularDistanciaTotal();
    }
}

class Cidade {
    double x;
    double y;
    String nome;

    public Cidade(double x, double y, String nome) {
        this.x = x;
        this.y = y;
        this.nome = nome;
    }
}

class Rota {
    List<Cidade> cidades;
    double distancia;

    public Rota(List<Cidade> cidades) {
        this.cidades = new ArrayList<>(cidades);
        Collections.shuffle(this.cidades);
        this.distancia = calcularDistanciaTotal();
    }

    double calcularDistanciaTotal() {
        double distanciaTotal = 0;
        for (int i = 0; i < cidades.size() - 1; i++) {
            distanciaTotal += distanciaEntre(cidades.get(i), cidades.get(i + 1));
        }
        distanciaTotal += distanciaEntre(cidades.get(cidades.size() - 1), cidades.get(0)); // Volta para a cidade
                                                                                           // inicial
        return distanciaTotal;
    }

    double distanciaEntre(Cidade cidade1, Cidade cidade2) {
        return Math.sqrt(Math.pow(cidade2.x - cidade1.x, 2) + Math.pow(cidade2.y - cidade1.y, 2));
    }
}
