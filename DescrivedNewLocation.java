//Md Reaz Morshed mdmo9317
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
public class DescrivedNewLocation extends Alert {
    private TextField nameField = new TextField();
    private TextField descrivedField = new TextField();

    public DescrivedNewLocation() {
        super(AlertType.CONFIRMATION);
        GridPane grid1 = new GridPane();
        grid1.addRow(0, new Label("Name: "), nameField);
        grid1.addRow(1, new Label("Description: "), descrivedField);
        getDialogPane().setContent(grid1);// we have put our label and field inside the dialog pane.
        setHeaderText(null);// taking off the header
        setTitle("Name");
    }
    public String getName() {
        return nameField.getText();
    }
    public String getDescrived() {
        return descrivedField.getText();
    }
}