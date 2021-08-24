package GraphicComponents;

import Applications.Explorer.Controllers.ExplorerController;
import Applications.List.Controllers.ListsListController;
import Applications.Messenger.Controllers.ConversationListController;
import Applications.Notification.Controllers.NotificationController;
import Applications.SelfProfile.Controllers.SelfProfileController;
import Applications.Setting.Controllers.SettingMenuController;
import Applications.TimeLine.Controllers.TimeLineController;
import FileHandling.ImageHandler;
import FileHandling.Properties;
import Utils.AutoUpdatingView;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
public class GraphicMenu {

    public static Insets buttonInsets() {
        return new Insets(Properties.loadSize("tiny-indent"), Properties.loadSize("big-indent"),
                Properties.loadSize("tiny-indent"), Properties.loadSize("big-indent"));
    }

    public static VBox Menu(AutoUpdatingView autoUpdatingView) {
        ImageView HomeImage =  ImageHandler.getImage("Home");
        HBox homeBox = new HBox(HomeImage);
        homeBox.setAlignment(Pos.CENTER);
        homeBox.setId("menu-button");
        homeBox.setPadding(buttonInsets());
        ImageView MessageImage = ImageHandler.getImage("chats");
        HBox messengerBox = new HBox( MessageImage);
        messengerBox.setAlignment(Pos.CENTER);
        messengerBox.setId("menu-button");
        messengerBox.setPadding(buttonInsets());
        homeBox.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                HomeImage.setImage(ImageHandler.getImage("home-hover").getImage());
            }
        });

        homeBox.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                HomeImage.setImage(ImageHandler.getImage("Home").getImage());
            }
        });
        homeBox.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                autoUpdatingView.hide();
                new TimeLineController();
            }
        });
        messengerBox.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                MessageImage.setImage(ImageHandler.getImage("chats-hover").getImage());
            }
        });
        messengerBox.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                MessageImage.setImage(ImageHandler.getImage("chats").getImage());
            }
        });
        messengerBox.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                autoUpdatingView.hide();
                new ConversationListController();
            }
        });
        ImageView listImage = ImageHandler.getImage("list");
        HBox listBox = new HBox(listImage);
        listBox.setAlignment(Pos.CENTER);
        listBox.setId("menu-button");
        listBox.setPadding(buttonInsets());
        listBox.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                listImage.setImage(ImageHandler.getImage("list-hover").getImage());
            }
        });
        listBox.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                listImage.setImage(ImageHandler.getImage("list").getImage());
            }
        });
        listBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                autoUpdatingView.hide();
                new ListsListController();
            }
        });
        ImageView searchImage = ImageHandler.getImage("search");
        HBox searchBox = new HBox(searchImage);
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setId("menu-button");
        searchBox.setPadding(buttonInsets());
        searchBox.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                searchImage.setImage(ImageHandler.getImage("search-hover").getImage());
            }
        });
        searchBox.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                searchImage.setImage(ImageHandler.getImage("search").getImage());
            }
        });
        searchBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                autoUpdatingView.hide();
                new ExplorerController();
            }
        });
        ImageView personalImage =ImageHandler.getImage("personal");
        HBox personalBox = new HBox(personalImage);
        personalBox.setAlignment(Pos.CENTER);
        personalBox.setId("menu-button");
        personalBox.setPadding(buttonInsets());
        personalBox.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                personalImage.setImage(ImageHandler.getImage("personal-hover").getImage());
            }
        });
        personalBox.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                personalImage.setImage(ImageHandler.getImage("personal").getImage());
            }
        });
        personalBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                autoUpdatingView.hide();
                new SelfProfileController(autoUpdatingView);
            }
        });


        ImageView settingImage = ImageHandler.getImage("setting");
        HBox settingBox = new HBox(settingImage);
        settingBox.setAlignment(Pos.CENTER);
        settingBox.setId("menu-button");
        settingBox.setPadding(buttonInsets());
        settingBox.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                settingImage.setImage(ImageHandler.getImage("setting-hover").getImage());
            }
        });
        settingBox.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                settingImage.setImage(ImageHandler.getImage("setting").getImage());
            }
        });
        settingBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                autoUpdatingView.hide();
                new SettingMenuController();
            }
        });

        ImageView notificationImage = ImageHandler.getImage("notification");
        HBox notificationBox = new HBox(notificationImage);
        notificationBox.setAlignment(Pos.CENTER);
        notificationBox.setId("menu-button");
        notificationBox.setPadding(buttonInsets());
        notificationBox.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                notificationImage.setImage(ImageHandler.getImage("notification-hover").getImage());
            }
        });
        notificationBox.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                notificationImage.setImage(ImageHandler.getImage("notification").getImage());
            }
        });
        notificationBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                autoUpdatingView.hide();
                new NotificationController();
            }
        });

        VBox res = new VBox(Properties.loadSize("medium-spacing"),homeBox, messengerBox, listBox, searchBox, personalBox, notificationBox,settingBox);
        res.setPadding(new Insets(
                Properties.loadSize("small-indent"), Properties.loadSize("big-indent"),
                Properties.loadSize("small-indent"), Properties.loadSize("big-indent")));
        res.setPrefWidth(Properties.loadSize("menu-panel-size"));
        res.setAlignment(Pos.BASELINE_CENTER);
        return res;
    }
}
