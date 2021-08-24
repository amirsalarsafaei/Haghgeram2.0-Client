package Applications.Messenger.Views;

import Applications.Messenger.Listeners.GroupMessageListener;
import FileHandling.ImageHandler;
import FileHandling.Properties;
import Models.Events.SendGroupMessageEvent;
import Models.Responses.GroupMessageResponse;
import Utils.Filter;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class GroupMessageView {
    public GroupMessageView(StackPane Pane, GroupMessageResponse groupMessageResponse,
                            GroupMessageListener listener) {
        ArrayList<String> toUsers = new ArrayList<>();
        BorderPane WhitePane = new BorderPane();
        Pane.getChildren().add(WhitePane);
        WhitePane.setId("white-fade");
        VBox vBox = new VBox();
        vBox.setMaxWidth(Properties.loadSize("forward-box-max-width"));
        vBox.setMaxHeight(Properties.loadSize("forward-box-max-height"));
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
        ImageView addUsers = ImageHandler.getImage("forward");
        TextArea messageArea = new TextArea();
        messageArea.setPrefColumnCount(Properties.loadNumbers("setGroupMessageColumn"));
        HBox addUsersBox = new HBox(Properties.loadSize("medium-spacing"), messageArea, addUsers);
        addUsersBox.setAlignment(Pos.CENTER);
        addUsersBox.setPadding(new Insets(Properties.loadSize("small-indent")));
        borderPane.setTop(addUsersBox);
        borderPane.setPrefSize(Properties.loadSize("forward-inbox-pref-width"),
                Properties.loadSize("forward-inbox-pref-height"));
        Label type = new Label(Properties.loadDialog("followings"));
        HBox typeBox = new HBox(type);
        typeBox.setId("down-line-black");
        typeBox.setAlignment(Pos.CENTER);
        userList.getChildren().add(typeBox);
        for (String following : groupMessageResponse.getFollowings()) {
            StackPane stackPane = new StackPane();
            HBox hBox = new HBox(stackPane);
            stackPane.setPrefWidth(Properties.loadSize("forward-following-width"));
            userList.getChildren().add(hBox);
            hBox.setPadding(new Insets(Properties.loadSize("small-indent")));
            Label name = new Label(following);
            stackPane.getChildren().add(name);
            StackPane.setAlignment(name, Pos.CENTER_LEFT);
            final boolean[] inList = {false};
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
                        toUsers.add(following);
                    else
                        Filter.delFind(toUsers, following);
                }
            });
        }

        type = new Label(Properties.loadDialog("groups"));

        typeBox = new HBox(type);
        typeBox.setId("down-line-black");
        typeBox.setAlignment(Pos.CENTER);
        userList.getChildren().add(typeBox);
        ArrayList<String> toGroups = new ArrayList<>();
        for (String group : groupMessageResponse.getGroups()) {
            StackPane stackPane = new StackPane();
            HBox hBox = new HBox(stackPane);
            stackPane.setPrefWidth(Properties.loadSize("forward-following-width"));
            userList.getChildren().add(hBox);
            hBox.setPadding(new Insets(Properties.loadSize("small-indent")));
            Label name = new Label(group);
            stackPane.getChildren().add(name);
            StackPane.setAlignment(name, Pos.CENTER_LEFT);
            final boolean[] inList = {false};
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
                        toGroups.add(group);
                    else
                        toGroups.remove(group);

                }
            });
        }

        type = new Label(Properties.loadDialog("conv-and-groups"));

        typeBox = new HBox(type);
        typeBox.setId("down-line-black");
        typeBox.setAlignment(Pos.CENTER);
        userList.getChildren().add(typeBox);
        ArrayList<Integer> toConv = new ArrayList<>();
        for (int i = 0; i < groupMessageResponse.getConversations().size(); i++) {
            String conv = groupMessageResponse.getConversations().get(i);
            StackPane stackPane = new StackPane();
            HBox hBox = new HBox(stackPane);
            stackPane.setPrefWidth(Properties.loadSize("forward-following-width"));
            userList.getChildren().add(hBox);
            hBox.setPadding(new Insets(Properties.loadSize("small-indent")));
            Label name = new Label(conv);
            stackPane.getChildren().add(name);
            StackPane.setAlignment(name, Pos.CENTER_LEFT);
            final boolean[] inList = {false};
            ImageView check = ImageHandler.getImage("check-" + inList[0]);
            stackPane.getChildren().add(check);
            StackPane.setAlignment(check, Pos.CENTER_RIGHT);
            hBox.setId("down-line-grey");
            int finalI = i;
            check.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    inList[0] = !inList[0];
                    check.setImage(ImageHandler.getImage("check-" + inList[0]).getImage());

                    if (inList[0])
                        toConv.add(groupMessageResponse.getGroupsId().get(finalI));
                    else
                        toConv.remove(groupMessageResponse.getGroupsId().get(finalI));
                }
            });
        }
        addUsers.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                WhitePane.setVisible(false);
                vBox.setVisible(false);
                listener.sendMessageGroup(new SendGroupMessageEvent(toUsers, toGroups, toConv,
                        messageArea.getText(), null));
            }
        });
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPadding(new Insets(0));
        userList.setPrefWidth(Properties.loadSize("forward-user-list-width"));

    }
}
