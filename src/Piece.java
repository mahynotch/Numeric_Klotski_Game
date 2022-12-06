import java.util.Arrays;
import java.util.Objects;

//本类为棋子
public class Piece implements Cloneable {
    private int[] value;
    Coordinate[] coordinate;
    PieceType pieceType;
    String code;

    public Piece(int[] value, PieceType pieceType, Coordinate[] coordinate) {
        this.value = value;
        this.coordinate = coordinate;
        this.pieceType = pieceType;
        this.code = code;
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

    public Piece() {
    }

    @Override
    protected Piece clone() {
        Piece clone = new Piece();
        if (value != null) {
            clone.value = new int[value.length];
            System.arraycopy(value, 0, clone.value, 0, value.length);
        }
        if (coordinate != null) {
            clone.coordinate = new Coordinate[coordinate.length];
            for (int i = 0; i < coordinate.length; i++) {
                clone.coordinate[i] = coordinate[i].clone();
            }
        }
        clone.pieceType = pieceType;
        clone.code = code;
        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Piece)) return false;
        Piece piece = (Piece) o;
        return code.equals(piece.code) &&
                Arrays.equals(value, piece.value) &&
                Arrays.equals(coordinate, piece.coordinate) &&
                pieceType == piece.pieceType;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(pieceType, code);
        result = 31 * result + Arrays.hashCode(value);
        result = 31 * result + Arrays.hashCode(coordinate);
        return result;
    }
}
