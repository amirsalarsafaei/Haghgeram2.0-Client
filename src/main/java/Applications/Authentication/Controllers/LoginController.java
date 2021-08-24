package Applications.Authentication.Controllers;

import Applications.Authentication.Views.Login;
import Applications.TimeLine.Controllers.TimeLineController;
import DataBase.OfflineRequest;
import DataBase.OfflineUnsentMessages;
import DataBase.OfflineUserDetails;
import FileHandling.FileHandler;
import Holder.IDHolder;
import Holder.LoggedInHolder;
import Holder.TokenHolder;
import Holder.UsernameHolder;
import Models.Events.AuthEvent;
import Models.Networking.Request;
import Models.Networking.RequestType;
import Models.Networking.Response;
import Models.Networking.ResponseType;
import Models.OfflineRequests;
import Models.UnsentMessages;
import Models.UserDetails;
import StreamHandler.StreamHandler;
import Utils.GsonHandler;
import Utils.Initialize;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class LoginController {
    Login login;
    Response response;
    public LoginController(Login login)  {
        this.login = login;
    }
    public void login(AuthEvent authEvent, Label responseError) {
        if (!StreamHandler.isConnected())
            return;

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                response = StreamHandler.sendReqGetResp(new Request(RequestType.Login, GsonHandler.getGson().toJson(authEvent)));
                return null;
            }
        };
        task.setOnSucceeded(event -> {
            if (response.responseType != ResponseType.Accepted) {
                responseError.setText(response.data);
                return;
            }
            UserDetails userDetails = GsonHandler.getGson().fromJson(response.data, UserDetails.class);
            UserDetails.holder = userDetails;
            OfflineUserDetails.Save(userDetails);
            UsernameHolder.username = userDetails.getUsername();
            IDHolder.Id = userDetails.getId();
            TokenHolder.token = userDetails.getToken();
            OfflineUserDetails.Save(userDetails);
            UnsentMessages.holder = new UnsentMessages();
            OfflineUnsentMessages.Save(UnsentMessages.holder);
            OfflineRequests.offlineRequests = new OfflineRequests();
            OfflineRequest.Save(OfflineRequests.offlineRequests);
            LoggedInHolder.loggedIn = true;
            Initialize.sendOnline();
            File file = new File(FileHandler.loadLocation("auth"));
            try {
                PrintStream printStream = new PrintStream(file);
                printStream.print(true);
                printStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            new TimeLineController();
        });
        Thread thread = new Thread(task);
        thread.start();

    }
}
