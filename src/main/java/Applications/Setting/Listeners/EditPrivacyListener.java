package Applications.Setting.Listeners;

import Applications.Setting.Controllers.EditPrivacyController;
import Models.Events.ChangePrivacyEvent;

public class EditPrivacyListener {
    private EditPrivacyController controller;
    public EditPrivacyListener(EditPrivacyController controller) {
        this.controller = controller;
    }

    public void privacySettingChanged(ChangePrivacyEvent changePrivacyEvent) {
        controller.changePrivacy(changePrivacyEvent);
    }
}
