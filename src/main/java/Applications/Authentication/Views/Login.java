package Applications.Authentication.Views;

import Applications.Authentication.Controllers.LoginController;
import FileHandling.ImageHandler;
import FileHandling.Properties;
import GraphicComponents.GraphicReconnectButton;
import Models.Events.AuthEvent;
import Utils.GraphicAgent;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Login {
    private final TextField userField;
    private final PasswordField passField;
    private final Label responseMessage;
    private final LoginController loginController;
    public Login() {
        loginController = new LoginController(this);
        Stage stage = GraphicAgent.stage;
        VBox vBox = new VBox(Properties.loadSize("big-spacing"));
        vBox.setId("loginPanel");
        StackPane mainPane = new StackPane(vBox);
        Scene scene = new Scene(mainPane);
        GraphicAgent.mainPane = mainPane;
        scene.getStylesheets().add("style.css");
        ImageView imageView = ImageHandler.getImage("MediumLogo");
        Hyperlink hyperlink = new Hyperlink(Properties.loadDialog("register"));
        vBox.setAlignment(Pos.CENTER);
        userField = new TextField();
        passField = new PasswordField();
        responseMessage = new Label();
        responseMessage.setId("error");
        Button SubmitButton = new Button(Properties.loadDialog("login-button"));
        HBox userBox = new HBox(15, new Label(Properties.loadDialog("username")), userField);
        HBox passBox = new HBox(15, new Label(Properties.loadDialog("password")), passField);
        HBox submitBox = new HBox(SubmitButton);
        HBox responseBox = new HBox(responseMessage);
        userBox.setAlignment(Pos.CENTER);
        passBox.setAlignment(Pos.CENTER);
        submitBox.setAlignment(Pos.CENTER);
        responseBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(imageView);
        vBox.getChildren().add(userBox);
        vBox.getChildren().add(passBox);
        vBox.getChildren().add(submitBox);
        vBox.getChildren().add(responseBox);
        vBox.getChildren().add(hyperlink);
        stage.setScene(scene);
        SubmitButton.setOnAction(this::loginButtonPressed);
        hyperlink.setOnAction(this::goToRegister);
        GraphicReconnectButton.setButton();
    }

    private void loginButtonPressed(ActionEvent actionEvent) {
        loginController.login(new AuthEvent(userField.getText(), passField.getText()),responseMessage);
    }

    private void goToRegister(ActionEvent actionEvent) {
        new Register();
    }
}
