package Utils;

import Holder.SocketHolder;
import Holder.StreamHolder;
import Models.Networking.Request;
import Models.OfflineRequests;
import StreamHandler.StreamHandler;
import javafx.concurrent.Task;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection {
    private static boolean isConnected = false;
    public  static void connect() {
        System.out.println("help");
        try {
            SocketHolder.socket = new Socket(properties.loadServerConfig("address"),
                    Integer.valueOf(properties.loadServerConfig("port")));
            StreamHolder.input = SocketHolder.socket.getInputStream();
            StreamHolder.output = SocketHolder.socket.getOutputStream();
            StreamHolder.out = new DataOutputStream(StreamHolder.output);
            StreamHolder.in = new DataInputStream(StreamHolder.input);
            isConnected = true;
            if (OfflineRequests.offlineRequests == null)
                return;
            for (Request request : OfflineRequests.offlineRequests.getRequests()) {
                Task task = new Task() {
                    @Override
                    protected Object call() throws Exception {
                        StreamHandler.sendReqGetResp(request);
                        return null;
                    }
                };
                Thread thread = new Thread(task);
                thread.start();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static boolean isConnected() {
        return isConnected;
    }

    public static void setConnected(boolean connected) {
        isConnected = connected;
    }
}
