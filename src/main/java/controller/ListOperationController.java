package controller;

import domain.AdjacencyListGraph;
import domain.GraphException;
import domain.list.ListException;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import util.FXUtil;
import util.Utility;
import java.util.ArrayList;

public class ListOperationController {
    @FXML
    private Canvas canvas;
    @FXML
    private Label label;
    @FXML
    private Pane pane;
    @FXML
    private TextArea textArea;
    AdjacencyListGraph graph;
    private ArrayList<String> unusedLetters;
    private ArrayList<String> usedLetters;
    private int max = 18;
    private double scale = 1.0;
    private final double SCALE_DELTA = 1.1;
    private Alert alert;

    @FXML
    private void initialize() {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Adjacency List Graph -  Error");
        graph = new AdjacencyListGraph(max);
        initGraph();
        pane.setOnScroll(event -> {
            if (event.getDeltaY() == 0) return;
            if (event.getDeltaY() > 0)
                scale *= SCALE_DELTA;
            else
                scale /= SCALE_DELTA;
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
    public void handleRandomize() {
        try {
            initGraph();
            for (int i = 0; i < 10; i++) {
                String name = unusedLetters.get(Utility.random(unusedLetters.size()));
                graph.addVertex(name);
                usedLetters.add(name);
                unusedLetters.remove(name);
            }
            label.setText("Randomized graph generated.");
            refreshMatrixDisplay();
            drawGraph();
        } catch (GraphException | ListException e) {
            alert.setContentText("Error: " + e.getMessage());
            alert.show();
        }
    }

    @FXML
    public void handleClear() {
        initGraph();
        pane.getChildren().clear();
        label.setText("Canvas cleared.");
        textArea.clear();
    }

    @FXML
    public void handleAddVertex() {
        try {
            int size = !graph.isEmpty()? graph.size() : 0;
            if (size > max)
                label.setText("Graph contains too many vertices.");
            else {
                String name = unusedLetters.get(Utility.random(unusedLetters.size()));
                graph.addVertex(name);
                usedLetters.add(name);
                unusedLetters.remove(name);
                label.setText("Vertex: [" + name + "] added. Total vertices: " + ++size);
                refreshMatrixDisplay();
                drawGraph();
            }
        } catch (GraphException | ListException e) {
            alert.setContentText("Error: " + e.getMessage());
            alert.show();
        }
    }

    @FXML
    public void handleRemoveVertex() {
        if (graph.isEmpty())
            label.setText("No vertices to remove.");
        else try {
            String name = usedLetters.get(Utility.random(usedLetters.size()));
            graph.removeVertex(name);
            unusedLetters.add(name);
            usedLetters.remove(name);
            label.setText("Vertex: [" + name + "] removed. Total vertices: " +
                    (!graph.isEmpty()? graph.size() : 0));
            refreshMatrixDisplay();
            drawGraph();
        } catch (GraphException | ListException e) {
            alert.setContentText("Error: " + e.getMessage());
            alert.show();
        }
    }

    @FXML
    public void handleAddEdge() {
        if (graph.isEmpty())
            label.setText("No vertices to add edges.");
        else try {
            int size = graph.size();
            if (size < 2)
                label.setText("Not enough vertices to add edges.");
            else {
                boolean added = false;
                String a = "", b = "";
                int weight = 0;
                for (int i = 0; i < size && !added; i++) {
                    for (int j = 0; j < size-1 && !added; j++) {
                        if (i == j) j++;
                        a = usedLetters.get(i);
                        b = usedLetters.get(j);
                        if (!graph.containsEdge(a, b)) {
                            weight = Utility.randomMinMax(1000, 2000);
                            graph.addEdgeWeight(a, b, weight);
                            added = true;
                        }
                    }
                }
                if (!added)
                    label.setText("All posible edges have been added.");
                else {
                    label.setText("Edge between vertex [" + a + "] and [" + b + "] added. Weight: " + weight);
                    refreshMatrixDisplay();
                    drawGraph();
                }
            }
        } catch (GraphException | ListException e) {
            alert.setContentText("Error: " + e.getMessage());
            alert.show();
        }
    }

    @FXML
    public void handleRemoveEdge() {
        if (graph.isEmpty())
            label.setText("No vertices to remove edges.");
        else try {
            int size = graph.size();
            if (size < 2)
                label.setText("Not enough vertices to remove edges.");
            else {
                boolean removed = false;
                String a = "", b = "";
                for (int i = 0; i < size-1 && !removed; i++) {
                    for (int j = 0; j < size-1 && !removed; j++) {
                        if (i == j) j++;
                        a = usedLetters.get(i);
                        b = usedLetters.get(j);
                        if (graph.containsEdge(a, b)) {
                            graph.removeEdge(a, b);
                            removed = true;
                        }
                    }
                }
                if (!removed)
                    label.setText("All edges have been removed.");
                else {
                    label.setText("Edge between vertex [" + a + "] and [" + b + "] added.");
                    refreshMatrixDisplay();
                    drawGraph();
                }
            }
        } catch (GraphException | ListException e) {
            alert.setContentText("Error: " + e.getMessage());
            alert.show();
        }
    }

    private void drawGraph() throws ListException, GraphException {
        if (graph.isEmpty())
            pane.getChildren().clear();
        else
            FXUtil.drawGraph(graph, pane);
    }

    private void refreshMatrixDisplay() {
        textArea.setText(graph.toString());
    }

    private void initGraph(){
        graph.clear();
        usedLetters = new ArrayList<>();
        unusedLetters = new ArrayList<>();
        for (char i = 'A'; i <= 'Z'; i++)
            unusedLetters.add(i + "");
    }
}
