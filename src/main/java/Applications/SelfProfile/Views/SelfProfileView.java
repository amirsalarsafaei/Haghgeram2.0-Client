package Applications.SelfProfile.Views;

import Applications.SelfProfile.Listeners.SelfProfileListener;
import FileHandling.ImageHandler;
import FileHandling.Properties;
import GraphicComponents.GraphicReconnectButton;
import GraphicComponents.GraphicTweet;
import Models.Responses.BigUserResponse;
import Models.Responses.SelfUserResponse;
import Utils.AutoUpdatingView;
import Utils.GraphicAgent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class SelfProfileView implements AutoUpdatingView {

    private final VBox tweets;
    private final StackPane stackPane;
    private final SelfProfileListener listener;
    private final Stage stage;
    private final Scene scene;
    public SelfProfileView(SelfUserResponse user, SelfProfileListener listener, AutoUpdatingView backView) {
        this.listener = listener;
        stage = GraphicAgent.stage;
        BorderPane borderPane = new BorderPane();
        stackPane = new StackPane(borderPane);
        scene = new Scene(stackPane);
        GraphicAgent.mainPane = stackPane;
        scene.getStylesheets().add("style.css");
        ImageView backButton = ImageHandler.getImage("back");
        HBox backButtonBox = new HBox(backButton);
        backButtonBox.setAlignment(Pos.CENTER_LEFT);
        VBox vBox = new VBox(backButtonBox);
        borderPane.setTop(vBox);
        VBox profileBox = new VBox();
        Circle circle = new Circle(0, 0, Properties.loadSize("big-profile-radius"));
        circle.setFill(Color.BLUEVIOLET);
        ImageView profilePic = ImageHandler.getUserProfileImage(user.getUser().getImage());
        circle.setFill(new ImagePattern(profilePic.getImage()));
        DropShadow dropShadow = new DropShadow(Properties.loadSize("small-shadow"), Color.BLACK);
        circle.setEffect(dropShadow);
        profileBox.setAlignment(Pos.TOP_LEFT);
        borderPane.setCenter(profileBox);
        StackPane CircleAndLine = new StackPane();
        HBox CircleAndLineBox = new HBox(CircleAndLine);
        Line line2 = new Line();
        line2.setStartX(0);
        line2.setEndX(scene.getWidth());
        line2.setOpacity(0.4);
        CircleAndLine.getChildren().add(line2);
        StackPane.setAlignment(line2, Pos.CENTER);
        CircleAndLine.getChildren().add(circle);
        StackPane.setAlignment(circle, Pos.CENTER);
        profileBox.getChildren().add(CircleAndLineBox);
        Label name = new Label(user.getUser().getName() + " " + user.getUser().getFamily_name());
        Label date = new Label("Online");
        Label bio = new Label(user.getUser().getBio());
        Label birthday = new Label(user.getUser().getBirthDay());
        name.setId("subtitle");
        date.setId("subtitle");
        bio.setId("subtitle-wrap");
        birthday.setId("subtitle-wrap");
        HBox nameBox = new HBox(name);
        HBox dateBox = new HBox(date);
        HBox bioBox = new HBox(7, ImageHandler.getImage("bio"), bio);
        HBox birthBox = new HBox(7, ImageHandler.getImage("birthday"), birthday);
        dateBox.setPadding(infoInsets());
        nameBox.setPadding(infoInsets());
        bioBox.setPadding(infoInsets());
        birthBox.setPadding(infoInsets());
        profileBox.getChildren().add(nameBox);
        profileBox.getChildren().add(dateBox);
        profileBox.getChildren().add(bioBox);
        profileBox.getChildren().add(birthBox);
        Line line = new Line();
        line.setStartX(0);
        line.setEndX(scene.getWidth());
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            line.setEndX(scene.getWidth());
            line2.setEndX(scene.getWidth());
        });
        profileBox.getChildren().add(line);
        BorderPane tweetPane = new BorderPane();
        borderPane.setBottom(tweetPane);
        tweets = new VBox();
        ScrollPane scrollPane = new ScrollPane(tweets);
        scrollPane.fitToWidthProperty().setValue(true);
        GraphicTweet.tweets(user.getTweets().tweets, stackPane, tweets, this);
        scrollPane.setMaxHeight(Properties.loadSize("personal-page-tweet-height"));
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        tweetPane.setCenter(scrollPane);
        tweetPane.setLeft(Followers(user.getUser()));
        tweetPane.setRight(Following(user.getUser()));
        GraphicAgent.stage.setScene(scene);
        backButtonBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                hide();
                backView.show();
            }
        });
        GraphicReconnectButton.setButton();
    }
    public static ScrollPane Followers(BigUserResponse user) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.fitToWidthProperty().setValue(true);
        scrollPane.setPrefHeight(Properties.loadSize("personal-page-follow-height"));
        scrollPane.setPrefWidth(Properties.loadSize("personal-page-follow-width"));
        VBox vBox = new VBox();
        HBox title = new HBox(new Label(user.getFollowers().size() + " " + Properties.loadDialog("followers")));
        title.setPadding(new Insets(Properties.loadSize("big-indent")));
        title.setAlignment(Pos.CENTER);
        vBox.getChildren().add(title);
        title.setId("down-line-black");
        for (String followers: user.getFollowers()) {
            HBox hBox = new HBox(new Label(followers));
            hBox.setAlignment(Pos.CENTER);
            vBox.getChildren().add(hBox);
            hBox.setPadding(new Insets(Properties.loadSize("small-indent")));
            hBox.setId("down-line-grey");
        }
        scrollPane.setContent(vBox);
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        return scrollPane;
    }


    public static ScrollPane Following(BigUserResponse user) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.fitToWidthProperty().setValue(true);
        scrollPane.setPrefHeight(Properties.loadSize("personal-page-follow-height"));
        scrollPane.setPrefWidth(Properties.loadSize("personal-page-follow-width"));
        VBox vBox = new VBox();
        HBox title = new HBox(new Label(user.getFollowing().size() + " " + Properties.loadDialog("followings")));
        title.setPadding(new Insets(Properties.loadSize("big-indent")));
        title.setAlignment(Pos.CENTER);
        vBox.getChildren().add(title);
        title.setId("down-line-black");
        for (String following: user.getFollowing()) {
            HBox hBox = new HBox(new Label(following));
            hBox.setAlignment(Pos.CENTER);
            vBox.getChildren().add(hBox);
            hBox.setPadding(new Insets(Properties.loadSize("small-indent")));
            hBox.setId("down-line-grey");
        }
        scrollPane.setContent(vBox);
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        return scrollPane;
    }

    public void refresh(SelfUserResponse user) {
        tweets.getChildren().clear();
        GraphicTweet.tweets(user.getTweets().tweets, stackPane, tweets, this);
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

    public Insets infoInsets() {
        return new Insets(Properties.loadSize("tiny-indent"), Properties.loadSize("big-indent")
                , Properties.loadSize("tiny-indent"), Properties.loadSize("big-indent"));
    }
}
