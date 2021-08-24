package GraphicController;

import FileHandling.ImageHandler;
import FileHandling.Properties;
import GraphicComponents.GraphicTweet;
import GraphicEvents.TweetEvents.ProfilePicClicked;
import Holder.TokenHolder;
import Models.Events.*;
import Models.Networking.Request;
import Models.Networking.RequestType;
import Models.Networking.Response;
import Models.Networking.ResponseType;
import Models.Responses.GroupMessageResponse;
import Models.Responses.HyperLinkResponse;
import StreamHandler.StreamHandler;
import Utils.AutoUpdatingView;
import Utils.GraphicAgent;
import Utils.GsonHandler;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class TweetController {
    AutoUpdatingView autoUpdatingView;
    private Response hyperLinkResponse;
    public TweetController(AutoUpdatingView autoUpdatingView) {
        this.autoUpdatingView = autoUpdatingView;
    }
    public void profilePicClicked(ProfilePicClicked e) {
        if (!StreamHandler.isConnected())
            return;

    }

    public void sendRetweet(SendRetweetEvent e) {
        if (!StreamHandler.isConnected())
            return;
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                StreamHandler.sendReqGetResp(new Request(RequestType.SendRetweet, GsonHandler.getGson().toJson(e),
                        TokenHolder.token));
                autoUpdatingView.refreshNow();
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();


    }

    public void likeTweet(LikeButtonEvent e) {
        if (!StreamHandler.isConnected())
            return;
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                StreamHandler.sendReqGetResp(new Request(RequestType.LikeTweet, GsonHandler.getGson().toJson(e),
                        TokenHolder.token));
                autoUpdatingView.refreshNow();
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();

    }

    public void hyperLink(HyperLinkEvent e) {
        if (!StreamHandler.isConnected())
            return;
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                hyperLinkResponse = StreamHandler.sendReqGetResp(
                        new Request(RequestType.HyperLink, GsonHandler.getGson().toJson(e), TokenHolder.token));
                return null;
            }
        };
        task.setOnSucceeded(event -> {
            if (hyperLinkResponse.responseType == ResponseType.Accepted) {
                HyperLinkResponse response = GsonHandler.getGson().fromJson(hyperLinkResponse.data, HyperLinkResponse.class);
                switch (response.getHyperLinkType()) {
                    case Tweet:
                        GraphicTweet.singleTweet(GraphicAgent.mainPane, Properties.loadSize("show-tweet-width"), autoUpdatingView, response.getTweetResponse());
                        break;
                    case Bot:
                        break;
                    case Conversation:
                        break;
                    case GroupInvite:
                        break;
                }
            }
            else  {
                StackPane stackPane = GraphicAgent.mainPane;
                BorderPane whitePane = new BorderPane();
                whitePane.setId("white-fade");
                stackPane.getChildren().add(whitePane);
                Pane pane = new Pane(new VBox(new Label(hyperLinkResponse.data)));
                pane.setPadding(new Insets(Properties.loadSize("medium-indent")));
                VBox vBox = new VBox(pane);
                vBox.setAlignment(Pos.CENTER);
                HBox hBox = new HBox(pane);
                hBox.setAlignment(Pos.CENTER);
                stackPane.getChildren().add(hBox);
                ImageView x_icon = ImageHandler.getImage("x_icon");
                stackPane.getChildren().add(x_icon);
                StackPane.setAlignment(hBox, Pos.CENTER);
                StackPane.setAlignment(x_icon, Pos.TOP_RIGHT);
                x_icon.setOnMouseClicked(event1 -> {
                    whitePane.setVisible(false);
                    hBox.setVisible(false);
                    x_icon.setVisible(false);
                });
            }
        });
        Thread thread = new Thread(task);
        thread.start();
    }

    public void sendGroupTweet(SendMessageGroupTweetEvent sendMessageGroupTweetEvent) {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                StreamHandler.sendReqGetResp(new Request(RequestType.SendTweetGroupMessage,
                        GsonHandler.getGson().toJson(sendMessageGroupTweetEvent), TokenHolder.token));
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    public void setForwardView(SetForwardViewEvent setForwardViewEvent) {
        final Response[] groupListResponse = new Response[1];
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                groupListResponse[0] = StreamHandler.sendReqGetResp(new Request(RequestType.GroupMessageList, "", TokenHolder.token));
                return null;
            }
        };task.setOnSucceeded(event->GraphicTweet.setForward(setForwardViewEvent.getStackPane(), setForwardViewEvent.getTweet()
                ,setForwardViewEvent.getListener(), GsonHandler.getGson().fromJson(groupListResponse[0].data, GroupMessageResponse.class)));
        Thread thread = new Thread(task);
        thread.start();
    }

    public void reportTweet(ReportTweetEvent reportTweetEvent) {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                StreamHandler.sendReqGetResp(new Request(RequestType.ReportTweet,
                        GsonHandler.getGson().toJson(reportTweetEvent), TokenHolder.token));
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }
}
