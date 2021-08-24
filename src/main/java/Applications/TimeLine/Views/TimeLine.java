package Applications.TimeLine.Views;

import Applications.TimeLine.Listeners.MainPageListener;
import FileHandling.ImageHandler;
import FileHandling.Properties;
import GraphicComponents.GraphicComponents;
import GraphicComponents.GraphicHeaderFooter;
import GraphicComponents.GraphicMenu;
import GraphicComponents.GraphicTweet;
import GraphicComponents.GraphicReconnectButton;
import Models.Events.SendTweetEvent;
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

public class TimeLine implements AutoUpdatingView {

    private VBox tweets;
    private StackPane stackPane;
    private Scene scene;
    private Stage stage;
    public VBox getTweets() {
        return tweets;
    }

    public void setTweets(VBox tweets) {
        this.tweets = tweets;
    }

    private MainPageListener mainPageListener;

    public TimeLine(MainPageListener mainPageListener) {
        this.mainPageListener = mainPageListener;
        stage = GraphicAgent.stage;
        BorderPane pane = new BorderPane();
        stackPane = new StackPane(pane);
        GraphicAgent.mainPane = stackPane;
        Scene scene = new Scene(stackPane);
        GraphicAgent.scene = scene;
        scene.getStylesheets().add("style.css");
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.fitToWidthProperty().set(true);
        FileChooser fileChooser = new FileChooser();

        pane.setTop(GraphicHeaderFooter.header(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                return;
            }
        }));
        pane.setLeft(GraphicMenu.Menu(this));

        scrollPane.setId("scrollPane");
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(scrollPane);
        TextArea tweet = new TextArea();
        tweet.setWrapText(true);
        tweet.setPrefRowCount(3);
        ImageView tweetButton = ImageHandler.getImage("hagh");
        ImageView imageButton = ImageHandler.getImage("image");
        VBox tweetButtonBox = new VBox(tweetButton, imageButton);
        HBox tweetBox = new HBox(Properties.loadSize("small-spacing"), tweet, tweetButtonBox);
        tweetBox.setPadding(new Insets(Properties.loadSize("medium-indent")));
        tweetBox.setAlignment(Pos.CENTER);
        tweetBox.setId("white-box");
        borderPane.setBottom(tweetBox);
        final File[] selectedFile = {null};
        tweetButton.setOnMouseClicked(mouseEvent -> {
            SendTweetEvent sendTweetEvent;
            if (selectedFile[0] != null)
                sendTweetEvent = new SendTweetEvent(tweet.getText(), Convertors.imageFileToByte(selectedFile[0]));
            else
                sendTweetEvent = new SendTweetEvent(tweet.getText());
            tweet.setText("");
            mainPageListener.sendTweetListener(sendTweetEvent);
        });
        imageButton.setOnMouseClicked(mouseEvent -> selectedFile[0] = fileChooser.showOpenDialog(stage));

        pane.setCenter(borderPane);

        pane.setRight(GraphicComponents.RightPanel());
        tweets = new VBox();
        scrollPane.setContent(tweets);
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        this.scene = scene;
        stage.setScene(scene);
        GraphicReconnectButton.setButton();
    }

    public void refresh(TweetListResponse tweetListResponse) {
        tweets.getChildren().clear();
        GraphicTweet.tweets(tweetListResponse.tweets, stackPane, tweets, this);
    }

    @Override
    public void hide() {
        mainPageListener.stopRefreshing();
    }

    @Override
    public void show() {
        mainPageListener.startRefreshing();
        stage.setScene(scene);
    }

    @Override
    public void refreshNow() {
        mainPageListener.refresh();
    }
}
