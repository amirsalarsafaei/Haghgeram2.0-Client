package Applications.Messenger.Controllers;

import Applications.Messenger.Listeners.AddMembersToGroupListener;
import Applications.Messenger.Views.AddMembersToGroupView;
import Holder.TokenHolder;
import Models.Events.AddMembersToGroupEvent;
import Models.Networking.Request;
import Models.Networking.RequestType;
import Models.Networking.Response;
import Models.Responses.FollowingsResponse;
import StreamHandler.StreamHandler;
import Utils.GsonHandler;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.layout.StackPane;
public class AddMembersToGroupController {

    private EventHandler<AddMembersToGroupEvent> eventHandler;
    private AddMembersToGroupView addMembersToGroupView;
    private Response response;
    public AddMembersToGroupController(StackPane stackPane, EventHandler<AddMembersToGroupEvent> eventHandler,
                                       boolean addGroupName) {
        this.eventHandler = eventHandler;
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                response = StreamHandler.sendReqGetResp(new Request(RequestType.Followings, "", TokenHolder.token));
                return null;
            }
        };
        task.setOnSucceeded(event -> {
            addMembersToGroupView = new AddMembersToGroupView(stackPane, GsonHandler.getGson().fromJson(response.data,
                FollowingsResponse.class), new AddMembersToGroupListener(this), addGroupName);
        });
        Thread thread = new Thread(task);
        thread.start();
    }

    public void addUsersToGroup(AddMembersToGroupEvent e) {
        eventHandler.handle(e);
    }
}
