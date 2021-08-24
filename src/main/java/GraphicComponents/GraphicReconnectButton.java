package GraphicComponents;

import FileHandling.ImageHandler;
import FileHandling.Properties;
import StreamHandler.StreamHandler;
import Utils.CheckConnection;
import Utils.Connection;
import Utils.GraphicAgent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class GraphicReconnectButton {
    public static ImageView getButton() {
        ImageView imageView = ImageHandler.getImage("reconnect");
        imageView.setOnMouseClicked(event -> {

            Connection.connect();
            if (StreamHandler.isConnected()) {
                imageView.setImage(null);
                (new CheckConnection()).checkConnectionLoop();
            }
        });
        return imageView;
    }

    public static void setButton() {
        if (StreamHandler.isConnected())
            return;
        StackPane stackPane = GraphicAgent.mainPane;
        ImageView button = getButton();


        stackPane.getChildren().add(button);
        StackPane.setMargin(button, new Insets(Properties.loadSize("medium-indent")));
        StackPane.setAlignment(button, Pos.BOTTOM_RIGHT);

    }
}
