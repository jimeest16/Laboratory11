package domain;

import domain.list.ListException;
import domain.queue.LinkedQueue;
import domain.queue.QueueException;
import domain.stack.LinkedStack;
import domain.stack.StackException;

import static util.Utility.compare;

public class AdjacencyMatrixGraph implements Graph {
    public Vertex[] vertexList; //arreglo de objetos tipo vértice
    public Object[][] adjacencyMatrix; //arreglo bidimensional
    private int n; //max de elementos
    private int counter; //contador de vertices

    //para los recorridos dfs, bfs
    private LinkedStack stack;
    private LinkedQueue queue;

    //Constructor
    public AdjacencyMatrixGraph(int n) {
        if (n <= 0) System.exit(1); //sale con status==1 (error)
        this.n = n;
        this.counter = 0;
        this.vertexList = new Vertex[n];
        this.adjacencyMatrix = new Object[n][n];
        this.stack = new LinkedStack();
        this.queue = new LinkedQueue();
        initMatrix(); //inicializa matriz de objetos con cero
    }

    private void initMatrix() {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                this.adjacencyMatrix[i][j] = 0; //init con ceros
    }


    private void expandCapacity() {
        int newSize = n * 2;  // duplicar tamaño
        Vertex[] newVertexList = new Vertex[newSize];
        Object[][] newAdjacencyMatrix = new Object[newSize][newSize];

        // Copiar vertices actuales
        for (int i = 0; i < counter; i++) {
            newVertexList[i] = vertexList[i];
        }

        // Inicializar nueva matriz con ceros
        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < newSize; j++) {
                newAdjacencyMatrix[i][j] = 0;
            }
        }

        // Copiar matriz vieja a la nueva
        for (int i = 0; i < counter; i++) {
            for (int j = 0; j < counter; j++) {
                newAdjacencyMatrix[i][j] = adjacencyMatrix[i][j];
            }
        }

        // Reemplazar referencias y actualizar tamaño
        this.vertexList = newVertexList;
        this.adjacencyMatrix = newAdjacencyMatrix;
        this.n = newSize;
    }

    @Override
    public int size() throws ListException {
        return counter;
    }

    @Override
    public void clear() {
        this.vertexList = new Vertex[n];
        this.adjacencyMatrix = new Object[n][n];
        this.counter = 0; //inicializo contador de vértices
        this.initMatrix();
    }

    @Override
    public boolean isEmpty() {
        return counter == 0;
    }

    @Override
    public boolean containsVertex(Object element) throws GraphException, ListException {
        if (isEmpty())
            throw new GraphException("Graph is Empty");

        return indexOf(element) != -1;
    }

    @Override
    public boolean containsEdge(Object a, Object b) throws GraphException, ListException {
        if (isEmpty())
            throw new GraphException("Graph is Empty");

        return !(compare(adjacencyMatrix[indexOf(a)][indexOf(b)], 0) == 0);
    }


    @Override
    public void addVertex(Object element) throws GraphException, ListException {
        if (counter >= vertexList.length) {
            expandCapacity();
        }
        vertexList[counter++] = new Vertex(element);
    }

    @Override
    public void addEdge(Object a, Object b) throws GraphException, ListException {
        if (!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Cannot match edge between vertexes [" + a + "] y [" + b + "]");
        adjacencyMatrix[indexOf(a)][indexOf(b)] = 1; // 1 significa que hay una arista

        // grafo no dirigido
        adjacencyMatrix[indexOf(b)][indexOf(a)] = 1; // también hay una arista
    }

    private int indexOf(Object element) {
        for (int i = 0; i < counter; i++) {
            if (compare(vertexList[i].data, element) == 0)
                return i;
        }
        return -1;
    }
    public Object getVertex(int index) throws GraphException {
        if (index < 0 || index >= counter) {
            throw new GraphException("Index out of bounds");
        }
        return vertexList[index].data;
    }


    @Override
    public void addWeight(Object a, Object b, Object weight) throws GraphException, ListException {
        if (!containsEdge(a, b))
            throw new GraphException("Cannot match edge between vertexes [" + a + "] y [" + b + "]");
        adjacencyMatrix[indexOf(a)][indexOf(b)] = weight; // peso

        // grafo no dirigido
        adjacencyMatrix[indexOf(b)][indexOf(a)] = weight;
    }

    @Override
    public void addEdgeWeight(Object a, Object b, Object weight) throws GraphException, ListException {
        if (!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Cannot match edge between vertexes [" + a + "] y [" + b + "]");
        adjacencyMatrix[indexOf(a)][indexOf(b)] = weight;

        // grafo no dirigido
        adjacencyMatrix[indexOf(b)][indexOf(a)] = weight;
    }

    @Override
    public void removeVertex(Object element) throws GraphException, ListException {
        if (isEmpty())
            throw new GraphException("Graph is empty");

        int index = indexOf(element);
        if (index == -1)
            throw new GraphException("Vertex not found: " + element);

        // Eliminar la fila (desplazar hacia arriba)
        for (int i = index; i < counter - 1; i++) {
            for (int j = 0; j < counter; j++) {
                adjacencyMatrix[i][j] = adjacencyMatrix[i + 1][j];
            }
        }

        // Limpiar la última fila
        for (int j = 0; j < counter; j++) {
            adjacencyMatrix[counter - 1][j] = 0;
        }

        // Eliminar la columna (desplazar hacia la izquierda)
        for (int i = 0; i < counter - 1; i++) {
            for (int j = index; j < counter - 1; j++) {
                adjacencyMatrix[i][j] = adjacencyMatrix[i][j + 1];
            }
            adjacencyMatrix[i][counter - 1] = 0; // limpiar la última columna
        }

        // Eliminar el vértice del arreglo
        for (int i = index; i < counter - 1; i++) {
            vertexList[i] = vertexList[i + 1];
        }
        vertexList[counter - 1] = null;

        // Decrementar el contador de vértices
        counter--;
    }

    @Override
    public void removeEdge(Object a, Object b) throws GraphException, ListException {
        if (isEmpty())
            throw new GraphException("Graph is Empty");

        int i = indexOf(a);
        int j = indexOf(b);

        if (i != -1 && j != -1) {
            adjacencyMatrix[i][j] = 0;
            adjacencyMatrix[j][i] = 0;
            // grafo no dirigido
        }
    }

    // Recorrido en profundidad
    @Override
    public String dfs() throws GraphException, StackException, ListException {
        setVisited(false);//marca todos los vertices como no visitados
        // inicia en el vertice 0
        String info = vertexList[0].data + ", ";
        vertexList[0].setVisited(true); // lo marca
        stack.clear();
        stack.push(0); //lo apila
        while (!stack.isEmpty()) {
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
            while ((v2 = adjacentVertexNotVisited(v1)) != -1) {
                vertexList[v2].setVisited(true); // lo marca
                info += vertexList[v2].data + ", "; //lo muestra
                queue.enQueue(v2); // lo encola
            }
        }
        return info;
    }

    private void setVisited(boolean value) {
        for (int i = 0; i < counter; i++) {
            vertexList[i].setVisited(value);
        }
    }

    private int adjacentVertexNotVisited(int index) {
        for (int i = 0; i < counter; i++) {
            if (!adjacencyMatrix[index][i].equals(0)
                    && !vertexList[i].isVisited())
                return i;
        }
        return -1;
    }

    @Override
    public String toString() {
        String result = "Adjacency Matrix Graph Content....";
        for (int i = 0; i < counter; i++) {
            result += "\nThe vertex in the position: " + i + " is: " + vertexList[i].data;
        }

        for (int i = 0; i < counter; i++) {
            for (int j = 0; j < counter; j++) {
                if (compare(adjacencyMatrix[i][j], 0) != 0) {
                    result += "\n There is an edge between the vertexes: " + vertexList[i].data +
                            " ... " + vertexList[j].data;

                    if (compare(adjacencyMatrix[i][j], 1) != 0) {
                        result += "  WEIGHT:" + adjacencyMatrix[i][j];
                    }
                }
            }
        }
        return result;
    }
}
