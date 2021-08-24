package Applications.Setting.Views;

import Applications.Setting.Controllers.BlackListController;
import Applications.Setting.Controllers.EditPrivacyController;
import Applications.Setting.Controllers.EditProfileController;
import Applications.Setting.Controllers.FollowRequestStatusController;
import Applications.Setting.Listeners.SettingMenuListener;
import Applications.TimeLine.Controllers.TimeLineController;
import FileHandling.ImageHandler;
import FileHandling.Properties;
import GraphicComponents.GraphicHeaderFooter;
import GraphicComponents.GraphicMenu;
import GraphicComponents.GraphicReconnectButton;
import Utils.AutoUpdatingView;
import Utils.GraphicAgent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SettingMenuView implements AutoUpdatingView {
    private Stage stage;
    private Scene scene;
    private SettingMenuListener listener;
    public SettingMenuView(SettingMenuListener listener) {
        this.listener = listener;
        this.stage = GraphicAgent.stage;
        BorderPane borderPane = new BorderPane();
        StackPane mainPane = new StackPane();
        GraphicAgent.mainPane = mainPane;
        mainPane.getChildren().add(borderPane);
        borderPane.setTop(GraphicHeaderFooter.header(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                new TimeLineController();
            }
        }));
        borderPane.setLeft(GraphicMenu.Menu(this));
        TilePane tilePane = new TilePane();
        tilePane.setId("grey-border");
        borderPane.setCenter(tilePane);
        ImageView edit_profile = ImageHandler.getImage("edit-profile");
        ImageView edit_privacy = ImageHandler.getImage("edit-privacy");
        ImageView delete_account = ImageHandler.getImage("delete-account");
        ImageView black_list = ImageHandler.getImage("black-list");
        ImageView requests_status = ImageHandler.getImage("requests-status");
        ImageView logout = ImageHandler.getImage("logout");
        addHoverShadow(edit_profile);
        addHoverShadow(edit_privacy);
        addHoverShadow(delete_account);
        addHoverShadow(black_list);
        addHoverShadow(requests_status);
        addHoverShadow(logout);
        edit_profile.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
               new EditProfileController();
            }
        });
        edit_privacy.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
               new EditPrivacyController();
            }
        });
        delete_account.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                BorderPane whitePane = new BorderPane();
                whitePane.setId("white-fade");
                mainPane.getChildren().add(whitePane);
                Label confirmLabel = new Label(Properties.loadDialog("delete-confirm-dialog"));
                Button yes = new Button(Properties.loadDialog("yes")),
                        no = new Button(Properties.loadDialog("no"));
                HBox yesNoBox = new HBox(Properties.loadSize("big-spacing"), yes, no);
                VBox confirmVBox = new VBox(Properties.loadSize("medium-spacing"),confirmLabel, yesNoBox);
                confirmVBox.setId("white");
                confirmVBox.setAlignment(Pos.CENTER);
                confirmVBox.setPadding(new Insets(Properties.loadSize("big-indent")));
                HBox sizingConfBox = new HBox(confirmVBox);
                VBox sizingConFVBox = new VBox(sizingConfBox);
                yesNoBox.setAlignment(Pos.CENTER);
                mainPane.getChildren().add(sizingConFVBox);
                sizingConFVBox.setAlignment(Pos.CENTER);
                sizingConfBox.setAlignment(Pos.CENTER);
                confirmVBox.setId("white-box");
                StackPane.setAlignment(sizingConFVBox, Pos.CENTER);
                yes.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        listener.deleteAccountButtonPressed();
                    }
                });
                no.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        sizingConFVBox.setVisible(false);
                        whitePane.setVisible(false);
                    }
                });

            }
        });
        black_list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                new BlackListController();
            }
        });
        logout.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                listener.logOutButtonPressed();
            }
        });
        requests_status.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                 new FollowRequestStatusController();
            }
        });
        tilePane.getChildren().add(edit_profile);
        tilePane.getChildren().add(edit_privacy);
        tilePane.getChildren().add(delete_account);
        tilePane.getChildren().add(black_list);
        tilePane.getChildren().add(requests_status);
        tilePane.getChildren().add(logout);
        tilePane.setPadding(new Insets(Properties.loadSize("big-indent")));
        tilePane.setVgap(Properties.loadSize("setting-page-tile-v-gap"));
        tilePane.setHgap(Properties.loadSize("setting-page-tile-h-gap"));
        scene = new Scene(mainPane);
        scene.getStylesheets().add("style.css");
        GraphicAgent.stage.setScene(scene);
        GraphicReconnectButton.setButton();
    }

    public void addHoverShadow(ImageView imageView) {
        imageView.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                imageView.setEffect(new DropShadow(Properties.loadSize("small-shadow"), Color.BLACK));
            }
        });
        imageView.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                imageView.setEffect(null);
            }
        });
    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {
        stage.setScene(scene);
    }

    @Override
    public void refreshNow() {

    }
}
