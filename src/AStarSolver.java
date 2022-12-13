import java.util.*;

public class AStarSolver {
    Board board;
    //已访问过，hashSet查重快
    HashSet<Distribution> closeList = new HashSet<>();
    //待访问的队列，此处定义该PQ以H值为依据排序
    Queue<Board> openList = new PriorityQueue<>(Comparator.comparingInt(o -> o.H));
    //储存排序后位置
    int[][] sorted;
    //空白棋子
    int[] blankIndexList;

    String[] solution = null;

    boolean[][] isSorted;

    public AStarSolver(Board board) {
        this.board = board;
        //获得排序后数组
        sorted = Main.sort2D(board.to2DArray());
        isSorted = new boolean[sorted.length][sorted[0].length];
    }

    public void solve() throws CloneNotSupportedException {
        //获得所有空白块
        blankIndexList = board.findAllIndexByValue(0);
        aStar();
    }

    public void aStar() throws CloneNotSupportedException {
        //加载初始
        Distribution distribution = new Distribution(board);
        closeList.add(distribution);
        openList.add(board);
        Direction[] directions = {Direction.UP, Direction.LEFT, Direction.DOWN, Direction.RIGHT};
        while (true) {
            //获得队首的Board，由于使用PQ，这里得到的是H最最小的
            Board present = openList.remove();
            for (int ind : blankIndexList) {
                for (Direction direction : directions) {
                    Board newBoard = present.clone();
                    //获得下一个空白块
                    Piece blank = newBoard.pieces[ind];
                    Object[] objects = newBoard.zeroMovableWithAim(blank, direction);
                    if ((boolean) objects[0]) {
                        //向空白块移动无效，跳过
                        if (((Piece) objects[1]).pieceType == PieceType.BLANK) continue;
                        newBoard.move(blank, direction);
                        distribution = new Distribution(newBoard);
                        //获得H
                        int H = calculateH(newBoard);
                        //H == 0时即所有块归位
                        if (H == 0) {
                            board = newBoard;
                            solution = newBoard.steps.toArray(new String[0]);
                            return;
                        }
                        //查重
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

    //计算H值（此处为曼哈顿距离）
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
