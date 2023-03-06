
//Md Reaz Morshed mdmo9317
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.*;
import java.util.*;


public class Main extends Application {
    private ObservableList<String> categories = FXCollections.observableArrayList("Bus", "Underground", "Train");
    private ListView<String> listView = new ListView<>(categories);

    private VBox top;
    private VBox right;
    private VBox top2;
    private Stage stage;
    private ImageView imageView = new ImageView();
    private Pane center;
    private BorderPane root;

    private RadioButton named;
    private RadioButton descrived;
    private TextField searchField;

    private HashMap<Coordinate, Place> coordinatePlace = new HashMap<>();
    private HashMap<String,HashSet<Coordinate>>categortyToCoordinate=new HashMap<String,HashSet<Coordinate>>();
    private HashMap<Triangel, Coordinate> coordinateTriangle = new HashMap<>();
    private HashMap<String, HashSet<Place>> searchWithName = new HashMap<String, HashSet<Place>>();

    private HashSet<Place> placesList = new HashSet<>();

    private HashSet<Coordinate> markedList = new HashSet<>();
    private HashSet<String>savePlace=new HashSet<>();

    private ClickHandler cl = new ClickHandler();

    public void start(Stage stage) {
        this.stage = stage;
        root = new BorderPane();

        center = new Pane();// i have changed here from borderpane to pane
        center.setLayoutX(0);
        center.setLayoutY(90);
        root.getChildren().add(center);
        //root.setCenter(center);
        center.getChildren().add(imageView);


        top = new VBox();
        top.setPadding(new Insets(0, 5, 0, 5));
        root.setTop(top);

        MenuBar menuBar = new MenuBar();
        top.getChildren().add(menuBar);
        Menu fileMenu = new Menu("File");
        menuBar.getMenus().add(fileMenu);
        MenuItem loadMap = new MenuItem("Load Map");
        loadMap.setOnAction(new LoadHandler());
        fileMenu.getItems().add(loadMap);
        MenuItem loadplaces = new MenuItem("Load Places");
        loadplaces.setOnAction(new LoadPlaceHandler());
        fileMenu.getItems().add(loadplaces);
        MenuItem save = new MenuItem("Save");
        save.setOnAction(new SaveHandler());
        fileMenu.getItems().add(save);
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(e -> Platform.exit());
        fileMenu.getItems().add(exit);

        FlowPane controls = new FlowPane(Orientation.HORIZONTAL);
        controls.setHgap(5);
        controls.setAlignment(Pos.CENTER);
        controls.setPadding(new Insets(10));
        top.getChildren().add(controls);
        Button newButton = new Button("New");
        newButton.setOnAction(new NewButtonHandler());
        top2 = new VBox();
        top2.setSpacing(10);
        named = new RadioButton("Named");
        descrived = new RadioButton("Descrived ");
        top2.getChildren().addAll(named, descrived);
        ToggleGroup group = new ToggleGroup();
        group.getToggles().addAll(named, descrived);
        named.setSelected(true);
        searchField = new TextField();
        Button search = new Button("Search");
        search.setOnAction(new SearchFiledHandler());
        Button hide = new Button("Hide");
        hide.setOnAction(new HideButtonHandler());
        Button remove = new Button("Remove");
        remove.setOnAction(new RemoveButtonHandler());
        Button coordinates = new Button("Coordinates");
        coordinates.setOnAction(new NewCoordinateHandler());
        controls.getChildren().addAll(top2, newButton, searchField, search, hide, remove, coordinates);

        listView.getSelectionModel().selectedItemProperty() .addListener(new ListHandler());

        right = new VBox();
        right.setAlignment(Pos.CENTER);
        root.setRight(right);
        Label categories = new Label("Categories");
        listView.setPrefSize(200, 100);
        Button hideCategories = new Button("Hide Categories");
        hideCategories.setOnAction(new HideButtonHandler());
        right.getChildren().addAll(categories, listView, hideCategories);


        Scene scene = new Scene(root, 1200, 840);
        stage.setScene(scene);
        stage.show();
    } // start

    public static void main(String[] args) {
        launch(args);
    }

    class LoadHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(stage);
            if (file == null)
                return;
            String filename = file.getPath();
            Image image = new Image("file:" + filename);
            System.out.println(filename);
            imageView.setImage(image);

            //imageView.setFitWidth(650);
            //imageView.setFitHeight(400);
            //stage.sizeToScene();
        }
    }

    class LoadPlaceHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(stage);
            if (file == null)
                return;
            String filename = file.getPath();
            BufferedReader reader;
            try{
                reader = new BufferedReader(new FileReader(filename));
                String line = reader.readLine();
                while (line != null) {
                    //System.out.println(line+' '+line.length());
                    if(line.length()>0)
                    createPlaceArray(line);
                    // read next line
                    line = reader.readLine();
                }
                reader.close();
            }catch(Exception e){System.out.println(e);}
        }
    }

    class SaveHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            try {
                clearTheFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream outputStream = null;
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(stage);
            if (file == null)
                return;
            String filename = file.getPath();
            try {
                outputStream = new FileOutputStream(filename);
                for(Place data:placesList){ //C:\Users\sumay\Karta_backup\input.txt
                    byte[] strToBytes = data.toString().getBytes();
                    outputStream.write(strToBytes);
                }
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void clearTheFile() throws IOException {
        FileWriter fwOb = new FileWriter("C:\\Users\\sumay\\Karta_backup\\input.txt", false);
        PrintWriter pwOb = new PrintWriter(fwOb, false);
        pwOb.flush();
        pwOb.close();
        fwOb.close();
    }

    public void createPlaceArray(String text){
        if(text==null) return;
        savePlace.add(text);
        ArrayList<String>info=new ArrayList<>();
        StringTokenizer st = new StringTokenizer(text,",");
        while (st.hasMoreTokens()) {
            info.add(st.nextToken());
        }
        if(info.get(0).equals("Named")){
            info.add(null);
        }
        if(info.get(1).equals("None")){
            info.set(1,"[]");
        }else{
            info.set(1,"["+info.get(1)+"]");
        }
        createPlace(info);
    }

    public void createPlace(ArrayList<String>info){
        Place tmp;
        String category=info.get(1);
        Coordinate coordinate=new Coordinate(Integer.parseInt(info.get(2)),Integer.parseInt(info.get(3)));
        String name=info.get(4);
        String description=info.get(5);
        if(info.get(0).equals("Named")){
            tmp = new NamedPlace(coordinate, category, name);
        }else{
            tmp = new DescrivedPlace(coordinate, category, name, description);
        }
        tmp.setVisible();
        center.getChildren().add(tmp.getTriangle());
        tmp.getTriangle().setOnMouseClicked(cl);
        coordinatePlace.put(coordinate, tmp);
        coordinateTriangle.put(tmp.getTriangle(), coordinate);
        placesList.add(tmp);
        String key = name;
        if (searchWithName.get(key) == null) {
            searchWithName.put(key, new HashSet<>());
        }
        if(categortyToCoordinate.get(category)==null){
            categortyToCoordinate.put(category,new HashSet<>());
        }
        System.out.println(category);
        categortyToCoordinate.get(category).add(coordinate);
        searchWithName.get(key).add(tmp);
        placesList.add(tmp);
        System.out.println("to string"+tmp);
        unselectListView();
    }

    class NewClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            String category;
            String name;
            String description;
            String text;
            category = listView.getSelectionModel().getSelectedItems().toString();
            category = category.substring(1, category.length()-1);
            if(category.isEmpty()){
                category="None";
            }
            center.setOnMouseClicked(null);
            center.setCursor(Cursor.DEFAULT);

            if(named.isSelected()){
                text="Named";
                NameNewLocation newLocation = new NameNewLocation();
                Optional<ButtonType> answer2 = newLocation.showAndWait();
                name=newLocation.getName();
                description="";
            }else{
                text="Described";
                DescrivedNewLocation newLocation = new DescrivedNewLocation();
                Optional<ButtonType> answer2 = newLocation.showAndWait();
                name=newLocation.getName();
                description=newLocation.getDescrived();
            }
            text=text+","+category+","+String.valueOf(x)+","+String.valueOf(y)+","+name;
            if(description.isEmpty()==false)
                text=text+","+description;
            System.out.println(text);
            createPlaceArray(text);
        }
    }

    class NewButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            center.setOnMouseClicked(new NewClickHandler());
            center.setCursor(Cursor.CROSSHAIR);
        }
    }

    class NewCoordinateHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            try {                                                              // lecture 6, minute 20, exception handling in dailouge box
                AlertForCoordinates getCoordinate = new AlertForCoordinates();
                Optional<ButtonType> answer2 = getCoordinate.showAndWait();
                if (answer2.isPresent() && answer2.get() == ButtonType.OK) {
                    int x = getCoordinate.getxCoordinate();
                    int y = getCoordinate.getyCoordinate();
                    Coordinate coordinate = new Coordinate(x, y);
                    if (coordinatePlace.containsKey(coordinate)) {
                        Place tmp = coordinatePlace.get(coordinate);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "No place is registered");
                        alert.showAndWait();
                    }
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Only numbers");
                alert.showAndWait();
            }
        }
    }

    class HideButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            for (Coordinate cur : markedList) {
                if (coordinatePlace.containsKey(cur)) {
                    Place tmp = coordinatePlace.get(cur);
                    center.getChildren().removeAll(tmp.getTriangle());
                }
            }
            setUnmark();
            markedList.clear();
            unselectListView();
        }
    }

    class RemoveButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            for (Coordinate cur : markedList) {
                if (coordinatePlace.containsKey(cur)) {
                    Place tmp = coordinatePlace.get(cur);
                    placesList.remove(tmp);
                    String cat=tmp.getCategory();
                    System.out.println(cat);
                    categortyToCoordinate.get(cat).remove(cur);
                    center.getChildren().remove(tmp.getTriangle());
                    searchWithName.get(tmp.getName()).remove(coordinatePlace.get(cur));
                    coordinatePlace.remove(cur);
                    coordinateTriangle.remove(tmp.getTriangle());
                }
            }
            setUnmark();
            markedList.clear();
            unselectListView();
        }
    }

    public String selectedCategory() {
        return listView.getSelectionModel().getSelectedItems().toString();
    }

    class ClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            Triangel b = (Triangel) event.getSource();
            Coordinate coordinate = coordinateTriangle.get(b);
            Place tmp = coordinatePlace.get(coordinate);
            if (event.getButton () == MouseButton.PRIMARY) {
                if (tmp.getMark() == false) {
                    tmp.setMark();
                    markedList.add(coordinate);
                } else {
                    tmp.setMark();
                    markedList.remove(coordinate);
                }
            }else if (event.getButton() == MouseButton.SECONDARY && markedList.size()<=1){
                String text;
                text="Name: "+tmp.getName()+' '+tmp.getCoordinate().toString();
                if(tmp.getDescription()!=null){
                    text=text+'\n'+"Description: "+tmp.getDescription();
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle("Info");
                alert.setContentText(text);
                alert.showAndWait();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR,"More then one place selected");
                alert.showAndWait();
            }
        }
    }

    public void setUnmark() {
        for (Coordinate cur : markedList) {
            Place tmp = coordinatePlace.get(cur);
            if(tmp!=null)
                tmp.setMark();
        }
        markedList.clear();
    }

    class SearchFiledHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            String place = searchField.getText();
            System.out.println(place);
            System.out.println("ekhnae");
            HashSet<Place> list = searchWithName.get(place);
            setUnmark();
            if(list!=null) {
                for (Place cur : list) {
                    if (center.getChildren().contains(cur.getTriangle()) == false) {
                        center.getChildren().add(cur.getTriangle());
                    }
                    cur.setMark();
                    markedList.add(cur.getCoordinate());
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "No such place");
                alert.showAndWait();
            }
        }
    }

    class ListHandler implements ChangeListener<String> {
        @Override
        public void changed(ObservableValue obs, String old, String nev) {
            nev="["+nev+"]";
            setUnmark();
            HashSet<Coordinate>tmp=categortyToCoordinate.get(nev);
            System.out.println(tmp);
            if(tmp!=null && tmp.size()>0){
                for(Coordinate cur:tmp){
                    Place place=coordinatePlace.get(cur);
                    if(place!=null) {
                        if (center.getChildren().contains(place.getTriangle()) == false) {
                            center.getChildren().add(place.getTriangle());
                        }
                        place.setMark();
                        markedList.add(cur);
                    }
                }
            }
        }
    }

    public void unselectListView(){
        listView.getSelectionModel().clearSelection();
    }
}
