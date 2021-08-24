package Applications.Setting.Controllers;

import Applications.Setting.Listeners.EditPrivacyListener;
import Applications.Setting.Views.EditPrivacyView;
import DataBase.OfflineUserDetails;
import FileHandling.Properties;
import Holder.TokenHolder;
import Models.Events.ChangePrivacyEvent;
import Models.Networking.Request;
import Models.Networking.RequestType;
import Models.Networking.Response;
import Models.Responses.EditPrivacyResponse;
import Models.UserDetails;
import StreamHandler.StreamHandler;
import Utils.AutoUpdatingController;
import Utils.GsonHandler;
import javafx.concurrent.Task;

import static java.lang.Thread.sleep;

public class EditPrivacyController implements AutoUpdatingController {
    //TODO:Make Offline
    private EditPrivacyView view;
    private Request updateRequest;
    private Response updateResponse;
    public EditPrivacyController() {
        updateRequest = new Request(RequestType.Privacy, "", TokenHolder.token);
        getData(false);
    }

    @Override
    public void getData(boolean repeat) {
        if (!StreamHandler.isConnected()) {
            show(false, UserDetails.holder.getEditPrivacyResponse());
            return;
        }
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                updateResponse = StreamHandler.sendReqGetResp(updateRequest);
                return null;
            }
        };
        task.setOnSucceeded(event ->
                show(repeat, GsonHandler.getGson().fromJson(updateResponse.data, EditPrivacyResponse.class)));
        Thread thread = new Thread(task);
        thread.start();
    }

    public void show(boolean repeat, EditPrivacyResponse response) {
        view = new EditPrivacyView(response, new EditPrivacyListener(this));
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

    }

    @Override
    public void setDoneFalse() {

    }

    public void changePrivacy(ChangePrivacyEvent changePrivacyEvent) {
        if (!StreamHandler.isConnected()) {

            UserDetails.holder.setEditPrivacyResponse(new EditPrivacyResponse(changePrivacyEvent));
            OfflineUserDetails.Save(UserDetails.holder);
            return;
        }
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                StreamHandler.sendReqGetResp(new Request(RequestType.ChangePrivacy,
                        GsonHandler.getGson().toJson(changePrivacyEvent), TokenHolder.token));
                System.out.println("Shit");
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }
}
