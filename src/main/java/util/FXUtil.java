package util;

import domain.*;
import domain.list.ListException;
import domain.list.SinglyLinkedList;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.converter.IntegerStringConverter;
import ucr.lab.HelloApplication;

import javafx.geometry.Point2D;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FXUtil {

    public static void loadPage(String className, String page, BorderPane bp) {
        try {
            Class cl = Class.forName(className);
            FXMLLoader fxmlLoader = new FXMLLoader(cl.getResource(page));
            cl.getResource("bp");
            bp.setCenter(fxmlLoader.load());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static TextFormatter<Integer> getTextFormatterInteger() {
        return new TextFormatter<>(new IntegerStringConverter(), 0,
                change -> (change.getControlNewText().matches("\\d*")) ? change : null);
    }

    public static Alert alert(String title, String headerText){
        Alert myalert = new Alert(Alert.AlertType.INFORMATION);
        myalert.setTitle(title);
        myalert.setHeaderText(headerText);
        DialogPane dialogPane = myalert.getDialogPane();
        String css = Objects.requireNonNull(HelloApplication.class.getResource("/ucr/lab/stylesheet.css")).toExternalForm();
        dialogPane.getStylesheets().add(css);
        dialogPane.getStyleClass().add("myDialog");
        return myalert;
    }

    public static Alert informationDialog(String title) {
        Alert myalert = new Alert(Alert.AlertType.NONE);
        myalert.setAlertType(Alert.AlertType.INFORMATION);
        myalert.setTitle(title);
        myalert.setHeaderText(null);
        return myalert;
    }

    public static Alert confirmationDialog(String title) {
        Alert myalert = new Alert(Alert.AlertType.NONE);
        myalert.setAlertType(Alert.AlertType.CONFIRMATION);
        myalert.setTitle(title);
        myalert.setHeaderText(null);
        return myalert;
    }


    public static TextInputDialog dialog(String title, String headerText){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        //String css = HelloApplication.class.getResource("moderna.css").toExternalForm();
        //dialog.getEditor().getStylesheets().add(css);
        return dialog;
    }

    public static String alertYesNo(String title, String headerText, String contextText){
        Alert myalert = new Alert(Alert.AlertType.CONFIRMATION);
        myalert.setTitle(title);
        myalert.setHeaderText(headerText);
        myalert.setContentText(contextText);
        ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);
        myalert.getDialogPane().getButtonTypes().clear(); //quita los botones defaults
        myalert.getDialogPane().getButtonTypes().add(buttonTypeYes);
        myalert.getDialogPane().getButtonTypes().add(buttonTypeNo);
        //dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        DialogPane dialogPane = myalert.getDialogPane();
        String css = Objects.requireNonNull(HelloApplication.class.getResource("dialog.css")).toExternalForm();
        dialogPane.getStylesheets().add(css);
        Optional<ButtonType> result = myalert.showAndWait();
        //if((result.isPresent())&&(result.get()== ButtonType.OK)) {
        if((result.isPresent())&&(result.get()== buttonTypeYes))
            return "YES";
        else return "NO";
    }

    public static void drawGraph(AdjacencyMatrixGraph graph, Pane pane) throws ListException {
        pane.getChildren().clear(); // limpiar antes de dibujar

        int size = graph.size();
        double radius = 100;
        double centerX = 200;
        double centerY = 200;

        // Coordenadas de los nodos
        Point2D[] positions = new Point2D[size];

        // 1. Dibujar vértices en círculo
        for (int i = 0; i < size; i++) {
            double angle = 2 * Math.PI * i / size;
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);
            positions[i] = new Point2D(x, y);

            Circle circle = new Circle(x, y, 20);
            circle.setFill(Color.LIGHTBLUE);
            circle.setStroke(Color.BLACK);

            Text text = new Text(x - 5, y + 5, graph.vertexList[i].data.toString());

            pane.getChildren().addAll(circle, text);
        }

        // 2. Dibujar aristas y pesos
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                Object edge = graph.adjacencyMatrix[i][j];
                if (!edge.equals(0)) {
                    Point2D p1 = positions[i];
                    Point2D p2 = positions[j];

                    // Línea entre los nodos
                    Line line = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
                    line.setStroke(Color.GRAY);

                    // Mostrar peso si no es 1
                    if (!edge.equals(1)) {
                        double midX = (p1.getX() + p2.getX()) / 2;
                        double midY = (p1.getY() + p2.getY()) / 2;
                        Text weightText = new Text(midX, midY, edge.toString());
                        weightText.setFill(Color.RED);
                        pane.getChildren().add(weightText);
                    }

                    pane.getChildren().add(line);
                }
            }
        }
    }

    public static void drawGraph(AdjacencyListGraph graph, Pane pane) throws ListException, GraphException {
        pane.getChildren().clear(); // Limpiar antes de dibujar

        int size = graph.size();
        double radius = 150;
        double centerX = 250;
        double centerY = 250;

        Point2D[] positions = new Point2D[size];

        // 1. Dibujar vértices en círculo
        for (int i = 0; i < size; i++) {
            double angle = 2 * Math.PI * i / size;
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);
            positions[i] = new Point2D(x, y);

            Circle circle = new Circle(x, y, 20);
            circle.setFill(Color.LIGHTBLUE);
            circle.setStroke(Color.BLACK);

            Text text = new Text(x - 5, y + 5, graph.getVertexAt(i).toString());

            pane.getChildren().addAll(circle, text);
        }

        // 2. Dibujar aristas y pesos
        for (int i = 0; i < size; i++) {
            Object fromVertex = graph.getVertexAt(i);
            SinglyLinkedList edgeList = graph.vertexList[i].edgesList;

            for (int j = 1; j <= edgeList.size(); j++) {
                EdgeWeight edge = (EdgeWeight) edgeList.getNode(j).getData();
                Object toVertex = edge.getEdge();

                int toIndex = -1;
                for (int k = 0; k < size; k++) {
                    if (graph.getVertexAt(k).equals(toVertex)) {
                        toIndex = k;
                        break;
                    }
                }

                // Para evitar dibujar la arista dos veces (grafo no dirigido)
                if (toIndex > i) {
                    Point2D p1 = positions[i];
                    Point2D p2 = positions[toIndex];

                    Line line = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
                    line.setStroke(Color.GRAY);

                    pane.getChildren().add(line);

                    // Mostrar peso si existe
                    if (edge.getWeight() != null) {
                        double midX = (p1.getX() + p2.getX()) / 2;
                        double midY = (p1.getY() + p2.getY()) / 2;
                        Text weightText = new Text(midX, midY, edge.getWeight().toString());
                        weightText.setFill(Color.RED);
                        pane.getChildren().add(weightText);
                    }
                }
            }
        }
    }



}//end FXUtil
