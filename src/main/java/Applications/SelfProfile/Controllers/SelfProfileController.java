package Applications.SelfProfile.Controllers;

import Applications.SelfProfile.Listeners.SelfProfileListener;
import Applications.SelfProfile.Views.SelfProfileView;
import FileHandling.Properties;
import Holder.TokenHolder;
import Models.Networking.Request;
import Models.Networking.RequestType;
import Models.Networking.Response;
import Models.Responses.SelfUserResponse;
import StreamHandler.StreamHandler;
import Utils.AutoUpdatingController;
import Utils.AutoUpdatingView;
import Utils.GsonHandler;
import javafx.concurrent.Task;

import static java.lang.Thread.sleep;

public class SelfProfileController implements AutoUpdatingController {
    private boolean done = false;
    private SelfProfileView view;
    private final Request request;
    private Response response;
    private final AutoUpdatingView backView;
    public SelfProfileController(AutoUpdatingView backView) {

        this.backView = backView;
        request = new Request(RequestType.SelfProfile, "", TokenHolder.token);
        getData(true);
        if (!StreamHandler.isConnected())
            return;

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
                response = StreamHandler.sendReqGetResp(request);
                return null;
            }
        };
        task.setOnSucceeded(event -> showData(repeat,
                GsonHandler.getGson().fromJson(response.data, SelfUserResponse.class)));
        Thread thread = new Thread(task);
        thread.start();
    }

    public void showData(boolean repeat, SelfUserResponse user) {
        if (!StreamHandler.isConnected())
            return;
        if (done)
            return;
        if (view == null) {
            view = new SelfProfileView(user, new SelfProfileListener(this), backView);
        }
        else {
            view.refresh(user);
        }
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
