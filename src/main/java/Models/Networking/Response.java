package Models.Networking;


import java.io.Serializable;

public class Response implements Serializable {
    public ResponseType responseType;
    public String data;
    public Response(ResponseType responseType, String data) {
        this.responseType = responseType;
        this.data = data;
    }
}
