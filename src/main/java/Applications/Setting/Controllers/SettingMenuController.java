package Applications.Setting.Controllers;

import Applications.Authentication.Views.Login;
import Applications.Setting.Listeners.SettingMenuListener;
import Applications.Setting.Views.SettingMenuView;
import FileHandling.FileHandler;
import Holder.LoggedInHolder;
import Holder.TokenHolder;
import Models.Networking.Request;
import Models.Networking.RequestType;
import Models.OfflineRequests;
import StreamHandler.StreamHandler;
import javafx.concurrent.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class SettingMenuController {
    private SettingMenuView settingMenuView;
    public SettingMenuController() {
        settingMenuView = new SettingMenuView(new SettingMenuListener(this));
    }

    public void logOut() {
        File file = new File(FileHandler.loadLocation("auth"));
        try {
            PrintStream printStream = new PrintStream(file);
            printStream.print(false);
            printStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        LoggedInHolder.loggedIn = false;
        new Login();
    }

    public void deleteAccount() {
        if (!StreamHandler.isConnected()) {
            OfflineRequests.offlineRequests.addRequest(new Request(RequestType.DeleteAccount, "", TokenHolder.token));
            new Login();
            LoggedInHolder.loggedIn = false;
            return;
        }
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                StreamHandler.sendReqGetResp(new Request(RequestType.DeleteAccount, "", TokenHolder.token));
                return null;
            }
        };task.setOnSucceeded(event -> new Login());
        Thread thread = new Thread(task);
        thread.start();
    }
}
