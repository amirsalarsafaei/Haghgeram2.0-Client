package Applications.Messenger.Controllers;

import Applications.Messenger.Listeners.GroupMessageListener;
import Applications.Messenger.Views.GroupMessageView;
import Holder.TokenHolder;
import Models.Events.SendGroupMessageEvent;
import Models.Networking.Request;
import Models.Networking.RequestType;
import Models.Responses.GroupMessageResponse;
import StreamHandler.StreamHandler;
import Utils.GsonHandler;
import javafx.concurrent.Task;
import javafx.scene.layout.StackPane;

public class GroupMessageController {


    public GroupMessageController(StackPane stackPane, GroupMessageResponse groupMessageResponse)  {
        new GroupMessageView(stackPane, groupMessageResponse, new GroupMessageListener(this));
    }

    public void sendGroupMessage(SendGroupMessageEvent e) {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                StreamHandler.sendReqGetResp(new Request(RequestType.SendGroupMessage, GsonHandler.getGson().toJson(e)
                        , TokenHolder.token));
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }
}
