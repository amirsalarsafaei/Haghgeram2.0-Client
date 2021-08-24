package Applications.Setting.Views;

import Applications.Setting.Listeners.FollowRequestStatusListener;
import FileHandling.Properties;
import GraphicComponents.GraphicHeaderFooter;
import GraphicComponents.GraphicReconnectButton;
import Models.Responses.FollowRequestsStatusResponse;
import Utils.AutoUpdatingView;
import Utils.GraphicAgent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class FollowRequestStatusView implements AutoUpdatingView {
    private ScrollPane scrollPane;
    private VBox requestList = new VBox();
    private FollowRequestStatusListener listener;
    private Scene scene;
    public FollowRequestStatusView(FollowRequestStatusListener listener) {
        this.listener = listener;
        BorderPane mainPane = new BorderPane();
        StackPane stackPane = new StackPane(mainPane);
        scene = new Scene(stackPane);
        GraphicAgent.mainPane = stackPane;
        GraphicAgent.stage.setScene(scene);
        scene.getStylesheets().add("style.css");
        mainPane.setTop(GraphicHeaderFooter.headerToSetting(this));

        scrollPane = new ScrollPane(requestList);
        scrollPane.fitToWidthProperty().setValue(true);
        scrollPane.fitToHeightProperty().setValue(true);
        mainPane.setCenter(scrollPane);
        GraphicReconnectButton.setButton();
    }

    public void refresh(FollowRequestsStatusResponse response) {
        requestList.getChildren().clear();
        addRequest(Properties.loadDialog("pending"),
                response.getPending(),
                requestList,
                scrollPane);
        addRequest(Properties.loadDialog("accepted"),
                response.getAccepted(),
                requestList,
                scrollPane);
        addRequest(Properties.loadDialog("rejected"),
                response.getRejected(),
                requestList,
                scrollPane);
    }

    public void addRequest(String type, ArrayList<String> arrayList, VBox list, ScrollPane scrollPane) {
        Label typeLabel = new Label(type);
        typeLabel.setId("subtitle-blue");
        typeLabel.setAlignment(Pos.CENTER);
        HBox typeBox = new HBox(typeLabel);
        typeBox.setAlignment(Pos.CENTER);
        typeBox.setId("down-line-black");
        scrollPane.widthProperty().addListener(event -> {
            typeLabel.setPrefWidth(scrollPane.getWidth()- 2 * (Properties.loadSize("scroll-border") + Properties.loadSize("big-indent")));
        });
        typeBox.setPadding(new Insets(Properties.loadSize("big-indent")));
        list.getChildren().add(typeBox);
        for (String user:arrayList) {
            Label userLabel = new Label(user);
            HBox userBox = new HBox(userLabel);
            userBox.setAlignment(Pos.CENTER);
            userBox.setPadding(new Insets(Properties.loadSize("medium-indent")));
            userBox.setId("down-line-grey");
            list.getChildren().add(userBox);
            userLabel.setAlignment(Pos.CENTER);
            scrollPane.widthProperty().addListener(event -> {
                userLabel.setPrefWidth(scrollPane.getWidth() - 2 * (Properties.loadSize("scroll-border") +
                        Properties.loadSize("medium-indent")));
            });
        }
    }

    @Override
    public void hide() {
        listener.stopRefreshing();
    }

    @Override
    public void show() {
        listener.startRefreshing();
        GraphicAgent.stage.setScene(scene);
    }

    @Override
    public void refreshNow() {
        listener.refresh();
    }
}
