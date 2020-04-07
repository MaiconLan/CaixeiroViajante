package sample.model;

import java.util.Stack;

public class Individuo {

    public String cromossomo[];

    public Double fitness;

    public Integer tempo;

    public Individuo(Integer size) {
        this.cromossomo = new String[size];
    }
}
