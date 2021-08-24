package Applications.Setting.Listeners;

import Applications.Setting.Controllers.EditProfileController;
import Models.Events.EditProfileEvent;

public class EditProfileListener {
    private EditProfileController controller;
    public EditProfileListener(EditProfileController editProfileController) {
        this.controller = editProfileController;
    }


    public void submitButtonPressed(EditProfileEvent event) {
        controller.editProfile(event);
    }
}
