package sample.logica;

import sample.model.Cidade;
import sample.model.Individuo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Genetica {

    private static final Integer MAX_INDIVIDUOS = 10;
    private static final Integer MAX_GERACOES = 50;
    private static final Integer MIN_GERACOES_PARA_PROGREDIR = 15;

    public static List<Cidade> cidades = new ArrayList<>();

    public static Integer[][] distancias;

    private static Random random = new Random();

    private static List<Individuo> individuos = new ArrayList<>();

    public static Individuo individuoMaisApto = null;

    public static void main(String[] args) {
        executar();

        System.out.println();
        System.out.println("-----------RESULTADO-----------");
        String[] cromossomo = individuoMaisApto.cromossomo;
        String percurso = "";

        for (int k = 0; k < cromossomo.length; k++) {
            percurso += getCidade(Integer.parseInt(cromossomo[k])).getNome() + " -> ";
        }

        System.out.println();
        System.out.println();

        percurso += "\n Tempo: " + individuoMaisApto.tempo;
        System.out.println(percurso);
    }

    public static void executarFRONT() {
        int geracoes = 1;
        Integer geracoesSemProgresso = 0;

        mostrarDistancias();

        criarIndividuos(MAX_INDIVIDUOS, cidades.get(0));

        while (geracoes <= MAX_GERACOES && !geracoesSemProgresso.equals(MIN_GERACOES_PARA_PROGREDIR)) {
            geracoesSemProgresso = verificarGeracoesProgredindo(geracoesSemProgresso);
            executarGeracoes();
            geracoes++;
        }
    }

    public static void executar() {
        int geracoes = 1;
        Integer geracoesSemProgresso = 0;

        iniciarPopulacao();
        preencherDistancias();
        criarIndividuos(MAX_INDIVIDUOS, cidades.get(0));

        while (geracoes <= MAX_GERACOES && !geracoesSemProgresso.equals(MIN_GERACOES_PARA_PROGREDIR)) {
            geracoesSemProgresso = verificarGeracoesProgredindo(geracoesSemProgresso);
            executarGeracoes();
            geracoes++;
        }
    }

    private static int verificarGeracoesProgredindo(Integer geracoesSemProgresso) {
        Individuo individuo = individuos.stream().min(Comparator.comparing(Individuo::getTempo)).get();

        if (individuoMaisApto == null) {
            individuoMaisApto = individuo;

        } else if (individuo.tempo >= individuoMaisApto.tempo) {
            geracoesSemProgresso++;

        } else {
            geracoesSemProgresso = 0;
            individuoMaisApto = individuo;
        }

        return geracoesSemProgresso;
    }

    private static void executarGeracoes() {
        calcularFitness();
        List<Individuo> individuosMaisAptos = selecionarIndividuosMaisAptos();

        while (individuosMaisAptos.size() < MAX_INDIVIDUOS) {
            mutacao(individuosMaisAptos);
        }

        individuos = new ArrayList<>(individuosMaisAptos);
    }

    private static void mutacao(List<Individuo> individuosMaisAptos) {
        Individuo individuo = individuosMaisAptos.stream().min(Comparator.comparing(Individuo::getTempo)).get();

        String[] cromossomo = individuo.cromossomo;

        int pontoA = random.nextInt(individuo.cromossomo.length - 2) + 1;
        int pontoB = random.nextInt(individuo.cromossomo.length - 2) + 1;

        mutar(cromossomo, pontoA, pontoB);

        Individuo novoInividuo = new Individuo(cromossomo);

        calcularTempo(novoInividuo);

        individuosMaisAptos.add(novoInividuo);
    }

    private static void mutar(String[] cromossomo, int pontoA, int pontoB) {
        String a = cromossomo[pontoA];
        String b = cromossomo[pontoB];

        cromossomo[pontoA] = b;
        cromossomo[pontoB] = a;
    }

    private static List<Individuo> selecionarIndividuosMaisAptos() {
        List<Individuo> individuosOrdenadosFitness = individuos.stream().sorted(Comparator.comparing(Individuo::getFitness)).collect(Collectors.toList());
        List<Individuo> individuosOrdenadosTempo = individuos.stream().sorted(Comparator.comparing(Individuo::getTempo)).collect(Collectors.toList());
        List<Individuo> individuosSelecionados = new ArrayList<>();

        individuosSelecionados.add(individuosOrdenadosTempo.get(0));
        individuosSelecionados.add(individuosOrdenadosTempo.get(1));

        int totalFitness = individuosOrdenadosFitness.stream().mapToInt(Individuo::getFitness).sum();
        int adicionados = 0;

        while (adicionados < 2) {
            Individuo selecionado = selecionarIndividuoMaisApto(individuosOrdenadosFitness, totalFitness);

            if (individuosSelecionados.contains(selecionado))
                continue;

            adicionados++;
            individuosSelecionados.add(selecionado);
        }

        return individuosSelecionados;
    }

    private static Individuo selecionarIndividuoMaisApto(List<Individuo> individuosOrdenados, int totalFitness) {
        int aleatorio = random.nextInt(totalFitness) + 1;

        int sum = 0;
        for (int i = 0; i < individuosOrdenados.size(); i++) {
            sum += individuosOrdenados.get(i).fitness;

            if (i == individuosOrdenados.size() - 1)
                return individuosOrdenados.get(i);

            if (aleatorio >= sum && aleatorio < sum + individuosOrdenados.get(i + 1).fitness)
                return individuosOrdenados.get(i);

        }

        throw new RuntimeException("Erro ao selecionar indivíduo mais apto!");
    }

    //cria individuos aleatorios
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
    }

    private static void calcularFitness() {
        List<Individuo> individuosOrdenados = individuos.stream().sorted(Comparator.comparing(Individuo::getTempo).reversed()).collect(Collectors.toList());

        for (int i = 0; i < individuosOrdenados.size(); i++) {
            individuosOrdenados.get(i).fitness = i + 1;
        }
    }

    public static Cidade getCidade(Integer id) {
        return cidades.stream().filter(c -> c.getId().equals(id)).findFirst().get();
    }

    public void preencherDistancias(int distancia, int i, int j) {
        if (distancias == null)
            distancias = new Integer[cidades.size()][cidades.size()];

        if (i == j) {
            distancias[i][j] = 0;
        } else if (distancias[j][i] != null) {
            distancias[i][j] = distancias[j][i];
        } else {
            distancias[i][j] = distancia;
        }
    }

    public boolean deveIrParaProximaIteracao(int i, int j) {
        return i == j || distancias[j][i] != null;
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
                    distancias[i][j] = random.nextInt(200) + 1;
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

    public static void adicionaCidade(String cidade) {
        cidades.add(new Cidade(generateId(), cidade));
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
