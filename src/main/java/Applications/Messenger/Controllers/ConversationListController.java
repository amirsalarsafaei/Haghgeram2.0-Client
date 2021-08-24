package Applications.Messenger.Controllers;

import Applications.Messenger.Listeners.ConversationListListener;
import Applications.Messenger.Views.ConversationListView;
import DataBase.OfflineConversationList;
import FileHandling.Properties;
import Holder.TokenHolder;
import Models.Events.AddMembersToGroupEvent;
import Models.Events.AddMembersToGroupSendableEvent;
import Models.Events.ShowConversationEvent;
import Models.Networking.Request;
import Models.Networking.RequestType;
import Models.Networking.Response;
import Models.Responses.ConversationListResponse;
import Models.Responses.ConversationResponse;
import Models.Responses.GroupMessageResponse;
import StreamHandler.StreamHandler;
import Utils.AutoUpdatingController;
import Utils.GsonHandler;
import javafx.concurrent.Task;
import javafx.event.EventHandler;

import static java.lang.Thread.sleep;

public class ConversationListController implements AutoUpdatingController {
    private boolean done = false;
    private Response updateResponse, groupMessageResponse, showConversationResponse;
    private Request updateRequest;
    private ConversationListView view;
    private ConversationController conversationController;
    public ConversationListController() {
        updateRequest = new Request(RequestType.ConversationList, "", TokenHolder.token);
        view = new ConversationListView(new ConversationListListener(this));
        getData(true);
    }

    @Override
    public void getData(boolean repeat) {
        if (!StreamHandler.isConnected()) {
            ConversationListResponse listResponse = OfflineConversationList.loadLast();
            if (listResponse != null)
                show(listResponse, false);
            return;
        }
        if (done)
            return;
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                updateResponse = StreamHandler.sendReqGetResp(updateRequest);
                return null;
            }
        };
        task.setOnSucceeded(event -> show(GsonHandler.getGson().fromJson(updateResponse.data, ConversationListResponse.class),
                repeat));
        Thread thread = new Thread(task);
        thread.start();
    }

    public void show(ConversationListResponse conversations, boolean repeat) {
        OfflineConversationList.Save(conversations);
        if (done)
            return;
        view.refresh(conversations);
        if (repeat) {
            Task task = new Task() {
                @Override
                protected Object call() throws Exception {
                    sleep(Properties.loadNumbers("refresh"));
                    return null;
                }
            };
            task.setOnSucceeded(event -> getData(repeat));
            Thread thread = new Thread(task);
            thread.start();
        }
    }

    public void showGroupMessageView() {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                groupMessageResponse = StreamHandler.sendReqGetResp(new Request(RequestType.GroupMessageList, "" ,
                        TokenHolder.token));
                return null;
            }
        };
        task.setOnSucceeded(event -> new GroupMessageController(view.getStackPane(),
                GsonHandler.getGson().fromJson(groupMessageResponse.data, GroupMessageResponse.class)));
        Thread thread = new Thread(task);
        thread.start();
    }


    public void showConversation(ShowConversationEvent e, boolean isGroup) {
        view.getConversationVBox().getChildren().clear();
        conversationController = new ConversationController(this, e, view.getConversationVBox(),
                view.getStackPane(), isGroup);
    }


    public void showConversation(ConversationResponse conversationResponse) {
        view.getConversationVBox().getChildren().clear();
        conversationController = new ConversationController(this,conversationResponse, view.getConversationVBox(), view.getStackPane());
    }
    @Override
    public void setDoneTrue() {
        done = true;
    }

    @Override
    public void setDoneFalse() {
        done = false;
    }


    public void stopSide() {
        if (conversationController != null)
            conversationController.stop();
    }

    public void showAddGroupView() {
        if (!StreamHandler.isConnected()) {
            return;
        }
        EventHandler<AddMembersToGroupEvent> eventHandler = new EventHandler<AddMembersToGroupEvent>() {
            @Override
            public void handle(AddMembersToGroupEvent event) {
                Task task = new Task() {
                    @Override
                    protected Object call() throws Exception {
                        StreamHandler.sendReqGetResp(new Request(RequestType.CreateGroup,
                                GsonHandler.getGson().toJson(new AddMembersToGroupSendableEvent(event)), TokenHolder.token));
                        return null;
                    }
                };
                task.setOnSucceeded(event1 -> getData(false));
                Thread thread = new Thread(task);
                thread.start();

            }
        };
        new AddMembersToGroupController(view.getStackPane(), eventHandler, true);
    }
}
