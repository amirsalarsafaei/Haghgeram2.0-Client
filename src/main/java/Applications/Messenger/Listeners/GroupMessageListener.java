package Applications.Messenger.Listeners;

import Applications.Messenger.Controllers.GroupMessageController;
import Models.Events.SendGroupMessageEvent;

public class GroupMessageListener{
    private GroupMessageController controller;
    public GroupMessageListener(GroupMessageController groupMessageController) {
        this.controller = groupMessageController;
    }

    public void sendMessageGroup(SendGroupMessageEvent e) {
        controller.sendGroupMessage(e);
    }


}
