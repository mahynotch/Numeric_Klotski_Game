import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class KlotskiSolverM {
    Board board;
    int maxStep = 1000;
    ArrayList<Distribution> distributions = new ArrayList<>();
    int[][] sorted;

    Piece[] blankList;

    String[] solution = null;

    public KlotskiSolverM(Board board) {
        this.board = board;
        sorted = Main.sort2D(board.to2DArray());
    }

    public void solve() throws CloneNotSupportedException {
        blankList = board.findAllPieceByValue(0);
        if (blankList.length == 1) {
            for (int i = 1000; i > 0; i = i - 100) {
                maxStep = i;
                singleSolverDFS(0);
                distributions = new ArrayList<>();
                if (solution == null) {
                    maxStep = maxStep + 100;
                    singleSolverDFS(0);
                    break;
                }
                solution = null;
            }
        } else {
            multiSolverDFS(0);
        }
    }

    public void singleSolverDFS(int step) throws CloneNotSupportedException {
        Distribution distribution = new Distribution(board);
        if (step >= maxStep) {
            return;
        }
        if (distributions.contains(distribution)) {
            return;
        }
        if (Arrays.deepEquals(distribution.dist, sorted)) {
            solution = board.steps.toArray(new String[0]);
            return;
        }
        distributions.add(distribution);
        Direction[] directions = {Direction.DOWN, Direction.RIGHT, Direction.UP, Direction.LEFT};
        for (Direction direction : directions) {
            if (board.zeroMovable(blankList[0], direction)) {
                board.move(blankList[0], direction);
                singleSolverDFS(step + 1);
                board.move(blankList[0], Board.counterDirection(direction));
            }
        }
    }

    public void multiSolverDFS(int step) throws CloneNotSupportedException {
        Distribution distribution = new Distribution(board);
        if (distributions.contains(distribution)) {
            return;
        }
        if (Arrays.deepEquals(distribution.dist, sorted)) {
            solution = board.steps.toArray(new String[0]);
            return;
        }
        distributions.add(distribution);
        Direction[] directions = {Direction.DOWN, Direction.RIGHT, Direction.UP, Direction.LEFT};
        for (Piece piece : blankList)
            for (Direction direction : directions) {
                if (board.zeroMovable(piece, direction)) {
                    board.move(piece, direction);
                    multiSolverDFS(step + 1);
                    board.move(piece, Board.counterDirection(direction));
                }
            }
    }
}

