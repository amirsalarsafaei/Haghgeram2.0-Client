package Applications.Authentication.Controllers;

import Applications.Authentication.Views.Login;
import Applications.Authentication.Views.Register;
import Models.Events.AuthEvent;
import Models.Networking.Request;
import Models.Networking.RequestType;
import Models.Networking.Response;
import Models.Networking.ResponseType;
import StreamHandler.StreamHandler;
import Utils.Convertors;
import Utils.GsonHandler;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

import java.io.File;

public class RegisterController {
    Register register;
    Response response;
    public RegisterController(Register register) {
        this.register = register;
    }


    public void register(AuthEvent authEvent, Label error, File file) {
        if (!StreamHandler.isConnected())
            return;
        if (file != null) {
            authEvent.image = Convertors.imageFileToByte(file);
        }
        String s =  GsonHandler.getGson().toJson(authEvent);
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                response = StreamHandler.sendReqGetResp(new Request(RequestType.SignUp, s));
                return null;
            }
        };
        task.setOnSucceeded((e)->{
            if (response.responseType != ResponseType.Created) {
                error.setText(response.data);
                System.out.println(response.data);
            }
            else {
                new Login();
            }
        });
        Thread thread = new Thread(task);
        thread.start();
    }

}
