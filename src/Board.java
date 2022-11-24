import edu.princeton.cs.algs4.RedBlackBST;

//本类用以储存棋盘
public class Board {
    Piece[][] pieces;
    RedBlackBST<Integer, Piece> redBlackBST = new RedBlackBST<>();

    public Board(Piece[][] pieces) {
        this.pieces = pieces;
        for (Piece[] i : pieces) {
            for (Piece j : i) {
                redBlackBST.put(j.getValue(), j);
            }
        }
    }

    public Piece findPieceByValue(int key) {
        return redBlackBST.get(key);
    }

    public Piece findPieceByCoordinate(int x, int y) {
        Coordinate coordinate = new Coordinate(x, y);
        for (Piece[] i : pieces) {
            for (Piece j : i) {
                if (j.getCoordinate().equals(coordinate)) {
                    return j;
                }
            }
        }
        throw new IllegalArgumentException("The coordinate is empty");
    }
}
