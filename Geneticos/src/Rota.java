import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Rota {
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
        distanciaTotal += distanciaEntre(cidades.get(cidades.size() - 1), cidades.get(0));
        return distanciaTotal;
    }

    double distanciaEntre(Cidade cidade1, Cidade cidade2) {
        return Math.sqrt(Math.pow(cidade2.getX() - cidade1.getX(), 2) + Math.pow(cidade2.getY() - cidade1.getY(), 2));
    }
}
