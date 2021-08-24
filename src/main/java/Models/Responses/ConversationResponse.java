package Models.Responses;

import java.util.ArrayList;

public class ConversationResponse {
    private ArrayList<MessageResponse> messages;
    private String name;
    private int id;
    private boolean group;

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<MessageResponse> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<MessageResponse> messages) {
        this.messages = messages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
