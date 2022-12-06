import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


//本类用以储存棋子
public class Board extends JComponent implements Cloneable {
    //棋子列表
    Piece[] pieces;
    //横向边缘
    int marginX;
    //纵向边缘
    int marginY;

    GameFrame gameFrame;

    int[][] sorted;

    ArrayList<String> steps = new ArrayList<>();

    public Board(Piece[] pieces, int marginX, int marginY) {
        this.pieces = pieces;
        this.marginX = marginX;
        this.marginY = marginY;
    }

    public void setGameFrame(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    public Piece findPieceByValue(int key) {
        for (Piece i : pieces) {
            for (int j : i.getValue()) {
                if (j == key) {
                    return i;
                }
            }
        }
        return null;
    }

    public Piece[] findAllPieceByValue(int key) {
        ArrayList<Piece> pieceList = new ArrayList<>();
        boolean bool = false;
        for (Piece i : pieces) {
            for (int j : i.getValue()) {
                if (j == key && !pieceList.contains(i)) {
                    pieceList.add(i);
                }
            }
        }
        return pieceList.toArray(new Piece[0]);
    }

    public Piece[] getPieces() {
        return pieces;
    }

    public Piece findPieceByCoordinate(int x, int y) {
        Coordinate coordinate = new Coordinate(x, y);
        for (Piece j : pieces) {
            for (Coordinate c : j.getCoordinate()) {
                if (c.equals(coordinate)) {
                    return j;
                }
            }
        }
        throw new IllegalArgumentException("The coordinate is empty");
    }

    public int findValueByCoordinate(int x, int y) {
        Coordinate coordinate = new Coordinate(x, y);
        for (Piece j : pieces) {
            for (int k = 0; k < j.getCoordinate().length; k++) {
                if (j.getCoordinate()[k].equals(coordinate)) {
                    return j.getValue()[k];
                }
            }
        }
        throw new IllegalArgumentException("The coordinate is empty");
    }


    public void move(Piece piece, Direction direction) {
        Coordinate[] Cor = piece.getCoordinate();
        int x = Cor[0].getX();
        int y = Cor[0].getY();
        //获取棋盘中的对象
        piece = findPieceByCoordinate(x, y);
        Piece aim;
        switch (direction) {
            case LEFT:
                aim = findPieceByCoordinate(x - 1, y);
                break;
            case RIGHT:
                aim = findPieceByCoordinate(piece.pieceType == PieceType.TWOTOTWO || piece.pieceType == PieceType.ONETOTWO ? x + 2 : x + 1, y);
                break;
            case UP:
                aim = findPieceByCoordinate(x, y - 1);
                break;
            case DOWN:
                aim = findPieceByCoordinate(x, piece.pieceType == PieceType.TWOTOTWO || piece.pieceType == PieceType.TWOTOONE ? y + 2 : y + 1);
                break;
            default:
                aim = findPieceByCoordinate(x, y);
        }
        switch (piece.pieceType) {
            case BLANK: {
                switch (aim.pieceType) {
                    case ONETOONE:
                    case BLANK: {
                        piece.move(direction);
                        aim.move(counterDirection(direction));
                        break;
                    }
                    case ONETOTWO: {
                        if (direction == Direction.UP || direction == Direction.DOWN) {
                            //纵向移动需判断当前数字块的左侧或右侧是否有数字0
                            Piece next;
                            if (x == aim.coordinate[0].getX()) {
                                //当前数字块在目标的左边，获取当前数字块右侧的数字块
                                next = findPieceByCoordinate(x + 1, y);
                            } else {
                                //当前数字块在目标的右边，获取当前数字块左侧的数字块
                                next = findPieceByCoordinate(x - 1, y);
                            }
                            if (next.getValue()[0] == 0) {
                                piece.move(direction);
                                next.move(direction);
                                aim.move(counterDirection(direction));
                            }
                        } else {
                            piece.move(direction);
                            piece.move(direction);
                            aim.move(counterDirection(direction));
                        }
                        break;
                    }
                    case TWOTOONE: {
                        if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                            //横向移动需判断当前数字块的上侧或下侧是否有数字0
                            Piece next;
                            if (y == aim.coordinate[0].getY()) {
                                //当前数字块在目标的上边，获取当前数字块下侧的数字块
                                next = findPieceByCoordinate(x, y + 1);
                            } else {
                                //当前数字块在目标的下边，获取当前数字块上侧的数字块
                                next = findPieceByCoordinate(x, y - 1);
                            }
                            if (next.getValue()[0] == 0) {
                                piece.move(direction);
                                next.move(direction);
                                aim.move(counterDirection(direction));
                            }
                        } else {
                            piece.move(direction);
                            piece.move(direction);
                            aim.move(counterDirection(direction));
                        }
                        break;
                    }
                    case TWOTOTWO: {
                        Piece next;
                        if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                            //横向移动需判断当前数字块的上侧或下侧是否有数字0
                            if (y == aim.coordinate[0].getY()) {
                                //当前数字块在目标的上边，获取当前数字块下侧的数字块
                                next = findPieceByCoordinate(x, y + 1);
                            } else {
                                //当前数字块在目标的下边，获取当前数字块上侧的数字块
                                next = findPieceByCoordinate(x, y - 1);
                            }
                        } else {
                            //纵向移动需判断当前数字块的左侧或右侧是否有数字0
                            if (x == aim.coordinate[0].getX()) {
                                //当前数字块在目标的左边，获取当前数字块右侧的数字块
                                next = findPieceByCoordinate(x + 1, y);
                            } else {
                                //当前数字块在目标的右边，获取当前数字块左侧的数字块
                                next = findPieceByCoordinate(x - 1, y);
                            }
                        }
                        if (next.getValue()[0] == 0) {
                            piece.move(direction);
                            piece.move(direction);
                            next.move(direction);
                            next.move(direction);
                            aim.move(counterDirection(direction));
                        }
                        break;
                    }
                }
            }
            case ONETOONE: {
                if (aim.pieceType == PieceType.BLANK) {
                    piece.move(direction);
                    aim.move(counterDirection(direction));
                }
                break;
            }
            case TWOTOONE: {
                if (direction == Direction.UP || direction == Direction.DOWN) {
                    if (aim.pieceType == PieceType.BLANK) {
                        piece.move(direction);
                        aim.move(counterDirection(direction));
                        aim.move(counterDirection(direction));
                    }
                } else {
                    if (aim.pieceType == PieceType.BLANK) {
                        //左右移动需获取目标的上侧或下侧是否为数字0
                        Piece next;
                        int aimX = aim.getCoordinate()[0].getX();
                        int aimY = aim.getCoordinate()[0].getY();
                        if (aimY == piece.coordinate[0].getY()) {
                            //目标在当前数字块的上边，获取目标下侧的数字块
                            next = findPieceByCoordinate(aimX, aimY + 1);
                        } else {
                            //目标在当前数字块的下边，获取目标上侧的数字块
                            next = findPieceByCoordinate(aimX, aimY - 1);
                        }
                        if (next.getValue()[0] == 0) {
                            piece.move(direction);
                            aim.move(counterDirection(direction));
                            next.move(counterDirection(direction));
                        }
                    }
                }
                break;
            }
            case ONETOTWO: {
                if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                    if (aim.pieceType == PieceType.BLANK) {
                        piece.move(direction);
                        aim.move(counterDirection(direction));
                        aim.move(counterDirection(direction));
                    }
                } else {
                    if (aim.pieceType == PieceType.BLANK) {
                        //纵向移动需判断目标块的左侧或右侧是否有数字0
                        Piece next;
                        int aimX = aim.getCoordinate()[0].getX();
                        int aimY = aim.getCoordinate()[0].getY();
                        if (aimX == piece.coordinate[0].getX()) {
                            //目标在当前数字块的左边，获取目标右侧的数字块
                            next = findPieceByCoordinate(aimX + 1, aimY);
                        } else {
                            //目标在当前数字块的右边，获取目标左侧的数字块
                            next = findPieceByCoordinate(aimX - 1, aimY);
                        }
                        if (next.getValue()[0] == 0) {
                            piece.move(direction);
                            aim.move(counterDirection(direction));
                            next.move(counterDirection(direction));
                        }
                    }
                    break;
                }
            }
            case TWOTOTWO: {
                if (aim.pieceType == PieceType.BLANK) {
                    Piece next;
                    int aimX = aim.getCoordinate()[0].getX();
                    int aimY = aim.getCoordinate()[0].getY();
                    if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                        //横向移动需判断目标块的上侧或下侧是否有数字0
                        if (aimY == piece.coordinate[0].getY()) {
                            //目标在当前数字块的上边，获取目标下侧的数字块
                            next = findPieceByCoordinate(aimX, aimY + 1);
                        } else {
                            //目标在当前数字块的下边，获取目标上侧的数字块
                            next = findPieceByCoordinate(aimX, aimY - 1);
                        }
                    } else {
                        //纵向移动需判断目标块的左侧或右侧是否有数字0
                        if (aimX == piece.coordinate[0].getX()) {
                            //目标在当前数字块的左边，获取目标右侧的数字块
                            next = findPieceByCoordinate(aimX + 1, aimY);
                        } else {
                            //目标在当前数字块的右边，获取目标左侧的数字块
                            next = findPieceByCoordinate(aimX - 1, aimY);
                        }
                    }
                    if (next.getValue()[0] == 0) {
                        piece.move(direction);
                        aim.move(counterDirection(direction));
                        aim.move(counterDirection(direction));
                        next.move(counterDirection(direction));
                        next.move(counterDirection(direction));
                    }
                }
                break;
            }
        }
        if (steps.size() >= 1) {
            if (steps.get(steps.size() - 1).equals(piece.getValue()[0] + " " + counterDirection(direction).toString().charAt(0))) {
                steps.remove(steps.size() - 1);
            } else {
                steps.add(piece.getValue()[0] + " " + direction.toString().charAt(0));
            }
        } else {
            steps.add(piece.getValue()[0] + " " + direction.toString().charAt(0));
        }
    }

    public boolean movable(Piece piece, Direction direction) {
        int x = piece.getCoordinate()[0].getX();
        int y = piece.getCoordinate()[0].getY();
        PieceType pieceType = piece.pieceType;
        Piece aim;
        switch (direction) {
            case LEFT:
                if (x == 0) return false;
                aim = findPieceByCoordinate(x - 1, y);
                break;
            case RIGHT:
                if (x == marginX) return false;
                aim = findPieceByCoordinate(piece.pieceType == PieceType.TWOTOTWO | piece.pieceType == PieceType.ONETOTWO ? x + 2 : x + 1, y);
                break;
            case UP:
                if (y == 0) return false;
                aim = findPieceByCoordinate(x, y - 1);
                break;
            case DOWN:
                if (y == marginY) return false;
                aim = findPieceByCoordinate(x, piece.pieceType == PieceType.TWOTOTWO | piece.pieceType == PieceType.TWOTOONE ? y + 2 : y + 1);
                break;
            default:
                aim = findPieceByCoordinate(x, y);
        }

        switch (pieceType) {
            case BLANK: {
                switch (aim.pieceType) {
                    case ONETOONE:
                    case BLANK:
                        return true;
                    case ONETOTWO: {
                        if (direction == Direction.UP || direction == Direction.DOWN) {
                            //纵向移动需判断当前数字块的左侧或右侧是否有数字0
                            Piece next;
                            if (x == aim.coordinate[0].getX()) {
                                //当前数字块在目标的左边，获取当前数字块右侧的数字块
                                next = findPieceByCoordinate(x + 1, y);
                            } else {
                                //当前数字块在目标的右边，获取当前数字块左侧的数字块
                                next = findPieceByCoordinate(x - 1, y);
                            }
                            return next.getValue()[0] == 0;
                        } else {
                            return true;
                        }
                    }
                    case TWOTOONE: {
                        if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                            //横向移动需判断当前数字块的上侧或下侧是否有数字0
                            Piece next;
                            if (y == aim.coordinate[0].getY()) {
                                //当前数字块在目标的上边，获取当前数字块下侧的数字块
                                next = findPieceByCoordinate(x, y + 1);
                            } else {
                                //当前数字块在目标的下边，获取当前数字块上侧的数字块
                                next = findPieceByCoordinate(x, y - 1);
                            }
                            return next.getValue()[0] == 0;
                        } else {
                            return true;
                        }
                    }
                    case TWOTOTWO: {
                        Piece next;
                        if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                            //横向移动需判断当前数字块的上侧或下侧是否有数字0
                            if (y == aim.coordinate[0].getY()) {
                                //当前数字块在目标的上边，获取当前数字块下侧的数字块
                                next = findPieceByCoordinate(x, y + 1);
                            } else {
                                //当前数字块在目标的下边，获取当前数字块上侧的数字块
                                next = findPieceByCoordinate(x, y - 1);
                            }
                        } else {
                            //纵向移动需判断当前数字块的左侧或右侧是否有数字0
                            if (x == aim.coordinate[0].getX()) {
                                //当前数字块在目标的左边，获取当前数字块右侧的数字块
                                next = findPieceByCoordinate(x + 1, y);
                            } else {
                                //当前数字块在目标的右边，获取当前数字块左侧的数字块
                                next = findPieceByCoordinate(x - 1, y);
                            }
                        }
                        return next.getValue()[0] == 0;
                    }
                }
            }
            case ONETOONE: {
                return aim.pieceType == PieceType.BLANK;
            }
            case TWOTOONE: {
                if (direction == Direction.UP || direction == Direction.DOWN) {
                    return aim.pieceType == PieceType.BLANK;
                } else {
                    if (aim.pieceType != PieceType.BLANK) {
                        return false;
                    }
                    //横向移动需判断目标块的上侧或下侧是否有数字0
                    Piece next;
                    int aimX = aim.getCoordinate()[0].getX();
                    int aimY = aim.getCoordinate()[0].getY();
                    if (aimY == piece.coordinate[0].getY()) {
                        //目标在当前数字块的上边，获取目标下侧的数字块
                        next = findPieceByCoordinate(aimX, aimY + 1);
                    } else {
                        //目标在当前数字块的下边，获取目标上侧的数字块
                        next = findPieceByCoordinate(aimX, aimY - 1);
                    }
                    return next.getValue()[0] == 0;
                }
            }
            case ONETOTWO: {
                if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                    return aim.pieceType == PieceType.BLANK;
                } else {
                    if (aim.pieceType != PieceType.BLANK) {
                        break;
                    }
                    //纵向移动需判断目标块的左侧或右侧是否有数字0
                    Piece next;
                    int aimX = aim.getCoordinate()[0].getX();
                    int aimY = aim.getCoordinate()[0].getY();
                    if (aimX == piece.coordinate[0].getX()) {
                        //目标在当前数字块的左边，获取目标右侧的数字块
                        next = findPieceByCoordinate(aimX + 1, aimY);
                    } else {
                        //目标在当前数字块的右边，获取目标左侧的数字块
                        next = findPieceByCoordinate(aimX - 1, aimY);
                    }
                    return next.getValue()[0] == 0;
                }
            }
            case TWOTOTWO: {
                if (aim.pieceType != PieceType.BLANK) {
                    break;
                }
                Piece next;
                int aimX = aim.getCoordinate()[0].getX();
                int aimY = aim.getCoordinate()[0].getY();
                if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                    //横向移动需判断目标块的上侧或下侧是否有数字0
                    if (aimY == piece.coordinate[0].getY()) {
                        //目标在当前数字块的上边，获取目标下侧的数字块
                        next = findPieceByCoordinate(aimX, aimY + 1);
                    } else {
                        //目标在当前数字块的下边，获取目标上侧的数字块
                        next = findPieceByCoordinate(aimX, aimY - 1);
                    }
                } else {
                    //纵向移动需判断目标块的左侧或右侧是否有数字0
                    if (aimX == piece.coordinate[0].getX()) {
                        //目标在当前数字块的左边，获取目标右侧的数字块
                        next = findPieceByCoordinate(aimX + 1, aimY);
                    } else {
                        //目标在当前数字块的右边，获取目标左侧的数字块
                        next = findPieceByCoordinate(aimX - 1, aimY);
                    }
                }
                return next.getValue()[0] == 0;
            }
        }
        return false;
    }

    public boolean zeroMovable(Piece piece, Direction direction) {
        if (piece.pieceType != PieceType.BLANK) {
            throw new IllegalArgumentException("This is not a BLANK piece!");
        }
        int x = piece.getCoordinate()[0].getX();
        int y = piece.getCoordinate()[0].getY();
        Piece aim;
        switch (direction) {
            case LEFT:
                if (x == 0) return false;
                aim = findPieceByCoordinate(x - 1, y);
                break;
            case RIGHT:
                if (x == marginX) return false;
                aim = findPieceByCoordinate(x + 1, y);
                break;
            case UP:
                if (y == 0) return false;
                aim = findPieceByCoordinate(x, y - 1);
                break;
            case DOWN:
                if (y == marginY) return false;
                aim = findPieceByCoordinate(x, y + 1);
                break;
            default:
                aim = findPieceByCoordinate(x, y);
        }
        return movable(aim, counterDirection(direction));
    }

    public Object[] zeroMovableWithAim(Piece piece, Direction direction) {
        if (piece.pieceType != PieceType.BLANK) {
            throw new IllegalArgumentException("This is not a BLANK piece!");
        }
        int x = piece.getCoordinate()[0].getX();
        int y = piece.getCoordinate()[0].getY();
        Piece aim;
        Object[] objects = new Object[]{
                Boolean.FALSE, null
        };
        switch (direction) {
            case LEFT:
                if (x == 0) return objects;
                aim = findPieceByCoordinate(x - 1, y);
                break;
            case RIGHT:
                if (x == marginX) return objects;
                aim = findPieceByCoordinate(x + 1, y);
                break;
            case UP:
                if (y == 0) return objects;
                aim = findPieceByCoordinate(x, y - 1);
                break;
            case DOWN:
                if (y == marginY) return objects;
                aim = findPieceByCoordinate(x, y + 1);
                break;
            default:
                aim = findPieceByCoordinate(x, y);
        }
        return new Object[]{
                movable(aim, counterDirection(direction)), aim
        };
    }

    public static Direction counterDirection(Direction direction) {
        switch (direction) {
            case RIGHT:
                return Direction.LEFT;
            case DOWN:
                return Direction.UP;
            case LEFT:
                return Direction.RIGHT;
            case UP:
                return Direction.DOWN;
            default:
                return Direction.NONE;
        }
    }

    public int[][] to2DArray() {
        int[][] dist = new int[marginY + 1][marginX + 1];
        for (int i = 0; i < dist.length; i++) {
            for (int j = 0; j < dist[0].length; j++) {
                dist[i][j] = findValueByCoordinate(j, i);
            }
        }
        return dist;
    }

    @Override
    public String toString() {
        String[][] boardC = new String[(marginY + 1) * 2 - 1][(marginX + 1) * 2 - 1];
//        for (int i = 0; i < boardC[0].length; i++) {
//            boardC[boardC.length - 1][i] = boardC[0][i] = "-";
//        }
//        for (int i = 1; i < boardC.length; i++) {
//            boardC[i][0] = boardC[i][boardC[0].length - 1] = "|";
//        }
        for (int i = 0; i < boardC.length; i++) {
            for (int j = 0; j < boardC[0].length; j++) {
                if (i % 2 == 0 & j % 2 == 1) {
                    boardC[i][j] = "|";
                } else if (i % 2 == 1 & j % 2 == 0) {
                    boardC[i][j] = "--";
                } else {
                    boardC[i][j] = " ";
                }
            }
        }
        for (int i = 0; i <= marginY; i++) {
            for (int j = 0; j <= marginX; j++) {
                Piece piece = findPieceByCoordinate(j, i);
                int value = findValueByCoordinate(j, i);
                if (piece.pieceType == PieceType.ONETOTWO && value == piece.getValue()[0]) {
//                    boardC[2 * i + 1][2 * j + 1] = String.valueOf(value);
                    boardC[2 * i][2 * j + 1] = " ";
                } else if (piece.pieceType == PieceType.TWOTOONE && value == piece.getValue()[0]) {
//                    boardC[2 * i + 1][2 * j + 1] = String.valueOf(value);
                    boardC[2 * i + 1][2 * j] = "  ";
                } else if (piece.pieceType == PieceType.TWOTOTWO && value == piece.getValue()[0]) {
//                    boardC[2 * i + 1][2 * j + 1] = String.valueOf(value);
                    boardC[2 * i][2 * j + 1] = " ";
                    boardC[2 * i + 1][2 * j] = "  ";
                    boardC[2 * i + 2][2 * j + 1] = " ";
                    boardC[2 * i + 1][2 * j + 2] = "  ";
                }
                boardC[2 * i][2 * j] = value != 0 ? String.format("%2d", value) : "  ";
            }
        }
        StringBuilder sb = new StringBuilder();
        for (String[] strings : boardC) {
            for (int j = 0; j < boardC[0].length; j++) {
                sb.append(strings[j]);
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public Piece[] cloneOfPieces() {
        Piece[] newPieces = new Piece[pieces.length];
        for (int i = 0; i < pieces.length; i++) {
            try {
                newPieces[i] = (Piece) pieces[i].clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
        return newPieces;
    }

    @Override
    public Board clone() throws CloneNotSupportedException {
        Board clone = (Board) super.clone();
        clone.pieces = cloneOfPieces();
        clone.steps = (ArrayList<String>) steps.clone();
        return clone;
    }
}
