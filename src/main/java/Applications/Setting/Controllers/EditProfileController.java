package Applications.Setting.Controllers;

import Applications.Setting.Listeners.EditProfileListener;
import Applications.Setting.Views.EditProfileView;
import Holder.TokenHolder;
import Models.Events.EditProfileEvent;
import Models.Networking.Request;
import Models.Networking.RequestType;
import Models.Networking.Response;
import Models.Networking.ResponseType;
import Models.Responses.UserInformationResponse;
import StreamHandler.StreamHandler;
import Utils.GsonHandler;
import javafx.concurrent.Task;

public class EditProfileController {
    private Response editProfileResponse, response;
    private EditProfileView view;

    public EditProfileController() {
        if (!StreamHandler.isConnected())
            return;
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                response = StreamHandler.sendReqGetResp(new Request(RequestType.EditProfileInfo, "", TokenHolder.token));
                return null;
            }
        };
        task.setOnSucceeded(event -> {
            view = new EditProfileView(GsonHandler.getGson().fromJson(response.data, UserInformationResponse.class),
                    new EditProfileListener(this));
        });
        Thread thread = new Thread(task);
        thread.start();
    }

    public void editProfile(EditProfileEvent event) {
        if (!StreamHandler.isConnected())
            return;
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                editProfileResponse = StreamHandler.sendReqGetResp(new Request(RequestType.EditProfile, GsonHandler.getGson().toJson(event),
                        TokenHolder.token));
                return null;
            }
        };
        task.setOnSucceeded((e)-> {
            if (editProfileResponse.responseType == ResponseType.Accepted) {

            }
            else {
                view.setError(editProfileResponse.data);
            }
        });
        Thread thread = new Thread(task);
        thread.start();
    }
}
