package GraphicComponents;

import FileHandling.ImageHandler;
import GraphicListeners.ButtonListener;
import Models.Responses.BigUserResponse;
import Models.Responses.SmallUserResponse;
import Utils.AutoUpdatingView;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class GraphicButtons {
    private ButtonListener buttonListener;
    private AutoUpdatingView autoUpdatingView;
    public GraphicButtons(AutoUpdatingView autoUpdatingView) {
        this.autoUpdatingView = autoUpdatingView;
        buttonListener = new ButtonListener(autoUpdatingView);
    }
    public ImageView muteButton(SmallUserResponse smallUserResponse) {
        ImageView mute;
        mute = ImageHandler.getImage("mute");
        mute.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                buttonListener.MuteUserListener(smallUserResponse);
            }
        });
        return mute;
    }

    public ImageView blockButton(SmallUserResponse smallUserResponse) {
        ImageView mute;
        mute = ImageHandler.getImage("block");
        mute.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                buttonListener.BlockUserListener(smallUserResponse);
            }
        });
        return mute;
    }


    public ImageView muteButton(BigUserResponse bigUserResponse) {
        ImageView mute;
        if (bigUserResponse.isMuted())
            mute = ImageHandler.getImage("unmute");
        else
            mute = ImageHandler.getImage("mute");
        mute.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                buttonListener.MuteUserListener(new SmallUserResponse(bigUserResponse));
            }
        });
        return mute;
    }


    public ImageView blockButton(BigUserResponse bigUserResponse) {
        ImageView block;
        if (bigUserResponse.isBlocked())
            block = ImageHandler.getImage("unblock");
        else
            block = ImageHandler.getImage("block");
        block.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                buttonListener.BlockUserListener(new SmallUserResponse(bigUserResponse));
            }
        });
        return block;
    }
}
