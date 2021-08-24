package Models.Responses;

public class SelfUserResponse {
    private TweetListResponse tweets;
    private BigUserResponse user;

    public TweetListResponse getTweets() {
        return tweets;
    }

    public void setTweets(TweetListResponse tweets) {
        this.tweets = tweets;
    }

    public BigUserResponse getUser() {
        return user;
    }

    public void setUser(BigUserResponse user) {
        this.user = user;
    }
}
