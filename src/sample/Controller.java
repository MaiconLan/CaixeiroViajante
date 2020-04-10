package sample;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import sample.logica.Genetica;
import sample.model.Cidade;

import java.util.List;

public class Controller {

    @FXML
    private TextField txtCidade;

    @FXML
    private Text erro;

    @FXML
    private Text percursoMenor;

    @FXML
    private Text erroDistancias;

    @FXML
    private ListView<Cidade> listCidades;

    @FXML
    private TextField txtCidadeOrigem;

    @FXML
    private TextField txtDistancia;

    @FXML
    private TextField txtCidadeDestino;

    @FXML
    private Button btnSalvarOrigem;

    @FXML
    private Button btnSalvarDistancia;

    @FXML
    private ComboBox<Cidade> boxOrigem;

    private Genetica genetica = new Genetica();

    private List<Cidade> cidadesDestino;

    private int i, j;

    private Cidade cidadeOrigem, cidadeDestino;

    @FXML
    public void salvarOrigem(ActionEvent event){
        cidadeOrigem = boxOrigem.getValue();
        boxOrigem.setDisable(true);
        btnSalvarOrigem.setDisable(true);
        btnSalvarDistancia.setDisable(false);

        genetica.preencherDistancias(0, i, j);

        proximaIteracao();
    }

    @FXML
    public void salvarDistancia(ActionEvent event) {
        genetica.preencherDistancias(Integer.parseInt(txtDistancia.getText()), i, j);

        if(i == genetica.getCidades().size() - 1 && j == genetica.getCidades().size() - 1) {
            txtDistancia.setDisable(true);
            btnSalvarDistancia.setDisable(true);
            genetica.executarFRONT();

            mostrarPercursoMenor();
            return;
        }

        //proxima iteracao
        proximaIteracao();

        boolean deveIrParaProximaIteracao = genetica.deveIrParaProximaIteracao(i, j);

        if(deveIrParaProximaIteracao) {
            salvarDistancia(event);
        }

        cidadeOrigem = genetica.getCidades().get(i);
        cidadeDestino = genetica.getCidades().get(j);

        txtDistancia.setText("");
    }

    private void proximaIteracao() {
        if (j < genetica.getCidades().size() - 1) {
            j++;
        } else {
            if (i < genetica.getCidades().size() - 1) {
                i++;
                j = 0;
            } else {

                i = 4;
                j = 4;
                return;
            }
        }

        cidadeOrigem = genetica.getCidades().get(i);
        cidadeDestino = genetica.getCidades().get(j);
        txtCidadeOrigem.setText(cidadeOrigem.getNome());
        txtCidadeDestino.setText(cidadeDestino.getNome());
    }

    private void mostrarPercursoMenor() {
        String[] cromossomo = genetica.individuoMaisApto.cromossomo;
        String percurso = "";

        for (int k = 0; k < cromossomo.length; k++) {
            percurso += genetica.getCidade(Integer.parseInt(cromossomo[k])).getNome() + " -> ";
        }

        percurso += "\n Tempo: " + genetica.individuoMaisApto.tempo;


        percursoMenor.setText(percurso);
    }

    @FXML
    void adicionarCidade(ActionEvent event) {

        if (listCidades.getItems().size() == 10) {
            erro.setText("LIMITE MÁXIMO DE 10 cidades");

        } else {
            genetica.adicionaCidade(txtCidade.getText());
            listCidades.getItems().clear();
            genetica.getCidades().forEach(listCidades.getItems()::add);
            boxOrigem.setItems(FXCollections.observableArrayList(genetica.getCidades()));
            txtCidade.setText("");
        }
    }

}
