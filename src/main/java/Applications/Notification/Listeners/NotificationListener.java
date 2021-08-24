package Applications.Notification.Listeners;

import Applications.Notification.Controllers.NotificationController;
import Models.Events.AcceptFollowRequestEvent;
import Models.Events.DeniedFollowRequestEvent;
import Utils.AutoUpdatingListener;

public class NotificationListener implements AutoUpdatingListener {

    private NotificationController controller;

    public NotificationListener(NotificationController controller) {
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

    public void AcceptRequestButtonPressed(AcceptFollowRequestEvent event) {
        controller.acceptRequest(event);
    }

    public void RejectRequestButtonPressed(DeniedFollowRequestEvent event) {
        controller.rejectRequest(event);
    }
}
