package Applications.Setting.Listeners;

import Applications.Setting.Controllers.SettingMenuController;

public class SettingMenuListener {
    private SettingMenuController controller;
    public SettingMenuListener(SettingMenuController controller) {
        this.controller = controller;
    }

    public void logOutButtonPressed() {
        this.controller.logOut();
    }

    public void deleteAccountButtonPressed() {
        controller.deleteAccount();
    }
}
