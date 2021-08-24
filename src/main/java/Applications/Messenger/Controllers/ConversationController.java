package Applications.Messenger.Controllers;

import Applications.Messenger.Listeners.ConversationListener;
import Applications.Messenger.Views.ConversationView;
import DataBase.OfflineConversation;
import FileHandling.ImageHandler;
import FileHandling.Properties;
import GraphicComponents.GraphicTweet;
import Holder.TokenHolder;
import Models.Events.*;
import Models.Networking.Request;
import Models.Networking.RequestType;
import Models.Networking.Response;
import Models.Networking.ResponseType;
import Models.OfflineRequests;
import Models.Responses.ConversationResponse;
import Models.Responses.HyperLinkResponse;
import Models.Responses.MessageResponse;
import Models.UnsentMessages;
import StreamHandler.StreamHandler;
import Utils.AutoUpdatingController;
import Utils.GsonHandler;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import static java.lang.Thread.sleep;

public class ConversationController implements AutoUpdatingController {
    private ConversationListController conversationListController;
    private ShowConversationEvent e;
    private Response updateResponse, hyperLinkResponse;
    private Request updateRequest;
    private ConversationView view;
    private boolean done = false;
    private int id;
    public ConversationController(ConversationListController conversationListController, ShowConversationEvent e,
                                  BorderPane conversationBox, StackPane stackPane, boolean isGroup) {
        this.conversationListController = conversationListController;
        this.e = e;
        id = e.getId();
        this.updateRequest = new Request(RequestType.Conversation, GsonHandler.getGson().toJson(e), TokenHolder.token);
        this.view = new ConversationView(conversationBox, new ConversationListener(this), stackPane, e.getId(), isGroup);
        getData(true);
    }

    public ConversationController(ConversationListController conversationListController,
                                  ConversationResponse conversationResponse, BorderPane conversationVBox, StackPane stackPane) {
        this.conversationListController = conversationListController;
        this.e = new ShowConversationEvent(conversationResponse.getId());
        id = e.getId();
        this.updateRequest = new Request(RequestType.Conversation, GsonHandler.getGson().toJson(e), TokenHolder.token);
        this.view = new ConversationView(conversationVBox, new ConversationListener(this), stackPane, e.getId(),
                conversationResponse.isGroup());
        getData(true);
    }


    @Override
    public void getData(boolean repeat) {
        if (!StreamHandler.isConnected()) {
            ConversationResponse conversationResponse = OfflineConversation.loadLast(id);
            if (conversationResponse != null) {
                for (SendMessageToConversationEvent event: UnsentMessages.holder.getSendMessageToConversationEvents()) {
                    if (event.getId() == id) {
                        conversationResponse.getMessages().add(new MessageResponse(event));
                    }
                }
                show(false, conversationResponse);
            }
            return;
        }
        if (done)
            return;
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                updateResponse = StreamHandler.sendReqGetResp(updateRequest);
                System.out.println(updateResponse.data);
                return null;
            }
        };
        task.setOnSucceeded(event -> {
            show(repeat, GsonHandler.getGson().fromJson(updateResponse.data, ConversationResponse.class));
        });
        Thread thread = new Thread(task);
        thread.start();
    }


    public void show(boolean repeat, ConversationResponse conversationResponse) {
        OfflineConversation.Save(conversationResponse);
        if (done)
            return;
        view.refresh(conversationResponse);
        if (repeat) {
            Task task = new Task() {
                @Override
                protected Object call() throws Exception {
                    sleep(Properties.loadNumbers("refresh"));
                    return null;
                }
            };
            task.setOnSucceeded(event -> getData(true));
            Thread thread = new Thread(task);
            thread.start();
        }
    }
    @Override
    public void setDoneTrue() {
        done = true;
    }

    @Override
    public void setDoneFalse() {
        done = false;
    }
    public void refreshSide() {
        conversationListController.getData(false);
    }

    public void sendMessage(SendMessageToConversationEvent e) {
        Request request = new Request(RequestType.SendMessageToConversation,
                GsonHandler.getGson().toJson(e), TokenHolder.token);
        if (!StreamHandler.isConnected()) {
            OfflineRequests.offlineRequests.addRequest(request);
            UnsentMessages.holder.addMessage(e);
            getData(false);
            return;
        }
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                StreamHandler.sendReqGetResp(request);
                return null;
            }
        };
        task.setOnSucceeded(event -> refresh());
        Thread thread = new Thread(task);
        thread.start();
    }

    public void refresh() {
        getData(false);
        refreshSide();
    }


    public void hyperLinkAction(HyperLinkEvent e) {
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
                        GraphicTweet.singleTweet(view.getStackPane(), 800, view, response.getTweetResponse());
                        break;
                    case Bot:
                        break;
                    case Conversation:
                        stop();
                        conversationListController.showConversation(response.getConversationResponse());
                        break;
                    case GroupInvite:
                        break;
                }
            }
            else  {
                StackPane stackPane = view.getStackPane();
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

    public void addMemberToGroup(AddMemberToGroupViewEvent addMemberToGroupViewEvent) {
        EventHandler<AddMembersToGroupEvent> eventHandler = new EventHandler<AddMembersToGroupEvent>() {
            @Override
            public void handle(AddMembersToGroupEvent event) {
                Task task = new Task() {
                    @Override
                    protected Object call() throws Exception {
                        event.setGroupId(addMemberToGroupViewEvent.getGroup_id());
                        StreamHandler.sendReqGetResp(
                                new Request(RequestType.AddMemberToGroup,
                                        GsonHandler.getGson().toJson(new AddMembersToGroupSendableEvent(event)),
                                        TokenHolder.token));
                        return null;
                    }
                };
                task.setOnSucceeded((e) -> refresh());
                Thread thread = new Thread(task);
                thread.start();
            }

        };
        new AddMembersToGroupController(view.getStackPane(), eventHandler, false);
    }


    public void leaveGroup(LeaveGroupEvent leaveGroupEvent) {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                StreamHandler.sendReqGetResp(
                        new Request(RequestType.LeaveGroup, GsonHandler.getGson().toJson(leaveGroupEvent),
                                TokenHolder.token));
                return null;
            }
        };
        task.setOnSucceeded(event -> {
            new ConversationListController();
        });
        Thread thread = new Thread(task);
        thread.start();
    }

    public void deleteMessage(DeleteMessageEvent deleteMessageEvent) {
        final Response[] response = new Response[1];
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                response[0] = StreamHandler.sendReqGetResp(new Request(RequestType.DeleteMessage,
                        GsonHandler.getGson().toJson(deleteMessageEvent), TokenHolder.token));
                return null;
            }
        };
        task.setOnSucceeded(event -> refresh());
        Thread thread = new Thread(task);
        thread.start();
    }

    public void editMessage(EditMessageEvent editMessageEvent) {
        final Response[] response = new Response[1];
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                response[0] = StreamHandler.sendReqGetResp(new Request(RequestType.EditMessage,
                        GsonHandler.getGson().toJson(editMessageEvent), TokenHolder.token));
                return null;
            }
        };
        task.setOnSucceeded(event ->refresh());
        Thread thread = new Thread(task);
        thread.start();
    }
}
