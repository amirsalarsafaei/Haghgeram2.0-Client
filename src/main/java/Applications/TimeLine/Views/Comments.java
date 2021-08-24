package Applications.TimeLine.Views;

import Applications.TimeLine.Listeners.CommentListener;
import FileHandling.ImageHandler;
import FileHandling.Properties;
import GraphicComponents.GraphicMenu;
import GraphicComponents.GraphicReconnectButton;
import GraphicComponents.GraphicTweet;
import Models.Events.SendCommentEvent;
import Models.Responses.TweetListResponse;
import Utils.AutoUpdatingView;
import Utils.Convertors;
import Utils.GraphicAgent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Comments implements AutoUpdatingView{
    private VBox comments;
    private CommentListener commentListener;
    private StackPane stackPane;
    private Stage stage;
    private Scene scene;
    public Comments(TweetListResponse tweetListResponse, AutoUpdatingView backView, CommentListener commentListener, int id) {
        this.commentListener = commentListener;
        stage = GraphicAgent.stage;
        BorderPane borderPane = new BorderPane();
        stackPane = new StackPane(borderPane);
        GraphicAgent.mainPane = stackPane;
        scene = new Scene(stackPane);
        scene.getStylesheets().add("style.css");
        ImageView backButton = ImageHandler.getImage("back");
        HBox backButtonBox = new HBox(backButton);
        backButtonBox.setAlignment(Pos.CENTER_LEFT);
        backButtonBox.setId("down-line-black");
        VBox vBox = new VBox(backButtonBox);
        borderPane.setTop(vBox);
        borderPane.setLeft(GraphicMenu.Menu(this));
        VBox rightPanel = new VBox();
        rightPanel.setPrefWidth(320);
        borderPane.setRight(rightPanel);
        BorderPane borderPane1 = new BorderPane();
        borderPane.setCenter(borderPane1);

        comments = new VBox();
        GraphicTweet.tweets(tweetListResponse.tweets, stackPane, comments, this);
        ScrollPane scrollPane = new ScrollPane(comments);
        scrollPane.fitToWidthProperty().set(true);
        scrollPane.setId("scrollPane");
        borderPane1.setCenter(scrollPane);
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        TextArea commentSender = new TextArea();
        commentSender.setWrapText(true);
        commentSender.setPrefRowCount(Properties.loadNumbers("comment-text-area-row"));
        ImageView tweetButton = ImageHandler.getImage("hagh");
        ImageView imageButton = ImageHandler.getImage("image");
        VBox tweetButtonBox = new VBox(tweetButton, imageButton);
        HBox tweetBox = new HBox(Properties.loadSize("small-spacing"), commentSender, tweetButtonBox);
        tweetBox.setPadding(new Insets(Properties.loadSize("medium-indent")));
        tweetBox.setAlignment(Pos.CENTER);
        tweetBox.setId("send-box");
        borderPane1.setBottom(tweetBox);
        FileChooser fileChooser = new FileChooser();
        final boolean[] hasImage = {false};
        final File[] selectedFile = {null};
        tweetButton.setOnMouseClicked(mouseEvent -> {
            SendCommentEvent e = null;
            if (selectedFile[0] == null) {
                e = new SendCommentEvent(commentSender.getText(),id );
            }
            else {
                e = new SendCommentEvent(commentSender.getText(), Convertors.imageFileToByte(selectedFile[0]), id);
            }
            commentListener.sendComment(e);
        });
        imageButton.setOnMouseClicked(mouseEvent -> {
            selectedFile[0] = fileChooser.showOpenDialog(stage);
            hasImage[0] = true;
        });



        backButton.setOnMouseClicked(backButtonPressed(backView));
        stage.setScene(scene);
        GraphicReconnectButton.setButton();
    }

    private EventHandler<MouseEvent> backButtonPressed(AutoUpdatingView backView) {
                return mouseEvent -> backView.show();
            }

    @Override
    public void hide() {
        commentListener.stopRefreshing();
    }

    @Override
    public void show() {
        stage.setScene(scene);
        commentListener.startRefreshing();
    }

    @Override
    public void refreshNow() {
        commentListener.refresh();
    }

    public void refresh(TweetListResponse tweetListResponse) {
        comments.getChildren().clear();
        GraphicTweet.tweets(tweetListResponse.tweets, stackPane, comments, this);
    }
}
