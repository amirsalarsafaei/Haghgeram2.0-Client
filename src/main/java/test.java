import Models.Networking.Request;
import Models.Networking.RequestType;
import Models.Networking.Response;
import StreamHandler.StreamHandler;
import Utils.GraphicAgent;
import Utils.GsonHandler;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

public class test {
    public test() {
        Response response = StreamHandler.sendReqGetResp(new Request(RequestType.Test, ""));
        GridPane gridPane = GsonHandler.getGson().fromJson(response.data, GridPane.class);
        Scene scene = new Scene(gridPane);
        GraphicAgent.stage.setScene(scene);
    }

}
