package Applications.List.Listeners;

import Applications.List.Controllers.ListController;
import Models.Events.AddUsersToListCompletedEvent;
import Models.Events.AddUsersToListEvent;
import Utils.AutoUpdatingListener;

public class ListListener implements AutoUpdatingListener {
    ListController controller;
    public ListListener(ListController listController) {
        controller= listController;
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
    }

    public void addUsersButtonPressed(AddUsersToListEvent e) {
        controller.addUsers(e);
    }

    public void addUsersCompletedButtonPressed(AddUsersToListCompletedEvent e) {
        controller.addUsersCompleted(e);
    }
}
