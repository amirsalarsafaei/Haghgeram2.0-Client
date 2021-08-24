package Applications.List.Controllers;

import Applications.List.Listeners.ListListener;
import Applications.List.Views.ListView;
import FileHandling.Properties;
import Holder.TokenHolder;
import Models.Events.AddUsersToListCompletedEvent;
import Models.Events.AddUsersToListEvent;
import Models.Events.ShowListEvent;
import Models.Networking.Request;
import Models.Networking.RequestType;
import Models.Networking.Response;
import Models.Responses.FollowingsResponse;
import Models.Responses.ListResponse;
import StreamHandler.StreamHandler;
import Utils.AutoUpdatingController;
import Utils.AutoUpdatingView;
import Utils.GsonHandler;
import javafx.concurrent.Task;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import static java.lang.Thread.sleep;

public class ListController implements AutoUpdatingController {
    private boolean done = false;
    private final Request updateRequest;
    private Response updateResponse, followingsResponse;
    private ListView view;
    private ListsListController listsListController;
    public ListController(BorderPane borderPane, AutoUpdatingView listsListView, ShowListEvent e,
                          ListsListController listsListController, StackPane Pane) {
        updateRequest = new Request(RequestType.List, GsonHandler.getGson().toJson(e), TokenHolder.token);
        view = new ListView(borderPane, new ListListener(this), listsListView, Pane);
        this.listsListController = listsListController;
        getData(true);
    }

    @Override
    public synchronized void getData(boolean repeat) {
        if (!StreamHandler.isConnected())
            return;
        if (done)
            return;
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                updateResponse = StreamHandler.sendReqGetResp(updateRequest);
                return null;
            }
        };
        task.setOnSucceeded(event -> {
            showData(repeat, GsonHandler.getGson().fromJson(updateResponse.data, ListResponse.class));
        });
        Thread thread = new Thread(task);
        thread.start();
    }

    public void showData(boolean repeat, ListResponse listResponse) {
        if (!StreamHandler.isConnected())
            return;
        if (done)
            return;
        view.refresh(listResponse);
        if (repeat) {
            Task task = new Task() {
                @Override
                protected Object call() throws Exception {
                    sleep(Properties.loadNumbers("refresh"));
                    return null;
                }
            };
            task.setOnSucceeded(event -> getData(repeat));
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


    public void addUsers(AddUsersToListEvent e) {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                followingsResponse = StreamHandler.sendReqGetResp(new Request(RequestType.Followings, "", TokenHolder.token));

                return null;
            }
        };
        task.setOnSucceeded((a)->{
            view.addUserToGroup(e, GsonHandler.getGson().fromJson(followingsResponse.data, FollowingsResponse.class));

        });
        Thread thread = new Thread(task);
        thread.start();
    }

    public void addUsersCompleted(AddUsersToListCompletedEvent e) {
        StreamHandler.sendReqGetResp(new Request(RequestType.AddUserToList, GsonHandler.getGson().toJson(e),
                TokenHolder.token));
        System.out.println(GsonHandler.getGson().toJson(e));
        getData(false);
    }
}
