package Models;

import DataBase.OfflineUnsentMessages;
import Models.Events.SendMessageToConversationEvent;

import java.util.ArrayList;

public class UnsentMessages {
    public static UnsentMessages holder = new UnsentMessages();
    private ArrayList<SendMessageToConversationEvent> sendMessageToConversationEvents;

    public ArrayList<SendMessageToConversationEvent> getSendMessageToConversationEvents() {
        return sendMessageToConversationEvents;
    }

    public void setSendMessageToConversationEvents(ArrayList<SendMessageToConversationEvent> sendMessageToConversationEvents) {
        this.sendMessageToConversationEvents = sendMessageToConversationEvents;
    }

    public UnsentMessages() {
        sendMessageToConversationEvents = new ArrayList<>();
    }

    public void addMessage(SendMessageToConversationEvent event) {
        sendMessageToConversationEvents.add(event);
        OfflineUnsentMessages.Save(this);
    }
}
