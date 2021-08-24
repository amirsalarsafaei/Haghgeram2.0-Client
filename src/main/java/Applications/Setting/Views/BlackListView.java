package Applications.Setting.Views;

import Applications.Setting.Listeners.BlackListListener;
import FileHandling.ImageHandler;
import FileHandling.Properties;
import GraphicComponents.GraphicHeaderFooter;
import GraphicComponents.GraphicReconnectButton;
import Models.Events.UnBlockButtonPressedEvent;
import Models.Responses.BlackListResponse;
import Utils.AutoUpdatingView;
import Utils.GraphicAgent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
public class BlackListView implements AutoUpdatingView {
    private Stage stage;
    private Scene scene;
    private BlackListListener listener;
    private VBox blockList;
    private ScrollPane blockScroll;
    public BlackListView(BlackListListener listener) {
        this.listener = listener;
        BorderPane mainPane = new BorderPane();
        StackPane stackPane = new StackPane(mainPane);
        scene = new Scene(stackPane);
        GraphicAgent.mainPane = stackPane;
        scene.getStylesheets().add("style.css");
        mainPane.setTop(GraphicHeaderFooter.headerToSetting(this));
        this.blockList = new VBox();
        blockScroll = new ScrollPane(blockList);

        mainPane.setCenter(blockScroll);

        GraphicAgent.stage.setScene(scene);
        blockScroll.fitToWidthProperty().setValue(true);
        GraphicReconnectButton.setButton();
    }

    public void refresh(BlackListResponse blackListResponse) {
        blockList.getChildren().clear();
        for (String blockedUsername: blackListResponse.getBlackList()) {
            StackPane userAndUnBlock = new StackPane();
            HBox userBox = new HBox(userAndUnBlock);
            Label blockedUserLabel = new Label(blockedUsername);
            userAndUnBlock.getChildren().add(blockedUserLabel);
            StackPane.setAlignment(blockedUserLabel, Pos.CENTER_LEFT);
            ImageView unblock = ImageHandler.getImage("unblock-setting");
            userAndUnBlock.getChildren().add(unblock);
            StackPane.setAlignment(unblock, Pos.CENTER_RIGHT);


            userAndUnBlock.setPrefWidth(blockScroll.getWidth()- 2 * (Properties.loadSize("scroll-border") +
                    Properties.loadSize("medium-indent")));
            blockScroll.widthProperty().addListener(event -> {
                userAndUnBlock.setPrefWidth(blockScroll.getWidth()- 2 * (Properties.loadSize("scroll-border") +
                        Properties.loadSize("medium-indent")));
            });
            unblock.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    listener.unblockButtonPressed(new UnBlockButtonPressedEvent(blockedUsername));
                }
            });
            blockList.getChildren().add(userBox);
            userBox.setId("down-line-grey");
            userBox.setPadding(new Insets(Properties.loadSize("medium-indent")));
        }
    }

    @Override
    public void hide() {
        listener.stopRefreshing();
    }

    @Override
    public void show() {
        stage.setScene(scene);
        listener.startRefreshing();
    }

    @Override
    public void refreshNow() {
        listener.refresh();
    }
}
