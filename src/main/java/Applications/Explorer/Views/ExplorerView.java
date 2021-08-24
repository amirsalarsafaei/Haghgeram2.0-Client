package Applications.Explorer.Views;

import Applications.Explorer.Controllers.ProfileController;
import Applications.Explorer.Listeners.ExplorerListener;
import Applications.TimeLine.Controllers.TimeLineController;
import FileHandling.ImageHandler;
import FileHandling.Properties;
import GraphicComponents.GraphicComponents;
import GraphicComponents.GraphicHeaderFooter;
import GraphicComponents.GraphicMenu;
import GraphicComponents.*;
import Models.Responses.TweetListResponse;
import Utils.AutoUpdatingView;
import Utils.GraphicAgent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
public class ExplorerView implements AutoUpdatingView {
    private Stage stage;
    private Scene scene;
    private ExplorerListener explorerListener;
    private StackPane stackPane;
    private VBox tweets;
    private ExplorerView explorerView;
    public ExplorerView(ExplorerListener explorerListener, TweetListResponse tweetListResponse) {
        this.explorerView = this;
        this.explorerListener = explorerListener;
        this.stage = GraphicAgent.stage;
        stackPane = new StackPane();
        scene = new Scene(stackPane);
        scene.getStylesheets().add("style.css");
        BorderPane borderPane = new BorderPane();
        stackPane.getChildren().add(borderPane);
        GraphicAgent.mainPane = stackPane;
        borderPane.setLeft(GraphicMenu.Menu(this));
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        tweets = new VBox();
        GraphicTweet.tweets(tweetListResponse.tweets, stackPane, tweets,this);
        scrollPane.setContent(tweets);
        scrollPane.fitToWidthProperty().setValue(true);
        borderPane.setCenter(scrollPane);
        borderPane.setRight(GraphicComponents.RightPanel());
        borderPane.setBottom(GraphicHeaderFooter.footer(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                hide();
                new TimeLineController();
            }
        }));
        TextField searchBar = new TextField();
        ImageView searchButton = ImageHandler.getImage("search-icon");
        HBox searchBox = new HBox(Properties.loadSize("small-spacing"), searchBar, searchButton);
        searchBox.setPadding(new Insets(Properties.loadSize("big-indent")));
        VBox tmp = new VBox(searchBox);
        tmp.setId("down-line-black");
        searchBox.setAlignment(Pos.CENTER);
        tmp.setAlignment(Pos.CENTER);
        searchBar.setId("search-bar");
        borderPane.setTop(tmp);
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        searchButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                new ProfileController(searchBar.getText(), explorerView);
            }
        });
        stage.setScene(scene);
        GraphicReconnectButton.setButton();
    }

    public void refresh(TweetListResponse tweetListResponse) {
        tweets.getChildren().clear();
        GraphicTweet.tweets(tweetListResponse.tweets, stackPane, tweets,this);
    }


    @Override
    public void hide() {
        explorerListener.stopRefreshing();
    }

    @Override
    public void show() {
        stage.setScene(scene);
        explorerListener.startRefreshing();
    }

    @Override
    public void refreshNow() {
        explorerListener.refresh();
    }
}
