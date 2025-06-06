package domain;

import domain.list.ListException;
import domain.queue.QueueException;
import domain.stack.StackException;
import org.junit.jupiter.api.Test;
import util.Utility;

class SinglyLinkedListGraphTest {
    @Test
    void test() {
        SinglyLinkedListGraph graph = new SinglyLinkedListGraph();
        try {
            for (char i = 'A'; i <= 'J'; i++)
                graph.addVertex(i);
            graph.addEdgeWeight('A', 'B', Utility.getName());
            graph.addEdgeWeight('A', 'C', Utility.getName());
            graph.addEdgeWeight('A', 'D', Utility.getName());
            graph.addEdgeWeight('B', 'F', Utility.getName());
            graph.addEdgeWeight('F', 'E', Utility.getName());
            graph.addEdgeWeight('F', 'J', Utility.getName());
            graph.addEdgeWeight('C', 'G', Utility.getName());
            graph.addEdgeWeight('G', 'J', Utility.getName());
            graph.addEdgeWeight('D', 'H', Utility.getName());
            graph.addEdgeWeight('H', 'I', Utility.getName());
            graph.addEdgeWeight('H', 'J', Utility.getName());
            System.out.println(graph);
            System.out.println("DFS Transversal Tour:\n" + graph.dfs());
            System.out.println("BFS Transversal Tour:\n" + graph.bfs());
            graph.removeVertex('F');
            graph.removeVertex('H');
            graph.removeVertex('J');
            System.out.println("DFS Transversal Tour:\n" + graph.dfs());
            System.out.println("BFS Transversal Tour:\n" + graph.bfs());
            System.out.println(graph);
        } catch (GraphException | ListException | QueueException | StackException e) {
            throw new RuntimeException(e);
        }
    }
}