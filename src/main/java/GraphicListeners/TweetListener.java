package GraphicListeners;

import GraphicController.TweetController;
import GraphicEvents.TweetEvents.ProfilePicClicked;
import Models.Events.*;
import Models.Responses.TweetResponse;
import Utils.AutoUpdatingView;

public class TweetListener {
    private TweetController tweetController;
    private TweetResponse tweet;
    private AutoUpdatingView autoUpdatingView;
    public TweetListener(TweetResponse tweetResponse, AutoUpdatingView autoUpdatingView) {
        this.autoUpdatingView = autoUpdatingView;
        tweetController = new TweetController(autoUpdatingView);
        tweet = tweetResponse;
    }
    public void profilePicClicked(ProfilePicClicked e) {
        tweetController.profilePicClicked(e);
    }

    public void retweetButtonClicked(SendRetweetEvent e) {
        tweetController.sendRetweet(e);
    }

    public void likeButtonClicked(LikeButtonEvent e) { tweetController.likeTweet(e); }

    public void hyperLinkClicked(HyperLinkEvent hyperLinkEvent) {
        tweetController.hyperLink(hyperLinkEvent);
    }

    public void sendGroupTweetMessage(SendMessageGroupTweetEvent sendMessageGroupTweetEvent) {
        tweetController.sendGroupTweet(sendMessageGroupTweetEvent);
    }

    public void setForwardView(SetForwardViewEvent setForwardViewEvent) {
        tweetController.setForwardView(setForwardViewEvent);
    }

    public void reportButtonPressed(ReportTweetEvent reportTweetEvent) {
        tweetController.reportTweet(reportTweetEvent);
    }
}
