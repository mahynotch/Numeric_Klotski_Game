import java.util.*;
import java.util.stream.Collectors;

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
                Piece zeroPiece = (Piece) piece.clone();
                //判断四个方向是否可移动，可移动则将移动后的棋盘依次入栈
                for (int j = 0; j < 4; j++) {
                    Board clone = first.clone();
                    Direction direction = directions[j];
                    boolean movable = clone.movable(zeroPiece, direction);
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


    /**
     * 利用bfs，获取将起点移到终点的最终棋盘
     *
     * @param board   棋盘
     * @param fixList 固定列表，不可移动
     * @param value   起点数字
     * @param target  终点
     * @return
     */
    public static Board bfs(Board board, List<String> fixList, int value, Coordinate target) throws CloneNotSupportedException {
        //存储棋盘状态
        List<String> boardStatus = new ArrayList<>();
        //队列
        Queue<Board> queue = new LinkedList<>();
        //方向
        Direction[] directions = {Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT};
        //第一个根节点入队列
        queue.add(board.clone());
        //存储当前棋盘状态
        String status = board.getStatus();
        boardStatus.add(status);
        //队列不为空，则一直循环
        while (queue.size() > 0) {
            //获取队列中第一个根节点
            Board first = queue.peek();
            //获取该棋盘上的所有数字0，排除已固定的
            List<Piece> zeroPieces = Arrays.stream(first.findAllPieceByValue(0))
                    .filter(obj -> !fixList.contains(obj.code))
                    .collect(Collectors.toList());
            //遍历所有数字0
            for (Piece piece : zeroPieces) {
                Piece zeroPiece = (Piece) piece.clone();
                //遍历四个方向
                for (int j = 0; j < 4; j++) {
                    Board clone = first.clone();
                    //判断方向是否可移动，不可移动则跳过
                    Direction direction = directions[j];
                    boolean movable = clone.movable(zeroPiece, direction);
                    if (!movable) {
                        continue;
                    }
                    //判断移动方向的数字是否也为数字0或者在固定列表中，是的话则跳过
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
                    if (next.getValue()[0] == 0 || fixList.contains(next.code)) {
                        continue;
                    }
                    //移动数字
                    clone.move(zeroPiece, direction);
                    //判断当前状态是否出现过
                    String currentStatus = clone.getStatus();
                    if (!boardStatus.contains(currentStatus)) {
                        //出现过则遍历下一个方向
                        //没有则判断起点数字是否到达终点
                        Piece targetVal = clone.findPieceByCoordinate(target.getX(), target.getY());
                        if (value == targetVal.getValue()[0]) {
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

    /**
     * 下移一格
     *
     * @param board 棋盘
     * @param first 移动目标
     */
    public static Boolean moveDown(Board board, List<String> fixList, Piece first) throws CloneNotSupportedException {
        int firstX = first.getCoordinate()[0].getX();
        int firstY = first.getCoordinate()[0].getY();
        int firstHeight = first.getCoordinate()[first.getCoordinate().length - 1].getY() - firstY + 1;
        int firstWidth = first.getCoordinate()[first.getCoordinate().length - 1].getX() - firstX + 1;
        Coordinate zeroTarget = new Coordinate(firstX, firstY + firstHeight);
        Piece zero1 = board.findPieceByCoordinate(zeroTarget.getX(), zeroTarget.getY());
        if (zero1.getValue()[0] != 0) {
            //若不为0，则通过bfs获取数字0移到到目标位置
            Board bfs = bfs(board, fixList, 0, zeroTarget);
            if (bfs == null) {
                return false;
            }
            updateBoard(board, bfs);
        }
        //若宽度大于1
        if (firstWidth > 1) {
            //获取其右侧的数字块
            Coordinate anotherZeroTarget = new Coordinate(zeroTarget.getX() + 1, zeroTarget.getY());
            Piece zero2 = board.findPieceByCoordinate(anotherZeroTarget.getX(), anotherZeroTarget.getY());
            //不是0的话继续通过bfs获取其余数字0移到到目标的路径
            if (zero2.getValue()[0] != 0) {
                //当前0加入固定列表
                fixList.add(zero1.code);
                Board bfs = bfs(board, fixList, 0, anotherZeroTarget);
                if (bfs == null) {
                    return false;
                }
                updateBoard(board, bfs);
                fixList.remove(zero1.code);
            }
        }
        //将该数字块下移
        board.move(first, Direction.DOWN);
        return true;
    }

    /**
     * 左移一格
     *
     * @param board 棋盘
     * @param first 移动目标
     */
    public static Boolean moveLeft(Board board, List<String> fixList, Piece first) throws CloneNotSupportedException {
        int firstX = first.getCoordinate()[0].getX();
        int firstY = first.getCoordinate()[0].getY();
        int firstHeight = first.getCoordinate()[first.getCoordinate().length - 1].getY() - firstY + 1;
        int firstWidth = first.getCoordinate()[first.getCoordinate().length - 1].getX() - firstX + 1;
        //获取左侧第一个数字0
        Coordinate zeroTarget = new Coordinate(firstX - 1, firstY);
        Piece zero1 = board.findPieceByCoordinate(zeroTarget.getX(), zeroTarget.getY());
        if (zero1.getValue()[0] != 0) {
            //若不为0，则通过bfs获取数字0移到到目标位置
            Board bfs = bfs(board, fixList, 0, zeroTarget);
            if (bfs == null) {
                return false;
            }
            updateBoard(board, bfs);
        }
        //若高度大于1
        if (firstHeight > 1) {
            //获取第一个数字0下方的数字块
            Coordinate anotherZeroTarget = new Coordinate(zeroTarget.getX(), zeroTarget.getY() + 1);
            Piece zero2 = board.findPieceByCoordinate(anotherZeroTarget.getX(), anotherZeroTarget.getY());
            //不是0的话继续通过bfs获取其余数字0移到到目标的路径
            if (zero2.getValue()[0] != 0) {
                //当前0加入固定列表
                fixList.add(zero1.code);
                Board bfs = bfs(board, fixList, 0, anotherZeroTarget);
                if (bfs == null) {
                    return false;
                }
                updateBoard(board, bfs);
                fixList.remove(zero1.code);
            }
        }
        //将该数字块左移
        board.move(first, Direction.LEFT);
        return true;
    }

    /**
     * 右移一格
     *
     * @param board 棋盘
     * @param first 移动目标
     */
    public static Boolean moveRight(Board board, List<String> fixList, Piece first) throws CloneNotSupportedException {
        int firstX = first.getCoordinate()[0].getX();
        int firstY = first.getCoordinate()[0].getY();
        int firstHeight = first.getCoordinate()[first.getCoordinate().length - 1].getY() - firstY + 1;
        int firstWidth = first.getCoordinate()[first.getCoordinate().length - 1].getX() - firstX + 1;
        //获取右侧第一个数字0
        Coordinate zeroTarget = new Coordinate(firstX + firstWidth, firstY);
        Piece zero1 = board.findPieceByCoordinate(zeroTarget.getX(), zeroTarget.getY());
        if (zero1.getValue()[0] != 0) {
            //若不为0，则通过bfs获取数字0移到到目标位置
            Board bfs = bfs(board, fixList, 0, zeroTarget);
            if (bfs == null) {
                return false;
            }
            updateBoard(board, bfs);
        }
        //若高度大于1
        if (firstHeight > 1) {
            //获取第一个数字0下方的数字块
            Coordinate anotherZeroTarget = new Coordinate(zeroTarget.getX(), firstY + firstHeight + 1);
            Piece zero2 = board.findPieceByCoordinate(anotherZeroTarget.getX(), anotherZeroTarget.getY());
            //不是0的话继续通过bfs获取其余数字0移到到目标的路径
            if (zero2.getValue()[0] != 0) {
                //当前0加入固定列表
                fixList.add(zero1.code);
                Board bfs = bfs(board, fixList, 0, anotherZeroTarget);
                if (bfs == null) {
                    return false;
                }
                updateBoard(board, bfs);
                fixList.remove(zero1.code);
            }
        }
        //将该数字块右移
        board.move(first, Direction.RIGHT);
        return true;
    }

    /**
     * 上移一格
     *
     * @param board 棋盘
     * @param first 移动目标
     */
    public static Boolean moveUp(Board board, List<String> fixList, Piece first) throws CloneNotSupportedException {
        int firstX = first.getCoordinate()[0].getX();
        int firstY = first.getCoordinate()[0].getY();
        int firstHeight = first.getCoordinate()[first.getCoordinate().length - 1].getY() - firstY + 1;
        int firstWidth = first.getCoordinate()[first.getCoordinate().length - 1].getX() - firstX + 1;
        Coordinate zeroTarget = new Coordinate(firstX, firstY - 1);
        Piece zero1 = board.findPieceByCoordinate(zeroTarget.getX(), zeroTarget.getY());
        if (zero1.getValue()[0] != 0) {
            //若不为0，则通过bfs获取数字0移到到目标位置
            Board bfs = bfs(board, fixList, 0, zeroTarget);
            if (bfs == null) {
                return false;
            }
            updateBoard(board, bfs);
        }
        //若宽度大于1
        if (firstWidth > 1) {
            //获取其右侧的数字块
            Coordinate anotherZeroTarget = new Coordinate(zeroTarget.getX() + 1, zeroTarget.getY());
            Piece zero2 = board.findPieceByCoordinate(anotherZeroTarget.getX(), anotherZeroTarget.getY());
            //不是0的话继续通过bfs获取其余数字0移到到目标的路径
            if (zero2.getValue()[0] != 0) {
                //当前0加入固定列表
                fixList.add(zero1.code);
                Board bfs = bfs(board, fixList, 0, anotherZeroTarget);
                if (bfs == null) {
                    return false;
                }
                updateBoard(board, bfs);
                fixList.remove(zero1.code);
            }
        }
        //将该数字块上移
        board.move(first, Direction.UP);
        return true;
    }

    private static void updateBoard(Board board, Board newBoard) {
        board.steps.addAll(newBoard.steps);
        board.pieces = newBoard.pieces;
    }
}
