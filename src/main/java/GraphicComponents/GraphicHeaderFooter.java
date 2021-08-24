package GraphicComponents;

import Applications.Setting.Controllers.SettingMenuController;
import Applications.TimeLine.Controllers.TimeLineController;
import FileHandling.ImageHandler;
import Utils.AutoUpdatingView;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class GraphicHeaderFooter {
    static public VBox header (EventHandler<MouseEvent> eventHandler) {
        HBox hBox = new HBox(ImageHandler.getImage("smallLogo"));
        hBox.setAlignment(Pos.CENTER);
        hBox.setOnMouseClicked(eventHandler);
        VBox vBox = new VBox(hBox);
        vBox.setAlignment(Pos.CENTER);
        hBox.setId("down-line-black");
        return vBox;
    }

    static public VBox footer (EventHandler<MouseEvent> eventHandler) {
        HBox hBox = new HBox(ImageHandler.getImage("smallLogo"));
        hBox.setAlignment(Pos.CENTER);
        hBox.setOnMouseClicked(eventHandler);
        VBox vBox = new VBox(hBox);
        vBox.setId("up-line-black");
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }
    static public VBox headerToSetting (AutoUpdatingView backView) {

        ImageView logo = ImageHandler.getImage("smallLogo");
        ImageView back = ImageHandler.getImage("back");
        StackPane stackPane = new StackPane();
        HBox hBox = new HBox(logo);
        hBox.setAlignment(Pos.CENTER);
        stackPane.getChildren().add(hBox);
        hBox = new HBox(back);
        hBox.setAlignment(Pos.CENTER_LEFT);
        stackPane.getChildren().add(hBox);

        logo.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                backView.hide();
                new TimeLineController();
            }
        });

        back.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                backView.hide();
                new SettingMenuController();
            }
        });
        VBox vBox = new VBox(stackPane);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

}
