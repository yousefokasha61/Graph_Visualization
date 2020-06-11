package sample;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.*;
import javafx.scene.control.*;

public class AlertClass {

    private Stage stage;

    public void display(String title, String msg){
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setMinWidth(400);
        stage.setMinHeight(100);
        Label label=new Label();
        label.setText(msg);
        VBox layout=new VBox(50);
        layout.getChildren().addAll(label);
        layout.setAlignment(Pos.CENTER);
        Scene scene=new Scene(layout);
        stage.setScene(scene);
        stage.showAndWait();
    }


}
