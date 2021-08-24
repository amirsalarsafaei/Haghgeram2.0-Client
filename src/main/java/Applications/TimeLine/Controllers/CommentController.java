package Applications.TimeLine.Controllers;

import Applications.TimeLine.Listeners.CommentListener;
import Applications.TimeLine.Views.Comments;
import FileHandling.Properties;
import Holder.TokenHolder;
import Models.Events.SendCommentEvent;
import Models.Networking.Request;
import Models.Networking.RequestType;
import Models.Networking.Response;
import Models.Responses.TweetListResponse;
import StreamHandler.StreamHandler;
import Utils.AutoUpdatingController;
import Utils.AutoUpdatingView;
import Utils.GsonHandler;
import javafx.concurrent.Task;

import static java.lang.Thread.sleep;

public class CommentController implements AutoUpdatingController {
    private boolean done = false;
    private int tweetId;
    private TweetListResponse tweetListResponse;
    private Comments comments;
    private AutoUpdatingView backView;
    public CommentController(int tweetId, AutoUpdatingView backView) {
        this.backView = backView;
        this.tweetId = tweetId;
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                Response response = StreamHandler.sendReqGetResp(new Request(RequestType.Comments, String.valueOf(tweetId), TokenHolder.token));
                tweetListResponse = GsonHandler.getGson().fromJson(response.data, TweetListResponse.class);
                return null;
            }
        };
        task.setOnSucceeded((e)->{
            comments = new Comments(tweetListResponse, backView, new CommentListener(this), tweetId);
            getData(true);
        });
        Thread thread = new Thread(task);
        thread.start();
    }

    public void showComments(TweetListResponse tweetListResponse, boolean repeat) {
        comments.refresh(tweetListResponse);
        if (repeat) {
            Task task = new Task() {
                @Override
                protected Object call() throws Exception {
                    sleep(Properties.loadNumbers("refresh"));
                    return null;
                }
            };
            task.setOnSucceeded((e) -> getData(true));
            Thread thread = new Thread(task);
            thread.start();
        }
    }

    @Override
    public void getData(boolean repeat) {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                Response response = StreamHandler.sendReqGetResp(new Request(RequestType.Comments, String.valueOf(tweetId), TokenHolder.token));
                System.out.println(response.data);
                tweetListResponse = GsonHandler.getGson().fromJson(response.data, TweetListResponse.class);
                return null;
            }
        };
        task.setOnSucceeded((e)->{
            showComments(tweetListResponse, repeat);
        });
        Thread thread = new Thread(task);
        thread.start();
    }

    public void sendComment(SendCommentEvent e) {
        StreamHandler.sendReqGetResp(new Request(RequestType.SendComment, GsonHandler.getGson().toJson(e), TokenHolder.token));
    }

    @Override
    public void setDoneTrue() {
        done=true;
    }

    @Override
    public void setDoneFalse() {
        done=false;
    }
}
