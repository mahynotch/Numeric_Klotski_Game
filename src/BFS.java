import java.util.*;

public class BFS {
    public static Board bfs(Board board) throws CloneNotSupportedException {
        //存储棋盘状态
        List<String> boardStatus = new ArrayList<>();
        //队列
        Queue<Board> queue = new LinkedList<>();
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
            Board first = queue.peek();
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
                    if (!boardStatus.contains(currentStatus)) {
                        //出现过则遍历下一个方向
                        //没有则判断是否为正确棋盘状态
                        if (correctStatus.equals(currentStatus)) {
                            //正确则返回当前棋盘
                            return clone;
                        }
                        //不正确则将当前状态存入棋盘中
                        boardStatus.add(currentStatus);
                        //移动后的棋盘存入队列
                        queue.add(clone);
                    }
                }
            }
            //所有数字0四个方向移动后都没有变成正确的棋盘状态，则移出队首，进行下一次循环
            queue.poll();
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
