package Models.Responses;

import Models.HyperLinkType;

public class HyperLinkResponse {
    private HyperLinkType hyperLinkType;
    private TweetResponse tweetResponse;
    private ConversationResponse conversationResponse;

    public HyperLinkResponse(TweetResponse tweetResponse) {
        this.tweetResponse = tweetResponse;
        hyperLinkType = HyperLinkType.Tweet;
    }

    public HyperLinkResponse(ConversationResponse conversationResponse) {
        this.conversationResponse = conversationResponse;
        hyperLinkType = HyperLinkType.Conversation;
    }

    public HyperLinkResponse(HyperLinkType hyperLinkType) {
        this.hyperLinkType = hyperLinkType;
    }

    public HyperLinkType getHyperLinkType() {
        return hyperLinkType;
    }

    public void setHyperLinkType(HyperLinkType hyperLinkType) {
        this.hyperLinkType = hyperLinkType;
    }

    public TweetResponse getTweetResponse() {
        return tweetResponse;
    }

    public void setTweetResponse(TweetResponse tweetResponse) {
        this.tweetResponse = tweetResponse;
    }

    public ConversationResponse getConversationResponse() {
        return conversationResponse;
    }

    public void setConversationResponse(ConversationResponse conversationResponse) {
        this.conversationResponse = conversationResponse;
    }
}
