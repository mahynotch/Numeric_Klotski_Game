import java.util.ArrayList;
import java.util.Arrays;

public class KlotskiSolverM {
    Board board;
    int step = 0;
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
            singleSolver(step);
        }
    }

    public void singleSolver(int step) throws CloneNotSupportedException {
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
        for (Direction direction : directions) {
            if (board.zeroMovable(blankList[0], direction)) {
                board.move(blankList[0], direction);
                singleSolver(step + 1);
                board.move(blankList[0], Board.counterDirection(direction));
            }
        }
    }
}

