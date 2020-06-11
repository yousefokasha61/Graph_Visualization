package sample;

import java.util.*;

import static sample.EdgeType.DIRECTED_EDGE;
import static sample.EdgeType.UNDIRECTED_EDGE;

public class Graph {

    public static ArrayList<Vertex> vertices = new ArrayList<>();
    public static ArrayList<Edge> edges = new ArrayList<>();
    public static ArrayList<Edge> maximumFlowEdges = new ArrayList<>();
    public static ArrayList<Edge> dijkstraEdges = new ArrayList<>();
    private Edge[] maxFlowEdgesFound;
    private Vertex[] visitedVerticesFound;
    private int maxweightFound;
    private Edge min;

    public Graph() {

    }

    public void restartProgram() {
        vertices.clear();
        edges.clear();
        maximumFlowEdges.clear();
        dijkstraEdges.clear();

    }

    public int maximumFLow(Vertex start, Vertex end) {
        Graph.maximumFlowEdges.clear();
        int graph[][] = new int[vertices.size()][vertices.size()];
        for (Edge edge : edges) {
            if (edge.getType() == UNDIRECTED_EDGE) {
                int index1 = vertices.indexOf(edge.getVertex_First());
                int index2 = vertices.indexOf(edge.getVertex_Second());
                graph[index1][index2] = edge.getWeight();
                graph[index2][index1] = edge.getWeight();
            } else if (edge.getType() == DIRECTED_EDGE) {
                int index1 = vertices.indexOf(edge.getVertex_First());
                int index2 = vertices.indexOf(edge.getVertex_Second());
                graph[index1][index2] = edge.getWeight();
            }
        }

        return fordFulkerson(graph, vertices.indexOf(start), vertices.indexOf(end));

    }


    private boolean isVertexVisited(Vertex vertex, Vertex[] visitedVertices, int counter) {
        for (int i = 0; i < counter + 1; i++) {
            if (visitedVertices[i] == vertex) {
                return false;
            }
        }
        return true;
    }

    public boolean dijkstra(Vertex start) {
        Graph.dijkstraEdges.clear();
        ArrayList<Vertex> visitedVertices = new ArrayList<>(vertices.size());
        Vertex newVertex = null;
        if (vertices.size() == 0)
            return false;
        visitedVertices.add(start);

        while (visitedVertices.size() < vertices.size()) {
            Edge min = new Edge(new Vertex("%___"), new Vertex("0"), UNDIRECTED_EDGE, "-", Integer.MAX_VALUE);
            for (Vertex visitedVertex : visitedVertices) {
                for (Edge edge : edges) {
                    if (edge.getType() == UNDIRECTED_EDGE) {
                        if (edge.getVertex_First() == visitedVertex) {
                            if (!visitedVertices.contains(edge.getVertex_Second())) {
                                if (min.getWeight() > edge.getWeight()) {
                                    min = edge;
                                    newVertex = edge.getVertex_Second();
                                }
                            }
                        } else if (edge.getVertex_Second() == visitedVertex) {
                            if (!visitedVertices.contains(edge.getVertex_First())) {
                                if (min.getWeight() > edge.getWeight()) {
                                    min = edge;
                                    newVertex = edge.getVertex_First();
                                }
                            }
                        }

                    } else if (edge.getType() == DIRECTED_EDGE) {
                        if (edge.getVertex_First() == visitedVertex) {
                            if (!visitedVertices.contains(edge.getVertex_Second())) {
                                if (min.getWeight() > edge.getWeight()) {
                                    min = edge;
                                    newVertex = edge.getVertex_Second();
                                }
                            }

                        }
                    }

                }
            }
            if (min.getVertex_First().getSymbol().equalsIgnoreCase("%___"))
                return false;
            Graph.dijkstraEdges.add(min);
            visitedVertices.add(newVertex);
        }
        return true;
    }

    //-----------------------------------------------------------------------------------------------------
    int numberOfVertices; //Number of vertices in graph

    boolean bfs(int rGraph[][], int s, int t, int parent[]) {
        // Create a visited array and mark all vertices as not
        // visited
        boolean visited[] = new boolean[numberOfVertices];
        for (int i = 0; i < numberOfVertices; ++i)
            visited[i] = false;

        // Create a queue, enqueue source vertex and mark
        // source vertex as visited
        LinkedList<Integer> queue = new LinkedList<Integer>();
        queue.add(s);
        visited[s] = true;
        parent[s] = -1;

        // Standard BFS Loop
        while (queue.size() != 0) {
            int u = queue.poll();

            for (int v = 0; v < numberOfVertices; v++) {
                if (visited[v] == false && rGraph[u][v] > 0) {
                    queue.add(v);
                    parent[v] = u;
                    visited[v] = true;
                }
            }
        }

        // If we reached sink in BFS starting from source, then
        // return true, else false
        return (visited[t] == true);
    }

    // Returns tne maximum flow from s to t in the given graph
    int fordFulkerson(int graph[][], int s, int t) {
        int u, v;
        numberOfVertices = graph.length;
        int parent[] = new int[numberOfVertices];
        int max_flow = 0;
        ArrayList<String> connectedVertices = new ArrayList<>();

        Vertex start = vertices.get(s);
        Vertex end = vertices.get(t);
        while (bfs(graph, s, t, parent)) {
            int path_flow = Integer.MAX_VALUE;
            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                if (path_flow > graph[u][v]) {
                    path_flow = graph[u][v];
                }
            }

            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                graph[u][v] -= path_flow;
                connectedVertices.add(u + " " + v + " " + path_flow);
                graph[v][u] += path_flow;
            }
            max_flow += path_flow;
        }

        for (int i = 0; i < connectedVertices.size() - 1; i++) {
            if (connectedVertices.get(i) == null) {
                continue;
            }
            for (int j = i + 1; j < connectedVertices.size(); j++) {
                String[] s1 = connectedVertices.get(i).split(" ");
                String[] s2 = connectedVertices.get(j).split(" ");
                if (s1[0].equals(s2[0]) && s2[1].equals(s1[1])) {
                    s1[2] = "" + (Integer.parseInt(s1[2]) + Integer.parseInt(s2[2]));
                    connectedVertices.set(i, s1[0] + " " + s1[1] + " " + s1[2]);
                    connectedVertices.remove(j);
                }
            }
        }
        for (int i = 0; i < connectedVertices.size(); i++) {
            String[] s1 = connectedVertices.get(i).split(" ");
            Graph.maximumFlowEdges.add(getEdge(s1));
        }
        setsOfEdges.clear();
        orderingMaximumFlowEdges(start, end);
        cleanUpEdges();
        // Return the overall flow
        return max_flow;
    }

    private void cleanUpEdges() {
        for (ArrayList<Edge> setsOfEdge : setsOfEdges) {
            int minValue = Integer.MAX_VALUE;
            for (Edge edge : setsOfEdge) {
                if (edge.getWeight() < minValue) {
                    minValue = edge.getWeight();
                }
            }
            for (Edge edge : setsOfEdge) {
                edge.setWeight(minValue);
            }
        }
        Graph.maximumFlowEdges.clear();
        for (ArrayList<Edge> setsOfEdge : setsOfEdges) {
            for (Edge edge : setsOfEdge) {
                Graph.maximumFlowEdges.add(edge);
            }
        }
    }

    ArrayList<ArrayList<Edge>> setsOfEdges = new ArrayList<>();
    ArrayList<Edge> orderedMaximumFlowEdges = new ArrayList<>();

    public void orderingMaximumFlowEdges(Vertex start, Vertex end) {
        if(start==end){
            return;
        }
        for (Edge maximumFlowEdge : Graph.maximumFlowEdges) {
            if (maximumFlowEdge.getType() == UNDIRECTED_EDGE) {
                if (maximumFlowEdge.getVertex_First() == start) {
                    if (!orderedMaximumFlowEdges.contains(maximumFlowEdge)) {
                        orderedMaximumFlowEdges.add(maximumFlowEdge);
                        if(maximumFlowEdge.getVertex_First()==end||maximumFlowEdge.getVertex_Second()==end){
                            saveDate();
                        }
                        orderingMaximumFlowEdges(maximumFlowEdge.getVertex_Second(), end);
                        orderedMaximumFlowEdges.remove(maximumFlowEdge);
                    }
                }else if (maximumFlowEdge.getVertex_Second()==start){
                    if(!orderedMaximumFlowEdges.contains(maximumFlowEdge)){
                        orderedMaximumFlowEdges.add(maximumFlowEdge);
                        if(maximumFlowEdge.getVertex_First()==end||maximumFlowEdge.getVertex_Second()==end){
                            saveDate();
                        }
                        orderingMaximumFlowEdges(maximumFlowEdge.getVertex_First(), end);
                        orderedMaximumFlowEdges.remove(maximumFlowEdge);
                    }
                }
            } else if (maximumFlowEdge.getType() == DIRECTED_EDGE) {
                if (maximumFlowEdge.getVertex_First() == start) {
                    if (!orderedMaximumFlowEdges.contains(maximumFlowEdge)) {
                        orderedMaximumFlowEdges.add(maximumFlowEdge);
                        if (maximumFlowEdge.getVertex_Second() == end) {
                            saveDate();
                        }
                        orderingMaximumFlowEdges(maximumFlowEdge.getVertex_Second(), end);
                        orderedMaximumFlowEdges.remove(maximumFlowEdge);
                    }
                }
            }
        }
    }


    private void saveDate() {
        ArrayList<Edge> duplicate = new ArrayList<>();
        for (Edge edge : orderedMaximumFlowEdges) {
            duplicate.add(new Edge(edge.getVertex_First(), edge.getVertex_Second()
                    , edge.getType(), edge.getEdgeName(), edge.getWeight()));
        }
        getMinWeight();
        setsOfEdges.add(duplicate);
    }

    private void getMinWeight() {
        int minValue=Integer.MAX_VALUE;
        for (Edge orderedMaximumFlowEdge : orderedMaximumFlowEdges) {
            minValue=Integer.min(orderedMaximumFlowEdge.getWeight(),minValue);
        }
        for (Edge orderedMaximumFlowEdge : orderedMaximumFlowEdges) {
            int index = Graph.maximumFlowEdges.indexOf(orderedMaximumFlowEdge);
            Graph.maximumFlowEdges.get(index).setWeight(Graph.maximumFlowEdges.get(index).getWeight()-minValue);
        }
    }

    public Vertex getVertex(int vertexIndex) {
        return vertices.get(vertexIndex);
    }

    public Edge getEdge(String[] s1) {
        Edge foundEdge = null;
        Vertex from = getVertex(Integer.parseInt(s1[0]));
        Vertex to = getVertex(Integer.parseInt(s1[1]));

        for (Edge edge : edges) {
            if ((edge.getVertex_First() == from && edge.getVertex_Second() == to) || (edge.getVertex_Second() == from && edge.getVertex_First() == to)) {
                foundEdge = edge;
                break;
            }
        }
        return new Edge(foundEdge.getVertex_First(), foundEdge.getVertex_Second(), foundEdge.getType()
                , foundEdge.getEdgeName(), Integer.parseInt(s1[2]));
    }
}
