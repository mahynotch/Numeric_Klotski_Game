//本类为棋子
public class Piece {
    private final int[] value;
    Coordinate[] coordinate;
    PieceType pieceType;

    public Piece(int[] value, PieceType pieceType, Coordinate[] coordinate) {
        this.value = value;
        this.coordinate = coordinate;
        this.pieceType = pieceType;
    }

    public Coordinate[] getCoordinate() {
        return coordinate;
    }

    public int[] getValue() {
        return value;
    }
}
