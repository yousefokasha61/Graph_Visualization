package sample;

import javafx.beans.property.SimpleStringProperty;

public class Edge {
    private SimpleStringProperty name;

    Vertex vertex_First;
    Vertex vertex_Second;


    EdgeType type;
    String edgeName;
    int weight;

    public Vertex getVertex_First() {
        return vertex_First;
    }


    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Vertex getVertex_Second() {
        return vertex_Second;
    }


    public EdgeType getType() {
        return type;
    }


    public String getEdgeName() {
        return edgeName;
    }

    public void setEdgeName(String edgeName) {
        this.edgeName = edgeName;
        name.set(edgeName);
    }

    public int getWeight() {
        return weight;
    }



    public Edge(Vertex vertex_First, Vertex vertex_Second, EdgeType type, String edgeName, int weight) {
        this.vertex_First = vertex_First;
        this.vertex_Second = vertex_Second;
        this.type = type;
        this.edgeName = edgeName;
        this.weight = weight;
        name = new SimpleStringProperty(edgeName);
    }

    @Override
    public String toString() {

        return (edgeName);
    }
}
