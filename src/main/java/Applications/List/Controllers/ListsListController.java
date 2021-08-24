package Applications.List.Controllers;

import Applications.List.Listeners.ListsListListener;
import Applications.List.Views.ListsListView;
import FileHandling.Properties;
import Holder.TokenHolder;
import Models.Events.AddListEvent;
import Models.Events.DeleteListEvent;
import Models.Events.ShowListEvent;
import Models.Networking.Request;
import Models.Networking.RequestType;
import Models.Networking.Response;
import Models.Responses.ListsListResponse;
import StreamHandler.StreamHandler;
import Utils.AutoUpdatingController;
import Utils.GsonHandler;
import javafx.concurrent.Task;

import static java.lang.Thread.sleep;

public class ListsListController implements AutoUpdatingController {
    private boolean done = false;
    private  Request request;
    private Response response, addListResponse;
    private  ListsListView view;
    private ListController listController;
    public ListsListController() {
        if (!StreamHandler.isConnected())
            return;
        request = new Request(RequestType.ListsList, "", TokenHolder.token);
        view = new ListsListView(new ListsListListener(this));
        getData(true);
    }
    @Override
    public synchronized void getData(boolean repeat) {
        if (done)
            return;
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                response = StreamHandler.sendReqGetResp(request);
                return null;
            }
        };
        task.setOnSucceeded(event -> showData(GsonHandler.getGson().fromJson(response.data, ListsListResponse.class),
                repeat));
        Thread thread = new Thread(task);
        thread.start();
    }

    public void showData(ListsListResponse listsListResponse, boolean repeat) {
        if (done)
            return;
        view.refresh(listsListResponse);
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

    public void addList(AddListEvent addListEvent) {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                addListResponse = StreamHandler.sendReqGetResp(new Request(RequestType.AddList, GsonHandler.getGson().toJson(addListEvent),
                        TokenHolder.token));

                return null;
            }
        };
        task.setOnSucceeded((e)->{
            view.setError(addListResponse.data);
            getData(false);
        });
        Thread thread = new Thread(task);
        thread.start();
    }

    public void showList(ShowListEvent e) {
        listController = new ListController(view.getBorderPane(), view, e, this, view.getPane());
        getData(false);
    }

    public void stopSide() {
        if (listController != null)
            listController.stop();
    }

    public void deleteList(DeleteListEvent e) {
        StreamHandler.sendReqGetResp(new Request(RequestType.DeleteList, GsonHandler.getGson().toJson(e), TokenHolder.token));
    }


}
