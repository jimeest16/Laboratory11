package domain;

import domain.list.SinglyLinkedList;

public class Vertex {
    public Object data;
    private boolean visited; //para los recorridos DFS, BFS
    public SinglyLinkedList edgesList; //lista de aristas

    //Constructor
    public Vertex(Object data){
        this.data = data;
        this.visited = false;
        this.edgesList = new SinglyLinkedList();
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}
