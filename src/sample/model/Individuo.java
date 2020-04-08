package sample.model;

public class Individuo {

    public String cromossomo[];

    public Integer fitness;

    public Integer tempo;

    public Individuo(Integer tamanhoCromossomo) {
        this.cromossomo = new String[tamanhoCromossomo];
    }

    public Integer getTempo() {
        return tempo;
    }

    public Integer getFitness() {
        return fitness;
    }
}
