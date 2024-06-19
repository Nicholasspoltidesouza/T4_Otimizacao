import java.util.*;
import java.io.*;

public class Utils {

    public static Cidade[] lerCidadesDoArquivo(String nomeArquivo) {
        try (Scanner scanner = new Scanner(new File(nomeArquivo))) {
            int numCidades = Integer.parseInt(scanner.nextLine().trim());
            Cidade[] cidades = new Cidade[numCidades];
            for (int i = 0; i < numCidades; i++) {
                String[] partes = scanner.nextLine().split(" ");
                double x = Double.parseDouble(partes[0]);
                double y = Double.parseDouble(partes[1]);
                String nome = partes[2];
                cidades[i] = new Cidade(x, y, nome);
            }
            return cidades;
        } catch (FileNotFoundException e) {
            System.err.println("Arquivo não encontrado: " + nomeArquivo);
            System.exit(1);
        } catch (NumberFormatException e) {
            System.err.println("Erro de formato no arquivo: " + nomeArquivo);
            System.exit(1);
        }
        return null;
    }

    public static double[] gerarRotaAleatoria(int tamanho) {
        double[] rota = new double[tamanho];
        for (int i = 0; i < tamanho; i++) {
            rota[i] = i;
        }
        List<Double> rotaList = new ArrayList<>();
        for (double v : rota) {
            rotaList.add(v);
        }
        Collections.shuffle(rotaList);
        for (int i = 0; i < tamanho; i++) {
            rota[i] = rotaList.get(i);
        }
        return rota;
    }

    public static double calcularDistanciaTotal(double[] rota, Cidade[] cidades) {
        double distanciaTotal = 0;
        for (int i = 0; i < rota.length - 1; i++) {
            distanciaTotal += distanciaEntre(cidades[(int) rota[i]], cidades[(int) rota[i + 1]]);
        }
        distanciaTotal += distanciaEntre(cidades[(int) rota[rota.length - 1]], cidades[(int) rota[0]]);
        return distanciaTotal;
    }

    public static double distanciaEntre(Cidade cidade1, Cidade cidade2) {
        return Math.sqrt(Math.pow(cidade2.getX() - cidade1.getX(), 2) + Math.pow(cidade2.getY() - cidade1.getY(), 2));
    }

    public static double[] selecionarRota(double[][] populacao, Cidade[] cidades) {
        Arrays.sort(populacao, Comparator.comparingDouble(rota -> calcularDistanciaTotal(rota, cidades)));
        double somaDistancias = Arrays.stream(populacao).mapToDouble(rota -> calcularDistanciaTotal(rota, cidades))
                .sum();
        double valorSorteado = Math.random() * somaDistancias;
        double acumulado = 0;
        for (double[] rota : populacao) {
            acumulado += calcularDistanciaTotal(rota, cidades);
            if (acumulado >= valorSorteado) {
                return rota;
            }
        }
        return populacao[0];
    }

    public static double[] cruzarRotas(double[] pai1, double[] pai2) {
        double[] filho = pai1.clone();
        int pontoCorte = new Random().nextInt(pai1.length);
        for (int i = pontoCorte; i < pai1.length; i++) {
            filho[i] = pai2[i];
        }
        return filho;
    }

    private static boolean contido(double[] array, double valor) {
        for (double v : array) {
            if (v == valor) {
                return true;
            }
        }
        return false;
    }

    public static void mutarRota(double[] rota) {
        int index1 = new Random().nextInt(rota.length);
        int index2 = new Random().nextInt(rota.length);
        double temp = rota[index1];
        rota[index1] = rota[index2];
        rota[index2] = temp;
    }
}
