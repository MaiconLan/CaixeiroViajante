package sample.logica;

import sample.model.Cidade;
import sample.model.Individuo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Genetica {

    private static final Integer MAX_INDIVIDUOS = 5;
    private static final Integer MAX_GERACOES = 50;

    private static List<Cidade> cidades = new ArrayList<>();

    private static Integer[][] distancias;

    private static Random random = new Random();

    private static List<Individuo> individuos = new ArrayList<>();

    public static void main(String[] args) {
        iniciarPopulacao();
        preencherDistancias();
        criarIndividuos(MAX_INDIVIDUOS, cidades.get(0));

    }

    private static void criarIndividuos(Integer numeroIndividuos, Cidade origem) {
        for (int i = 0; i < numeroIndividuos; i++)
            criarIndividuos(origem);
    }

    private static void criarIndividuo(Cidade origem) {
        Individuo individuo = new Individuo(cidades.size());
        List<Integer> listaAleatoria = new ArrayList<>();

        listaAleatoria.add(origem.getId());

        while (listaAleatoria.size() < cidades.size()) {
            int aleatorio = random.nextInt(cidades.size()) + 1;
            if(!listaAleatoria.contains(aleatorio)) {
                listaAleatoria.add(aleatorio);
            }
        }

        for (int i = 0; i < listaAleatoria.size(); i++)
            individuo.cromossomo[i] = getCidade(listaAleatoria.get(i)).getId().toString();

        calcularTempo(individuo);

        individuos.add(individuo);
    }

    private static void calcularTempo(Individuo individuo) {

        for (int i = 0; i < individuo.cromossomo.length; i++) {

        }
        return null;
    }

    private static void calcularFitness(Individuo individuo) {
        individuo.fitness = Math.abs(individuo.x * individuo.y * Math.sin((Math.pow(individuo.y, Math.PI)) / 4));
    }

    private static Cidade getCidade(Integer id) {
        return cidades.stream().filter(c -> c.getId().equals(id)).findFirst().get();
    }

    private static void preencherDistancias() {
        distancias = new Integer[cidades.size()][cidades.size()];

        for (int i = 0; i < cidades.size(); i++) {
            for (int j = 0; j < cidades.size(); j++) {
                if (i == j) {
                    distancias[i][j] = 0;
                } else if (distancias[j][i] != null) {
                    distancias[i][j] = distancias[j][i];
                } else {
                    distancias[i][j] = random.nextInt(201);
                }
            }
        }


        for (int i = 0; i < cidades.size(); i++) {
            String result = "";

            for (int j = 0; j < cidades.size(); j++) {
                result += distancias[i][j] + " - ";
            }
            System.out.println(result);
        }
    }

    private static void fitnessFunction() {

    }

    private static void iniciarPopulacao() {
        cidades.add(new Cidade(generateId(), "Tubarão"));
        cidades.add(new Cidade(generateId(), "Laguna"));
        cidades.add(new Cidade(generateId(), "Criciúma"));
        cidades.add(new Cidade(generateId(), "Florianópolis"));
        cidades.add(new Cidade(generateId(), "Braço do norte"));
        cidades.add(new Cidade(generateId(), "Jaguaruna"));
        cidades.add(new Cidade(generateId(), "Imbituba"));
        cidades.add(new Cidade(generateId(), "Sangão"));
        cidades.add(new Cidade(generateId(), "13 de maio"));
        cidades.add(new Cidade(generateId(), "Gravatal"));
    }

    private static int generateId() {
        return cidades.size() + 1;
    }

    public List<Cidade> getCidades() {
        return cidades;
    }

    public void setCidades(List<Cidade> cidades) {
        this.cidades = cidades;
    }

}
