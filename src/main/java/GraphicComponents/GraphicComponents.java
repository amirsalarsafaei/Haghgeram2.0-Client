package GraphicComponents;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import FileHandling.Properties;
import java.time.LocalDateTime;

public class GraphicComponents {

    public static HBox date(LocalDateTime time) {
        String res = "";
        Label label = new Label();
        HBox hBox = new HBox(label);
        hBox.setAlignment(Pos.CENTER);
        label.setId("date-in-chat");
        label.setPadding(new Insets(Properties.loadSize("tiny-indent"),Properties.loadSize("medium-indent")
                , Properties.loadSize("tiny-indent"), Properties.loadSize("medium-indent")));
        if (time.getYear() == LocalDateTime.now().getYear() && time.getDayOfYear() == LocalDateTime.now().getDayOfYear()) {
            label.setText(Properties.loadDialog("today"));
            return hBox;
        }
        if (time.getYear() != LocalDateTime.now().getYear())
            res += time.getYear() + " ";
        res += time.getMonth() + " " + time.getDayOfMonth();
        label.setText(res);
        return hBox;
    }


    public static VBox RightPanel() {
        VBox rightPanel = new VBox();
        rightPanel.setPrefWidth(Properties.loadSize("right-panel"));
        return rightPanel;
    }
}