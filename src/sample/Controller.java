package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import sample.logica.Genetica;
import sample.model.Cidade;
import sample.model.Percurso;

import java.util.List;

public class Controller {

    @FXML
    private TextField txtCidade;

    @FXML
    private Text erro;

    @FXML
    private ListView<Cidade> listCidades;

    @FXML
    private TextField txtCidadeOrigem;

    @FXML
    private TextField txtDistancia;

    @FXML
    private TextField txtCidadeDestino;

    @FXML
    private Button btnPreencherDistancias;

    private Genetica genetica = new Genetica();

    private List<Cidade> cidadesDestino;

    private int indexOrigem, indexDestino;

    private Cidade cidadeOrigem, cidadeDestino;

    @FXML
    void preencherDistancias(ActionEvent event) {

        cidadeOrigem = genetica.getCidades().get(indexOrigem);
        cidadeDestino = genetica.getCidades().get(indexDestino);

        if(cidadeOrigem.equals(cidadeDestino)) {
            txtDistancia.setText("0");
            salvarDistancia(null);
        }

        Percurso percurso = new Percurso(cidadeOrigem, cidadeDestino);

        while (!percursoJaCadastrado(percurso)) {
            cidadesDestino.remove(cidadeDestino);
            cidadeDestino = cidadesDestino.get(0);
        }

        txtCidadeOrigem.setText(cidadeOrigem.toString());
        txtCidadeDestino.setText(cidadeDestino.toString());
        btnPreencherDistancias.setDisable(true);
    }

    @FXML
    void salvarDistancia(ActionEvent event) {
        Percurso percurso = new Percurso(cidadeOrigem, cidadeDestino, Integer.parseInt(txtDistancia.getText()));
        //genetica.getPercursos().put(cidadeOrigem.getId().toString() + cidadeDestino.getId().toString(), percurso);

        /*
            if () {

            }
            i++;
        */
        txtDistancia.setText(null);
    }

    private boolean percursoJaCadastrado(Percurso percurso) {
        return false;
        //return genetica.getPercursos().stream().anyMatch(p -> p.equals(percurso));
    }


    @FXML
    void adicionarCidade(ActionEvent event) {

        if (listCidades.getItems().size() == 10) {
            erro.setText("LIMITE MÁXIMO DE 10 cidades");

        } else {
            listCidades.getItems().add(new Cidade(genetica.getCidades().size() + 1, txtCidade.getText()));
            txtCidade.setText("");
            genetica.setCidades(listCidades.getItems());
        }
    }

}
