package domain;

import domain.list.ListException;
import domain.list.SinglyLinkedList;
import domain.queue.LinkedQueue;
import domain.queue.QueueException;
import domain.stack.LinkedStack;
import domain.stack.StackException;

import static util.Utility.compare;

public class AdjacencyListGraph implements Graph {
    public Vertex[] vertexList; //arreglo de objetos tipo vértice

    private int n; //max de elementos
    private int counter; //contador de vertices

    //para los recorridos dfs, bfs
    private LinkedStack stack;
    private LinkedQueue queue;

    //Constructor
    public AdjacencyListGraph(int n) {
        if (n <= 0) System.exit(1); //sale con status==1 (error)
        this.n = n;
        this.counter = 0;
        this.vertexList = new Vertex[n];

        this.stack = new LinkedStack();
        this.queue = new LinkedQueue();

    }


    @Override
    public int size() throws ListException {
        return counter;
    }

    @Override
    public void clear() {
        this.vertexList = new Vertex[n];

        this.counter = 0; //inicializo contador de vértices

    }

    @Override
    public boolean isEmpty() {
        return counter == 0;
    }

    @Override
    public boolean containsVertex(Object element) throws GraphException, ListException {
        if (isEmpty())
            throw new GraphException("Adjacency list is full");
//
//        for( int i =0;i<counter;i++){
//            if(compare(vertexList[i].data, element)==0)
//                return true;
//
//        }

        // opcion 2:
        return indexOf(element) != -1;

    }


    @Override
    public boolean containsEdge(Object a, Object b) throws GraphException, ListException {
        if (isEmpty())
            throw new GraphException("Graph is full");

        //return indexOf(compare(a,0))!=-1;
        return !vertexList[indexOf(a)].edgesList.isEmpty() && vertexList[indexOf(a)].edgesList.contains(new EdgeWeight(b, null));


    }

    @Override
    public void addVertex(Object element) throws GraphException, ListException {
        if (counter >= vertexList.length)
            throw new GraphException("Graph is full");
        vertexList[counter++] = new Vertex(element);

    }

    @Override
    public void addEdge(Object a, Object b) throws GraphException, ListException {

        if (!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Cannot match edge between vertexes ["
                    + a + "]" + "y" + "[" +
                    b + "]");
        // para que logre contener el peso
        vertexList[indexOf(a)].edgesList.add(new EdgeWeight(b, null));
        // grafo no dirigido
        // grafo no dirigido
        vertexList[indexOf(b)].edgesList.add(new EdgeWeight(a, null));
    }

    private int indexOf(Object element) {
        for (int i = 0; i < counter; i++) {
            if (compare(vertexList[i].data, element) == 0)
                return i;
        }
        return -1;
    }

    @Override
    public void addWeight(Object a, Object b, Object weight) throws GraphException, ListException {
        if (!containsEdge(a, b))
            throw new GraphException("Cannot match edge between vertexes ["
                    + a + "]" + "y" + "[" +
                    b + "]");

        updateEdgesListEdgesWeight(a, b, weight);
        updateEdgesListEdgesWeight(b, a, weight);
        // se hizo metodo para simplificar
//        EdgdeWeight ew= (EdgdeWeight) vertexList[indexOf(a)].edgesList
//                .getNode(new EdgdeWeight(b,null)).getData();
//
//        // ahora agrego el peso
//        ew.setWeight(weight);
//
//       // ahora actualizo la info en la lista de aristas correspondiente
//        vertexList[indexOf(a)].edgesList.getNode(new EdgdeWeight(b, null))
//                .setData(ew);
//
//        // para grafo no dirigido
//         ew= (EdgdeWeight) vertexList[indexOf(b)].edgesList
//                .getNode(new EdgdeWeight(a,null)).getData();
//
//        // ahora agrego el peso
//        ew.setWeight(weight);
//
//        // ahora actualizo la info en la lista de aristas correspondiente
//        vertexList[indexOf(b)].edgesList.getNode(new EdgdeWeight(a, null))
//                .setData(ew);
    }

    private void updateEdgesListEdgesWeight(Object a, Object b, Object weight) throws ListException {
        EdgeWeight ew = (EdgeWeight) vertexList[indexOf(a)].edgesList
                .getNode(new EdgeWeight(b, null)).getData();

        // ahora agrego el peso
        ew.setWeight(weight);

        // ahora actualizo la info en la lista de aristas correspondiente
        vertexList[indexOf(a)].edgesList.getNode(new EdgeWeight(b, null))
                .setData(ew);


    }

    @Override
    public void addEdgeWeight(Object a, Object b, Object weight) throws GraphException, ListException {
        if (!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Cannot match edge between vertexes ["
                    + a + "]" + "y" + "[" +
                    b + "]");
        if (!containsEdge(a, b)) {
            vertexList[indexOf(a)].edgesList.add(new EdgeWeight(b, weight));
            vertexList[indexOf(b)].edgesList.add(new EdgeWeight(a, weight));
        }
    }

    @Override
    public void removeVertex(Object element) throws GraphException, ListException {
        if (isEmpty())
            throw new GraphException("Adjacency List Graph is empty");

        if (containsVertex(element)) {
            for (int i = 0; i < counter; i++) {
                if (compare(vertexList[i].data, element) == 0) {
                    // ya lo encontro, ahora
                    // se debe suprimir el vertice a eliminar de todas las listas
                    // enlazadas de los otros vertices
                    for (int j = 0; j < counter; j++) {
                        // contains edge son la lista al final
                        if (containsEdge(vertexList[j].data, element))
                            removeEdge(vertexList[j].data, element);
                    }
                    for (int j = i; j < counter - 1; j++) {
                        vertexList[j] = vertexList[j + 1];


                    }
                    counter--;
                }
            }
        }


    }

    @Override
    public void removeEdge(Object a, Object b) throws GraphException, ListException {
        if (!containsVertex(a) || !containsVertex(b))
            throw new GraphException("There´s no some of the vertexes");

        if (!vertexList[indexOf(a)].edgesList.isEmpty()) {

            vertexList[indexOf(a)].edgesList.remove(new EdgeWeight(b, null));
        }
        // grafo no dirigido
        if (!vertexList[indexOf(b)].edgesList.isEmpty()) {

            vertexList[indexOf(b)].edgesList.remove(new EdgeWeight(a, null));
        }
    }

    // Recorrido en profundidad
    @Override
    public String dfs() throws GraphException, StackException, ListException {
        setVisited(false);//marca todos los vertices como no vistados
        // inicia en el vertice 0
        String info = vertexList[0].data + ", ";
        vertexList[0].setVisited(true); // lo marca
        stack.clear();
        stack.push(0); //lo apila
        while (!stack.isEmpty()) {
            // obtiene un vertice adyacente no visitado,
            //el que esta en el tope de la pila
            int index = adjacentVertexNotVisited((int) stack.top());
            if (index == -1) // no lo encontro
                stack.pop();
            else {
                vertexList[index].setVisited(true); // lo marca
                info += vertexList[index].data + ", "; //lo muestra
                stack.push(index); //inserta la posicion
            }
        }
        return info;
    }

    //Recorrido en amplitud
    @Override
    public String bfs() throws GraphException, QueueException, ListException {
        setVisited(false);//marca todos los vertices como no visitados
        // inicia en el vertice 0
        String info = vertexList[0].data + ", ";
        vertexList[0].setVisited(true); // lo marca
        queue.clear();
        queue.enQueue(0); // encola el elemento
        int v2;
        while (!queue.isEmpty()) {
            int v1 = (int) queue.deQueue(); // remueve el vertice de la cola
            // hasta que no tenga vecinos sin visitar
            while ((v2 = adjacentVertexNotVisited(v1)) != -1) {
                // obtiene uno
                vertexList[v2].setVisited(true); // lo marca
                info += vertexList[v2].data + ", "; //lo muestra
                queue.enQueue(v2); // lo encola
            }
        }
        return info;
    }

    //setteamos el atributo visitado del vertice respectivo
    private void setVisited(boolean value) {
        for (int i = 0; i < counter; i++) {
            vertexList[i].setVisited(value); //value==true o false
        }//for
    }

    private int adjacentVertexNotVisited(int index) throws ListException {
        Object vertexData = vertexList[index].data;

        for (int i = 0; i < counter; i++) {
            if (vertexList[index].edgesList.contains(new EdgeWeight(vertexList[i].data, null))
                    && !vertexList[i].isVisited()) {
                return i; // retorna la posición del vértice adyacente no visitado
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        String result = "Adjacency List Graph Content:\n";

        for (int i = 0; i < counter; i++) {
            result += "Vertex [" + vertexList[i].data + "] -> ";
            try {
                if (!vertexList[i].edgesList.isEmpty()) {
                    for (int j = 1; j <= vertexList[i].edgesList.size(); j++) {
                        EdgeWeight edge = (EdgeWeight) vertexList[i].edgesList.getNode(j).getData();
                        result += "(" + edge.getEdge() + ", weight=" + edge.getWeight() + ") ";
                    }
                }
            } catch (ListException e) {
                result += "ERROR: " + e.getMessage();
            }
            result += "\n";
        }

        return result;
    }
    public Object getVertexAt(int index) throws GraphException {
        if (index < 0 || index >= counter) {
            throw new GraphException("Índice fuera de rango");
        }
        return vertexList[index].data;
    }
    public String getAdjacencyList(Object vertex) throws GraphException, ListException {
        int index = indexOf(vertex);
        if (index == -1) {
            throw new GraphException("El vértice no existe en el grafo");
        }

        String result = "Adyacencias de [" + vertex + "]: ";
        if (vertexList[index].edgesList.isEmpty()) {
            result += "No tiene vértices adyacentes.";
        } else {
            for (int j = 1; j <= vertexList[index].edgesList.size(); j++) {
                EdgeWeight edge = (EdgeWeight) vertexList[index].edgesList.getNode(j).getData();
                result += "(" + edge.getEdge() + ", weight=" + edge.getWeight() + ") ";
            }
        }
        return result;
    }

    public Object getWeight(Object a, Object b) throws GraphException, ListException {
        if (!containsVertex(a) || !containsVertex(b)) {
            throw new GraphException("Uno o ambos vértices no existen.");
        }

        int index = indexOf(a);
        for (int i = 1; i <= vertexList[index].edgesList.size(); i++) {
            EdgeWeight edge = (EdgeWeight) vertexList[index].edgesList.getNode(i).getData();
            if (compare(edge.getEdge(), b) == 0) {
                return edge.getWeight();
            }
        }

        return null; // No existe una arista entre a y b
    }
    public SinglyLinkedList getAdjacencyListVertices(Object vertex) throws GraphException {
        int index = indexOf(vertex);
        if (index == -1) {
            throw new GraphException("Vertex not found: " + vertex);
        }
        return vertexList[index].edgesList; // Retorna la lista de aristas (vecinos)
    }
    public String toStringGraph() throws GraphException, ListException {
        StringBuilder result = new StringBuilder("SINGLY LINKED LIST GRAPH CONTENT\n");

        for (int i = 0; i < size(); i++) {
            String vertex = (String) getVertexAt(i);
            result.append("The vertex in the position ").append(i + 1).append(" is: ").append(vertex).append("\n");

            SinglyLinkedList edges = getAdjacencyListVertices(vertex);

            if (edges != null && !edges.isEmpty()) {
                result.append("Edges and weight:\n");
                for (int j = 1; j <= edges.size(); j++) {
                    EdgeWeight edge = (EdgeWeight) edges.getNode(j).getData();
                    result.append("  Edge: ").append(edge.getEdge())
                            .append(", weight = ").append(edge.getWeight()).append("\n");
                }
            } else {
                result.append("  No edges.\n");
            }
            result.append("\n");
        }

        return result.toString();
    }

}