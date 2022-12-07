import java.util.Arrays;
import java.util.Objects;
import javax.swing.*;
import javax.swing.text.Style;
import java.awt.*;

//本类为棋子
public class Piece extends JLabel implements Cloneable {
    private int[] value;
    Coordinate[] coordinate;
    PieceType pieceType;
    String code;

    public Piece(int[] value, PieceType pieceType, Coordinate[] coordinate) {
        this.value = value;
        this.coordinate = coordinate;
        this.pieceType = pieceType;
        this.code = code;
        setLocation(coordinate[0].getX() * 100, coordinate[0].getY() * 100);
        setBorder(BorderFactory.createRaisedBevelBorder());
        setHorizontalAlignment(SwingConstants.CENTER);
        switch (this.pieceType) {
            case BLANK: {
                setBorder(BorderFactory.createLoweredBevelBorder());
                setSize(100, 100);
                setBackground(new Color(199, 210, 212));
                setText(" ");
                break;
            }
            case ONETOONE: {
                setSize(100, 100);
                setBackground(new Color(76, 128, 69));
                setText(String.valueOf(value[0]));
                break;
            }
            case ONETOTWO: {
                setSize(200, 100);
                setBackground(new Color(104, 148, 92));
                setText("<html><body>" + String.valueOf(value[0])
                        + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                        + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&ensp;"
                        + String.valueOf(value[1])+"<body></html>");
                break;
            }
            case TWOTOONE: {
                setSize(100, 200);
                setBackground(new Color(104, 148, 92));
                setText("<html><body>" + String.valueOf(value[0])
                        + "<br>" + " " + "<br>" + " " + "<br>" + " " + "<br>" + " " + "<br>" + " " + "<br>"
                        + String.valueOf(value[1]) + "<body></html>");
                break;
            }
            case TWOTOTWO: {
                setSize(200, 200);
                setBackground(new Color(122, 134, 108));
                setText("<html><body>" + String.valueOf(value[0])
                        + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                        + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&ensp;"
                        + String.valueOf(value[1])
                        + "<br>" + " " + "<br>" + " " + "<br>" + " " + "<br>" + " " + "<br>" + " " + "<br>"
                        + String.valueOf(value[2])
                        + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                        + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&ensp;"
                        + String.valueOf(value[3]) + "<body></html>");
                break;
            }
        }
        setOpaque(true);
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

    public Coordinate[] cloneOfCoordinates() {
        Coordinate[] newCoord = new Coordinate[coordinate.length];
        for (int i = 0; i < coordinate.length; i++) {
            newCoord[i] = (Coordinate) coordinate[i].clone();
        }
        return newCoord;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Piece clone = (Piece) super.clone();
        clone.value = value.clone();
        clone.coordinate = cloneOfCoordinates();
        return clone;
    }
    /*@Override
    public boolean equals(Object o) {
       if (this == o) return true;
        if (!(o instanceof Piece)) return false;
        Piece piece = (Piece) o;
        return code.equals(piece.code) &&
                Arrays.equals(value, piece.value) &&
                Arrays.equals(coordinate, piece.coordinate) &&
                pieceType == piece.pieceType;
    }*/

    @Override
    public int hashCode() {
        int result = Objects.hash(pieceType, code);
        result = 31 * result + Arrays.hashCode(value);
        result = 31 * result + Arrays.hashCode(coordinate);
        return result;
    }
}
