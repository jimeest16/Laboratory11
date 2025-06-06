package controller;

import domain.AdjacencyMatrixGraph;
import domain.GraphException;
import domain.list.ListException;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdjacencyMatrixOperationController {

    @FXML
    private TextArea taMatrix;

    @FXML
    private Label labelMatrix;

    @FXML
    private Canvas canvas;

    @FXML
    private Button btnAddVertex, btnRemoveVertex, btnAddEdges, btnRemoveEdge, btnRandomize, btnClear;

    @FXML
    private Pane pane;

    private AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(10);

    private List<int[]> edgePairs = new ArrayList<>();
    private int currentEdgeIndex = 0;

    private double scale = 1.0;
    private final double SCALE_DELTA = 1.1;

 // inento de seleccionar un arista
    private Object selectedVertex = null;

    @FXML
    private void initialize() {
        pane.setOnScroll(event -> {
            if (event.getDeltaY() == 0) return;

            if (event.getDeltaY() > 0) {
                scale *= SCALE_DELTA;
            } else {
                scale /= SCALE_DELTA;
            }

            scale = limit(scale, 0.1, 10);

            pane.setScaleX(scale);
            pane.setScaleY(scale);

            event.consume();
        });

    }

    // Limits the pane scale
    private double limit(double value, double min, double max) {
        if(value < min) return min;
        if(value > max) return max;
        return value;
    }

    @FXML
    private void handleAddVertex() {
        try {
            int vertexValue;
            boolean repeated;

            do {
                vertexValue = util.Utility.randomMinMax(0, 99);
                repeated = graph.containsVertex(vertexValue);
            } while (repeated);

            graph.addVertex(vertexValue);

            labelMatrix.setText("Vertex: " + vertexValue + " added. Total vertices: " + graph.size());
            refreshMatrixDisplay();
            drawGraph();
        } catch (ListException | GraphException e) {
            labelMatrix.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleRemoveVertex() {
        try {
            if (graph.isEmpty()) {
                labelMatrix.setText("No vertices to remove.");
                return;
            }

            List<Object> vertices = new ArrayList<>();
            for (int i = 0; i < graph.size(); i++) {
                vertices.add(graph.getVertex(i));
            }

            int randomIndex = util.Utility.randomMinMax(0, vertices.size() - 1);
            Object vertexToRemove = vertices.get(randomIndex);

            graph.removeVertex(vertexToRemove);

            labelMatrix.setText("Vertex: " + vertexToRemove + " removed. Total vertices: " + graph.size());
            refreshMatrixDisplay();
            drawGraph();

            // If removed vertex was selected, clear selection
            if (vertexToRemove.equals(selectedVertex)) {
                selectedVertex = null;
            }
        } catch (ListException | GraphException e) {
            labelMatrix.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleRemoveEdgeAndWeights() {
        try {
            if (edgePairs.isEmpty()) {
                labelMatrix.setText("No edges to remove.");
                return;
            }

            boolean removed = false;
            for (int i = 0; i < currentEdgeIndex && !removed; i++) {
                int[] pair = edgePairs.get(i);
                if (graph.containsEdge(pair[0], pair[1])) {
                    graph.removeEdge(pair[0], pair[1]);
                    labelMatrix.setText("Edge removed between " + pair[0] + " and " + pair[1]);
                    removed = true;
                }
            }

            if (!removed) {
                labelMatrix.setText("No active edges found to remove.");
            }

            refreshMatrixDisplay();
            drawGraph();
        } catch (ListException | GraphException e) {
            labelMatrix.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddEdgesAndWeights() throws ListException, GraphException {
        if (currentEdgeIndex >= edgePairs.size()) {
            labelMatrix.setText("All edges have already been added.");
            return;
        }

        int[] pair = edgePairs.get(currentEdgeIndex++);
        int weight = util.Utility.randomMinMax(1, 50);
        graph.addEdgeWeight(pair[0], pair[1], weight);

        labelMatrix.setText("Edge added between " + pair[0] + " and " + pair[1] + " with weight " + weight);
        refreshMatrixDisplay();
        drawGraph();
    }

    @FXML
    private void handleRandomizeGraph() {
        try {
            randomize();
            labelMatrix.setText("Randomized graph generated.");
            refreshMatrixDisplay();
            drawGraph();
        } catch (ListException | GraphException e) {
            labelMatrix.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleClearCanvas() {
        pane.getChildren().clear();
        labelMatrix.setText("Canvas cleared.");
        taMatrix.clear();
        selectedVertex = null;
    }

    private void randomize() throws ListException, GraphException {
        graph.clear();

        int[] numbers = new int[10];
        int count = 0;

        while (count < 10) {
            int num = util.Utility.randomMinMax(0, 99);
            boolean repeated = false;

            for (int i = 0; i < count; i++) {
                if (numbers[i] == num) {
                    repeated = true;
                    break;
                }
            }

            if (!repeated) {
                numbers[count] = num;
                count++;
            }
        }

        for (int i = 0; i < 10; i++) {
            graph.addVertex(numbers[i]);
        }

        edgePairs.clear();
        currentEdgeIndex = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = i + 1; j < 10; j++) {
                edgePairs.add(new int[]{numbers[i], numbers[j]});
            }
        }
        Collections.shuffle(edgePairs);
    }

    private void drawGraph() throws ListException {
        util.FXUtil.drawGraph(graph, pane);
    }


    private void refreshMatrixDisplay() {
        taMatrix.setText(graph.toString());
    }
}
