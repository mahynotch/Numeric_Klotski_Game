//本类为棋子
public class Piece {
    private int value;
    Coordinate coordinate;

    public Piece(int value, int x, int y) {
        this.value = value;
        this.coordinate = new Coordinate(x, y);
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public int getValue() {
        return value;
    }
}
