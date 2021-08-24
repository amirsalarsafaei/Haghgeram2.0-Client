package GraphicListeners;

import GraphicController.ButtonController;
import Models.Responses.SmallUserResponse;
import Utils.AutoUpdatingView;

public class ButtonListener {
    private ButtonController buttonController;

    public ButtonListener(AutoUpdatingView autoUpdatingView) {
        buttonController = new ButtonController(autoUpdatingView);
    }

    public void MuteUserListener(SmallUserResponse smallUserResponse) {
        buttonController.MuteUser(smallUserResponse);
    }
    public void BlockUserListener(SmallUserResponse smallUserResponse) {
        buttonController.BlockUser(smallUserResponse);
    }
}
