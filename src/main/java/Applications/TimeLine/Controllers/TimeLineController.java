package Applications.TimeLine.Controllers;

import Applications.TimeLine.Listeners.MainPageListener;
import Applications.TimeLine.Views.TimeLine;
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

public class TimeLineController implements AutoUpdatingController {
    public boolean isDone = false;
    TimeLine timeLine;
    TweetListResponse tweetListResponse;
    public TimeLineController() {
        MainPageListener mainPageListener = new MainPageListener(this);
        timeLine = new TimeLine( mainPageListener);
        getData(true);
    }

    public void showTimeLine(TweetListResponse tweetListResponse, boolean repeat) {
        if (!StreamHandler.isConnected()) {
            return;
        }
        if (isDone)
            return;
        timeLine.refresh(tweetListResponse);
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
        if (!StreamHandler.isConnected()) {
            return;
        }
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                Response response = StreamHandler.sendReqGetResp(
                        new Request(RequestType.TimeLine, "", TokenHolder.token));
                tweetListResponse = GsonHandler.getGson().fromJson(response.data, TweetListResponse.class);
                return null;
            }
        };
        task.setOnSucceeded((e)->{
            showTimeLine(tweetListResponse, repeat);
        });
        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void setDoneTrue() {
        isDone = true;
    }

    @Override
    public void setDoneFalse() {
        isDone = false;
    }
}
