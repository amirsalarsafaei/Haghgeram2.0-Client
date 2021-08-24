package Utils;

import GraphicComponents.GraphicReconnectButton;
import StreamHandler.StreamHandler;
import javafx.concurrent.Task;

import static java.lang.Thread.sleep;

public class CheckConnection {
    public  void checkConnectionLoop() {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                sleep(5000);

                return null;
            }
        };
        task.setOnSucceeded(event -> {
            if (!StreamHandler.isConnected()) {
                GraphicReconnectButton.setButton();
            }
            else {
                checkConnectionLoop();
            }
        });
        Thread thread = new Thread(task);
        thread.start();
    }
}
