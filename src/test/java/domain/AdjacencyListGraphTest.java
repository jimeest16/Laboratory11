package domain;

import domain.list.ListException;
import domain.queue.QueueException;
import domain.stack.StackException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static util.Utility.*;

class AdjacencyListGraphTest {


    private void conectarPorParidad(AdjacencyListGraph graph) throws ListException, GraphException {
        int n = graph.size();

        for (int i = 0; i < n; i++) {
            int v1 = (Integer) graph.getVertexAt(i);

            for (int j = i + 1; j < n; j++) {
                int v2 = (Integer) graph.getVertexAt(j);

                // Conectar si ambos son pares o ambos impares
                if ((v1 % 2) == (v2 % 2)) {
                    int peso = 1 + random(40);
                    graph.addEdgeWeight(v1, v2, peso);
                }
            }
        }
    }

    @Test
    public void testGraphOperations() {
        try {
            // a) Crear grafo
            AdjacencyListGraph graph = new AdjacencyListGraph(30);
            System.out.println("Creando grafo...");

            // b) Agregar 30 vértices aleatorios entre 10 y 50
            for (int i = 0; i < 30; i++) {
                int randomValue = randomMinMax(10, 51);
                graph.addVertex(randomValue);
            }
            System.out.println("Se han creado 30 vértices con números entre 10 y 50:");

            // Separar vértices en pares e impares para futuros usos
            List<Integer> pares = new ArrayList<>();
            List<Integer> impares = new ArrayList<>();

            for (int i = 0; i < graph.size(); i++) {
                int vertxValue = (Integer) graph.getVertexAt(i);
                if (vertxValue % 2 == 0) pares.add(vertxValue);
                else impares.add(vertxValue);
            }

            System.out.println("Vértices pares: " + pares);
            System.out.println("Vértices impares: " + impares);

            // c) Conectar todos los pares entre sí y todos los impares entre sí
            conectarPorParidad(graph);
            System.out.println("\nVértices pares e impares conectados entre sí.");

            // e) Conectar aleatoriamente 10 pares con 10 impares
            int maxConnections = Math.min(10, Math.min(pares.size(), impares.size()));
            System.out.println("\nConectando aleatoriamente " + maxConnections + " pares con impares:");
            for (int i = 0; i < maxConnections; i++) {
                int paresNumbers = pares.get(i);
                int imparesNumbers = impares.get(i);
                int weight = 1 + random(40);
                graph.addEdgeWeight(paresNumbers, imparesNumbers, weight);
                System.out.printf("Conectado %d (par) con %d (impar) con peso %d%n", paresNumbers, imparesNumbers, weight);
            }

            // d) Mostrar contenido del grafo tras conexiones iniciales
            System.out.println("\nContenido del grafo tras conexiones iniciales:");
            System.out.println(graph);

            // f) Probar recorridos DFS y BFS
            System.out.println("\nRecorrido DFS:");
            System.out.println(graph.dfs());

            System.out.println("\nRecorrido BFS:");
            System.out.println(graph.bfs());

            // g) Suprimir 5 vértices al azar junto con sus aristas
            System.out.println("\nEliminando 5 vértices al azar:");
            List<Integer> verticesToRemove = new ArrayList<>();
            for (int i = 0; i < graph.size(); i++) {
                verticesToRemove.add((Integer) graph.getVertexAt(i));
            }
            Collections.shuffle(verticesToRemove);// para que sea alzar

            for (int i = 0; i < 5 && i < verticesToRemove.size(); i++) {
                int toRemove = verticesToRemove.get(i);
                graph.removeVertex(toRemove);
                System.out.println("Vértice eliminado: " + toRemove);
            }

            // h) Mostrar contenido del grafo tras eliminaciones
            System.out.println("\nContenido del grafo tras eliminar 5 vértices:");
            System.out.println(graph);

        } catch (GraphException | ListException | StackException | QueueException e) {
            throw new RuntimeException(e);
        }
    }

}
