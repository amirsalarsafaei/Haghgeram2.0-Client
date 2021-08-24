package GraphicController;

import Holder.TokenHolder;
import Models.Networking.Request;
import Models.Networking.RequestType;
import Models.Responses.SmallUserResponse;
import StreamHandler.StreamHandler;
import Utils.AutoUpdatingView;
import Utils.GsonHandler;

public class ButtonController {
    private AutoUpdatingView autoUpdatingView;
    public ButtonController(AutoUpdatingView autoUpdatingView) {
        this.autoUpdatingView = autoUpdatingView;
    }
    public void MuteUser(SmallUserResponse smallUserResponse) {
        StreamHandler.sendReqGetResp(new Request(RequestType.Mute, GsonHandler.getGson().toJson(smallUserResponse),
                TokenHolder.token));
        autoUpdatingView.refreshNow();
    }

    public void BlockUser(SmallUserResponse smallUserResponse) {
        StreamHandler.sendReqGetResp(new Request(RequestType.Block, GsonHandler.getGson().toJson(smallUserResponse),
                TokenHolder.token));
        autoUpdatingView.refreshNow();
    }
}
