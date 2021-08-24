package Models.Events;

import GraphicListeners.TweetListener;
import Models.Responses.TweetResponse;
import javafx.scene.layout.StackPane;

public class SetForwardViewEvent {
    private TweetListener listener;
    private StackPane stackPane;
    private TweetResponse tweet;

    public SetForwardViewEvent(TweetListener listener, StackPane stackPane, TweetResponse tweet) {
        this.listener = listener;
        this.stackPane = stackPane;
        this.tweet = tweet;
    }

    public TweetListener getListener() {
        return listener;
    }

    public void setListener(TweetListener listener) {
        this.listener = listener;
    }

    public StackPane getStackPane() {
        return stackPane;
    }

    public void setStackPane(StackPane stackPane) {
        this.stackPane = stackPane;
    }

    public TweetResponse getTweet() {
        return tweet;
    }

    public void setTweet(TweetResponse tweet) {
        this.tweet = tweet;
    }
}
