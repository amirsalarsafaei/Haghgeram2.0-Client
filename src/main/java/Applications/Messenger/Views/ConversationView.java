package Applications.Messenger.Views;

import Applications.Messenger.Listeners.ConversationListener;
import FileHandling.ImageHandler;
import FileHandling.Properties;
import GraphicComponents.GraphicComponents;
import GraphicComponents.GraphicTweet;
import Holder.UsernameHolder;
import Models.Events.*;
import Models.HyperLinkText;
import Models.Responses.ConversationResponse;
import Models.Responses.MessageResponse;
import Utils.AutoUpdatingView;
import Utils.Convertors;
import Utils.GraphicAgent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ConversationView implements AutoUpdatingView {
    private final BorderPane conversationBox;
    private final VBox messages;
    private final ConversationListener listener;
    private final StackPane stackPane;
    private ConversationResponse response;
    private int id;
    private ScrollPane scrollPane;
    private boolean first = false;
    private Scene scene;
    public ConversationView(BorderPane conversationBox, ConversationListener listener, StackPane stackPane,
                            int id, boolean group) {
        this.id = id;
        this.listener = listener;
        this.stackPane = stackPane;
        this.conversationBox = conversationBox;
        conversationBox.setId("black-box");
        ImageView leaveGroup = ImageHandler.getImage("leave");
        ImageView addMemberToGroup = ImageHandler.getImage("add-group-chat");
        HBox toolBoxHBox = new HBox(Properties.loadSize("medium-indent"), leaveGroup, addMemberToGroup);
        toolBoxHBox.setAlignment(Pos.CENTER);
        toolBoxHBox.setPadding(new Insets(Properties.loadSize("small-indent")));
        VBox toolboxVBox = new VBox(toolBoxHBox);
        messages = new VBox(Properties.loadSize("tiny-indent"));
        scrollPane = new ScrollPane(messages);
        TextArea textArea = new TextArea();
        textArea.setPrefRowCount(Properties.loadNumbers("message-text-area-row"));
        ImageView sendButton = ImageHandler.getImage("forward");
        ImageView imageButton = ImageHandler.getImage("image");
        ImageView timerButton = ImageHandler.getImage("clock");
        VBox sendButtonBox = new VBox(Properties.loadSize("medium-spacing"), sendButton, imageButton, timerButton);
        sendButtonBox.setAlignment(Pos.CENTER);
        HBox sendMessageBox = new HBox(Properties.loadSize("big-spacing"), textArea, sendButtonBox);
        sendMessageBox.setPadding(new Insets(Properties.loadSize("big-indent")));
        sendMessageBox.setAlignment(Pos.CENTER);
        if (group)
            conversationBox.setTop(toolboxVBox);
        conversationBox.setBottom(sendMessageBox);
        conversationBox.setCenter(scrollPane);
        scrollPane.fitToHeightProperty().setValue(true);
        scrollPane.fitToWidthProperty().setValue(true);
        File[] selectedFile = {null};
        FileChooser fileChooser = new FileChooser();
        final boolean[] hasTimer = {false};
        final LocalDateTime[] localDateTime = {null};
        imageButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                selectedFile[0] = fileChooser.showOpenDialog(GraphicAgent.stage);
            }
        });
        sendButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (response == null)
                    return;
                SendMessageToConversationEvent e;
                if (selectedFile[0] != null)
                    e = new SendMessageToConversationEvent(textArea.getText(), Convertors.imageFileToByte(selectedFile[0]), response.getId());
                else
                    e = new SendMessageToConversationEvent(textArea.getText(), response.getId());
                if (hasTimer[0] && localDateTime[0] != null)
                    e.setLocalDateTime(localDateTime[0]);
                else
                    e.setLocalDateTime(LocalDateTime.now());
                listener.sendMessageButtonPressed(e);
            }
        });

        timerButton.setOnMouseClicked(event -> {
            BorderPane borderPane = new BorderPane();
            borderPane.setId("white-fade");
            stackPane.getChildren().add(borderPane);
            VBox vBox = new VBox(Properties.loadSize("medium-indent"));
            Pane pane = new Pane();
            pane.getChildren().add(vBox);
            vBox.setPadding(new Insets(Properties.loadSize("big-indent")));
            vBox.setId("white-box");
            DatePicker datePicker = new DatePicker(LocalDate.now());
            vBox.getChildren().add(datePicker);
            Slider hourSlider = new Slider(0, Properties.loadNumbers("hours"), LocalDateTime.now().getHour());
            Button submit = new Button("submit");
            hourSlider.setMajorTickUnit(1);

            hourSlider.setMinorTickCount(0);
            hourSlider.setSnapToTicks(true);
            hourSlider.setShowTickMarks(true);
            hourSlider.setShowTickLabels(true);
            Slider minuteSlider = new Slider(0, Properties.loadNumbers("minutes"), LocalDateTime.now().getMinute());

            minuteSlider.setMajorTickUnit(15);
            minuteSlider.setMinorTickCount(2);
            minuteSlider.setSnapToTicks(true);
            minuteSlider.setShowTickMarks(true);
            minuteSlider.setShowTickLabels(true);
            vBox.getChildren().addAll(hourSlider, minuteSlider, submit);
            VBox vBox1 = new VBox(pane);
            HBox hBox = new HBox(vBox1);
            StackPane.setAlignment(hBox, Pos.CENTER);
            stackPane.getChildren().add(hBox);
            hBox.setAlignment(Pos.CENTER);
            vBox1.setAlignment(Pos.CENTER);
            vBox.setAlignment(Pos.CENTER);
            hasTimer[0] = true;
            submit.setOnAction(event1 -> {
                borderPane.setVisible(false);
                hBox.setVisible(false);
                LocalDate tmp = datePicker.getValue();
                localDateTime[0] = LocalDateTime.of(tmp, LocalTime.of((int)hourSlider.getValue(), (int)minuteSlider.getValue()));
                //System.out.println((int)hourSlider.getValue() + " " + (int)minuteSlider.getValue());
            });
        });
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);

        leaveGroup.setOnMouseClicked(event -> {
            listener.leaveGroupButtonPressed(new LeaveGroupEvent(id));
        });
        addMemberToGroup.setOnMouseClicked(event -> {
            listener.addMemberToGroupButtonPressed(new AddMemberToGroupViewEvent(id));
        });
    }

    public void refresh(ConversationResponse conversation) {
        this.response = conversation;
        messages.getChildren().clear();
        LocalDateTime tmp = null;
        if (conversation.getMessages().size() != 0) {
            messages.getChildren().add(GraphicComponents.date(conversation.getMessages().get(0).getTimeSent()));
            tmp = conversation.getMessages().get(0).getTimeSent();
        }
        for (MessageResponse message:conversation.getMessages()) {
            if (tmp.getYear() != message.getTimeSent().getYear() || tmp.getDayOfYear() != message.getTimeSent().getDayOfYear()) {
                messages.getChildren().add(GraphicComponents.date(message.getTimeSent()));
                tmp = message.getTimeSent();
            }

            if (message.getUser().getUsername().equals(UsernameHolder.username)) {
                String hp = message.getTimeSent().getHour() + ":" + message.getTimeSent().getMinute();
                Label time = new Label(hp);
                time.setId("small-text");

                HBox timeAndSeenBox = new HBox(time, new Label(message.getStatus().toString()));
                timeAndSeenBox.setAlignment(Pos.CENTER_RIGHT);
                VBox content = new VBox();
                TextFlow contentText = addHyperLinksToMessages(message.getContent());
                HBox hBox = new HBox(contentText);

                setMessageImage(message, content);
                setMessageTweet(message, content, stackPane);
                content.getChildren().add(hBox);
                contentText.setId("medium-text");
                hBox.setAlignment(Pos.CENTER_LEFT);
                VBox messageBox = new VBox(content, timeAndSeenBox);
                messageBox.setId("message-box-user");
                HBox salar = new HBox(messageBox);
                salar.setAlignment(Pos.CENTER_RIGHT);
                messages.setId("white");
                Circle circle = new Circle(0, 0, Properties.loadSize("tiny-profile-radius"));
                ImageView profilePic = ImageHandler.getUserProfileImage(message.getUser().getImage());
                circle.setFill(new ImagePattern(profilePic.getImage()));
                DropShadow dropShadow = new DropShadow(Properties.loadSize("tiny-shadow"), Color.BLACK);
                circle.setEffect(dropShadow);
                MenuItem edit = new MenuItem("edit");
                MenuItem delete = new MenuItem("delete");
                ContextMenu contextMenu = new ContextMenu(edit, delete);
                salar.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                    @Override
                    public void handle(ContextMenuEvent contextMenuEvent) {
                        contextMenu.show(salar, contextMenuEvent.getSceneX(), contextMenuEvent.getSceneY());
                    }
                });
                delete.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        listener.deleteMessageButtonPressed(new DeleteMessageEvent(message.getId()));
                    }
                });
                edit.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        TextArea messageEditArea = new TextArea();
                        ImageView messageEditButton = ImageHandler.getImage("forward");
                        messageEditArea.setText(message.getContent());
                        BorderPane whitePane = new BorderPane();
                        whitePane.setId("white-fade");
                        stackPane.getChildren().add(whitePane);
                        HBox messageEditBox = new HBox(Properties.loadSize("medium-indent"), messageEditArea, messageEditButton);
                        messageEditBox.setPrefWidth(Properties.loadSize("message-edit-width"));
                        messageEditBox.setAlignment(Pos.CENTER);
                        VBox vBox = new VBox(messageEditBox);
                        vBox.setPrefHeight(Properties.loadSize("message-edit-height"));
                        VBox vBox1 = new VBox(vBox);
                        vBox1.setAlignment(Pos.CENTER);
                        HBox hBox1 = new HBox(vBox1);
                        hBox1.setAlignment(Pos.CENTER);
                        stackPane.getChildren().add(hBox1);
                        StackPane.setAlignment(hBox1, Pos.CENTER);
                        messageEditButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                listener.editMessageButtonPressed(new EditMessageEvent(message.getId(),
                                        messageEditArea.getText()));
                                hBox1.setVisible(false);
                                whitePane.setVisible(false);
                            }
                        });
                    }
                });
                HBox PicAndMessage = new HBox(Properties.loadSize("small-spacing"), salar, circle);
                PicAndMessage.setAlignment(Pos.CENTER_RIGHT);
                messages.getChildren().add(PicAndMessage);
            } else {
                String hp = message.getTimeSent().getHour() + ":" + message.getTimeSent().getMinute();
                Label time = new Label(hp);
                time.setId("small-text");

                HBox timeBox = new HBox(time);
                timeBox.setAlignment(Pos.CENTER_RIGHT);
                TextFlow contentText = addHyperLinksToMessages(message.getContent());
                HBox hBox = new HBox(contentText);
                VBox content = new VBox();
                setMessageImage(message, content);
                setMessageTweet(message, content, stackPane);
                content.getChildren().add(hBox);
                content.setAlignment(Pos.CENTER_LEFT);
                content.setId("medium-text");
                hBox.setAlignment(Pos.CENTER_RIGHT);
                Label userLabel = new Label(message.getUser().getUsername());
                userLabel.setId("message-sender");
                HBox userBox = new HBox(userLabel);
                userBox.setAlignment(Pos.CENTER_LEFT);
                VBox messageBox = new VBox(userBox, content, timeBox);
                messageBox.setId("message-box-target");
                HBox salar = new HBox(messageBox);
                salar.setAlignment(Pos.CENTER_LEFT);
                messages.setId("white");
                Circle circle = new Circle(0, 0, Properties.loadSize("tiny-profile-radius"));
                ImageView profilePic = ImageHandler.getUserProfileImage(message.getUser().getImage());
                circle.setFill(new ImagePattern(profilePic.getImage()));
                DropShadow dropShadow = new DropShadow(Properties.loadSize("small-shadow"), Color.BLACK);
                circle.setEffect(dropShadow);

                HBox PicAndMessage = new HBox(Properties.loadSize("small-indent"), circle, salar);
                PicAndMessage.setAlignment(Pos.CENTER_LEFT);
                messages.getChildren().add(PicAndMessage);

            }
        }
        if (!first) {
            conversationBox.applyCss();
            conversationBox.layout();
            scrollPane.setVvalue(scrollPane.getVmax());
            first = true;
        }
    }

    private void setMessageImage(MessageResponse message, VBox content) {
        if (message.getImage() != null) {
            Image image = Convertors.load(message.getImage());
            int widthSize = Properties.loadSize("message-pic-width");
            ImagePattern pattern = new ImagePattern(
                    image, 0, 0, widthSize, image.getHeight() * widthSize / image.getWidth() , false
            );
            Rectangle rectangle = new Rectangle(0, 0, widthSize, image.getHeight() * widthSize / image.getWidth());
            rectangle.setArcWidth(Properties.loadSize("message-pic-radius"));
            rectangle.setArcHeight(Properties.loadSize("message-pic-radius"));
            rectangle.setFill(pattern);
            rectangle.setEffect(new DropShadow(Properties.loadSize("tiny-shadow"), Color.BLACK));
            HBox hp2 = new HBox(rectangle);
            hp2.setAlignment(Pos.CENTER);
            hp2.setPadding(new Insets(Properties.loadSize("big-indent")));
            content.getChildren().add(hp2);
        }
    }


    private void setMessageTweet(MessageResponse message, VBox content, StackPane stackPane) {
        if (message.getTweet() != null) {
            VBox vBox = new VBox(GraphicTweet.tweet(message.getTweet(), stackPane,
                    Properties.loadSize("message-tweet-width"), false, this));
            vBox.setPadding(new Insets(Properties.loadSize("medium-indent")));
            content.getChildren().add(vBox);
        }
    }

    private TextFlow addHyperLinksToMessages(String message) {
        TextFlow textFlow = new TextFlow();
        int lastInd = 0;
        for (int i = 0; i < message.length(); i++) {
            if (message.charAt(i) == '@') {
                textFlow.getChildren().add(new Text(message.substring(lastInd, i)));
                String hyperString = "";
                int j = i+1;
                while(j != message.length() && isAlphaNumber(message.charAt(j))) {
                    hyperString += message.charAt(j);
                    j++;
                }
                Hyperlink hyperlink = new Hyperlink(hyperString);
                String finalHyperString = hyperString;
                hyperlink.setOnAction(event -> {
                    listener.hyperLinkClicked(new HyperLinkEvent(finalHyperString, HyperLinkText.Message));
                });
                textFlow.getChildren().add(hyperlink);
                lastInd = j;
                i = j - 1;
            }
        }
        textFlow.getChildren().add(new Text(message.substring(lastInd)));
        return textFlow;
    }

    private boolean isAlphaNumber(char ch) {
        if ('a' <= ch && ch <= 'z')
            return true;
        if ('A' <= ch && ch <= 'Z')
            return true;
        if ('0' <= ch && ch <= '9')
            return true;
        return false;
    }

    @Override
    public void hide() {
        listener.stopRefreshing();
    }

    @Override
    public void show() {
        listener.stopRefreshing();
    }

    @Override
    public void refreshNow() {
        listener.refresh();
    }

    public StackPane getStackPane() {
        return stackPane;
    }
}
