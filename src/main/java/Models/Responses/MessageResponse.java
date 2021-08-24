package Models.Responses;


import Models.Events.SendMessageToConversationEvent;
import Models.MessageStatus;
import Models.UserDetails;

import java.time.LocalDateTime;

public class MessageResponse {
    private SmallUserResponse user;
    private String content;
    private byte[] image;
    private LocalDateTime timeSent;
    private TweetResponse tweet;
    private int id;
    private MessageStatus status;

    public MessageResponse(SendMessageToConversationEvent event) {
        user = new SmallUserResponse(UserDetails.holder.getUsername(), UserDetails.holder.getId(),
                UserDetails.getHolder().getUserInformationResponse().getImage());
        content = event.getContent();
        image = event.getImage();
        timeSent = event.getLocalDateTime();
        id = -1;
        status = MessageStatus.UnSent;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TweetResponse getTweet() {
        return tweet;
    }

    public void setTweet(TweetResponse tweet) {
        this.tweet = tweet;
    }

    public LocalDateTime getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(LocalDateTime timeSent) {
        this.timeSent = timeSent;
    }

    public SmallUserResponse getUser() {
        return user;
    }

    public void setUser(SmallUserResponse user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
