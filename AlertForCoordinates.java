//Md Reaz Morshed mdmo9317
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
public class AlertForCoordinates extends Alert {
    private TextField xCoordinate = new TextField();
    private TextField yCoordinate = new TextField();

    public AlertForCoordinates() {
        super(AlertType.CONFIRMATION);
        GridPane grid1 = new GridPane();
        grid1.addRow(0, new Label("x: "), xCoordinate);
        grid1.addRow(1, new Label("y: "), yCoordinate);
        getDialogPane().setContent(grid1);// we have put our label and field inside the dialog pane.
        setHeaderText(null);// taking off the header
        setTitle("Name");
    }

    public int getxCoordinate() {
        return Integer.parseInt(xCoordinate.getText());
    }

    public int getyCoordinate() {
        return Integer.parseInt(yCoordinate.getText());
    }
}