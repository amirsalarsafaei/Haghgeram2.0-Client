package Applications.SelfProfile.Listeners;

import Applications.SelfProfile.Controllers.SelfProfileController;
import Utils.AutoUpdatingListener;

public class SelfProfileListener implements AutoUpdatingListener {
    SelfProfileController controller;
    public SelfProfileListener(SelfProfileController controller) {
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
