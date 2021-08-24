package Utils;

import DataBase.OfflineRequest;
import DataBase.OfflineUnsentMessages;
import DataBase.OfflineUserDetails;
import FileHandling.FileHandler;
import Holder.IDHolder;
import Holder.LoggedInHolder;
import Holder.TokenHolder;
import Holder.UsernameHolder;
import Models.Networking.Request;
import Models.Networking.RequestType;
import Models.OfflineRequests;
import Models.UnsentMessages;
import Models.UserDetails;
import StreamHandler.StreamHandler;
import javafx.concurrent.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Initialize {
    public Initialize() throws FileNotFoundException {
        Connection.connect();
        new CheckConnection().checkConnectionLoop();

        File file = new File(FileHandler.loadLocation("auth"));
        try {
            Scanner scanner = new Scanner(file);

            if (!scanner.nextBoolean()) {
                scanner.close();
                return;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        OfflineRequests.offlineRequests = OfflineRequest.load();
        UnsentMessages.holder = OfflineUnsentMessages.load();
        UserDetails.holder = OfflineUserDetails.load();
        UsernameHolder.username = UserDetails.getHolder().getUsername();
        IDHolder.Id = UserDetails.getHolder().getId();
        TokenHolder.token = UserDetails.getHolder().getToken();
        LoggedInHolder.loggedIn = true;
        sendOnline();
    }

    public static void sendOnline() {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                sleep(10000);
                if (!LoggedInHolder.loggedIn)
                    return null;
                StreamHandler.sendReqGetResp(new Request(RequestType.Online, "", TokenHolder.token));
                return null;
            }
        };
        task.setOnSucceeded(event -> sendOnline());
        Thread thread = new Thread(task);
        thread.start();
    }
}
