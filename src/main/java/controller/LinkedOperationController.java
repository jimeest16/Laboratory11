package controller;

import domain.GraphException;
import domain.SinglyLinkedListGraph;
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
import java.util.Arrays;

public class LinkedOperationController {
    @FXML
    private Canvas canvas;
    @FXML
    private Pane pane;
    @FXML
    private Label label;
    @FXML
    private TextArea textArea;
    SinglyLinkedListGraph graph;
    private ArrayList<String> unusedNames;
    private ArrayList<String> usedNames;
    private double scale = 1.0;
    private final double SCALE_DELTA = 1.1;
    private Alert alert;
    private Alert infoAlert;

    @FXML
    private void initialize() {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Singly Linked Graph -  Error");
        infoAlert = FXUtil.informationDialog("Singly Linked Graph -  Information");
        graph = new SinglyLinkedListGraph();
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
                String name = unusedNames.get(Utility.random(unusedNames.size()));
                graph.addVertex(name);
                usedNames.add(name);
                unusedNames.remove(name);
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
            if (size > 17)
                label.setText("Graph contains too many vertices.");
            else {
                String name = unusedNames.get(Utility.random(unusedNames.size()));
                graph.addVertex(name);
                usedNames.add(name);
                unusedNames.remove(name);
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
            String name = usedNames.get(Utility.random(usedNames.size()));
            graph.removeVertex(name);
            unusedNames.add(name);
            usedNames.remove(name);
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
                for (int i = 0; i < size-1 && !added; i++) {
                    for (int j = 0; j < size-1 && !added; j++) {
                        if (i == j) j++;
                        a = usedNames.get(i);
                        b = usedNames.get(j);
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
                        a = usedNames.get(i);
                        b = usedNames.get(j);
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

    private void drawGraph() throws ListException {
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
        usedNames = new ArrayList<>();
        unusedNames = new ArrayList<>(Arrays.asList(
                "Platon", "Aristoteles", "DaVinci", "Galileo", "Newton",
                "Darwin", "Tesla", "Freud", "Einstein", "Napoleon",
                "Lincoln", "Bolivar", "Cleopatra", "Socrates", "Voltaire",
                "Homer", "FridaKahlo", "MarieCurie", "Kepler", "Fermi",
                "Lavoisier", "Plato", "Turing", "Hammurabi", "Bach",
                "Mozart", "Beethoven", "Caesar"
        ));
    }
}
