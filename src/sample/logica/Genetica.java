package sample.logica;

import sample.model.Cidade;
import sample.model.Individuo;

import java.util.*;
import java.util.stream.Collectors;

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

        calcularFitness();
        List<Individuo> individuosMaisAptos = selecionarIndividuosMaisAptos();
        
    }

    private static List<Individuo> selecionarIndividuosMaisAptos() {
        List<Individuo> individuosOrdenados = individuos.stream().sorted(Comparator.comparing(Individuo::getFitness)).collect(Collectors.toList());
        List<Individuo> individuosSelecionados = new ArrayList<>();

        int totalFitness = individuosOrdenados.stream().mapToInt(Individuo::getFitness).sum();

        while (individuosSelecionados.size() < 2) {
            Individuo selecionado = selecionarIndividuoMaisApto(individuosOrdenados, totalFitness);

            if(individuosSelecionados.contains(selecionado))
                continue;

            individuosSelecionados.add(selecionado);
        }

        return individuosSelecionados;
    }

    private static Individuo selecionarIndividuoMaisApto(List<Individuo> individuosOrdenados, int totalFitness) {
        int aleatorio = random.nextInt(totalFitness) + 1;

        int sum = 0;
        for (Individuo individuo : individuosOrdenados) {
            sum += individuo.fitness;
            if (sum > aleatorio)
                return individuo;

        }

        throw new RuntimeException("Erro ao selecionar indivíduo mais apto!");
    }

    private static void criarIndividuos(Integer numeroIndividuos, Cidade origem) {
        for (int i = 0; i < numeroIndividuos; i++)
            criarIndividuo(origem);
    }

    private static void criarIndividuo(Cidade origem) {
        Individuo individuo = new Individuo(cidades.size() + 1);
        List<Integer> listaAleatoria = new ArrayList<>();

        // ADD a cidade de origem no começo na lista de valores aleatórios de cidades
        listaAleatoria.add(origem.getId());

        // Gera em uma lista o percurso aleatório para percorrer
        while (listaAleatoria.size() < cidades.size()) {
            int aleatorio = random.nextInt(cidades.size()) + 1;
            if (!listaAleatoria.contains(aleatorio)) {
                listaAleatoria.add(aleatorio);
            }
        }

        // popula o cromossomo do indivíduo com o id das cidades
        for (int i = 0; i < listaAleatoria.size(); i++)
            individuo.cromossomo[i] = getCidade(listaAleatoria.get(i)).getId().toString();

        // ADD a cidade de origem no final do array de cromossomos
        individuo.cromossomo[cidades.size()] = individuo.cromossomo[0];

        individuos.add(individuo);
        calcularTempo(individuo);
    }

    private static void calcularTempo(Individuo individuo) {
        Integer tempo = 0;

        for (int i = 0; i < individuo.cromossomo.length - 1; i++) {

            // pega a posição da cidade de partida na matriz de distâncias
            int linha = Integer.parseInt(individuo.cromossomo[i]);

            // pega a posição da cidade de chegada na matriz de distâncias
            int coluna = Integer.parseInt(individuo.cromossomo[i + 1]);

            // -1 é pra pegar a posição da cidade na matriz de distancias
            // a matriz está populada conforme o id da cidade (n-1 onde n é o ID da cidade)
            // se a cidade tem ID 1, então ela é a primeira cidade, logo esta na posição 0
            tempo += distancias[linha - 1][coluna - 1];
        }

        individuo.tempo = tempo;

        System.out.print("Indivíduo: ");

        for (String s : individuo.cromossomo) {
            System.out.print(s);
        }

        System.out.println("");
        System.out.println("------");
        System.out.println("Tempo: " + individuo.tempo);
        System.out.println("Fitness: " + individuo.tempo);
    }

    private static void calcularFitness() {
        List<Individuo> individuosOrdenados = individuos.stream().sorted(Comparator.comparing(Individuo::getTempo).reversed()).collect(Collectors.toList());

        for (int i = 0; i < individuosOrdenados.size(); i++) {
            individuosOrdenados.get(i).fitness = i + 1;
        }
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

        mostrarDistancias();
    }

    private static void mostrarDistancias() {
        for (int i = 0; i < cidades.size(); i++) {
            String result = "";

            for (int j = 0; j < cidades.size(); j++) {
                result += distancias[i][j] + " - ";
            }
            System.out.println(result);
        }
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
