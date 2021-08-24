package Applications.Explorer.Views;

import Applications.Explorer.Listeners.ProfileListener;
import FileHandling.ImageHandler;
import FileHandling.Properties;
import GraphicComponents.*;
import GraphicComponents.GraphicReconnectButton;
import Models.Events.FollowEvent;
import Models.Events.SendMessageToUserEvent;
import Models.Responses.BigUserResponse;
import Utils.AutoUpdatingView;
import Utils.GraphicAgent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class ProfileView implements AutoUpdatingView {
    Circle ProfileCircle;
    Label name, date, bio, birthday;
    ImageView followButton, mute, block, message;
    HBox tools;
    ProfileListener listener;
    Stage stage;
    Scene scene;
    public ProfileView(BigUserResponse bigUserResponse, ProfileListener listener){
        this.listener = listener;
        stage = GraphicAgent.stage;
        BorderPane borderPane = new BorderPane();
        StackPane stackPane = new StackPane(borderPane);
        stackPane.prefWidthProperty().bind(stage.widthProperty());
        GraphicAgent.mainPane = stackPane;
        scene = new Scene(stackPane);
        scene.getStylesheets().add("style.css");
        ImageView backButton = ImageHandler.getImage("back");
        HBox backButtonBox = new HBox(backButton);
        backButtonBox.setAlignment(Pos.CENTER_LEFT);
        VBox vBox = new VBox(backButtonBox);
        borderPane.setTop(vBox);
        VBox profileBox = new VBox();
        ProfileCircle = new Circle(0, 0, Properties.loadSize("big-profile-radius"));
        ProfileCircle.setFill(Color.BLUEVIOLET);
        ImageView profilePic = ImageHandler.getUserProfileImage(bigUserResponse.getImage());
        ProfileCircle.setFill(new ImagePattern(profilePic.getImage()));
        DropShadow dropShadow = new DropShadow(Properties.loadSize("small-shadow"), Color.BLACK);
        ProfileCircle.setEffect(dropShadow);
        profileBox.setAlignment(Pos.TOP_LEFT);
        borderPane.setCenter(profileBox);
        StackPane ProfileCircleAndLine = new StackPane();
        ProfileCircleAndLine.prefWidthProperty().bind(stage.widthProperty());
        HBox ProfileCircleAndLineBox = new HBox(ProfileCircleAndLine);
        Line line2 = new Line();
        line2.setStartX(0);
        line2.setEndX(stage.getWidth());
        line2.setOpacity(0.4);
        ProfileCircleAndLine.getChildren().add(line2);
        StackPane.setAlignment(line2, Pos.CENTER);
        ProfileCircleAndLine.getChildren().add(ProfileCircle);
        StackPane.setAlignment(ProfileCircle, Pos.CENTER);
        profileBox.getChildren().add(ProfileCircleAndLineBox);
        name = new Label(bigUserResponse.getName() + " " + bigUserResponse.getFamily_name());
        date = new Label(bigUserResponse.getDate());
        bio = new Label(bigUserResponse.getBio());
        birthday = new Label(bigUserResponse.getBirthDay());
        name.setId("subtitle");
        date.setId("subtitle");
        bio.setId("subtitle-wrap");
        birthday.setId("subtitle-wrap");
        HBox nameBox = new HBox(name);
        HBox dateBox = new HBox(date);
        HBox bioBox = new HBox(Properties.loadSize("small-spacing"), ImageHandler.getImage("bio"), bio);
        HBox birthBox = new HBox(Properties.loadSize("small-spacing"), ImageHandler.getImage("birthday"), birthday);
        dateBox.setPadding(infoInsets());
        nameBox.setPadding(infoInsets());
        bioBox.setPadding(infoInsets());
        birthBox.setPadding(infoInsets());
        profileBox.getChildren().add(nameBox);
        profileBox.getChildren().add(dateBox);
        profileBox.getChildren().add(bioBox);
        profileBox.getChildren().add(birthBox);
        followButton = new ImageView();
        if (!bigUserResponse.isBlocked()) {
            if (bigUserResponse.isFollowed())
                followButton = ImageHandler.getImage("unfollow");
            else if (bigUserResponse.isPending())
                followButton = ImageHandler.getImage("pending");
            else
                followButton = ImageHandler.getImage("follow");
        }
        followButton.setOnMouseClicked(followButtonClicked(bigUserResponse, listener));
        tools = new HBox(Properties.loadSize("medium-spacing"));
        tools.setPadding(new Insets(Properties.loadSize("big-indent")));
        tools.setAlignment(Pos.CENTER);
        GraphicButtons graphicButtons = new GraphicButtons(this);
        mute = graphicButtons.muteButton(bigUserResponse);
        block = graphicButtons.blockButton(bigUserResponse);
        message = new ImageView();
        if (bigUserResponse.isFollowed())
            message = ImageHandler.getImage("forward");
        message.setOnMouseClicked(getForward(stackPane, bigUserResponse.getUsername()));
        tools.getChildren().addAll(followButton ,mute, block, message);
        profileBox.getChildren().add(tools);
        Line line = new Line();
        line.setStartX(0);
        line.setEndX(stage.getWidth());
        profileBox.getChildren().add(line);
        GraphicAgent.stage.setScene(scene);
        stage = GraphicAgent.stage;
        backButtonBox.setOnMouseClicked(backButtonClicked(listener));

        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            line.setEndX(scene.getWidth());
            line2.setEndX(scene.getWidth());
        });
        GraphicReconnectButton.setButton();

    }

    private EventHandler<MouseEvent> backButtonClicked(ProfileListener listener) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                hide();
                listener.back();
            }
        };
    }

    private EventHandler<MouseEvent> followButtonClicked(BigUserResponse bigUserResponse, ProfileListener listener) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                listener.followButtonPressed(new FollowEvent(bigUserResponse.getUsername()));
            }
        };
    }

    private EventHandler<MouseEvent> getForward(StackPane stackPane, String target) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                TextArea messageArea = new TextArea();
//                messageArea.setPrefRowCount(Properties.loadNumbers("message-area-row"));
//                messageArea.setPrefColumnCount(Properties.loadNumbers("message-area-column"));
                ImageView sendMessage = ImageHandler.getImage("forward");
                HBox messageBox = new HBox(Properties.loadSize("small-spacing"), messageArea, sendMessage);
                messageBox.setPadding(new Insets(Properties.loadSize("message-box-insets")));
                messageArea.setStyle("-fx-wrap-text: true;");
                messageBox.setAlignment(Pos.CENTER);
                Pane BlackPane = new Pane();
                BorderPane pane = new BorderPane();
                pane.setCenter(messageBox);
                BlackPane.setId("black-fade");
                stackPane.getChildren().addAll(BlackPane, pane);
                StackPane.setAlignment(pane, Pos.CENTER);
                sendMessage.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        listener.sendMessageButtonPressed(new SendMessageToUserEvent(messageArea.getText(), target));
                        BlackPane.setVisible(false);
                        pane.setVisible(false);
                    }
                });
            }
        };
    }

    public Insets infoInsets() {
        return new Insets(Properties.loadSize("tiny-indent"), Properties.loadSize("big-indent")
                , Properties.loadSize("tiny-indent"), Properties.loadSize("big-indent"));
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

    public void refresh(BigUserResponse bigUserResponse) {
        name.setText(bigUserResponse.getName() + " " + bigUserResponse.getFamily_name());
        date.setText(bigUserResponse.getDate());
        bio.setText(bigUserResponse.getBio());
        birthday.setText(bigUserResponse.getBirthDay());
        ImageView profilePic = ImageHandler.getUserProfileImage(bigUserResponse.getImage());
        ProfileCircle.setFill(new ImagePattern(profilePic.getImage()));
        if (bigUserResponse.isFollowed())
            message = ImageHandler.getImage("forward");
        else if (tools.getChildren().contains(message))
            tools.getChildren().remove(message);
        if (!bigUserResponse.isBlocked()) {
            if (bigUserResponse.isFollowed())
                followButton.setImage(ImageHandler.getImage("unfollow").getImage());
            else if (bigUserResponse.isPending())
                followButton.setImage(ImageHandler.getImage("pending").getImage());
            else
                followButton.setImage(ImageHandler.getImage("follow").getImage());
        }
        if (bigUserResponse.isBlocked())
            block.setImage(ImageHandler.getImage("unblock").getImage());
        else
            block.setImage(ImageHandler.getImage("block").getImage());
        if (bigUserResponse.isMuted())
            mute.setImage(ImageHandler.getImage("unmute").getImage());
        else
            mute.setImage(ImageHandler.getImage("mute").getImage());

    }
}
