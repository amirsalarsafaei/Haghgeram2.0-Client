package Applications.Setting.Listeners;

import Applications.Setting.Controllers.FollowRequestStatusController;
import Utils.AutoUpdatingListener;

public class FollowRequestStatusListener implements AutoUpdatingListener {
    private FollowRequestStatusController controller;

    public FollowRequestStatusListener(FollowRequestStatusController controller) {
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
}
