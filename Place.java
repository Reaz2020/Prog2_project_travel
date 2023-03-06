//Md Reaz Morshed mdmo9317
import javafx.scene.paint.Color;
public class Place {
    private Coordinate coordinate;
    private String category;
    private  Triangel mytriangle;
    private boolean mark;
    private String name;
    private String description;

    public Place(Coordinate coordinate,String category,String name) {
        this.coordinate = coordinate;
        this.category=category;
        this.mark=false;
        this.name=name;
        this.description=null;
    }
    public Place(Coordinate coordinate,String category,String name,String description) {
        this.coordinate = coordinate;
        this.category=category;
        this.mark=false;
        this.name=name;
        this.description=description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }
    public String getCategory() {
        return category;
    }

    public void setVisible()
    {
        mytriangle=new Triangel(coordinate.getxCoordinate(),coordinate.getyCoordinate(),category);
    }

    public void hide()
    {
        mytriangle=null;
    }

    public Triangel getTriangle()
    {
        return mytriangle;
    }

    public boolean getMark()
    {
        return mark;
    }

    public void setMarkTrue(){
        mark=false;
    }

    public void setMark(){
        mark=!mark;
        if(mark){
            mytriangle.setFill(Color.YELLOW);
        }else{
            mytriangle.setColor();
        }
    }

    @Override
    public String toString() {
        String text;
        String cat;
        cat = category.substring(1, category.length()-1);
        if(cat.isEmpty()){
            cat="None";
        }
        text=","+cat+","+String.valueOf(coordinate.getxCoordinate())+","+String.valueOf(coordinate.getyCoordinate())+","+name;
        if(description==null){
            text="Named"+text;
        }else{
            text="Described"+text+","+description;
        }

        return text+"\n";
    }
}
