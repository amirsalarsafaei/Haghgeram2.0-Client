package Applications.Setting.Controllers;

import Applications.Setting.Listeners.FollowRequestStatusListener;
import Applications.Setting.Views.FollowRequestStatusView;
import FileHandling.Properties;
import Holder.TokenHolder;
import Models.Networking.Request;
import Models.Networking.RequestType;
import Models.Networking.Response;
import Models.Responses.FollowRequestsStatusResponse;
import StreamHandler.StreamHandler;
import Utils.AutoUpdatingController;
import Utils.GsonHandler;
import javafx.concurrent.Task;

import static java.lang.Thread.sleep;

public class FollowRequestStatusController implements AutoUpdatingController {
    private boolean done = false;
    private FollowRequestStatusView view;

    public FollowRequestStatusController() {
        view = new FollowRequestStatusView(new FollowRequestStatusListener(this));
        getData(true);
    }
    @Override
    public void getData(boolean repeat) {
        if (!StreamHandler.isConnected())
            return;
        if (done)
            return;
        final Response[] response = {null};
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                response[0] = StreamHandler.sendReqGetResp(new Request(RequestType.FollowRequests, "", TokenHolder.token));
                return null;
            }
        };
        task.setOnSucceeded(event ->
                showData(repeat, GsonHandler.getGson().fromJson(response[0].data, FollowRequestsStatusResponse.class)));
        Thread thread = new Thread(task);
        thread.start();
    }

    public void showData(boolean repeat, FollowRequestsStatusResponse response) {
        if (!StreamHandler.isConnected())
            return;
        if (done)
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
}
