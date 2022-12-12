import java.util.*;
import java.util.stream.Collectors;

public class BFS {
    public static Board bfs(Board board) throws CloneNotSupportedException {
        //存储棋盘状态
        List<String> boardStatus = new ArrayList<>();
        //队列
        PriorityQueue<Board> queue = new PriorityQueue<>(Comparator.comparingInt(o -> o.H));
        //方向
        Direction[] directions = {Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT};
        //棋盘正确状态
        String correctStatus = board.getCorrectStatus();
        //第一个根节点入队列
        queue.add(board.clone());
        //存储当前棋盘状态
        String status = board.getStatus();
        boardStatus.add(status);
        //队列不为空，则一直循环
        while (queue.size() > 0) {
            //获取队列中第一个根节点
            Board first = queue.poll();
            //获取该节点上的所有数字0
            Piece[] zeroPieces = first.findAllPieceByValue(0);
            //遍历所有数字0
            for (Piece piece : zeroPieces) {
                //判断四个方向是否可移动，可移动则将移动后的棋盘依次入栈
                for (int j = 0; j < 4; j++) {
                    Board clone = first.clone();
                    Piece zeroPiece = clone.findPieceByCoordinate(piece.getCoordinate()[0].getX(), piece.getCoordinate()[0].getY());
                    Direction direction = directions[j];
                    boolean movable = clone.zeroMovable(zeroPiece, direction);
                    if (!movable) {
                        continue;
                    }
                    //判断移动方向的数字是否也为数字0，是的话则跳过
                    int x = zeroPiece.getCoordinate()[0].getX();
                    int y = zeroPiece.getCoordinate()[0].getY();
                    Piece next;
                    switch (direction) {
                        case LEFT: {
                            next = clone.findPieceByCoordinate(x - 1, y);
                            break;
                        }
                        case DOWN: {
                            next = clone.findPieceByCoordinate(x, y + 1);
                            break;
                        }
                        case RIGHT: {
                            next = clone.findPieceByCoordinate(x + 1, y);
                            break;
                        }
                        case UP: {
                            next = clone.findPieceByCoordinate(x, y - 1);
                            break;
                        }
                        default:
                            return null;
                    }
                    if (next.getValue()[0] == 0) {
                        continue;
                    }
                    //移动数字
                    clone.move(zeroPiece, direction);
                    //判断当前状态是否出现过
                    String currentStatus = clone.getStatus();
                    //出现过则遍历下一个方向
                    if (boardStatus.contains(currentStatus)) {
                        continue;
                    }
                    //没有则判断是否为正确棋盘状态
                    if (correctStatus.equals(currentStatus)) {
                        //正确则返回当前棋盘
                        return clone;
                    }
                    //通过spfa算法获取每个点到正确位置的最短距离，队列按所有点的最短距离的总和h排列
                    clone.H = spfaSolver(clone, zeroPiece);
//                    clone.H = distance(clone);
                    //不正确则将当前状态存入棋盘中
                    boardStatus.add(currentStatus);
                    //移动后的棋盘存入队列
                    queue.add(clone);
                }
            }
        }
        return null;
    }

    private static int spfaSolver(Board board, Piece zero) throws CloneNotSupportedException {
        int total = 0;
        for (Piece piece : board.getPieces()) {
            if (piece.pieceType == PieceType.BLANK) {
                continue;
            }
            Coordinate correct = correct(board.marginX + 1, piece.getValue()[0]);
            if (piece.getCoordinate()[0].equals(correct)) {
                continue;
            }
            Integer spfa = spfa(board.clone(), zero.getCoordinate()[0], piece.getCoordinate()[0], correct);
            if (spfa != null) {
                total += spfa;
            }
        }
        return total;
    }

    private static int distance(Board board) {
        int total = 0;
        for (Piece piece : board.getPieces()) {
            if (piece.pieceType == PieceType.BLANK) {
                continue;
            }
            Coordinate correct = correct(board.marginX + 1, piece.getValue()[0]);
            if (piece.getCoordinate()[0].equals(correct)) {
                continue;
            }
            int absX = Math.abs(piece.getCoordinate()[0].getX() - correct.getX());
            int absY = Math.abs(piece.getCoordinate()[0].getY() - correct.getY());
            total += absX + absY;
        }
        return total;
    }

    private static Board bfs(Board board, boolean[][] fix, Coordinate x, Coordinate y) throws CloneNotSupportedException {
        if (x.equals(y)) {
            Board clone = board.clone();
            clone.H = 0;
            return clone;
        }
        int columns = board.marginX + 1;
        int rows = board.marginY + 1;
        //队列
        Queue<Coordinate> queue = new LinkedList<>();
        //方向
        Direction[] directions = {Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT};
        //存储x点到每个点的移动次数
        Integer[][] deep = new Integer[columns][rows];
        //存储已访问过的点，true表示访问过，false表示未访问
        boolean[][] visited = new boolean[columns][rows];
        //存储x点移动后的棋盘状态
        Board[][] boardStatus = new Board[columns][rows];
        //初始化当前x点
        queue.add(x);
        deep[x.getX()][x.getY()] = 0;
        visited[x.getX()][x.getY()] = true;
        boardStatus[x.getX()][x.getY()] = board.clone();
        //当前取出的点
        Coordinate now;
        while (!queue.isEmpty()) {
            now = queue.poll();
            //若当前坐标点已在目标点位置，则跳出直接返回目标点
            if (now.equals(y)) {
                break;
            }
            //遍历四个方向
            for (int i = 0; i < 4; i++) {
                //获取当前点的棋盘
                Board clone = boardStatus[now.getX()][now.getY()].clone();
                //当前方向
                Direction direction = directions[i];
                //判断空白格是否可向当前方向移动，不可则返回
                Piece nowPiece = clone.findPieceByCoordinate(now.getX(), now.getY());
                if (!clone.zeroMovable(nowPiece, direction)) {
                    continue;
                }
                //获取空白格往指定方向移动一格后的坐标
                clone.move(nowPiece, direction);
                Coordinate moved = nowPiece.getCoordinate()[0];
                //判断是否已访问过.或者是否为固定点不能移动
                if (visited[moved.getX()][moved.getY()] || fix[moved.getX()][moved.getY()]) {
                    continue;
                }
                //没访问过当前点置为已访问
                visited[moved.getX()][moved.getY()] = true;
                //空白格移动后的距离=当前点移动前的距离+1
                deep[moved.getX()][moved.getY()] = deep[now.getX()][now.getY()] + 1;
                //保存当前棋盘状态
                clone.H = deep[moved.getX()][moved.getY()];
                boardStatus[moved.getX()][moved.getY()] = clone;
                queue.add(moved);
            }
        }
        //返回到目标点的距离
        if (boardStatus[y.getX()][y.getY()] == null) {
            return null;
        }
        return boardStatus[y.getX()][y.getY()];
    }

    private static Coordinate where(Coordinate x, Direction direction) {
        if (direction == Direction.UP) {
            //向上
            return new Coordinate(x.getX(), x.getY() - 1);
        } else if (direction == Direction.RIGHT) {
            //向右
            return new Coordinate(x.getX() + 1, x.getY());
        } else if (direction == Direction.DOWN) {
            //向下
            return new Coordinate(x.getX(), x.getY() + 1);
        } else {
            //向左
            return new Coordinate(x.getX() - 1, x.getY());
        }
    }

    private static Coordinate correct(int columns, int val) {
        int y = val % columns == 0 ? (val / columns - 1) : val / columns;
        int x = val % columns == 0 ? (columns - 1) : val % columns - 1;
        return new Coordinate(x, y);
    }
    
    public static Integer spfa(Board board, Coordinate x, Coordinate y, Coordinate target) throws CloneNotSupportedException {
        int columns = board.marginX + 1;
        int rows = board.marginY + 1;
        //队列
        Queue<Map<String, Integer>> queue = new LinkedList<>();
        //方向
        Direction[] directions = {Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT};
        //反向方向
        Direction[] reverse = {Direction.DOWN, Direction.LEFT, Direction.UP, Direction.RIGHT};
        int[] reverseIndex = {2, 3, 0, 1};
        //存储空白格x点到每个点上下左右格子的最小花费
        Integer[][][] dis = new Integer[columns][rows][4];
        //存储已访问过的点，true表示访问过，false表示未访问
        boolean[][][] visited = new boolean[columns][rows][4];
        //固定点，true表示不能移动，false表示可以移动
        boolean[][] fix = new boolean[columns][rows];
        //棋盘状态
        Board[][][] boardStatus = new Board[columns][rows][4];


        //初始化dis最小花费，第一列的左方向置0，最后一列的右方向置0，第一行的上方向置0，最后一行的下方向置0
        for (int i = 0; i < rows; i++) {
            dis[0][i][3] = 0;
            dis[columns - 1][i][1] = 0;
        }
        for (int i = 0; i < columns; i++) {
            dis[i][0][0] = 0;
            dis[i][rows - 1][2] = 0;
        }


        //获取初始节点，空白格x点到y的k方向格子的花费，目标点y固定不能移动
        fix[y.getX()][y.getY()] = true;
        for (int i = 0; i < 4; i++) {
            Coordinate to = where(new Coordinate(y.getX(), y.getY()), directions[i]);
            if (to.getX() < 0 || to.getY() < 0 || to.getX() > columns - 1 || to.getY() > rows - 1 || fix[to.getX()][to.getY()]) {
                continue;
            }
            Board newBoard = bfs(board, fix, x, to);
            if (newBoard != null) {
                dis[y.getX()][y.getY()][i] = newBoard.H;
                HashMap<String, Integer> map = new HashMap<>();
                map.put("x", y.getX());
                map.put("y", y.getY());
                map.put("k", i);
                queue.add(map);
                visited[y.getX()][y.getY()][i] = true;
                boardStatus[y.getX()][y.getY()][i] = newBoard;
            }
        }
        fix[y.getX()][y.getY()] = false;
        while (!queue.isEmpty()) {
            Map<String, Integer> now = queue.poll();
            visited[now.get("x")][now.get("y")][now.get("k")] = false;
            //遍历数字块要移动的方向
            for (int i = 0; i < 4; i++) {
                Coordinate to = where(new Coordinate(now.get("x"), now.get("y")), directions[i]);
                if (to.getX() < 0 || to.getY() < 0 || to.getX() > columns - 1 || to.getY() > rows - 1 || fix[to.getX()][to.getY()]) {
                    continue;
                }
                //空白格x到当前点的k方向的最小花费
                Integer distance1 = dis[now.get("x")][now.get("y")][now.get("k")];
                if (distance1 == null) {
                    //标记最少花费为0
                    dis[to.getX()][to.getY()][reverseIndex[i]] = 0;
                    continue;
                }

                //空白格移动到当前点k方向后向i方向移动的最少花费
                Board currentBoard = boardStatus[now.get("x")][now.get("y")][now.get("k")];
                //k方向的空白格坐标为
                Coordinate blank = where(new Coordinate(now.get("x"), now.get("y")), directions[now.get("k")]);
                if (currentBoard.findPieceByCoordinate(blank.getX(), blank.getY()).pieceType != PieceType.BLANK) {
                    //标记最少花费为0
                    dis[to.getX()][to.getY()][reverseIndex[i]] = 0;
                    continue;
                }
                //当前点不能移动
                fix[now.get("x")][now.get("y")] = true;
                Board bfs = bfs(currentBoard, fix, blank, to);
                fix[now.get("x")][now.get("y")] = false;
                if (bfs == null) {
                    //标记最少花费为0
                    dis[to.getX()][to.getY()][reverseIndex[i]] = 0;
                    continue;
                }
                int min = bfs.H + 1;

                Integer distance2 = dis[to.getX()][to.getY()][reverseIndex[i]];
                if (distance2 == null || distance1 + min < distance2) {
                    dis[to.getX()][to.getY()][reverseIndex[i]] = distance1 + min;
                }

                //没有访问过则置为已访问，加入队列
                if (!visited[to.getX()][to.getY()][reverseIndex[i]]) {
                    visited[to.getX()][to.getY()][reverseIndex[i]] = true;
                    //保存当前坐标空白格在i方向的棋盘，即往to移动后再往相反的方向移动
                    bfs.move(bfs.findPieceByCoordinate(to.getX(), to.getY()), reverse[i]);
                    boardStatus[to.getX()][to.getY()][reverseIndex[i]] = bfs;
                    HashMap<String, Integer> map = new HashMap<>();
                    map.put("x", to.getX());
                    map.put("y", to.getY());
                    map.put("k", reverseIndex[i]);
                    queue.add(map);
                }
                List<Integer> collect = Arrays.stream(dis[target.getX()][target.getY()]).filter(Objects::nonNull)
                        .collect(Collectors.toList());
                //过滤0
                List<Integer> filter = collect.stream().filter(obj -> obj != 0).collect(Collectors.toList());
                if ((filter.size() > 0 && Collections.min(filter) == 1) || collect.size() == 4) {
                    return Collections.min(filter);
                }
            }
        }
        return null;
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int row = scanner.nextInt();
        int column = scanner.nextInt();
        //这个boardArray用以以数值的形式储存所有的块
        int[][] boardArray = new int[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                boardArray[i][j] = scanner.nextInt();
            }
        }
        int morbidLength = scanner.nextInt();//这个变量是特殊块数量
        //这个hashmap用以储存特殊块，key为块名，value为类型
        HashMap<Integer, String> hashMap = new HashMap<>();
        for (int i = 0; i < morbidLength; i++) {
            hashMap.put(scanner.nextInt(), scanner.next());
        }
        Board board = Main.arrayToBoard(hashMap, boardArray);
        Board init = null;
        try {
            init = board.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        Board result = null;
        long time = 0;
        try {
            long start = System.currentTimeMillis();
            result = bfs(board);
            long end = System.currentTimeMillis();
            time = end - start;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            System.out.println("No");
        }
        if (result != null) {
            System.out.println("Yes");
            System.out.println(result.steps.size());
            for (String step : result.steps) {
                System.out.println(step);
            }
            System.out.println("time: " + time + "ms");
        } else {
            System.out.println("No");
        }
    }
}
