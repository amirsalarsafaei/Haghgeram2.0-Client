package Applications.Setting.Controllers;

import Applications.Setting.Listeners.BlackListListener;
import Applications.Setting.Views.BlackListView;
import FileHandling.Properties;
import Holder.TokenHolder;
import Models.Events.UnBlockButtonPressedEvent;
import Models.Networking.Request;
import Models.Networking.RequestType;
import Models.Networking.Response;
import Models.Responses.BlackListResponse;
import StreamHandler.StreamHandler;
import Utils.AutoUpdatingController;
import Utils.GsonHandler;
import javafx.concurrent.Task;

import static java.lang.Thread.sleep;

public class BlackListController implements AutoUpdatingController {
    private boolean done = false;
    private Response updateResponse;
    private Request updateRequest;
    private BlackListView view;
    public BlackListController() {
        updateRequest = new Request(RequestType.BlackList, "", TokenHolder.token);
        view = new BlackListView(new BlackListListener(this));
        getData(false);
    }

    @Override
    public void getData(boolean repeat) {
        if (!StreamHandler.isConnected())
            return;
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                updateResponse = StreamHandler.sendReqGetResp(updateRequest);
                return null;
            }
        };
        task.setOnSucceeded(event ->
                show(GsonHandler.getGson().fromJson(updateResponse.data, BlackListResponse.class), repeat));
        Thread thread = new Thread(task);
        thread.start();
    }

    public void show(BlackListResponse response, boolean repeat) {
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

    public void unblock(UnBlockButtonPressedEvent event) {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                StreamHandler.sendReqGetResp(new Request(RequestType.UnBlock, GsonHandler.getGson().toJson(event),
                        TokenHolder.token));
                System.out.println("shit");
                return null;
            }
        };
        task.setOnSucceeded((e)->getData(false));
        Thread thread = new Thread(task);
        thread.start();
    }
}
