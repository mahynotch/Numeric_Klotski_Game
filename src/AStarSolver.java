import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;

public class AStarSolver {
    Board board;

    HashMap<Distribution, Distribution> closeList = new HashMap<>();

    Queue<Board> openList = new PriorityQueue<>((o1, o2) -> o1.H - o2.H);

    int[][] sorted;

    int[] blankIndexList;

    String[] solution = null;

    int minStep = -1;

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
        Distribution distribution = new Distribution(board, 0);
        closeList.put(distribution, distribution);
        openList.add(board);
        Direction[] directions = {Direction.UP, Direction.LEFT, Direction.DOWN, Direction.RIGHT};
        while (true) {
            Board present = openList.remove();
            for (int ind : blankIndexList) {
                for (Direction direction : directions) {
                    Board newBoard = present.clone();
                    Piece blank = newBoard.pieces[ind];
                    Object[] objects = newBoard.zeroMovableWithAim(blank, direction);
                    if ((Boolean) objects[0]) {
                        Coordinate temp = ((Piece) objects[1]).coordinate[0];
                        if (isSorted[temp.y][temp.x]) {
                            continue;
                        }
                        newBoard.move(blank, direction);
                        distribution = new Distribution(newBoard, 0);
                        int H = calculateH(distribution);
                        if (H == 0) {
                            board = newBoard;
                            solution = newBoard.steps.toArray(new String[0]);
                            return;
                        }
                        if (closeList.containsKey(distribution)) {
                            continue;
                        }
                        newBoard.H = H;
                        openList.add(newBoard);
                        closeList.put(distribution, distribution);
                    }
                }
            }
        }
    }

    public int calculateH(Distribution distribution) {
        int H = 0;
        for (int i = 0; i < sorted.length; i++) {
            for (int j = 0; j < sorted[0].length; j++) {
                int val = distribution.dist[i][j];
                if (val == 0) continue;
                int[] xAndY = RandomBoardGenerator.findValue(sorted, val);
                H += Math.abs(j - xAndY[0]) + Math.abs(i - xAndY[1]);
            }
        }
        return H;
    }
}
