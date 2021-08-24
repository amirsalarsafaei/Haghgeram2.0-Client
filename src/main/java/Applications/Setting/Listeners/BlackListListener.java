package Applications.Setting.Listeners;

import Applications.Setting.Controllers.BlackListController;
import Models.Events.UnBlockButtonPressedEvent;
import Utils.AutoUpdatingListener;

public class BlackListListener implements AutoUpdatingListener {
    private BlackListController controller;
    public BlackListListener(BlackListController controller) {
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
    }

    public void unblockButtonPressed(UnBlockButtonPressedEvent event) {
        controller.unblock(event);
    }
}
