package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import ucr.lab.HelloApplication;

import java.io.IOException;

public class HelloController {
    @FXML
    private Text txtMessage;
    @FXML
    private BorderPane bp;
    @FXML
    private AnchorPane ap;
    public void load (String form){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(form));
        try {
            this.bp.setCenter(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void Home(ActionEvent actionEvent) {
        this.txtMessage.setText("Laboratory No. 11" +
                "\n Click on the bottons in your left!");
        this.bp.setCenter(ap);
    }

    @FXML
    public void AdjacencyMatrixGraph(ActionEvent actionEvent) {
        load("/ucr/lab/adjacencyMatrixGraph.fxml");
    }

    @FXML
    public void AdjacencyListGraph(ActionEvent actionEvent) {
        load("/ucr/lab/listGraph.fxml");
    }

    @FXML
    public void SinglyLinkedListGraph(ActionEvent actionEvent) {
        load("/ucr/lab/linkedGraph.fxml");
    }
    @FXML
    public void MatrizOperations(ActionEvent actionEvent) {
        load("/ucr/lab/matrixOperation.fxml");
    }
    @FXML
    public void Exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    @FXML
    public void LinkedOperations(ActionEvent actionEvent) {
        load("/ucr/lab/linkedOperation.fxml");
    }

    @FXML
    public void ListOperations(ActionEvent actionEvent) {
        load("/ucr/lab/listOperation.fxml");
    }
}