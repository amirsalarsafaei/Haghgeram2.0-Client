package Applications.Messenger.Listeners;

import Applications.Messenger.Controllers.ConversationListController;
import Models.Events.ShowConversationEvent;
import Utils.AutoUpdatingListener;

public class ConversationListListener implements AutoUpdatingListener {
    ConversationListController controller;
    public ConversationListListener(ConversationListController controller) {
        this.controller = controller;
    }
    @Override
    public void startRefreshing() {
        controller.start();
    }

    @Override
    public void stopRefreshing() {
        controller.stop();
        controller.stopSide();
    }

    @Override
    public void refresh() {
        controller.getData(false);
    }

    public void setGroupMessageButton() {
        controller.showGroupMessageView();
    }

    public void showConversationSelected(ShowConversationEvent e, boolean isGroup) {
        controller.showConversation(e, isGroup);
    }

    public void addGroupButtonPressed() {
        controller.showAddGroupView();
    }
}
