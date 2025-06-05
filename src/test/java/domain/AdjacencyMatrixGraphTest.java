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

            graph.addVertex('P');
            graph.addVertex('T');
            graph.addVertex('K');
            graph.addVertex('D');
            graph.addVertex('S');
            graph.addVertex('M');
            graph.addVertex('H');
            graph.addVertex('A');
            graph.addVertex('E');
            graph.addVertex('Q');
            graph.addVertex('G');
            graph.addVertex('B');
            graph.addVertex('R');
            graph.addVertex('J');

            graph.addEdgeWeight('P', 'T',util.Utility.getColor());
            graph.addEdgeWeight('T', 'S',util.Utility.getColor());
            graph.addEdgeWeight('S', 'A',util.Utility.getColor());
            graph.addEdgeWeight('A', 'G',util.Utility.getColor());
            graph.addEdgeWeight('G', 'B',util.Utility.getColor());

            graph.addEdgeWeight('P', 'K',util.Utility.getColor());
            graph.addEdgeWeight('K', 'M',util.Utility.getColor());
            graph.addEdgeWeight('M', 'E',util.Utility.getColor());

            graph.addEdgeWeight('D', 'H',util.Utility.getColor());
            graph.addEdgeWeight('H', 'Q',util.Utility.getColor());
            graph.addEdgeWeight('Q', 'R',util.Utility.getColor());
            graph.addEdgeWeight('R', 'J',util.Utility.getColor());
            System.out.println(graph);

            System.out.println("DFS Transversal Tour: " + graph.dfs());
            System.out.println("BFS Transversal Tour:" + graph.bfs());
            // para eliminar vertices ->
            System.out.println("Vertex deleted: T, K, H");
            graph.removeVertex('T');
            graph.removeVertex('K');
            graph.removeVertex('H');

            System.out.println("Edge deleted: P--T");
            graph.removeEdge('P', 'T');
            System.out.println("Edge deleted: T--S");
            graph.removeEdge('T', 'S');
            System.out.println("Edge deleted: P--K");
            graph.removeEdge('P', 'K');
            System.out.println("Edge deleted: K--M");
            graph.removeEdge('K', 'M');
            System.out.println("Edge deleted: D--H");
            graph.removeEdge('D', 'H');
            System.out.println("Edge deleted: H--Q");
            graph.removeEdge('H', 'Q');
            System.out.println(graph);
        } catch (ListException | GraphException e) {
            throw new RuntimeException(e);
        } catch (QueueException e) {
            throw new RuntimeException(e);
        } catch (StackException e) {
            throw new RuntimeException(e);
        }
    }

    public void test2() {
        try {
            AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(50);

            for (char i = 'a'; i <= 'e'; i++) {
                graph.addVertex(i);
            }
            graph.addEdgeWeight('P', 'T', random(20)+2);
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