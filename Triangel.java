//Md Reaz Morshed mdmo9317

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.Objects;

public class Triangel extends Polygon {
    private int xcoordinate;
    private int ycoordinate;
    private String category;
    public Triangel(int x, int y, String category) {
        super(x, y, x - 15, y - 30, x + 15, y - 30);
        this.xcoordinate = x;
        this.ycoordinate =y;
        this.category=category;
        setColor();
    }
    public void setColor()
    {
        if(category.equals("[Bus]")) {
            setFill(Color.RED);
            setStroke(Color.RED);
        }else if(category.equals("[Train]")){
            setFill(Color.GREEN);
            setStroke(Color.GREEN);
        }else if(category.equals("[Underground]")){
            setFill(Color.BLUE);
            setStroke(Color.BLUE);
        }else{
            setFill(Color.BLACK);
            setStroke(Color.BLACK);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triangel triangel = (Triangel) o;
        return xcoordinate == triangel.xcoordinate &&
                ycoordinate == triangel.ycoordinate &&
                Objects.equals(category, triangel.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(xcoordinate, ycoordinate, category);
    }
}