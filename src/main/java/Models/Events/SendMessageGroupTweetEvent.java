package Models.Events;

import java.util.ArrayList;

public class SendMessageGroupTweetEvent {
    private int tweet_id;
    private ArrayList<String> followings, groups;
    private ArrayList<Integer> conversations;

    public SendMessageGroupTweetEvent(ArrayList<String> followings, ArrayList<String> groups,
                                      ArrayList<Integer> conversations, int tweet_id) {
        this.followings = followings;
        this.groups = groups;
        this.conversations = conversations;
        this.tweet_id = tweet_id;
    }
}
