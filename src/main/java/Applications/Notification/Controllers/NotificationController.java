package Applications.Notification.Controllers;

import Applications.Notification.Listeners.NotificationListener;
import Applications.Notification.Views.NotificationView;
import FileHandling.Properties;
import Holder.TokenHolder;
import Models.Events.AcceptFollowRequestEvent;
import Models.Events.DeniedFollowRequestEvent;
import Models.Networking.Request;
import Models.Networking.RequestType;
import Models.Networking.Response;
import Models.Responses.NotificationsResponse;
import StreamHandler.StreamHandler;
import Utils.AutoUpdatingController;
import Utils.GsonHandler;
import javafx.concurrent.Task;

import static java.lang.Thread.sleep;

public class NotificationController implements AutoUpdatingController {
    private Request updateRequest;
    private Response updateResponse;
    private NotificationView view;
    private boolean done = false;
    public NotificationController() {
        if (!StreamHandler.isConnected())
            return;
        updateRequest = new Request(RequestType.Notifications, "", TokenHolder.token);
        view = new NotificationView(new NotificationListener(this));
        getData(true);
    }




    @Override
    public void getData(boolean repeat) {
        if (!StreamHandler.isConnected())
            return;
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
        task.setOnSucceeded(event -> show(repeat,
                GsonHandler.getGson().fromJson(updateResponse.data, NotificationsResponse.class)));
        Thread thread = new Thread(task);
        thread.start();
    }

    public void show(boolean repeat, NotificationsResponse response) {
        if (!StreamHandler.isConnected())
            return;
        view.refresh(response);
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

    public void acceptRequest(AcceptFollowRequestEvent event) {
        if (!StreamHandler.isConnected())
            return;
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                StreamHandler.sendReqGetResp(new Request(RequestType.AcceptRequest,
                        GsonHandler.getGson().toJson(event), TokenHolder.token));
                return null;
            }
        };task.setOnSucceeded(event1 -> getData(false));
        Thread thread = new Thread(task);
        thread.start();
    }


    public void rejectRequest(DeniedFollowRequestEvent event) {
        if (!StreamHandler.isConnected())
            return;
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                StreamHandler.sendReqGetResp(new Request(RequestType.DenyRequest,
                        GsonHandler.getGson().toJson(event), TokenHolder.token));
                return null;
            }
        };task.setOnSucceeded(event1 -> getData(false));
        Thread thread = new Thread(task);
        thread.start();
    }
}
