package sample.model;

import java.util.Arrays;

public class Individuo {

    public String cromossomo[];

    public Integer fitness;

    public Integer tempo;

    public Individuo(String[] cromossomo) {
        this.cromossomo = cromossomo;
    }

    public Individuo(Integer tamanhoCromossomo) {
        this.cromossomo = new String[tamanhoCromossomo];
    }

    public Integer getTempo() {
        return tempo;
    }

    public Integer getFitness() {
        return fitness;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Individuo individuo = (Individuo) o;
        return Arrays.equals(cromossomo, individuo.cromossomo);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(cromossomo);
    }
}
