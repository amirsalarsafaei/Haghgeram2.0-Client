package Applications.List.Views;

import Applications.List.Listeners.ListListener;
import FileHandling.ImageHandler;
import FileHandling.Properties;
import Models.Events.AddUsersToListCompletedEvent;
import Models.Events.AddUsersToListEvent;
import Models.Responses.FollowingsResponse;
import Models.Responses.ListResponse;
import Utils.AutoUpdatingView;
import Utils.Filter;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

import java.util.ArrayList;

public class ListView implements AutoUpdatingView {

    private VBox vBox;
    private ListListener listener;
    private AutoUpdatingView listsListView;
    private StackPane Pane;
    private ListResponse  listResponse;
    public ListView(BorderPane borderPane, ListListener listener, AutoUpdatingView listsListView, StackPane Pane) {
        this.Pane = Pane;
        this.listsListView = listsListView;
        this.listener = listener;
        BorderPane addAndScrollBorderPane = new BorderPane();
        ScrollPane userScroll = new ScrollPane();
        addAndScrollBorderPane.setCenter(userScroll);
        ImageView addUserButton = ImageHandler.getImage("add-user");
        HBox addUserBox = new HBox(addUserButton);
        addUserBox.setAlignment(Pos.CENTER);
        addUserBox.setPadding(new Insets(Properties.loadSize("medium-indent")));
        addAndScrollBorderPane.setTop(addUserBox);
        vBox = new VBox();
        userScroll.setContent(vBox);
        vBox.setPrefWidth(Properties.loadSize("setGroup-width"));

        userScroll.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        userScroll.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        addUserButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                listener.addUsersButtonPressed(new AddUsersToListEvent(listResponse));
            }
        });
        borderPane.setRight(addAndScrollBorderPane);
    }

    public void refresh(ListResponse listResponse) {
        vBox.getChildren().clear();
        this.listResponse = listResponse;
        for (String user: listResponse.getUsers()) {
            HBox hBox = new HBox();
            StackPane stackPane = new StackPane();
            Label name = new Label(user);

            ImageView trashIcon = ImageHandler.getImage("trash");
            HBox nameBox = new HBox(name);
            nameBox.setAlignment(Pos.CENTER_LEFT);
            stackPane.getChildren().add(nameBox);
            stackPane.setPrefWidth(Properties.loadSize("setGroup-width"));
            hBox.getChildren().add(stackPane);
            hBox.setPadding(new Insets(Properties.loadSize("small-indent")));
            hBox.setId("down-line-black");
            vBox.getChildren().add(hBox);
        }
    }

    public void addUserToGroup(AddUsersToListEvent e , FollowingsResponse followingsResponse) {
        ArrayList<String> res = new ArrayList<>(e.getListResponse().getUsers());
        BorderPane WhitePane = new BorderPane();
        Pane.getChildren().add(WhitePane);
        WhitePane.setId("white-fate");
        VBox vBox = new VBox();
        vBox.setMaxWidth(Properties.loadSize("add-users-box-width"));
        vBox.setMaxHeight(Properties.loadSize("add-users-box-height"));
        vBox.setAlignment(Pos.CENTER);
        BorderPane borderPane = new BorderPane();
        vBox.getChildren().add(borderPane);
        vBox.setId("white-box");
        DropShadow dropShadow = new DropShadow(Properties.loadSize("small-shadow"), Color.BLACK);
        vBox.setEffect(dropShadow);
        Pane.getChildren().add(vBox);
        StackPane.setAlignment(vBox, Pos.CENTER);
        ScrollPane scrollPane = new ScrollPane();
        borderPane.setCenter(scrollPane);
        VBox userList = new VBox();
        scrollPane.setContent(userList);
        ImageView addUsers = ImageHandler.getImage("add-user");
        HBox addUsersBox = new HBox(addUsers);
        addUsersBox.setAlignment(Pos.CENTER);
        addUsersBox.setPadding(new Insets(Properties.loadSize("small-indent")));
        borderPane.setTop(addUsersBox);
        borderPane.setPrefSize(Properties.loadSize("add-users-box-width"),
                Properties.loadSize("add-users-box-height") - 1);

        scrollPane.setPadding(new Insets(0));
        userList.setPrefWidth(Properties.loadSize("add-users-box-width") - 4);
        for (String following : followingsResponse.getUsers() ) {
            StackPane stackPane = new StackPane();
            HBox hBox = new HBox(stackPane);
            stackPane.setPrefWidth(Properties.loadSize("add-users-inbox-width"));
            userList.getChildren().add(hBox);
            hBox.setPadding(new Insets(Properties.loadSize("small-indent")));
            Label name = new Label(following);
            stackPane.getChildren().add(name);
            StackPane.setAlignment(name, Pos.CENTER_LEFT);
            final boolean[] inList = {Filter.boolFind(e.getListResponse().getUsers(), following)};
            ImageView check = ImageHandler.getImage("check-" + inList[0]);
            stackPane.getChildren().add(check);
            StackPane.setAlignment(check, Pos.CENTER_RIGHT);
            hBox.setId("down-line-grey");

            check.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    inList[0] = !inList[0];
                    check.setImage(ImageHandler.getImage("check-" + inList[0]).getImage());

                    if (inList[0])
                        res.add(following);
                    else
                        Filter.delFind(res, following);
                }
            });

        }

        addUsers.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                WhitePane.setVisible(false);
                vBox.setVisible(false);
                listener.addUsersCompletedButtonPressed
                        (new AddUsersToListCompletedEvent(res, e.getListResponse().getListName()));
            }
        });
    }

    public StackPane getPane() {
        return Pane;
    }

    public void setPane(StackPane pane) {
        Pane = pane;
    }

    @Override
    public void hide() {
        listener.stopRefreshing();
        listsListView.hide();
    }

    @Override
    public void show() {
        listsListView.show();
        listener.startRefreshing();
    }

    @Override
    public void refreshNow() {

    }
}
