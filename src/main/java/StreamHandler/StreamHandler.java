package StreamHandler;

import Holder.StreamHolder;
import Models.Networking.Request;
import Models.Networking.RequestType;
import Models.Networking.Response;
import Utils.Connection;
import Utils.GsonHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

public class StreamHandler {
    private static final Logger logger = LogManager.getLogger(StreamHandler.class);
    private static final ReentrantLock outputStreamLock = new ReentrantLock();

    private synchronized static void sendRequest( Request request) {
        System.out.println(request.requestType);
        String serializedRequest = GsonHandler.getGson().toJson(request);
        byte[] bytes = serializedRequest.getBytes();
        if (StreamHolder.out == null) {
            return;
        }
        try {
            StreamHolder.out.writeInt(bytes.length);
            StreamHolder.out.write(bytes);
            StreamHolder.out.flush();
        } catch (IOException e) {
            Connection.setConnected(false);
        }

    }

    private synchronized static Response getResponse() {
        if (StreamHolder.in == null)
            return null;
        byte[] bytes = null;
        try {
            int len = StreamHolder.in.readInt();
            bytes = StreamHolder.in.readNBytes(len);
        } catch (IOException e) {
            Connection.setConnected(false);
            return null;
        }
        String s = new String(bytes);
        return GsonHandler.getGson().fromJson(s, Response.class);
    }
    public synchronized static Response sendReqGetResp(Request request) {
        sendRequest(request);
        return getResponse();
    }

    public synchronized static boolean isConnected() {
        boolean res =  sendReqGetResp(new Request(RequestType.Ping, "")) != null;
        return res;
    }
}
