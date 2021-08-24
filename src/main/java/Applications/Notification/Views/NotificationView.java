package Applications.Notification.Views;

import Applications.Notification.Listeners.NotificationListener;
import Applications.TimeLine.Controllers.TimeLineController;
import FileHandling.ImageHandler;
import FileHandling.Properties;
import GraphicComponents.GraphicHeaderFooter;
import GraphicComponents.GraphicMenu;
import GraphicComponents.GraphicReconnectButton;
import Models.Events.AcceptFollowRequestEvent;
import Models.Events.DeniedFollowRequestEvent;
import Models.Responses.NotificationsResponse;
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

import java.util.ArrayList;

public class NotificationView implements AutoUpdatingView {
    private VBox requestsBox = new VBox();
    private VBox eventBox = new VBox();
    private BorderPane centerPane = new BorderPane();
    private NotificationListener listener;
    private Scene scene;
    private void makeRequests(ArrayList<String> requests) {
        requestsBox.getChildren().clear();
        Label title = new Label(Properties.loadDialog("follow-requests"));
        title.setId("subtitle");
        HBox titleBox = new HBox(title);
        titleBox.setId("down-line-black");
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(Properties.loadSize("medium-indent")));
        requestsBox.getChildren().add(titleBox);

        for (String requestUser: requests) {
            StackPane stackPane = new StackPane();
            VBox requestUserBox = new VBox(stackPane);
            requestUserBox.setId("down-line-grey");
            requestUserBox.setPadding(new Insets(Properties.loadSize("medium-indent")));
            requestsBox.getChildren().add(requestUserBox);
            Label requestUserLabel = new Label(requestUser);
            stackPane.getChildren().add(requestUserLabel);
            StackPane.setAlignment(requestUserLabel, Pos.CENTER_LEFT);
            ImageView accept = ImageHandler.getImage("accept-request");
            ImageView reject = ImageHandler.getImage("reject-request");
            ImageView reject_mute = ImageHandler.getImage("reject-request-mute");
            HBox accept_rejectBox = new HBox(Properties.loadSize("medium-spacing"), accept, reject, reject_mute);
            accept_rejectBox.setAlignment(Pos.CENTER_RIGHT);
            stackPane.getChildren().add(accept_rejectBox);
            StackPane.setAlignment(accept_rejectBox, Pos.CENTER_RIGHT);
            accept.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    AcceptFollowRequestEvent event = new AcceptFollowRequestEvent(requestUser);
                    listener.AcceptRequestButtonPressed(event);
                }
            });
            reject.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    DeniedFollowRequestEvent event = new DeniedFollowRequestEvent(requestUser, false);
                    listener.RejectRequestButtonPressed(event);
                }
            });
            reject_mute.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    DeniedFollowRequestEvent event = new DeniedFollowRequestEvent(requestUser, true);
                    listener.RejectRequestButtonPressed(event);
                }
            });
        }
    }


    private void makeEvents(ArrayList<String> events) {
        Label title = new Label(Properties.loadDialog("events"));
        title.setId("subtitle");
        HBox titleBox = new HBox(title);
        titleBox.setId("down-line-black");
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(Properties.loadSize("medium-indent")));
        eventBox.getChildren().add(titleBox);

        for (int i = events.size() - 1; i >= 0; i--) {
            String event = events.get(i);
            Label eventLabel = new Label(event);
            eventLabel.setWrapText(true);
            HBox eventBox = new HBox(eventLabel);
            eventBox.setId("down-line-grey");
            eventBox.setPadding(new Insets(Properties.loadSize("medium-indent")));
            this.eventBox.getChildren().add(eventBox);
        }
    }

    public NotificationView(NotificationListener listener) {
        this.listener = listener;
        BorderPane mainPane = new BorderPane();
        StackPane stackPane = new StackPane(mainPane);
        scene = new Scene(stackPane);
        GraphicAgent.mainPane = stackPane;
        scene.getStylesheets().add("style.css");
        mainPane.setTop(GraphicHeaderFooter.header(event -> new TimeLineController()));

        mainPane.setLeft(GraphicMenu.Menu(this));
        mainPane.setCenter(centerPane);


        ScrollPane requestScroll = new ScrollPane(requestsBox);
        ScrollPane eventScroll = new ScrollPane(eventBox);
        centerPane.setLeft(requestScroll);
        centerPane.setRight(eventScroll);
        GraphicAgent.stage.setScene(scene);
        GraphicReconnectButton.setButton();
    }

    public void refresh(NotificationsResponse response) {
        requestsBox.getChildren().clear();
        eventBox.getChildren().clear();
        requestsBox.setPrefWidth(centerPane.getWidth()/ 2 - 2 * Properties.loadSize("scroll-border"));
        eventBox.setPrefWidth(centerPane.getWidth() / 2 - 2 * Properties.loadSize("scroll-border"));
        centerPane.widthProperty().addListener(event -> {
            requestsBox.setPrefWidth(centerPane.getWidth()/ 2 - 2 * Properties.loadSize("scroll-border"));
            eventBox.setPrefWidth(centerPane.getWidth() / 2 - 2 * Properties.loadSize("scroll-border"));
        });
        makeEvents(response.getEvents());
        makeRequests(response.getRequests());
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
