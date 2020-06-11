package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static sample.EdgeType.DIRECTED_EDGE;
import static sample.EdgeType.UNDIRECTED_EDGE;

public class Controller implements Initializable {


    @FXML
    ComboBox graphType;
    @FXML
    Button addVertex;
    @FXML
    Button addEdge;
    @FXML
    Button restartProgram;
    @FXML
    Button showGraphEntered;
    @FXML
    Button showDijkstra;
    @FXML
    Button resetVertices;
    @FXML
    Button resetEdges;
    @FXML
    Button clearMaxFlowBtn;
    @FXML
    Button clearDijkstraButton;
    @FXML
    TextField edgeFrom;
    @FXML
    TextField maxFlowStart;
    @FXML
    TextField maxFlowEnd;
    @FXML
    TextField DijkstraStart;
    @FXML
    TextField edgeTo;
    @FXML
    TextField edgeName;
    @FXML
    TextField edgeWeight;
    @FXML
    TextField vSymbol;
    @FXML
    TableView<Edge> dataTable;
    @FXML
    TableColumn<Edge, String> edgeNameCol = new TableColumn<>("Edge Name");
    @FXML
    TableColumn<Edge, String> edgeFromCol = new TableColumn<>("From");
    @FXML
    TableColumn<Edge, String> edgeToCol = new TableColumn<>("To");
    @FXML
    TableColumn<Edge, String> edgeWeightCol = new TableColumn<>("Weight");

    ObservableList<Edge> edgeData;


    public static String chosen;
    public static boolean flag;

    Graph G = new Graph();
    private Stage stage;
    private Stage stage2;
    private Stage stage3;

    public void reOrdering(Vertex start, Vertex end) {

    }

    public Controller() {

    }

    public void clearDijkstra() {
        DijkstraStart.clear();
    }

    public void clearMaxFlow() {
        maxFlowStart.clear();
        maxFlowEnd.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        edgeNameCol.setCellValueFactory(new PropertyValueFactory<>("edgeName"));
        edgeNameCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Edge, String>>() {
                    @Override
                    public void handle(CellEditEvent<Edge, String> event) {
                        ((Edge) event.getTableView().getItems().get(
                                event.getTablePosition().getRow())
                        ).setEdgeName(event.getNewValue());
                    }
                }
        );
        edgeFromCol.setCellValueFactory(new PropertyValueFactory<>("vertex_First"));
        edgeToCol.setCellValueFactory(new PropertyValueFactory<>("vertex_Second"));
        edgeWeightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
        fillTable();

        dataTable.setTooltip(new Tooltip("Displays entire graph's data you entered"));
        graphType.setTooltip(new Tooltip("You should specify graph's type if directed or undirected"));
        addVertex.setTooltip(new Tooltip("Press this button to add the vertex you entered"));
        addEdge.setTooltip(new Tooltip("Press this button after filling Edge's data above"));
        restartProgram.setTooltip(new Tooltip("To restart entire program and delete previous results"));
        edgeFrom.setTooltip(new Tooltip("Name of vertex where edge starts from"));
        edgeTo.setTooltip(new Tooltip("Name of vertex where edge ends at"));
        edgeName.setTooltip(new Tooltip("Name of the edge"));
        edgeWeight.setTooltip(new Tooltip("Enter weight of the edge"));
        vSymbol.setTooltip(new Tooltip("Here you enter the name of vertex"));
        dataTable.setEditable(true);
    }

    public void fillTable() {
        edgeData = FXCollections.observableArrayList();
        edgeData.addAll(G.edges);
        dataTable.setItems(edgeData);
    }


    public void addVertex() {
        AlertClass alert = new AlertClass();
        if (vSymbol.getText().isEmpty()) {
            alert.display("Input invalid", "you must type vertex name");
            return;
        }
        if (!vSymbol.getText().isEmpty()) {
            for (Vertex vertex : G.vertices) {
                if (vertex.toString().equals(vSymbol.getText())) {
                    alert.display("Input invalid", "Vertex with name: " + vSymbol.getText() + " already exists.");
                    return;
                }
            }
            G.vertices.add(new Vertex(vSymbol.getText()));
            vSymbol.clear();
        }
    }

    public void addEdge() {
        AlertClass alert = new AlertClass();
        try {
            chosen = graphType.getSelectionModel().getSelectedItem().toString();

        } catch (Exception e) {
            alert.display("Input Invalid", "you must choose the edge type");
            return;
        }

        if (chosen.equals("Undirected"))
            flag = true;
        else
            flag = false;

        int idx = searchVertex(edgeFrom.getText());
        if (idx == -1) {
            alert.display("Input Invalid", "Vertex from does not exist");
        }

        int idx2 = searchVertex(edgeTo.getText());
        if (idx2 == -1) {
            alert.display("Input Invaliid", "Vertex To does not exist");
        }
        int cost = 0;
        try {
            cost = Integer.parseInt(edgeWeight.getText());
        } catch (NumberFormatException e) {
            alert.display("Input Invalid", "weight value is invalid");
            return;
        }
        int exists = searchForEdge(edgeName.getText());
        if (exists == -1) {
            alert.display("Input Invalid", "there is an edge already with the name: " + edgeName.getText());
        }
        if (edgeName.getText().isEmpty()) {
            alert.display("Input Invalid", "edge name is empty");
        }
        if (idx != -1 && idx2 != -1 && exists != -1) {
            if (flag)
                G.edges.add(new Edge(G.vertices.get(idx), G.vertices.get(idx2), UNDIRECTED_EDGE, edgeName.getText(), cost));
            else
                G.edges.add(new Edge(G.vertices.get(idx), G.vertices.get(idx2), DIRECTED_EDGE, edgeName.getText(), cost));
            fillTable();
            resetEdge();

        }
        // flag = false;

    }

    private int searchForEdge(String text) {
        if (text.isEmpty())
            return -1;
        for (Edge edge : G.edges) {
            if (edge.getEdgeName().equals(text)) {
                return -1;
            }
        }
        return 1;
    }


    public void resetEdge() {
        edgeFrom.clear();
        edgeTo.clear();
        edgeName.clear();
        edgeWeight.clear();

    }

    public void cleanUp() {
        if (stage != null) {
            if (stage.isShowing()) {
                stage.close();
            }
        }
        if (stage2 != null) {
            if (stage2.isShowing()) {
                stage2.close();
            }
        }
        if (stage3 != null) {
            if (stage3.isShowing()) {
                stage3.close();
            }
        }
    }

    public int searchVertex(String vertex) {
        for (int i = 0; i < G.vertices.size(); i++) {
            if (G.vertices.get(i).getSymbol().equals(vertex))
                return i;
        }
        return -1;
    }


    public void restart() {
        flag = false;
        dataTable.getItems().clear();
        G.restartProgram();
        fillTable();
        G = new Graph();
        cleanUp();

    }

    public void showGraph() {
        try {
            if (stage != null) {
                if (stage.isShowing()) ;
                stage.close();
            }
            if (Graph.vertices.size() > 0) {
                GraphDrawer pl = new GraphDrawer();
                StackPane root = new StackPane();
                root.setPadding(new Insets(20));
                stage = new Stage();
                pl.pane = new Pane();
                root.getChildren().add(pl.pane);
                Scene sc = new Scene(root, 600, 600);
                stage.setScene(sc);
                stage.setTitle("Graph Representation");
                pl.start(stage);
            } else {
                AlertClass alert = new AlertClass();
                alert.display("Invalid Input", "No vertices or edges found to draw");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showMaximumFLowGraph() {
        try {
            if (stage2 != null) {
                if (stage2.isShowing()) {
                    stage2.close();
                }
            }
            AlertClass alert = new AlertClass();
            if (maxFlowStart.getText().isEmpty()) {
                alert.display("Input Invalid", "vertex start is empty");
                return;
            } else if (maxFlowEnd.getText().isEmpty()) {
                alert.display("Input Invalid", "vertex to is empty");
                return;
            } else if (searchVertex(maxFlowStart.getText()) == -1) {
                alert.display("Input Invalid", "There is no vertex with name= " + maxFlowStart.getText());
                return;
            } else if (searchVertex(maxFlowEnd.getText()) == -1) {
                alert.display("Input Invalid", "There is no vertex with name= " + maxFlowEnd.getText());
                return;
            }
            if (!maxFlowStart.getText().toString().isEmpty() && !maxFlowEnd.getText().toString().isEmpty()
                    && searchVertex(maxFlowStart.getText()) != -1 && searchVertex(maxFlowEnd.getText()) != -1) {

                int result = G.maximumFLow(Graph.vertices.get(searchVertex(maxFlowStart.getText().toString()))
                        , Graph.vertices.get(searchVertex(maxFlowEnd.getText().toString())));
                if (Graph.maximumFlowEdges.size() == 0) {
                    alert.display("error", "no path found");
                    return;
                }
                if (result == Integer.MAX_VALUE) {

                    alert.display("No Maximum FLow Graph ", "There is no maximum flow graph found between the 2 given vertices");
                    return;
                }
                System.out.println(result);
                MaximumFlowDrawer md = new MaximumFlowDrawer();
                StackPane root = new StackPane();
                root.setPadding(new Insets(20));
                stage2 = new Stage();
                md.pane = new Pane();
                root.getChildren().add(md.pane);
                Scene sc = new Scene(root, 600, 600);
                stage2.setScene(sc);
                stage2.setTitle("maximum flow Representation");
                md.start(stage2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showDijkstraGraph() {
        AlertClass alert = new AlertClass();
        if (stage3 != null) {
            if (stage3.isShowing()) {
                stage3.close();
            }
        }
        try {
            if (searchVertex(DijkstraStart.getText().toString()) == -1) {
                alert.display("Input Invalid", "No vertex found with the name: " + DijkstraStart.getText().toString());
                return;
            }
            if (DijkstraStart.getText().isEmpty()) {
                alert.display("Input Invalid", "input field is empty");
                return;
            }
            Vertex start = Graph.vertices.get(searchVertex(DijkstraStart.getText().toString()));
            DijkstraDrawer dd = new DijkstraDrawer();
            StackPane root = new StackPane();
            root.setPadding(new Insets(20));
            stage3 = new Stage();
            dd.pane = new Pane();
            root.getChildren().add(dd.pane);
            Scene sc = new Scene(root, 600, 600);
            stage3.setScene(sc);
            stage3.setTitle("dijkstra Representation");
            if (!G.dijkstra(start)) {
                alert.display("No Dijkstra Graph Found", "There is no dijkstra graph in the given graph");
                return;
            }
            dd.start(stage3);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetVertices() {
        Graph.vertices.clear();
        resetEdges();
    }

    public void resetEdges() {
        Graph.edges.clear();
        fillTable();
        cleanUp();
    }

}