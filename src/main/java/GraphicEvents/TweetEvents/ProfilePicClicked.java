package GraphicEvents.TweetEvents;

import Models.Responses.SmallUserResponse;

public class ProfilePicClicked {
    //TODO:Fix this
    private SmallUserResponse smallUserResponse;

    public SmallUserResponse getSmallUserResponse() {
        return smallUserResponse;
    }

    public void setSmallUserResponse(SmallUserResponse smallUserResponse) {
        this.smallUserResponse = smallUserResponse;
    }

    public ProfilePicClicked(SmallUserResponse smallUserResponse) {
        this.smallUserResponse = smallUserResponse;
    }
}
