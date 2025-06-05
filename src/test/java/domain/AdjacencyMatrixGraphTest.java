package domain;

import domain.list.ListException;
import domain.queue.QueueException;
import domain.stack.StackException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static util.Utility.random;

class AdjacencyMatrixGraphTest {

    @Test
    public void test() {
        try {
        AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(50);

            for (char i = 'a'; i <= 'e'; i++) {
                graph.addVertex(i);
            }
            graph.addEdge('a', 'b');
            graph.addEdge('a', 'c');
            graph.addEdge('a', 'd');
            graph.addEdge('b', 'e');
            graph.addEdge('c', 'd');
            graph.addEdge('c', 'e');

            System.out.println(graph);

        } catch (ListException | GraphException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void test2() {
        try {
            AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(50);

            for (char i = 'a'; i <= 'e'; i++) {
                graph.addVertex(i);
            }
            graph.addEdgeWeight('a', 'b', random(20)+2);
            graph.addEdgeWeight('a', 'c',random(20)+2);
            graph.addEdgeWeight('a', 'd',random(20)+2);
            graph.addEdgeWeight('b', 'e',random(20)+2);
            graph.addEdgeWeight('c', 'd',random(20)+2);
            graph.addEdgeWeight('c', 'e',random(20)+2);

            System.out.println(graph);

        } catch (GraphException e) {
            throw new RuntimeException(e);
        } catch (ListException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void test3() {
        try {
            AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(50);

            for (char i = 'a'; i <= 'e'; i++) {
                graph.addVertex(i);
            }
            // para añadir
            graph.addEdgeWeight('a', 'b', random(20) + 2);
            graph.addEdgeWeight('a', 'c', random(20) + 2);
            graph.addEdgeWeight('a', 'd', random(20) + 2);
            graph.addEdgeWeight('b', 'e', random(20) + 2);
            graph.addEdgeWeight('c', 'd', random(20) + 2);
            graph.addEdgeWeight('c', 'e', random(20) + 2);

            System.out.println(graph);
            System.out.println("DFS Transversal Tour: " + graph.dfs());
            System.out.println("BFS Transversal Tour:" + graph.bfs());
            // para eliminar vertices ->
            System.out.println("Vertex deleted: a");
            graph.removeVertex('a');
            System.out.println(graph);

            System.out.println("Edge deleted: e--b");
            graph.removeEdge('e', 'b');
            System.out.println(graph);

        } catch (GraphException e) {
            throw new RuntimeException(e);
        } catch (ListException e) {
            throw new RuntimeException(e);
        } catch (StackException e) {
            throw new RuntimeException(e);
        } catch (QueueException e) {
            throw new RuntimeException(e);

        }
    }

}