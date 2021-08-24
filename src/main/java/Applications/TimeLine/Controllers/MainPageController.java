package Applications.TimeLine.Controllers;

import Holder.TokenHolder;
import Models.Events.SendTweetEvent;
import Models.Networking.Request;
import Models.Networking.RequestType;
import StreamHandler.StreamHandler;
import Utils.GsonHandler;

public class MainPageController{

    public void sendTweet(SendTweetEvent e) {
        StreamHandler.sendReqGetResp(new Request(RequestType.SendTweet, GsonHandler.getGson().toJson(e), TokenHolder.token));
    }
}
