import Applications.Authentication.Views.Login;
import Applications.TimeLine.Controllers.TimeLineController;
import FileHandling.Properties;
import Holder.LoggedInHolder;
import Utils.GraphicAgent;
import Utils.Initialize;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class main {
    public static Stage stage;
    public static class YourRealApplication extends Application {

        @Override
        public void start(Stage stage) throws Exception {
            stage.setTitle("HaghGeram");
            stage.setHeight(Properties.loadSize("frame-height"));
            stage.setWidth(Properties.loadSize("frame-width"));
            stage.setAlwaysOnTop(true);
            stage.show();
            try {
                new Initialize();
            } catch (FileNotFoundException e) {
                System.exit(-1);
            }
            GraphicAgent.stage = stage;
            if (!LoggedInHolder.loggedIn)
                new Login();
            else
                new TimeLineController();
        }

    }
    public static void main(String[] args) {

        Application.launch(YourRealApplication.class);

    }

}