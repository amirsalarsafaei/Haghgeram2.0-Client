package Models.Events;

import java.util.ArrayList;

public class SendGroupMessageEvent {
    private ArrayList<String> followings, groups;
    private ArrayList<Integer> conversations;
    private String content;
    private byte[] image;

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

    public SendGroupMessageEvent(ArrayList<String> followings, ArrayList<String> groups, ArrayList<Integer> conversations,
                                 String content, byte[] image) {
        this.followings = followings;
        this.groups = groups;
        this.content = content;
        this.image = image;
        this.conversations = conversations;
    }
}