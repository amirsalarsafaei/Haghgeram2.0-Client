package Models.Responses;

import java.util.ArrayList;

public class ConversationListResponse {
    private ArrayList<ConversationPreviewResponse> conversations;

    public ArrayList<ConversationPreviewResponse> getConversations() {
        return conversations;
    }

    public void setConversations(ArrayList<ConversationPreviewResponse> conversations) {
        this.conversations = conversations;
    }

}
