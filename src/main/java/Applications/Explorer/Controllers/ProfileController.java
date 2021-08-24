package Applications.Explorer.Controllers;

import Applications.Explorer.Listeners.ProfileListener;
import Applications.Explorer.Views.ProfileView;
import Holder.TokenHolder;
import Models.Events.FollowEvent;
import Models.Events.SendMessageToUserEvent;
import Models.Networking.Request;
import Models.Networking.RequestType;
import Models.Networking.Response;
import Models.Networking.ResponseType;
import Models.Responses.BigUserResponse;
import StreamHandler.StreamHandler;
import Utils.AutoUpdatingController;
import Utils.AutoUpdatingView;
import Utils.GsonHandler;
import javafx.concurrent.Task;

import static java.lang.Thread.sleep;

public class ProfileController implements AutoUpdatingController {
    private boolean done = false;
    private ProfileView profileView;
    private String username;
    private Request request;
    private Response response;
    private ProfileListener profileListener;
    private AutoUpdatingView backView;
    public ProfileController(String username, AutoUpdatingView backView) {
        if (!StreamHandler.isConnected())
            return;
        this.username = username;
        this.backView = backView;
        profileListener = new ProfileListener(this);
        request = new Request(RequestType.Profile, username, TokenHolder.token);
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                response = StreamHandler.sendReqGetResp(request);
                return null;
            }
        };
        task.setOnSucceeded((e)-> {
            if (response.responseType != ResponseType.Accepted) {
                stop();
                return;
            }
            BigUserResponse bigUserResponse = GsonHandler.getGson().fromJson(response.data, BigUserResponse.class);
            profileView = new ProfileView(bigUserResponse, profileListener);
            getData(true);
        });
        Thread thread = new Thread(task);
        thread.start();
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
        task.setOnSucceeded((e)-> {
            if (response.responseType != ResponseType.Accepted)
                stop();
            BigUserResponse bigUserResponse = GsonHandler.getGson().fromJson(response.data, BigUserResponse.class);
            refresh(bigUserResponse, true);
        });
        Thread thread = new Thread(task);
        thread.start();
    }



    public void refresh(BigUserResponse bigUserResponse, boolean repeat) {
        if (!StreamHandler.isConnected())
            return;
        if (done)
            return;
        System.out.println("refreshing");
        profileView.refresh(bigUserResponse);
        if (repeat) {
            Task task = new Task() {
                @Override
                protected Object call() throws Exception {
                    sleep(6000);
                    return null;
                }
            };
            task.setOnSucceeded((e)->getData(repeat));
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

    public void goBack() {
        backView.show();
    }

    public void toggleFollow(FollowEvent e) {
        if (!StreamHandler.isConnected())
            return;
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                StreamHandler.sendReqGetResp(new Request(RequestType.Follow, GsonHandler.getGson().toJson(e), TokenHolder.token));
                return null;
            }
        };
        task.setOnSucceeded(event -> getData(false));
        Thread thread = new Thread(task);
        thread.start();
    }

    public void sendMessage(SendMessageToUserEvent sendMessageToUserEvent) {
        if (!StreamHandler.isConnected())
            return;
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                StreamHandler.sendReqGetResp(new Request(RequestType.SendMessageToUser,
                        GsonHandler.getGson().toJson(sendMessageToUserEvent), TokenHolder.token));
                return null;
            }
        };
        task.setOnSucceeded(event -> getData(false));
        Thread thread = new Thread(task);
        thread.start();
    }
}
