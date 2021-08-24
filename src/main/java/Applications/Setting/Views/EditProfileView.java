package Applications.Setting.Views;

import Applications.Setting.Listeners.EditProfileListener;
import FileHandling.Properties;
import GraphicComponents.GraphicHeaderFooter;
import GraphicComponents.GraphicReconnectButton;
import Models.Events.EditProfileEvent;
import Models.Responses.UserInformationResponse;
import Utils.AutoUpdatingView;
import Utils.Convertors;
import Utils.GraphicAgent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;

public class EditProfileView implements AutoUpdatingView {

    private Label error;
    public EditProfileView(UserInformationResponse user, EditProfileListener listener) {
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(GraphicHeaderFooter.headerToSetting(this));
        StackPane stackPane = new StackPane(borderPane);
        Scene scene = new Scene(stackPane);
        GraphicAgent.mainPane = stackPane;
        scene.getStylesheets().add("style.css");
        GraphicAgent.scene = scene;
        TextField name = new TextField(user.getName());
        TextField familyName = new TextField(user.getFamilyName());
        TextField email = new TextField(user.getEmail());
        TextField phone = new TextField(user.getPhone());
        PasswordField passwordField = new PasswordField();
        passwordField.setText(user.getPassword());
        TextArea bio = new TextArea(user.getBio());
        DatePicker birthDate = new DatePicker();
        Button submit = new Button(Properties.loadDialog("submit"));
        Label nameError = new Label();
        nameError.setId("error");
        Label FamilyNameError = new Label();
        FamilyNameError.setId("error");
        Label emailError = new Label();
        emailError.setId("error");
        Label phoneError = new Label();
        phoneError.setId("error");
        Label passwordError = new Label();
        phoneError.setId("error");
        HBox nameBox = new HBox(Properties.loadSize("small-spacing"), new Label(
                Properties.loadDialog("name")), name, nameError);
        HBox familyNameBox = new HBox(Properties.loadSize("small-spacing"), new Label(
                Properties.loadDialog("surname")), familyName, FamilyNameError);
        HBox emailBox = new HBox(Properties.loadSize("small-spacing"), new Label(
                Properties.loadDialog("Email")), email, emailError);
        HBox phoneBox = new HBox(Properties.loadSize("small-spacing"), new Label(
                Properties.loadDialog("phone")), phone, phoneError);
        HBox passwordBox = new HBox(Properties.loadSize("small-spacing"), new Label(
                Properties.loadDialog("password")), passwordField, passwordError);
        HBox bioBox = new HBox(Properties.loadSize("small-spacing"), new Label(
                Properties.loadDialog("bio")), bio);
        HBox birthdateBox = new HBox(Properties.loadSize("small-spacing"), new Label(
                Properties.loadDialog("birthday")), birthDate);
        error = new Label();
        error.setId("error");
        VBox submitBox = new VBox(submit, error);
        bio.setPrefRowCount(3);
        submitBox.setAlignment(Pos.CENTER);
        passwordBox.setAlignment(Pos.CENTER);
        nameBox.setAlignment(Pos.CENTER);
        familyNameBox.setAlignment(Pos.CENTER);
        emailBox.setAlignment(Pos.CENTER);
        phoneBox.setAlignment(Pos.CENTER);
        bioBox.setAlignment(Pos.CENTER);
        birthdateBox.setAlignment(Pos.CENTER);
        Button uploadPhoto = new Button(Properties.loadDialog("upload-photo-button"));
        HBox uploadPhotoBox = new HBox(uploadPhoto);
        uploadPhotoBox.setAlignment(Pos.CENTER);
        VBox editBox = new VBox(Properties.loadSize("medium-spacing"),nameBox, familyNameBox, emailBox, phoneBox, passwordBox,bioBox, birthdateBox,
                uploadPhotoBox, submitBox);
        editBox.setPadding(new Insets(Properties.loadSize("big-indent")));
        borderPane.setCenter(editBox);
        FileChooser fileChooser = new FileChooser();
        final File[] selectedFile = {null};

        uploadPhoto.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                selectedFile[0] = fileChooser.showOpenDialog(GraphicAgent.stage);
            }
        });
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                EditProfileEvent event = new EditProfileEvent(name.getText(), familyName.getText(), passwordField.getText(),
                        bio.getText(), email.getText(), birthDate.getValue(), Convertors.imageFileToByte(selectedFile[0]),
                        phone.getText());
                listener.submitButtonPressed(event);
            }
        });
        GraphicAgent.stage.setScene(scene);
        GraphicReconnectButton.setButton();
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

    public void setError(String data) {
        error.setText(data);
    }
}
