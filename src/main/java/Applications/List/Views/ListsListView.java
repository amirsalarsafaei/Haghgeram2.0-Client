package Applications.List.Views;

import Applications.List.Listeners.ListsListListener;
import Applications.TimeLine.Controllers.TimeLineController;
import FileHandling.ImageHandler;
import FileHandling.Properties;
import GraphicComponents.GraphicHeaderFooter;
import GraphicComponents.GraphicMenu;
import GraphicComponents.GraphicReconnectButton;
import Models.Events.AddListEvent;
import Models.Events.DeleteListEvent;
import Models.Events.ShowListEvent;
import Models.Responses.ListsListResponse;
import Utils.AutoUpdatingView;
import Utils.GraphicAgent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ListsListView implements AutoUpdatingView {
    private Stage stage;
    private Scene scene;
    private StackPane Pane;
    private VBox Groups;
    private Label AddGroupErrorLabel;
    private TextField addGroupField;
    private ListsListListener listener;
    private BorderPane borderPane;
    private String last;
    private  BorderPane addAndScroll;
    public ListsListView(ListsListListener listener) {
        this.listener = listener;
        stage = GraphicAgent.stage;
        borderPane = new BorderPane();
        Pane = new StackPane(borderPane);
        Scene scene = new Scene(Pane);
        GraphicAgent.mainPane = Pane;
        scene.getStylesheets().add("style.css");
        borderPane.setTop(GraphicHeaderFooter.header((e)->{
            hide();
            new TimeLineController();
        }));
        stage.setScene(scene);
        borderPane.setLeft(GraphicMenu.Menu(this));
        VBox rightPanel = new VBox();
        rightPanel.setPrefWidth(Properties.loadSize("group-page-right-panel"));
        rightPanel.getChildren().add(new Label(Properties.loadDialog("no-group-selected")));
        rightPanel.setAlignment(Pos.CENTER);
        BorderPane rightPanelBorderPane = new BorderPane();
        rightPanelBorderPane.setCenter(rightPanel);
        borderPane.setRight(rightPanelBorderPane);
        ScrollPane scrollPane = new ScrollPane();
        BorderPane groupPane = new BorderPane();
        borderPane.setCenter(groupPane);
        Groups = new VBox();

        scrollPane.setContent(Groups);
        addAndScroll = new BorderPane();
        addAndScroll.setCenter(scrollPane);
        ImageView AddGroupButton = ImageHandler.getImage("add-group");
        addGroupField = new TextField();
        addGroupField.setPromptText(Properties.loadDialog("enter-list-name"));
        scrollPane.fitToWidthProperty().setValue(true);
        HBox addGroupHBox = new HBox(Properties.loadSize("medium-indent"), addGroupField,AddGroupButton);
        addGroupHBox.setAlignment(Pos.CENTER);
        AddGroupErrorLabel = new Label();
        AddGroupErrorLabel.setId("error");
        VBox addGroupBox = new VBox(Properties.loadSize("small-spacing"), addGroupHBox, AddGroupErrorLabel);
        addGroupBox.setId("grey-border");
        addGroupBox.setPadding(new Insets(Properties.loadSize("big-indent")));
        addGroupBox.setAlignment(Pos.CENTER);
        addAndScroll.setTop(addGroupBox);
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        groupPane.setCenter(addAndScroll);
        AddGroupButton.setOnMouseClicked(this::addListButtonPressed);
        GraphicReconnectButton.setButton();
    }

    private void addListButtonPressed(MouseEvent mouseEvent) {
        listener.addListButtonPressed(new AddListEvent(addGroupField.getText()));
    }


    public void refresh(ListsListResponse listsListResponse) {
        Groups.getChildren().clear();

        for (String listName: listsListResponse.getLists()) {
            StackPane stackPane = new StackPane();
            VBox hBox = new VBox(stackPane);
            Label name = new Label(listName);
            ImageView trashIcon = ImageHandler.getImage("trash");
            HBox nameBox = new HBox(name);
            HBox trashBox = new HBox(trashIcon);
            nameBox.setAlignment(Pos.CENTER_LEFT);
            trashBox.setAlignment(Pos.CENTER_RIGHT);
            stackPane.getChildren().add(nameBox);
            stackPane.getChildren().add(trashBox);
            hBox.setPadding(new Insets(Properties.loadSize("medium-indent")));

            if (last != null && last.equals(listName))
                hBox.setId("blue");
            VBox lineBox =  new VBox(hBox);
            lineBox.setId("down-line-black");
            Groups.getChildren().add(lineBox);
            hBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    last = listName;
                    listener.showList(new ShowListEvent(listName));
                }
            });
            trashIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (last != null && last.equals(listName)) {
                        listener.stopSide();
                    }
                    listener.trashButtonPressed(new DeleteListEvent(listName));
                }
            });
        }
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    public StackPane getPane() {
        return Pane;
    }

    public void setPane(StackPane pane) {
        Pane = pane;
    }

    @Override
    public void hide() {
        listener.stopRefreshing();
    }

    @Override
    public void show() {
        listener.startRefreshing();
        stage.setScene(scene);
    }

    @Override
    public void refreshNow() {

    }

    public void setError(String error) {
        AddGroupErrorLabel.setText(error);
    }


}
