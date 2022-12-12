import java.util.*;

public class AStarSolver {
    Board board;

    HashSet<Distribution> closeList = new HashSet<>();

    Queue<Board> openList = new PriorityQueue<>(Comparator.comparingInt(o -> o.H));

    int[][] sorted;

    int[] blankIndexList;

    String[] solution = null;

    boolean[][] isSorted;

    public AStarSolver(Board board) {
        this.board = board;
        sorted = Main.sort2D(board.to2DArray());
        isSorted = new boolean[sorted.length][sorted[0].length];
    }

    public void solve() throws CloneNotSupportedException {
        blankIndexList = board.findAllIndexByValue(0);
        aStar();
    }

    public void aStar() throws CloneNotSupportedException {
        Distribution distribution = new Distribution(board);
        closeList.add(distribution);
        openList.add(board);
        Direction[] directions = {Direction.UP, Direction.LEFT, Direction.DOWN, Direction.RIGHT};
        while (true) {
            Board present = openList.remove();
            for (int ind : blankIndexList) {
                for (Direction direction : directions) {
                    Board newBoard = present.clone();
                    Piece blank = newBoard.pieces[ind];
                    Object[] objects = newBoard.zeroMovableWithAim(blank, direction);
                    if ((boolean) objects[0]) {
                        if (((Piece) objects[1]).pieceType == PieceType.BLANK) continue;
                        newBoard.move(blank, direction);
                        distribution = new Distribution(newBoard);
                        int H = calculateH(newBoard);
                        if (H == 0) {
                            board = newBoard;
                            solution = newBoard.steps.toArray(new String[0]);
                            return;
                        }
                        if (closeList.contains(distribution)) {
                            continue;
                        }
                        newBoard.H = H;
                        openList.add(newBoard);
                        closeList.add(distribution);
                    }
                }
            }
            if (Runtime.getRuntime().freeMemory() < 10) throw new RuntimeException();
        }
    }

    public int calculateH(Board board) {
        int H = 0;
        for (int i = 0; i < board.pieces.length; i++) {
            int val = board.pieces[i].getValue()[0];
            if (val == 0) continue;
            int[] xAndY = RandomBoardGenerator.findValue(sorted, val);
            H += Math.abs(board.pieces[i].coordinate[0].x - xAndY[0]) + Math.abs(board.pieces[i].coordinate[0].y - xAndY[1]);
        }
        return H;
    }
}
