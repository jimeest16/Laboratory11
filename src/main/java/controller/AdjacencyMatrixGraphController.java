package controller;

import domain.AdjacencyMatrixGraph;
import domain.GraphException;
import domain.list.ListException;
import domain.queue.QueueException;
import domain.stack.StackException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import static util.FXUtility.showAlert;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class AdjacencyMatrixGraphController {
    @FXML
    private Label labelMatrix;
    @FXML
    private TextArea taMatrix;
    @FXML
    private Pane pane;
    AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(10);
    private List<int[]> edgePairs = new ArrayList<>();
    private int currentEdgeIndex = 0;
    private int[] numeros; // los vértices usados

    public void containsEdge(ActionEvent actionEvent) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Check Edge");
        dialog.setHeaderText("Enter two vertex values to check if an edge exists: ");

        // Botón OK
        ButtonType okButtonType = new ButtonType("OK", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // Campos de texto
        TextField fromField = new TextField();
        fromField.setPromptText("From");
        TextField toField = new TextField();
        toField.setPromptText("To");

        // Diseño
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(fromField, 0, 0);
        grid.add(toField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        // Resultado
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return new Pair<>(fromField.getText(), toField.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(pair -> {
            try {
                int a = Integer.parseInt(pair.getKey());
                int b = Integer.parseInt(pair.getValue());

                boolean exists = graph.containsEdge(a, b);
                showAlert("Edge between " + a + " and " + b + (exists ? " exists." : " does not exist."));
            } catch (NumberFormatException e) {
                showAlert("Invalid input! Please enter numbers.");
            } catch (GraphException | ListException e) {
                showAlert("Error: " + e.getMessage());
            }
        });
    }

    public void containsVextex(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Enter value to search:");
        dialog.showAndWait().ifPresent(value -> {
            try {
                int val = Integer.parseInt(value);
                boolean found = graph.containsVertex(val);
                showAlert("Value " + (found ? "found." : "not found."));
            } catch (NumberFormatException e) {
                showAlert("Invalid number!");
            } catch (GraphException e) {
                showAlert("Error: " + e.getMessage());
            } catch (ListException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void dfsTour(ActionEvent actionEvent) throws GraphException, ListException, StackException {
        labelMatrix.setText("DFS Tour");
        taMatrix.setText("DFS Tour: "+graph.dfs());
    }
    public void bfsTour(ActionEvent actionEvent) throws GraphException, QueueException, ListException {
        labelMatrix.setText("BFS Tour");
        taMatrix.setText("BFS Tour: "+graph.bfs());
    }
    public void randomize(ActionEvent actionEvent) throws ListException, GraphException {
        graph.clear(); // Limpiar el grafo actual

        int[] numeros = new int[10];
        int count = 0;

        for (; count < 10;) {
            int num = util.Utility.randomMinMax(0,99); // entre 0 y 99
            boolean repetido = false;

            // Verificar si ya existe
            for (int i = 0; i < count; i++) {
                if (numeros[i] == num) {
                    repetido = true;
                    break;
                }
            }

            if (!repetido) {
                numeros[count] = num;
                count++;
            }
        }

        // colocar los numeros en los vertices del grafico opción 1, no es uno por una
        for (int i = 0; i < 10; i++) {
            graph.addVertex(numeros[i]);
        }
        /*
        //agregar aristas opción 1, no es una por una
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10 ; j++) {
                graph.addEdgeWeight(numeros[i],numeros[j],util.Utility.randomMinMax(1,50));
            }
        }*/
        //opción 2 generar pares aleatorios posibles (sin repetir ni simétricos)
        edgePairs.clear();
        currentEdgeIndex = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = i + 1; j < 10; j++) {
                edgePairs.add(new int[]{numeros[i], numeros[j]});
            }
        }
        Collections.shuffle(edgePairs); // orden aleatorio

        labelMatrix.setText("You have autogenerated a graph!");
        util.FXUtil.drawGraph(graph, pane);
    }

    public void addNextEdge(ActionEvent event) throws GraphException, ListException {
        if (currentEdgeIndex >= edgePairs.size()) {
            labelMatrix.setText("Todas las aristas ya fueron agregadas.");
            return;
        }

        int[] pair = edgePairs.get(currentEdgeIndex++);
        int weight = util.Utility.randomMinMax(1, 50);
        graph.addEdgeWeight(pair[0], pair[1], weight);

        labelMatrix.setText("Arista agregada entre " + pair[0] + " y " + pair[1] + " con peso " + weight);
        util.FXUtil.drawGraph(graph, pane);
    }


    public void toString(ActionEvent actionEvent) {
        labelMatrix.setText("Adjacency matrix graph content: ");
        taMatrix.setText(graph.toString());
    }

}
