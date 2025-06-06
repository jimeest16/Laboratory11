package controller;

import domain.AdjacencyListGraph;
import domain.EdgdeWeight;
import domain.GraphException;
import domain.list.ListException;
import domain.list.SinglyLinkedList;
import domain.queue.QueueException;
import domain.stack.StackException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.Optional;
import java.util.Random;

public class LinkedGraphController {

    @FXML
    private Pane graphPane;
    @FXML
    private TextArea movementTextArea, infoTextArea;
    @FXML
    private Button btnRandomize, btnContainsVertex, btnContainsEdge, btnToString, btnDFS, btnBFS, btnClear;

    private SinglyLinkedList list;
    private AdjacencyListGraph graph;

    private final String[] personajes = {
            "Napoleon", "Cleopatra", "Einstein", "Da Vinci", "Mandela",
            "Aristoteles", "Juana de Arco", "Churchill", "Mozart", "Colon"
    };

    @FXML
    private void initialize() {
        movementTextArea.setPromptText("Aquí se mostrarán los movimientos.");
        cargarPersonajes();

        configurarZoom();
    }

    private void configurarZoom() {
        final double SCALE_DELTA = 1.1;
        graphPane.setOnScroll(event -> {
            double scale = graphPane.getScaleX();
            if (event.getDeltaY() < 0) {
                scale /= SCALE_DELTA;
            } else {
                scale *= SCALE_DELTA;
            }
            graphPane.setScaleX(scale);
            graphPane.setScaleY(scale);
            event.consume();
        });
    }

    private void cargarPersonajes() {
        list = new SinglyLinkedList();
        for (String personaje : personajes) {
            list.addLast(personaje);

        }
    }
    private void shufflePersonajes() {
        Random random = new Random();
        for (int i = personajes.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            // Intercambia personajes[i] con personajes[j]
            String temp = personajes[i];
            personajes[i] = personajes[j];
            personajes[j] = temp;
        }
    }


    @FXML
    private void handleRandomize() {
        try {
            shufflePersonajes();
            cargarPersonajes();
            graph = new AdjacencyListGraph(list.size());

            Random random = new Random();

            for (int i = 1; i <= list.size(); i++) {
                graph.addVertex((String) list.getNode(i).getData());
            }

            int n = list.size();

            for (int i = 1; i <= n; i++) {
                String v1 = (String) list.getNode(i).getData();
                int aristas = 1 + random.nextInt(n - 1);

                for (int k = 0; k < aristas; k++) {
                    int idxDestino;
                    do {
                        idxDestino = 1 + random.nextInt(n);
                    } while (idxDestino == i);

                    String v2 = (String) list.getNode(idxDestino).getData();

                    if (!graph.containsEdge(v1, v2)) {
                        int peso = 1000 + random.nextInt(1001);
                        graph.addEdgeWeight(v1, v2, peso);
                    }
                }
            }

            drawGraph();
            handleToString();

        } catch (GraphException | ListException e) {
            infoTextArea.setText("Error generando grafo: " + e.getMessage());
        }
    }

    private void drawGraph() {
        graphPane.getChildren().clear();

        try {
            int n = graph.size();
            double centerX = graphPane.getWidth() / 2;
            double centerY = graphPane.getHeight() / 2;
            double radius = Math.min(centerX, centerY) - 50;

            double[] x = new double[n];
            double[] y = new double[n];

            for (int i = 0; i < n; i++) {
                double angle = 2 * Math.PI * i / n;
                x[i] = centerX + radius * Math.cos(angle);
                y[i] = centerY + radius * Math.sin(angle);
            }

            for (int i = 0; i < n; i++) {
                String v1 = (String) graph.getVertexAt(i);
                SinglyLinkedList vecinos = graph.getAdjacencyListVertices(v1);

                for (int j = 1; j <= vecinos.size(); j++) {
                    EdgdeWeight edge = (EdgdeWeight) vecinos.getNode(j).getData();
                    String v2 = (String) edge.getEdge();
                    int idxV2 = indexOfVertex(v2);
                    if (idxV2 == -1 || i >= idxV2) continue;

                    Line line = new Line(x[i], y[i], x[idxV2], y[idxV2]);
                    line.setStroke(Color.GRAY);
                    line.setStrokeWidth(2);
                    graphPane.getChildren().add(line);
                }
            }

            for (int i = 0; i < n; i++) {
                String label = (String) graph.getVertexAt(i);
                Circle circle = new Circle(x[i], y[i], 25);
                circle.setFill(Color.LIGHTBLUE);
                circle.setStroke(Color.BLACK);
                circle.setStrokeWidth(2);

                final int selectedIndex = i;
                circle.setOnMouseClicked(e -> highlightVertex(selectedIndex, x, y));

                graphPane.getChildren().add(circle);

                Text text = new Text(x[i] - label.length() * 3.5, y[i] + 5, label);
                text.setStyle("-fx-font-weight: bold;");
                graphPane.getChildren().add(text);
            }

        } catch (GraphException | ListException e) {
            infoTextArea.setText("Error al dibujar el grafo: " + e.getMessage());
        }
    }

    private void highlightVertex(int selectedIndex, double[] x, double[] y) {
        graphPane.getChildren().clear();

        try {
            int n = graph.size();
            String edgesInfo = "";

            for (int i = 0; i < n; i++) {
                String v1 = (String) graph.getVertexAt(i);
                SinglyLinkedList vecinos = graph.getAdjacencyListVertices(v1);

                for (int j = 1; j <= vecinos.size(); j++) {
                    EdgdeWeight edge = (EdgdeWeight) vecinos.getNode(j).getData();
                    String v2 = (String) edge.getEdge();
                    int idxV2 = indexOfVertex(v2);

                    if (idxV2 == -1 || i >= idxV2) continue;

                    Line line = new Line(x[i], y[i], x[idxV2], y[idxV2]);
                    boolean isSelectedEdge = (i == selectedIndex || idxV2 == selectedIndex);

                    line.setStroke(isSelectedEdge ? Color.RED : Color.GRAY);
                    line.setStrokeWidth(isSelectedEdge ? 3 : 2);
                    graphPane.getChildren().add(line);

                    if (isSelectedEdge) {
                        if (i == selectedIndex) {
                            edgesInfo += "Edge Between Vertexes: " + v1 + "----" + v2 + " Weight: " + edge.getWeight() + "\n";
                        } else {
                            edgesInfo += "Edge Between Vertexes: " + v2 + "----" + v1 + " Weight: " + edge.getWeight() + "\n";
                        }
                    }
                }
            }

            if (edgesInfo.isEmpty()) {
                movementTextArea.setText("No edges for vertex: " + graph.getVertexAt(selectedIndex));
            } else {
                movementTextArea.setText(edgesInfo);
            }

            for (int i = 0; i < n; i++) {
                String label = (String) graph.getVertexAt(i);
                Circle circle = new Circle(x[i], y[i], 25, i == selectedIndex ? Color.ORANGE : Color.LIGHTBLUE);
                circle.setStroke(Color.BLACK);
                circle.setStrokeWidth(2);
                final int idx = i;
                circle.setOnMouseClicked(e -> highlightVertex(idx, x, y));
                graphPane.getChildren().addAll(circle, new Text(x[i] - label.length() * 3.5, y[i] + 5, label));
            }

        } catch (GraphException | ListException e) {
            infoTextArea.setText("Error al dibujar el grafo: " + e.getMessage());
        }
    }

    private int indexOfVertex(String vertex) throws GraphException, ListException {
        for (int i = 0; i < graph.size(); i++) {
            if (((String) graph.getVertexAt(i)).equals(vertex)) {
                return i;
            }
        }
        return -1;
    }

    @FXML
    private void handleContainsVertex() {
        if (graph == null) {
            infoTextArea.setText("Primero genera el grafo.");
            return;
        }
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Verificar Vértice");
        dialog.setHeaderText("Ingrese el nombre del vértice a buscar:");
        dialog.setContentText("Nombre:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String vertex = result.get().trim();
            try {
                if (graph.containsVertex(vertex)) {
                    infoTextArea.setText("El vértice \"" + vertex + "\" sí existe en el grafo.");
                } else {
                    infoTextArea.setText("El vértice \"" + vertex + "\" NO existe en el grafo.");
                }
            } catch (GraphException | ListException e) {
                infoTextArea.setText("Error al buscar vértice: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleContainsEdge() {
        if (graph == null) {
            infoTextArea.setText("Primero genera el grafo.");
            return;
        }
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Verificar Arista");
        dialog.setHeaderText("Ingrese los nombres de los vértices para la arista:");
        dialog.setContentText("Ingrese:Vertice,Arista");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String[] vertices = result.get().split(",");
            if (vertices.length != 2) {
                infoTextArea.setText("Formato inválido. Ingrese:Vertice,Arista");
                return;
            }
            String v1 = vertices[0].trim();
            String v2 = vertices[1].trim();

            try {
                if (graph.containsEdge(v1, v2)) {
                    infoTextArea.setText("La arista (" + v1 + " - " + v2 + ") sí existe en el grafo.");
                } else {
                    infoTextArea.setText("La arista (" + v1 + " - " + v2 + ") NO existe en el grafo.");
                }
            } catch (GraphException | ListException e) {
                infoTextArea.setText("Error al buscar arista: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleToString() {
        if (graph == null) {
            infoTextArea.setText("Primero genera el grafo.");
            return;
        }
        try {
            infoTextArea.setText(graph.toString());
        } catch (Exception e) {
            infoTextArea.setText("Error al mostrar grafo: " + e.getMessage());
        }
    }

    @FXML
    private void handleDFS() {
        if (graph == null) {
            infoTextArea.setText("Primero genera el grafo.");
            return;
        }
        try {
            String recorrido = graph.dfs();
            infoTextArea.appendText("\n[DFS]: " + recorrido);
        } catch (GraphException | StackException | ListException e) {
            infoTextArea.setText("Error en recorrido DFS: " + e.getMessage());
        }
    }
    @FXML
    private void handleBFS() {
        if (graph == null) {
            infoTextArea.setText("Primero genera el grafo.");
            return;
        }
        try {
            String recorrido = graph.bfs();
            infoTextArea.appendText("\n[BFS]: " + recorrido);
        } catch (GraphException | QueueException | ListException e) {
            infoTextArea.setText("Error en recorrido BFS: " + e.getMessage());
        }
    }
    private void mostrarRecorrido(SinglyLinkedList recorrido, String tipo) throws ListException {
        StringBuilder sb = new StringBuilder(tipo + " recorrido: ");
        for (int i = 1; i <= recorrido.size(); i++) {
            sb.append(recorrido.getNode(i).getData());
            if (i < recorrido.size()) sb.append(" -> ");
        }
        movementTextArea.setText(sb.toString());
    }

    @FXML
    private void handleClear() {
        graphPane.getChildren().clear();
        movementTextArea.clear();
        infoTextArea.clear();
        graph = null;
    }
}
