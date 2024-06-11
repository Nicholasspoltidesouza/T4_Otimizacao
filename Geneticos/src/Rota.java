import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Rota {
    List<Cidade> cidades;
    double distancia;

    public Rota(List<Cidade> cidades) {
        this.cidades = new ArrayList<>(cidades);
        Collections.shuffle(this.cidades);
        this.distancia = calcularDistanciaTotal();
    }
    
    public double calcularDistanciaTotal() {
        double distanciaTotal = 0;
        for (int i = 0; i < cidades.size() - 1; i++) {
            distanciaTotal += cidades.get(i).distanciaParaOutraCidade(cidades.get(i + 1));
        }
        distanciaTotal += cidades.get(cidades.size() - 1).distanciaParaOutraCidade(cidades.get(0)); // Volta para a cidade inicial
        return distanciaTotal;
    }
}
