package controller;

import domain.AdjacencyListGraph;
import domain.GraphException;
import domain.list.ListException;
import domain.queue.QueueException;
import domain.stack.StackException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static util.FXUtility.showAlert;

public class AdjacencyListGraphController {
    @FXML
    private Label labelToGraph;

    @FXML
    private Pane pane;

    @FXML
    private TextArea textAreaGraph;

    AdjacencyListGraph graph = new AdjacencyListGraph(10);
    private List<char[]> edgePairs = new ArrayList<char[]>();
    private int currentEdgeIndex = 0;
    private char[] letras; // los vértices usados

    public void containsEdge(ActionEvent actionEvent) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Check Edge");
        dialog.setHeaderText("Enter two vertex values to check if an edge exists: ");

        // Botón OK
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
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
                char a = pair.getKey().charAt(0);
                char b = pair.getValue().charAt(0);

                boolean exists = graph.containsEdge(a, b);
                showAlert("Edge between " + a + " and " + b + (exists ? " exists." : " does not exist."));
            } catch (NumberFormatException e) {
                showAlert("Invalid input! Please enter letters.");
            } catch (GraphException | ListException e) {
                showAlert("Error: " + e.getMessage());
            }
        });
    }

    public void containsVextex(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Enter letter to search:");
        dialog.showAndWait().ifPresent(value -> {
            try {
                char val = value.charAt(0);
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
        labelToGraph.setText("DFS Tour");
        textAreaGraph.setText("DFS Tour: "+graph.dfs());
    }
    public void bfsTour(ActionEvent actionEvent) throws GraphException, QueueException, ListException {
        labelToGraph.setText("BFS Tour");
        textAreaGraph.setText("BFS Tour: "+graph.bfs());
    }
    public void randomize(ActionEvent actionEvent) throws ListException, GraphException {
        graph.clear(); // Limpiar el grafo actual

        char[] letras = new char[10];
        int count = 0;

        for (; count < 10;) {
            char letter = util.Utility.getLetters(); // entre A y Z
            boolean repetido = false;

            // Verificar si ya existe
            for (int i = 0; i < 10; i++) {
                if (letras[i] == letter) {
                    repetido = true;
                    break;
                }
            }

            if (!repetido) {
                letras[count] = letter;
                count++;
            }
        }

        // colocar los numeros en los vertices del grafico opción 1, no es uno por una
        for (int i = 0; i < 10; i++) {
            graph.addVertex(letras[i]);
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
                edgePairs.add(new char[]{letras[i], letras[j]});
            }
        }
        Collections.shuffle(edgePairs); // orden aleatorio

        labelToGraph.setText("You have autogenerated a graph!");
        util.FXUtil.drawGraph(graph, pane);
    }

    public void addNextEdge(ActionEvent event) throws GraphException, ListException {
        if (currentEdgeIndex >= edgePairs.size()) {
            labelToGraph.setText("All edges have already been added.");
            return;
        }

        char[] pair = edgePairs.get(currentEdgeIndex++);
        int weight = util.Utility.randomMinMax(1, 50);
        try {
            graph.addEdgeWeight(pair[0], pair[1], weight);
        } catch (GraphException e) {
            System.out.println("ERROR: " + e.getMessage());
        }


        labelToGraph.setText("Edge added between " + pair[0] + " y " + pair[1] + " with weight " + weight);
        util.FXUtil.drawGraph(graph, pane);
    }


    public void toString(ActionEvent actionEvent) {
        labelToGraph.setText("Adjacency matrix graph content: ");
        textAreaGraph.setText(graph.toString());
    }
}
