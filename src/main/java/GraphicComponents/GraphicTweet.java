package GraphicComponents;

import Applications.TimeLine.Controllers.CommentController;
import FileHandling.ImageHandler;
import FileHandling.Properties;
import GraphicEvents.TweetEvents.ProfilePicClicked;
import GraphicListeners.TweetListener;
import Holder.IDHolder;
import Holder.UsernameHolder;
import Models.Events.*;
import Models.HyperLinkText;
import Models.Responses.GroupMessageResponse;
import Models.Responses.TweetResponse;
import Utils.AutoUpdatingView;
import Utils.Convertors;
import Utils.Filter;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;

public class GraphicTweet {
    public static VBox tweet(TweetResponse tweet, StackPane stackPane, int widthSize, boolean isReTweeted,
                             AutoUpdatingView autoUpdatingView) {
        TweetListener listener = new TweetListener(tweet, autoUpdatingView);
        VBox vBox = new VBox(Properties.loadSize("big-spacing"));
        vBox.setPrefWidth(Properties.loadSize("tweet-width"));
        Label user = new Label("@" + tweet.getSmallUserResponse().getUsername());
        user.setAlignment(Pos.BASELINE_LEFT);
        Circle circle = new Circle(0, 0, Properties.loadSize("small-profile-radius"));
        ImageView profilePic = new ImageView(ImageHandler.getUserProfileImage(tweet.getSmallUserResponse().getImage()).getImage());
        circle.setFill(new ImagePattern(profilePic.getImage()));
        DropShadow dropShadow = new DropShadow(Properties.loadSize("tiny-shadow"), Color.BLACK);
        circle.setEffect(dropShadow);

        circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ProfilePicClicked profilePicClicked = new ProfilePicClicked(tweet.getSmallUserResponse());
                listener.profilePicClicked(profilePicClicked);
            }
        });

        HBox userBox = new HBox(Properties.loadSize("small-spacing"),circle , user);
        userBox.setAlignment(Pos.CENTER_LEFT);
        vBox.getChildren().add(userBox);
        vBox.setId("white");
        TextFlow content = addHyperLinksToTweets(tweet.getContent(), listener);
        content.setPadding(new Insets(Properties.loadSize("small-indent")));
        content.setStyle("-fx-wrap-text: true");
        vBox.getChildren().add(content);
        if (tweet.getImage() != null) {
            Image image = Convertors.load(tweet.getImage());
            ImagePattern pattern = new ImagePattern(
                    image, 0, 0, widthSize, image.getHeight() * widthSize / image.getWidth() , false
            );
            Rectangle rectangle = new Rectangle(0, 0, widthSize, image.getHeight() * widthSize / image.getWidth());
            rectangle.setArcWidth(Properties.loadSize("tweet-image-radius"));
            rectangle.setArcHeight(Properties.loadSize("tweet-image-radius"));
            rectangle.setFill(pattern);
            rectangle.setEffect(new DropShadow(Properties.loadSize("medium-shadow"), Color.BLACK));  // Shadow
            HBox hp = new HBox(rectangle);
            hp.setAlignment(Pos.CENTER);
            hp.setPadding(new Insets(Properties.loadSize("big-indent")));
            vBox.getChildren().add(hp);
        }
        vBox.setPadding(new Insets(Properties.loadSize("big-indent")));
        if (isReTweeted) {
            vBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    BorderPane whitePane = new BorderPane();
                    whitePane.setId("white-fade");
                    stackPane.getChildren().add(whitePane);
                    VBox tmp = tweet(tweet, stackPane, widthSize, false, autoUpdatingView);
                    tmp.setPrefWidth(Properties.loadSize("retweet-width"));
                    ScrollPane scrollPane = new ScrollPane(tmp);
                    VBox position = new VBox(scrollPane);
                    position.setAlignment(Pos.CENTER);
                    position.setPrefWidth(Properties.loadSize("retweet-width"));
                    HBox hPosition = new HBox(position);
                    hPosition.setAlignment(Pos.CENTER);
                    ImageView x_icon = ImageHandler.getImage("x_icon");
                    stackPane.getChildren().add(hPosition);
                    stackPane.getChildren().add(x_icon);
                    StackPane.setAlignment(hPosition, Pos.CENTER_RIGHT);
                    StackPane.setAlignment(x_icon, Pos.TOP_RIGHT);
                    x_icon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            whitePane.setVisible(false);
                            hPosition.setVisible(false);
                            x_icon.setVisible(false);
                        }
                    });
                    scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
                    scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
                }
            });
            return vBox;


        }
        if (tweet.getRetweeted() != null) {
            System.out.println(tweet.getId());
            VBox tmp = tweet(tweet.getRetweeted(), stackPane, (int)(widthSize/1.1), true, autoUpdatingView);
            tmp.setId("grey-round-border");
            vBox.getChildren().add(tmp);
        }
        ImageView like = ImageHandler.getImage("like");
        final boolean[] isLiked = {Filter.boolFind(tweet.getLikes(), IDHolder.Id)};
        ImageView Comment = ImageHandler.getImage("comment");
        ImageView forward = ImageHandler.getImage("forward");
        ImageView retweet =ImageHandler.getImage("retweet");
        ImageView report = ImageHandler.getImage("report");
        HBox toolBar;
        GraphicButtons graphicButtons = new GraphicButtons(autoUpdatingView);
        if (!UsernameHolder.username.equals(tweet.getSmallUserResponse().getUsername()))
            toolBar = new HBox(Properties.loadSize("big-spacing"), like, Comment, retweet, forward,
                    graphicButtons.blockButton(tweet.getSmallUserResponse()),
                    graphicButtons.muteButton(tweet.getSmallUserResponse()), report);
        else
            toolBar = new HBox(Properties.loadSize("big-spacing"), like, Comment, retweet, forward);
        forward.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                listener.setForwardView(new SetForwardViewEvent(listener, stackPane, tweet));
            }
        });

        report.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                listener.reportButtonPressed(new ReportTweetEvent(tweet.getId()));
            }
        });

        retweet.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                TextArea retweetArea = new TextArea();
                ImageView retweetButton = ImageHandler.getImage("hagh");
                BorderPane whitePane = new BorderPane();
                whitePane.setId("white-fade");
                stackPane.getChildren().add(whitePane);
                HBox retweetBox = new HBox(Properties.loadSize("medium-spacing"), retweetArea, retweetButton);
                retweetBox.setPrefWidth(Properties.loadSize("quote-retweet-box-width"));
                retweetBox.setAlignment(Pos.CENTER);
                VBox vBox = new VBox(retweetBox);
                vBox.setPrefHeight(Properties.loadSize("quote-retweet-box-height"));
                vBox.setId("white-box");
                vBox.setPadding(new Insets(Properties.loadSize("big-spacing")));
                VBox vBox1 = new VBox(vBox);
                vBox1.setAlignment(Pos.CENTER);
                HBox hBox1 = new HBox(vBox1);
                hBox1.setAlignment(Pos.CENTER);
                stackPane.getChildren().add(hBox1);
                StackPane.setAlignment(hBox1, Pos.CENTER);
                retweetButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        listener.retweetButtonClicked(new SendRetweetEvent(retweetArea.getText(), tweet.getId()));
                        stackPane.getChildren().remove(hBox1);
                        stackPane.getChildren().remove(whitePane);
                    }
                });
            }
        });
        toolBar.setAlignment(Pos.CENTER_LEFT);

        vBox.getChildren().add(toolBar);
        final Integer[] tmp = {tweet.getLikes().size()};
        Label likeCnt = new Label(tmp[0].toString());
        HBox likeCounts = new HBox(Properties.loadSize("tiny-spacing") ,likeCnt, new Label(Properties.loadDialog("likes")));
        HBox counts = new HBox(likeCounts);
        vBox.getChildren().add(counts);
        vBox.setUserData(tweet.getId());
        if (isLiked[0])
            like.setImage(ImageHandler.getImage("unlike").getImage());
        else
            like.setImage(ImageHandler.getImage("like").getImage());
        like.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                listener.likeButtonClicked(new LikeButtonEvent(tweet.getId()));
            }
        });
        Comment.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                autoUpdatingView.hide();
                new CommentController(tweet.getId(), autoUpdatingView);
            }
        });
        return vBox;
    }

    public static VBox tweets(ArrayList<TweetResponse> Tweets, StackPane stackPane, VBox tweets,
                              AutoUpdatingView autoUpdatingView) {
        for (TweetResponse tweet : Tweets) {
            VBox tweetBox = tweet(tweet, stackPane, Properties.loadSize("normal-tweet-pic-size"), false,
                    autoUpdatingView);
            tweetBox.setId("white-down-line-grey");
            tweets.getChildren().add(tweetBox);
        }
        return tweets;
    }

    public static void singleTweet(StackPane stackPane, int widthSize, AutoUpdatingView autoUpdatingView, TweetResponse tweet) {
        BorderPane whitePane = new BorderPane();
        whitePane.setId("white-fade");
        stackPane.getChildren().add(whitePane);
        VBox tmp = tweet(tweet, stackPane, widthSize, false, autoUpdatingView);
        tmp.setPrefWidth(Properties.loadSize("retweet-width"));
        ScrollPane scrollPane = new ScrollPane(tmp);
        VBox position = new VBox(scrollPane);
        position.setAlignment(Pos.CENTER);
        position.setPrefWidth(Properties.loadSize("retweet-width"));
        HBox hPosition = new HBox(position);
        hPosition.setAlignment(Pos.CENTER);
        ImageView x_icon = ImageHandler.getImage("x_icon");
        stackPane.getChildren().add(hPosition);
        stackPane.getChildren().add(x_icon);
        StackPane.setAlignment(hPosition, Pos.CENTER_RIGHT);
        StackPane.setAlignment(x_icon, Pos.TOP_RIGHT);
        x_icon.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                whitePane.setVisible(false);
                hPosition.setVisible(false);
                x_icon.setVisible(false);
            }
        });
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
    }
    public static void setForward(StackPane Pane, TweetResponse tweet, TweetListener listener, GroupMessageResponse groupMessageResponse) {
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
        HBox addUsersBox = new HBox(Properties.loadSize("medium-spacing"), addUsers);
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
                    groupMessageResponse.getGroupsId().get(finalI);
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
                listener.sendGroupTweetMessage(new SendMessageGroupTweetEvent(toUsers, toGroups, toConv, tweet.getId()));
            }
        });
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPadding(new Insets(0));
        userList.setPrefWidth(Properties.loadSize("forward-user-list-width"));

    }

    private static TextFlow addHyperLinksToTweets(String Tweet, TweetListener listener) {
        TextFlow textFlow = new TextFlow();
        int lastInd = 0;
        for (int i = 0; i < Tweet.length(); i++) {
            if (Tweet.charAt(i) == '@') {
                textFlow.getChildren().add(new Text(Tweet.substring(lastInd, i)));
                String hyperString = "";
                int j = i+1;
                while(j != Tweet.length() && isAlphaNumber(Tweet.charAt(j))) {
                    hyperString += Tweet.charAt(j);
                    j++;
                }
                Hyperlink hyperlink = new Hyperlink(hyperString);
                String finalHyperString = hyperString;
                hyperlink.setOnAction(event -> {
                    listener.hyperLinkClicked(new HyperLinkEvent(finalHyperString, HyperLinkText.Tweet));
                });
                textFlow.getChildren().add(hyperlink);
                lastInd = j;
                i = j - 1;
            }
        }
        textFlow.getChildren().add(new Text(Tweet.substring(lastInd)));
        return textFlow;
    }

    private static boolean isAlphaNumber(char ch) {
        if ('a' <= ch && ch <= 'z')
            return true;
        if ('A' <= ch && ch <= 'Z')
            return true;
        if ('0' <= ch && ch <= '9')
            return true;
        return false;
    }
}
