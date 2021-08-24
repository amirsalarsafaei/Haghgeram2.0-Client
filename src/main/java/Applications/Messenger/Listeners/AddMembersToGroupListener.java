package Applications.Messenger.Listeners;

import Applications.Messenger.Controllers.AddMembersToGroupController;
import Models.Events.AddMembersToGroupEvent;

public class AddMembersToGroupListener {
    private AddMembersToGroupController controller;
    public AddMembersToGroupListener(AddMembersToGroupController controller) {
        this.controller = controller;
    }
    public void addUsersButtonClicked(AddMembersToGroupEvent e) {
        controller.addUsersToGroup(e);
    }
}
