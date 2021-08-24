package Applications.Messenger.Views;

import Applications.Messenger.Listeners.ConversationListListener;
import Applications.TimeLine.Controllers.TimeLineController;
import FileHandling.ImageHandler;
import FileHandling.Properties;
import GraphicComponents.GraphicHeaderFooter;
import GraphicComponents.GraphicMenu;
import GraphicComponents.GraphicReconnectButton;
import Models.Events.ShowConversationEvent;
import Models.Responses.ConversationListResponse;
import Models.Responses.ConversationPreviewResponse;
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
public class ConversationListView implements AutoUpdatingView {


    private Stage stage;
    private Scene scene;
    private StackPane mainPane;
    private VBox conversations;
    private BorderPane conversationBox;
    private int lastId;
    private final ConversationListListener listener;
    public ConversationListView(ConversationListListener listener) {
        this.listener = listener;
        stage = GraphicAgent.stage;
        mainPane = new StackPane();
        scene = new Scene(mainPane);
        GraphicAgent.mainPane = mainPane;
        scene.getStylesheets().add("style.css");
        BorderPane borderPane = new BorderPane();
        mainPane.getChildren().add(borderPane);
        borderPane.setLeft(GraphicMenu.Menu(this));
        GridPane gridPane = new GridPane();
        borderPane.setCenter(gridPane);
        borderPane.setTop(GraphicHeaderFooter.header(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                new TimeLineController();
            }
        }));
        BorderPane conversationsBox = new BorderPane();
        conversationBox = new BorderPane();
        gridPane.add(conversationsBox, 0, 0);
        gridPane.add(conversationBox, 1, 0);
        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(100.0/3.0);
        cc.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().add(cc);
        cc = new ColumnConstraints();
        cc.setPercentWidth(2.0 * 100.0/3.0);
        cc.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().add(cc);
        RowConstraints rc = new RowConstraints();
        rc.setVgrow(Priority.ALWAYS);
        gridPane.getRowConstraints().add(rc);
        ScrollPane scrollConversations = new ScrollPane(conversations);
        ImageView add_group = ImageHandler.getImage("add-group-chat");
        ImageView forward = ImageHandler.getImage("forward");
        HBox conversationTools = new HBox(10, add_group, forward);
        conversationsBox.setTop(conversationTools);
        conversationsBox.setCenter(scrollConversations);
        scrollConversations.fitToHeightProperty().setValue(true);
        scrollConversations.fitToWidthProperty().setValue(true);
        conversationTools.setPadding(new Insets(Properties.loadSize("medium-indent")));
        forward.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                listener.setGroupMessageButton();
            }
        });
        conversationTools.setAlignment(Pos.CENTER);
        add_group.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                listener.addGroupButtonPressed();
            }
        });
        conversationTools.setId("black-border");
        conversations = new VBox();
        scrollConversations.setContent(conversations);
        stage.setScene(scene);
        GraphicReconnectButton.setButton();
    }


    public void refresh(ConversationListResponse conversationListResponse) {
        conversations.getChildren().clear();
        for (ConversationPreviewResponse conversation:conversationListResponse.getConversations())
            conversations.getChildren().add(getConversationBox(conversation));
    }

    public VBox getConversationBox(ConversationPreviewResponse conversation) {
        VBox vBox = new VBox();
        Label nameLabel = new Label(conversation.getName());
        vBox.getChildren().add(new HBox(Properties.loadSize("small-spacing"), nameLabel, unread(conversation)));
        vBox.setPadding(new Insets(Properties.loadSize("medium-indent")));
        Label preview = new Label(conversation.getPreview());
        preview.setId("preview-grey");
        HBox hBox = new HBox(preview);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(Properties.loadSize("medium-indent")));
        vBox.getChildren().add(hBox);
        if (conversation.getId() == lastId) {
            vBox.setId("blue");
            preview.setId("preview-white");
        }
        vBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                listener.showConversationSelected(new ShowConversationEvent(conversation.getId()), conversation.isGroup());

            }
        });
        vBox.setId("down-line-grey");
        vBox.fillWidthProperty().setValue(true);
        return vBox;
    }

    @Override
    public void hide() {
        listener.stopRefreshing();
    }

    @Override
    public void show() {
        listener.startRefreshing();
        stage.setScene(scene);
    }

    @Override
    public void refreshNow() {
        listener.refresh();
    }

    public static StackPane unread(ConversationPreviewResponse conversation) {
        StackPane stackPane = new StackPane();
        Circle circle = new Circle();
        String unread = String.valueOf(conversation.getUnread());
        while(unread.length() < 3) {
            unread = "0" + unread;
        }
        Label label = new Label(unread);
        label.setId("white-text");
        label.setStyle("-fx-font-size: 10;");
        circle.setFill(Color.BLUE);
        circle.radiusProperty().bind(label.widthProperty());
        stackPane.getChildren().addAll(circle, label);
        return stackPane;
    }

    public StackPane getStackPane() {
        return mainPane;
    }

    public BorderPane getConversationVBox(){
        return conversationBox;
    }
}
