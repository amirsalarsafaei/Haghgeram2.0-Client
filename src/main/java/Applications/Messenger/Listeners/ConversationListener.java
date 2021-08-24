package Applications.Messenger.Listeners;

import Applications.Messenger.Controllers.ConversationController;
import Models.Events.*;
import Utils.AutoUpdatingListener;

public class ConversationListener implements AutoUpdatingListener {
    private final ConversationController controller;
    public ConversationListener(ConversationController controller) {
        this.controller = controller;
    }

    @Override
    public void startRefreshing() {
        controller.start();
    }

    @Override
    public void stopRefreshing() {
        controller.stop();
    }

    @Override
    public void refresh() {
        controller.getData(false);
        controller.refreshSide();
    }

    public void sendMessageButtonPressed(SendMessageToConversationEvent e) {
        controller.sendMessage(e);
    }

    public void hyperLinkClicked(HyperLinkEvent e) {
        controller.hyperLinkAction(e);
    }

    public void addMemberToGroupButtonPressed(AddMemberToGroupViewEvent addMemberToGroupEvent) {
        controller.addMemberToGroup(addMemberToGroupEvent);
    }

    public void leaveGroupButtonPressed(LeaveGroupEvent leaveGroupEvent) {
        controller.leaveGroup(leaveGroupEvent);
    }

    public void deleteMessageButtonPressed(DeleteMessageEvent deleteMessageEvent) {
        controller.deleteMessage(deleteMessageEvent);
    }

    public void editMessageButtonPressed(EditMessageEvent editMessageEvent) {
        controller.editMessage(editMessageEvent);
    }
}
