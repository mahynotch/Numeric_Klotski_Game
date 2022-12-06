//import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

//本类为棋子
public class Piece extends JLabel implements Cloneable {
    private int[] value;
    Coordinate[] coordinate;
    PieceType pieceType;

    public Piece(int[] value, PieceType pieceType, Coordinate[] coordinate) {
        this.value = value;
        this.coordinate = coordinate;
        this.pieceType = pieceType;
        setLocation(coordinate[0].getX()*100,coordinate[0].getY()*100);
        setBorder(BorderFactory.createRaisedBevelBorder());
        setHorizontalAlignment(SwingConstants.CENTER);
        switch(this.pieceType){
            case BLANK:{
                setBorder(BorderFactory.createLoweredBevelBorder());
                setSize(100,100);
                setBackground(new Color(199,210,212));
                setText(" ");
                break;
            }
            case ONETOONE : {
                setSize(100,100);
                setBackground(new Color(76, 128, 69));
                setText(String.valueOf(value[0]));
                break;
            }
            case ONETOTWO:{
                setSize(200,100);
                setBackground(new Color(104,148,92));
                setText(String.valueOf(value[0])
                        + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                        + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&ensp;"
                        + String.valueOf(value[1]));
                break;
            }
            case TWOTOONE:{
                setSize(100,200);
                setBackground(new Color(104,148,92));
                setText("<html><body>"+String.valueOf(value[0])
                        +"<br>"+" "+"<br>"+" "+ "<br>"+" "+"<br>"+" "+"<br>"+" "+"<br>"
                        + String.valueOf(value[1])+"<body></html>");
                break;
            }
            case TWOTOTWO:{
                setSize(200,200);
                setBackground(new Color(122, 134, 108));
                setText("<html><body>"+String.valueOf(value[0])
                        + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                        + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&ensp;"
                        + String.valueOf(value[1])
                        +"<br>"+" "+"<br>"+" "+ "<br>"+" "+"<br>"+" "+"<br>"+" "+"<br>"
                        + String.valueOf(value[2])
                        + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                        + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&ensp;"
                        + String.valueOf(value[3])+"<body></html>");
                break;
            }
    }setOpaque(true);
        setVisible(true);
    }


    public void move(Direction direction) {
        switch (direction) {
            case UP:
                innerMove(0, -1);
                return;
            case DOWN:
                innerMove(0, 1);
                return;
            case LEFT:
                innerMove(-1, 0);
                return;
            case RIGHT:
                innerMove(1, 0);
        }
    }

    private void innerMove(int dx, int dy) {
        for (Coordinate i : coordinate) {
            i.move(dx, dy);
        }
    }

    public Coordinate[] getCoordinate() {
        return coordinate;
    }

    public int[] getValue() {
        return value;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Piece clone = (Piece) super.clone();
        clone.value = value.clone();
        clone.coordinate = coordinate.clone();
        return clone;
    }
}
