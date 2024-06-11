public class Cidade {
    double latitude;
    double longitude;

    public Cidade(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double distanciaParaOutraCidade(Cidade cidade) {
        return Math.sqrt(Math.pow((latitude - cidade.latitude), 2) + Math.pow((longitude - cidade.longitude), 2));
    }
}
