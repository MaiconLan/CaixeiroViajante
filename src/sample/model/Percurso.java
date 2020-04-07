package sample.model;

import java.util.Objects;

public class Percurso {

    private Cidade origem;
    private Cidade destino;
    private Integer distancia;

    public Percurso(Cidade origem, Cidade destino, Integer distancia) {
        this.origem = origem;
        this.destino = destino;
        this.distancia = distancia;
    }

    public Percurso(Cidade origem, Cidade destino) {
        this.origem = origem;
        this.destino = destino;
    }

    public Cidade getOrigem() {
        return origem;
    }

    public void setOrigem(Cidade origem) {
        this.origem = origem;
    }

    public Cidade getDestino() {
        return destino;
    }

    public void setDestino(Cidade destino) {
        this.destino = destino;
    }

    public Integer getDistancia() {
        return distancia;
    }

    public void setDistancia(Integer distancia) {
        this.distancia = distancia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Percurso percurso = (Percurso) o;
        return Objects.equals(origem, percurso.origem) && Objects.equals(destino, percurso.destino)
                    || Objects.equals(origem, percurso.destino) && Objects.equals(destino, percurso.origem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origem, destino, distancia);
    }
}
