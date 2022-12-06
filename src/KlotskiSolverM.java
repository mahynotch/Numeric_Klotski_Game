import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class KlotskiSolverM {
    Board board;
    int maxStep = 1000;
    HashMap<Integer, Distribution> distributions = new HashMap<>();
    int[][] sorted;
    int[] blankIndexList;
    String[] solution = null;

    int minStep = -1;

    boolean[][] isSorted;

    public KlotskiSolverM(Board board) {
        this.board = board;
        sorted = Main.sort2D(board.to2DArray());
        isSorted = new boolean[sorted.length][sorted[0].length];
    }

    public void solve() throws CloneNotSupportedException {
        blankIndexList = board.findAllIndexByValue(0);
        if (blankIndexList.length == 1) {
            singleSolver();
        } else {
            multiSolver();
        }
    }

    public void singleSolver() throws CloneNotSupportedException {
        int x, y;
        if (sorted.length <= 2 || sorted[0].length <= 2) {
            singleSolverDFS(0);
            return;
        }
        for (int i = 0; i < sorted.length; i++) {
            for (int j = 0; j < sorted[0].length; j++) {
                isSorted[i][j] = false;
            }
        }
        int ind = sorted.length < sorted[0].length ? sorted.length - 2 : sorted[0].length - 2;
        for (int i = 0; i < ind; i++) {
            for (int j = 0; j < sorted[0].length - 2; j++) {
                Distribution distribution = new Distribution(board, 0);
                if (sorted[i][j] != distribution.dist[i][j]) {
                    int present = sorted[i][j];
                    x = j;
                    y = i;
                    singleSolverSimplifier(x, y, present);
                }
                isSorted[i][j] = true;
            }
            singleSolverSimplifier(i);
            isSorted[i][isSorted[0].length - 1] = true;
            isSorted[i][isSorted[0].length - 2] = true;
            for (int j = 0; j < sorted[0].length - 2; j++) {
                Distribution distribution = new Distribution(board, 0);
                if (sorted[i][j] != distribution.dist[i][j]) {
                    int present = sorted[i][j];
                    x = j;
                    y = i;
                    singleSolverSimplifier(x, y, present);
                }
                isSorted[i][j] = true;
            }
            singleSolverSimplifier(i, 0);
            isSorted[isSorted.length - 1][i] = true;
            isSorted[isSorted.length - 2][i] = true;
        }
        singleSolverDFS(0);
    }

    public void singleSolverSimplifier(int x, int y, int value) throws CloneNotSupportedException {
        Distribution distribution = new Distribution(board, 0);
        distributions.put(distribution.hashCode(), distribution);
        Queue<Board> queue = new Queue<>();
        queue.enqueue(board);
        Direction[] directions = {Direction.UP, Direction.LEFT, Direction.DOWN, Direction.RIGHT};
        while (true) {
            Board present = queue.dequeue();
            for (Direction direction : directions) {
                Board newBoard = present.clone();
                Piece blank = newBoard.pieces[blankIndexList[0]];
                if (direction == newBoard.lastDirection) {
                    continue;
                }
                Object[] objects = newBoard.zeroMovableWithAim(blank, direction);
                if ((Boolean) objects[0]) {
                    Coordinate temp = ((Piece) objects[1]).coordinate[0];
                    if (isSorted[temp.y][temp.x]) {
                        continue;
                    }
                    newBoard.move(blank, direction);
                    distribution = new Distribution(newBoard, 0);
                    if (distribution.dist[y][x] == value) {
                        board = newBoard;
                        distributions = new HashMap<>();
                        return;
                    }
                    if (distributions.containsKey(distribution.hashCode())) {
                        continue;
                    }
                    queue.enqueue(newBoard);
                    distributions.put(distribution.hashCode(), distribution);
                }
            }
        }
    }

    public void singleSolverSimplifier(int y) throws CloneNotSupportedException {
        Distribution distribution = new Distribution(board, 0);
        distributions.put(distribution.hashCode(), distribution);
        Queue<Board> queue = new Queue<>();
        queue.enqueue(board);
        Direction[] directions = {Direction.UP, Direction.LEFT, Direction.DOWN, Direction.RIGHT};
        while (true) {
            Board present = queue.dequeue();
            for (Direction direction : directions) {
                Board newBoard = present.clone();
                Piece blank = newBoard.pieces[blankIndexList[0]];
                if (direction == newBoard.lastDirection) {
                    continue;
                }
                Object[] objects = newBoard.zeroMovableWithAim(blank, direction);
                if ((Boolean) objects[0]) {
                    Coordinate temp = ((Piece) objects[1]).coordinate[0];
                    if (isSorted[temp.y][temp.x]) {
                        continue;
                    }
                    newBoard.move(blank, direction);
                    distribution = new Distribution(newBoard, 0);
                    if (intArrayEqual(distribution.dist[y], sorted[y])) {
                        board = newBoard;
                        distributions = new HashMap<>();
                        return;
                    }
                    if (distributions.containsKey(distribution.hashCode())) {
                        continue;
                    }
                    queue.enqueue(newBoard);
                    distributions.put(distribution.hashCode(), distribution);
                }
            }
        }
    }

    public void singleSolverSimplifier(int x, int justSign) throws CloneNotSupportedException {
        Distribution distribution = new Distribution(board, 0);
        distributions.put(distribution.hashCode(), distribution);
        Queue<Board> queue = new Queue<>();
        queue.enqueue(board);
        Direction[] directions = {Direction.UP, Direction.LEFT, Direction.DOWN, Direction.RIGHT};
        while (true) {
            Board present = queue.dequeue();
            for (Direction direction : directions) {
                Board newBoard = present.clone();
                Piece blank = newBoard.pieces[blankIndexList[0]];
                if (direction == newBoard.lastDirection) {
                    continue;
                }
                Object[] objects = newBoard.zeroMovableWithAim(blank, direction);
                if ((Boolean) objects[0]) {
                    Coordinate temp = ((Piece) objects[1]).coordinate[0];
                    if (isSorted[temp.y][temp.x]) {
                        continue;
                    }
                    newBoard.move(blank, direction);
                    distribution = new Distribution(newBoard, 0);
                    if (intArrayEqualColumn(sorted, distribution.dist, x)) {
                        board = newBoard;
                        distributions = new HashMap<>();
                        return;
                    }
                    if (distributions.containsKey(distribution.hashCode())) {
                        continue;
                    }
                    queue.enqueue(newBoard);
                    distributions.put(distribution.hashCode(), distribution);
                }
            }
        }
    }


    public void singleSolverDFS(int step) {
        Board board1 = board;
        Distribution distribution = new Distribution(board, step);
        Piece blank = board1.pieces[blankIndexList[0]];
        if (Arrays.deepEquals(distribution.dist, sorted)) {
            if (minStep == -1 || board1.steps.size() < minStep) {
                solution = board1.steps.toArray(new String[0]);
                minStep = solution.length;
                return;
            }
            return;
        }
        if (step >= maxStep) {
            return;
        }
        Distribution lastSame = distributions.get(distribution.hashCode());
        if (lastSame != null) {
            if (lastSame.step < step) {
                return;
            } else {
                distributions.remove(distribution.hashCode());
            }
        }
        distributions.put(distribution.hashCode(), distribution);
        Direction[] directions = {Direction.DOWN, Direction.RIGHT, Direction.UP, Direction.LEFT};
        for (int i = 0; i < 4; i++) {
            Direction direction = directions[i];
            Object[] objects = board1.zeroMovableWithAim(blank, direction);
            if ((boolean) objects[0]) {
                Coordinate coordinate = ((Piece) objects[1]).coordinate[0];
                if (!isSorted[coordinate.y][coordinate.x]) {
                    board1.move(blank, direction);
                    singleSolverDFS(step + 1);
                    board1.move(blank, Board.counterDirection(direction));
                }
            }
        }
    }

    public void singleSolverBFS() throws CloneNotSupportedException {
        Distribution distribution = new Distribution(board, 0);
        distributions.put(distribution.hashCode(), distribution);
        Queue<Board> queue = new Queue<>();
        queue.enqueue(board);
        Direction[] directions = {Direction.DOWN, Direction.RIGHT, Direction.UP, Direction.LEFT};
        while (true) {
            Board present = queue.dequeue();
            for (Direction direction : directions) {
                Board newBoard = present.clone();
                if (direction == newBoard.lastDirection) {
                    continue;
                }
                Piece blank = newBoard.pieces[blankIndexList[0]];
                if (newBoard.zeroMovable(blank, direction)) {
                    newBoard.move(blank, direction);
                    distribution = new Distribution(newBoard, 0);
                    if (Arrays.deepEquals(distribution.dist, sorted)) {
                        solution = newBoard.steps.toArray(new String[0]);
                        return;
                    }
                    if (distributions.containsKey(distribution.hashCode())) {
                        continue;
                    }
                    queue.enqueue(newBoard);
                    distributions.put(distribution.hashCode(), distribution);
                }
            }
        }
    }

    public void multiSolver() throws CloneNotSupportedException {
        int x, y;
        isSorted = new boolean[sorted.length][sorted[0].length];
        for (int i = 0; i < sorted.length; i++) {
            for (int j = 0; j < sorted[0].length; j++) {
                isSorted[i][j] = false;
            }
        }
        for (int i = 0; i < sorted.length - 2; i++) {
            for (int j = 0; j < sorted[0].length; j++) {
                Distribution distribution = new Distribution(board, 0);
                if (sorted[i][j] != distribution.dist[i][j]) {
                    int present = sorted[i][j];
                    x = j;
                    y = i;
                    multiSolverSimplifier(x, y, present);
                    distributions = new HashMap<>();
                }
                isSorted[i][j] = true;
            }
        }
        multiSolverBFS();
    }

    public void multiSolverSimplifier(int x, int y, int value) throws CloneNotSupportedException {
        Distribution distribution = new Distribution(board, 0);
        distributions.put(distribution.hashCode(), distribution);
        Queue<Board> queue = new Queue<>();
        queue.enqueue(board);
        Direction[] directions = {Direction.UP, Direction.LEFT, Direction.DOWN, Direction.RIGHT};
        while (true) {
            Board present = queue.dequeue();
            for (int i = 0; i < blankIndexList.length; i++) {
                for (Direction direction : directions) {
                    Board newBoard = present.clone();
                    Piece blank = newBoard.pieces[blankIndexList[i]];
                    if (direction == newBoard.lastDirection) {
                        continue;
                    }
                    Object[] objects = newBoard.zeroMovableWithAim(blank, direction);
                    if ((Boolean) objects[0]) {
                        Coordinate temp = ((Piece) objects[1]).coordinate[0];
                        if (isSorted[temp.y][temp.x] || ((Piece) objects[1]).pieceType == PieceType.BLANK) {
                            continue;
                        }
                        newBoard.move(blank, direction);
                        distribution = new Distribution(newBoard, 0);
                        if (distribution.dist[y][x] == value) {
                            board = newBoard;
                            return;
                        }
                        if (distributions.containsKey(distribution.hashCode())) {
                            continue;
                        }
                        queue.enqueue(newBoard);
                        distributions.put(distribution.hashCode(), distribution);
                    }
                }
            }
        }
    }

    public void multiSolverBFS() throws CloneNotSupportedException {
        Distribution distribution = new Distribution(board, 0);
        distributions.put(distribution.hashCode(), distribution);
        Queue<Board> queue = new Queue<>();
        queue.enqueue(board);
        Direction[] directions = {Direction.UP, Direction.LEFT, Direction.DOWN, Direction.RIGHT};
        while (true) {
            Board present = queue.dequeue();
            for (int i = 0; i < blankIndexList.length; i++) {
                for (Direction direction : directions) {
                    Board newBoard = present.clone();
                    Piece blank = newBoard.pieces[blankIndexList[i]];
                    if (direction == newBoard.lastDirection) {
                        continue;
                    }
                    Object[] objects = newBoard.zeroMovableWithAim(blank, direction);
                    if ((Boolean) objects[0]) {
                        Coordinate temp = ((Piece) objects[1]).coordinate[0];
                        if (isSorted[temp.y][temp.x] || ((Piece) objects[1]).pieceType == PieceType.BLANK) {
                            continue;
                        }
                        newBoard.move(blank, direction);
                        distribution = new Distribution(newBoard, 0);
                        if (Arrays.deepEquals(distribution.dist, sorted)) {
                            solution = newBoard.steps.toArray(new String[0]);
                            board = newBoard;
                            return;
                        }
                        if (distributions.containsKey(distribution.hashCode())) {
                            continue;
                        }
                        queue.enqueue(newBoard);
                        distributions.put(distribution.hashCode(), distribution);
                    }
                }
            }
        }
    }

    public static boolean intArrayEqual(int[] a, int[] b) {
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) return false;
        }
        return true;
    }

    public static boolean intArrayEqualColumn(int[][] a, int[][] b, int x) {
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) {
            if (a[i][x] != b[i][x]) return false;
        }
        return true;
    }

    public static Coordinate findDest(Coordinate coordinate, Direction direction) throws CloneNotSupportedException {
        Coordinate coordinate1 = (Coordinate) coordinate.clone();
        switch (direction) {
            case UP:
                coordinate1.move(0, -1);
            case DOWN:
                coordinate1.move(0, 1);
            case LEFT:
                coordinate1.move(-1, 0);
            case RIGHT:
                coordinate1.move(1, 0);
        }
        return coordinate1;
    }
}

