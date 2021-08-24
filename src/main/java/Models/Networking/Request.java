package Models.Networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;

public class Request implements Serializable {
    public RequestType requestType;
    public String data;
    public String token;


    public Request(RequestType requestType, String data) {
        this.requestType = requestType;
        this.data = data;
    }

    public Request(RequestType requestType, String data, String token) {
        this.requestType = requestType;
        this.data = data;
        this.token = token;
    }

}
