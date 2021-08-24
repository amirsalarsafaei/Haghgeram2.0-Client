package Testing;

import Utils.GraphicAgent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class splitScene {
    public splitScene() {
        Stage stage = GraphicAgent.stage;
        GridPane gridPane = new GridPane();
        Scene scene = new Scene(gridPane);
        VBox box1 = new VBox();
        box1.setStyle("-fx-background-color: -fx-background; -fx-background: red ;");
        box1.getChildren().add(new Label("Content"));
        VBox box2 = new VBox();
        box2.setStyle("-fx-background-color: green ;");
        VBox box3 = new VBox();
        box3.setStyle("-fx-background-color: blue ;");

        gridPane.add(box1, 0, 0);
        gridPane.add(box2, 1, 0);
        gridPane.add(box3, 2, 0);
        for (int i = 0 ; i < 3 ; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0/3.0);
            cc.setHgrow(Priority.ALWAYS);
            gridPane.getColumnConstraints().add(cc);
        }

        RowConstraints rc = new RowConstraints();
        rc.setVgrow(Priority.ALWAYS);
        gridPane.getRowConstraints().add(rc);
        stage.setScene(scene);
    }

}
