package Applications.Authentication.Views;

import Applications.Authentication.Controllers.RegisterController;
import GraphicComponents.GraphicReconnectButton;
import Models.Events.AuthEvent;
import Utils.GraphicAgent;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import FileHandling.Properties;
import java.io.File;

public class Register {
    File[] selectedFile = {null};
    Stage stage;
    Label requiredError;
    TextField nameField;
    TextField FamilyNameField;
    TextField usernameField;
    TextField passField;
    TextField passField2;
    TextField emailField;
    TextField phoneField;
    TextArea bio;
    DatePicker birthdateField;
    RegisterController registerController;
    public Register() {
        registerController = new RegisterController(this);
        stage = GraphicAgent.stage;
        VBox vBox = new VBox(Properties.loadSize("small-spacing"));
        vBox.setAlignment(Pos.BASELINE_CENTER);
        StackPane stackPane = new StackPane(vBox);
        GraphicAgent.mainPane = stackPane;
        Scene scene = new Scene(stackPane);
        scene.getStylesheets().add("style.css");
        vBox.setPadding(new Insets(Properties.loadSize("huge-indent"), 0, 0, Properties.loadSize("small-indent")));
        vBox.setId("registerPanel");
        Label name = new Label(Properties.loadDialog("name")+"*");
        Label FamilyName = new Label(Properties.loadDialog("surname")+"*");
        Label username = new Label(Properties.loadDialog("username")+"*");
        Label password = new Label(Properties.loadDialog("password") + "*");
        Label password2 = new Label(Properties.loadDialog("repeat-password") + "*");
        Label Email = new Label(Properties.loadDialog("Email") );
        Label Phone = new Label(Properties.loadDialog("phone"));
        Label Birthdate = new Label(Properties.loadDialog("birthday"));
        Label Bio = new Label(Properties.loadDialog("bio"));
        Label usernameError = new Label();
        usernameError.setId("error");
        Label passwordError = new Label();
        passwordError.setId("error");
        Label emailError = new Label();
        emailError.setId("error");
        Label phoneError = new Label();
        phoneError.setId("error");
        nameField = new TextField();
        FamilyNameField = new TextField();
        usernameField = new TextField();
        passField = new TextField();
        passField2 = new TextField();
        emailField = new TextField();
        phoneField = new TextField();
        requiredError = new Label();
        birthdateField = new DatePicker();
        requiredError.setId("error");
        requiredError.setStyle("-fx-font-size: 8;");
        bio = new TextArea();
        bio.setPrefColumnCount(Properties.loadNumbers("register-bio-column-count"));
        bio.setPrefRowCount(Properties.loadNumbers("register-bio-row-count"));
        Button submitButton = new Button(Properties.loadDialog("register-button"));
        vBox.getChildren().add(new HBox(Properties.loadSize("small-spacing"), name, nameField));
        vBox.getChildren().add(new HBox(Properties.loadSize("small-spacing"), FamilyName, FamilyNameField));
        vBox.getChildren().add(new HBox(Properties.loadSize("small-spacing"), username, usernameField
                , usernameError));
        vBox.getChildren().add(new HBox(Properties.loadSize("small-spacing"), password, passField
                , passwordError));
        vBox.getChildren().add(new HBox(Properties.loadSize("small-spacing"), password2, passField2));
        vBox.getChildren().add(new HBox(Properties.loadSize("small-spacing"), Email, emailField, emailError));
        vBox.getChildren().add(new HBox(Properties.loadSize("small-spacing"), Phone, phoneField, phoneError));
        vBox.getChildren().add(new HBox(Properties.loadSize("small-spacing"), Birthdate, birthdateField));
        vBox.getChildren().add(new HBox(Properties.loadSize("small-spacing"), Bio, bio));
        Button uploadPhoto = new Button(Properties.loadDialog("upload-photo-button"));
        vBox.getChildren().add(uploadPhoto);



        uploadPhoto.setOnAction(this::UploadPhotoButtonSelected);
        vBox.getChildren().add(requiredError);
        vBox.getChildren().add(submitButton);


        submitButton.setOnAction(this::RegisterButtonPressed);
        stage.setScene(scene);
        GraphicReconnectButton.setButton();
    }

    private void RegisterButtonPressed(ActionEvent actionEvent) {
        if (passField.getText().equals(passField2.getText()))
            registerController.register(new AuthEvent(usernameField.getText(), passField.getText(), nameField.getText(),
                    FamilyNameField.getText(), bio.getText(), emailField.getText(), phoneField.getText(), birthdateField.getValue())
                    , requiredError, selectedFile[0]);
        else
            requiredError.setText("Passwords don't match");
    }

    private void UploadPhotoButtonSelected(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        selectedFile[0] = fileChooser.showOpenDialog(stage);
    }
}
