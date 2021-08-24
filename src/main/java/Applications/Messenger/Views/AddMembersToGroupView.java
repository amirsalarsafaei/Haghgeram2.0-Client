package Applications.Messenger.Views;

import Applications.Messenger.Listeners.AddMembersToGroupListener;
import FileHandling.ImageHandler;
import FileHandling.Properties;
import Models.Events.AddMembersToGroupEvent;
import Models.Responses.FollowingsResponse;
import Utils.Filter;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class AddMembersToGroupView {
    public AddMembersToGroupView(StackPane stackPane, FollowingsResponse followingsResponse,
                                 AddMembersToGroupListener listener, boolean addGroupName) {
        ArrayList<String> res = new ArrayList<>();
        BorderPane WhitePane = new BorderPane();
        stackPane.getChildren().add(WhitePane);
        WhitePane.setId("whitePane");
        VBox vBox = new VBox();
        vBox.setMaxWidth(Properties.loadSize("set-group-chat-box-width"));
        vBox.setMaxHeight(Properties.loadSize("set-group-chat-box-height"));
        vBox.setAlignment(Pos.CENTER);
        BorderPane borderPane = new BorderPane();
        vBox.getChildren().add(borderPane);
        vBox.setId("white-box");
        DropShadow dropShadow = new DropShadow(Properties.loadSize("small-shadow"), Color.BLACK);
        vBox.setEffect(dropShadow);
        stackPane.getChildren().add(vBox);
        StackPane.setAlignment(vBox, Pos.CENTER);
        ScrollPane scrollPane = new ScrollPane();
        borderPane.setCenter(scrollPane);
        VBox userList = new VBox();
        scrollPane.setContent(userList);
        ImageView addUsers = ImageHandler.getImage("add-group-chat");
        TextField groupName = new TextField();
        HBox tmpBox = new HBox(Properties.loadSize("small-spacing"));
        if (addGroupName)
            tmpBox.getChildren().add(groupName);
        tmpBox.getChildren().add(addUsers);
        tmpBox.setAlignment(Pos.CENTER);
        Label group_name_error = new Label(Properties.loadDialog("empty-field"));
        group_name_error.setId("error");
        group_name_error.setVisible(false);
        VBox addUsersBox = new VBox(tmpBox);
        addUsersBox.setAlignment(Pos.CENTER);
        addUsersBox.setPadding(new Insets(Properties.loadSize("small-indent")));
        borderPane.setTop(addUsersBox);
        borderPane.setPrefSize(Properties.loadSize("set-group-chat-box-width"),
                Properties.loadSize("set-group-chat-box-height") - 1);
        scrollPane.setPadding(new Insets(0));
        userList.setPrefWidth(Properties.loadSize("set-group-chat-box-width") - 4);
        for (String following : followingsResponse.getUsers()) {
            StackPane stackPaneTmp = new StackPane();
            HBox hBox = new HBox(stackPaneTmp);
            stackPaneTmp.setPrefWidth(Properties.loadSize("set-group-chat-inbox-width"));
            userList.getChildren().add(hBox);
            hBox.setPadding(new Insets(Properties.loadSize("small-indent")));
            Label name = new Label(following);
            stackPaneTmp.getChildren().add(name);
            StackPane.setAlignment(name, Pos.CENTER_LEFT);
            final boolean[] inList = {false};
            ImageView check = ImageHandler.getImage("check-" + inList[0]);
            stackPaneTmp.getChildren().add(check);
            StackPane.setAlignment(check, Pos.CENTER_RIGHT);
            hBox.setId("grey-border");

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

                if (addGroupName && groupName.getText().equals("")) {
                    group_name_error.setVisible(true);
                    return;
                }
                AddMembersToGroupEvent event = new AddMembersToGroupEvent(res);
                if (addGroupName)
                    event.setName(groupName.getText());
                WhitePane.setVisible(false);
                vBox.setVisible(false);
                listener.addUsersButtonClicked(event);

            }
        });
    }
}
