package Applications.Setting.Views;

import Applications.Setting.Listeners.EditPrivacyListener;
import FileHandling.Properties;
import GraphicComponents.GraphicHeaderFooter;
import GraphicComponents.GraphicReconnectButton;
import Models.Events.ChangePrivacyEvent;
import Models.LastSeen;
import Models.Responses.EditPrivacyResponse;
import Utils.AutoUpdatingView;
import Utils.GraphicAgent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
public class EditPrivacyView implements AutoUpdatingView {
    public EditPrivacyView(EditPrivacyResponse editPrivacyResponse, EditPrivacyListener listener) {
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(GraphicHeaderFooter.headerToSetting(this));
        StackPane stackPane = new StackPane(borderPane);
        Scene scene = new Scene(stackPane);
        GraphicAgent.mainPane = stackPane;
        scene.getStylesheets().add("style.css");
        VBox vBox = new VBox(Properties.loadSize("edit-privacy-spacing"));
        Label lastSeenLabel = new Label(Properties.loadDialog("lastSeen"));
        Label privateLabel = new Label(Properties.loadDialog("privateAccount"));
        ChoiceBox<LastSeen> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().add(LastSeen.Everyone);
        choiceBox.getItems().add(LastSeen.Followings);
        choiceBox.getItems().add(LastSeen.noOne);
        choiceBox.setValue(editPrivacyResponse.getLastSeen());
        CheckBox checkBox = new CheckBox(), deActive = new CheckBox();
        checkBox.setSelected(editPrivacyResponse.isPrivate());
        deActive.setSelected(editPrivacyResponse.isDeActive());
        HBox lastSeenBox = new HBox(Properties.loadSize("big-spacing"), lastSeenLabel, choiceBox);
        HBox privateBox = new HBox(Properties.loadSize("big-spacing"), privateLabel, checkBox);
        HBox deActiveBox = new HBox(Properties.loadSize("big-spacing"), new Label("DeActive Account"), deActive);
        lastSeenBox.setAlignment(Pos.CENTER);
        vBox.setAlignment(Pos.CENTER);
        privateBox.setAlignment(Pos.CENTER);
        deActiveBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(lastSeenBox);
        vBox.getChildren().add(privateBox);
        vBox.getChildren().add(deActiveBox);
        borderPane.setCenter(vBox);
        GraphicAgent.stage.setScene(scene);
        choiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                listener.privacySettingChanged(new
                        ChangePrivacyEvent(IndexToLastSeen(choiceBox.getSelectionModel().getSelectedIndex()), deActive.isSelected(),
                        checkBox.isSelected()));
            }
        });
        checkBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                listener.privacySettingChanged(new
                        ChangePrivacyEvent(IndexToLastSeen(choiceBox.getSelectionModel().getSelectedIndex()), deActive.isSelected(),
                        checkBox.isSelected()));
            }
        });
        deActive.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                listener.privacySettingChanged(new
                        ChangePrivacyEvent(IndexToLastSeen(choiceBox.getSelectionModel().getSelectedIndex()), deActive.isSelected(),
                        checkBox.isSelected()));
            }
        });
        GraphicReconnectButton.setButton();
    }

    public LastSeen IndexToLastSeen(int a) {
        if (a == 0)
            return LastSeen.Everyone;
        else if (a == 1)
            return LastSeen.Followings;
        return LastSeen.noOne;
    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }

    @Override
    public void refreshNow() {

    }
}
