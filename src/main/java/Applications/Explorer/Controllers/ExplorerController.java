package Applications.Explorer.Controllers;

import Applications.Explorer.Listeners.ExplorerListener;
import Applications.Explorer.Views.ExplorerView;
import FileHandling.Properties;
import Holder.TokenHolder;
import Models.Networking.Request;
import Models.Networking.RequestType;
import Models.Networking.Response;
import Models.Responses.TweetListResponse;
import StreamHandler.StreamHandler;
import Utils.AutoUpdatingController;
import Utils.GsonHandler;
import javafx.concurrent.Task;

import static java.lang.Thread.sleep;

public class ExplorerController implements AutoUpdatingController {
    private boolean done = false;
    private Response response;
    private ExplorerListener explorerListener;
    private ExplorerView explorerView;
    public ExplorerController() {
        if (!StreamHandler.isConnected())
            return;
        explorerListener = new ExplorerListener(this);
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                response = StreamHandler.sendReqGetResp(new Request(RequestType.Discovery, "", TokenHolder.token));
                return null;
            }
        };
        task.setOnSucceeded((e)->{
            TweetListResponse tweetListResponse = GsonHandler.getGson().fromJson(response.data, TweetListResponse.class);
            explorerView = new ExplorerView(explorerListener, tweetListResponse);
            getData(true);
        });
        Thread thread = new Thread(task);
        thread.start();
    }

    public void showData(TweetListResponse tweetListResponse, boolean repeat) {
        if (!StreamHandler.isConnected())
            return;
        if (done)
            return;
        explorerView.refresh(tweetListResponse);
        if (repeat) {
            Task task = new Task() {
                @Override
                protected Object call() throws Exception {
                    sleep(Properties.loadNumbers("refresh"));
                    return null;
                }
            };
            task.setOnSucceeded((e) -> {
                getData(true);
            });
            Thread thread = new Thread(task);
            thread.start();
        }
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
                response = StreamHandler.sendReqGetResp(new Request(RequestType.Discovery, "", TokenHolder.token));
                return null;
            }
        };
        task.setOnSucceeded((e)->{
            TweetListResponse tweetListResponse = GsonHandler.getGson().fromJson(response.data, TweetListResponse.class);
            showData(tweetListResponse, repeat);
        });
        Thread thread = new Thread(task);
        thread.start();
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
