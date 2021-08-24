package Applications.List.Listeners;

import Applications.List.Controllers.ListsListController;
import Models.Events.AddListEvent;
import Models.Events.DeleteListEvent;
import Models.Events.ShowListEvent;
import Utils.AutoUpdatingListener;

public class ListsListListener implements AutoUpdatingListener {
    private ListsListController controller;

    public ListsListListener(ListsListController controller) {
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

    public void addListButtonPressed(AddListEvent addListEvent) {
        controller.addList(addListEvent);
    }

    public void showList(ShowListEvent e) {
        controller.stopSide();

        controller.showList(e);
        refresh();
    }

    public void trashButtonPressed(DeleteListEvent e) {
        controller.deleteList(e);
    }

    public void stopSide() {
        controller.stopSide();
    }
}
