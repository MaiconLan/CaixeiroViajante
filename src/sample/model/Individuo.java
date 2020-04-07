package sample.model;

import java.util.Stack;

public class Individuo {

    public Stack<Cidade> caminho = new Stack<>();

    public String cromossomo[];

    public Double fitness;

    public Individuo(Integer size) {
        this.cromossomo = new String[size];
    }
}
