package Models.Events;

import java.time.LocalDateTime;

public class SendMessageToConversationEvent {
    private String content;
    private byte[] image;
    private int id;
    private LocalDateTime localDateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public SendMessageToConversationEvent(String content, byte[] image, int id) {
        this.content = content;
        this.image = image;
        this.id = id;
        localDateTime = LocalDateTime.now();
    }

    public SendMessageToConversationEvent(String content, int id) {
        this.content = content;
        this.id = id;
        localDateTime = LocalDateTime.now();
    }


}
