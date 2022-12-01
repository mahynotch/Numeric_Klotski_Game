import org.jetbrains.annotations.NotNull;

//本类为棋子
public class Piece implements Cloneable {
    private int[] value;
    Coordinate[] coordinate;
    PieceType pieceType;

    public Piece(int[] value, PieceType pieceType, Coordinate[] coordinate) {
        this.value = value;
        this.coordinate = coordinate;
        this.pieceType = pieceType;
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
