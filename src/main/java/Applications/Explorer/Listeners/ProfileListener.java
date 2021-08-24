package Applications.Explorer.Listeners;

import Applications.Explorer.Controllers.ProfileController;
import Models.Events.FollowEvent;
import Models.Events.SendMessageToUserEvent;
import Utils.AutoUpdatingListener;

public class ProfileListener implements AutoUpdatingListener {
    ProfileController profileController;
    public ProfileListener(ProfileController profileController) {
        this.profileController = profileController;
    }
    @Override
    public void startRefreshing() {
        profileController.start();
    }

    @Override
    public void stopRefreshing() {
        profileController.stop();
    }

    @Override
    public void refresh() {
        profileController.getData(false);
    }

    public void back() {
        profileController.goBack();
    }

    public void followButtonPressed(FollowEvent e) {
        profileController.toggleFollow(e);
    }

    public void sendMessageButtonPressed(SendMessageToUserEvent sendMessageToUserEvent) {
        profileController.sendMessage(sendMessageToUserEvent);
    }
}
