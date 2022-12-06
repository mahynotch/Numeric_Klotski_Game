import javax.swing.*;
import java.awt.*;
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

    int H = 0;
    ArrayList<String> steps = new ArrayList<>();

    Direction lastDirection = Direction.NONE;

    public Board(Piece[] pieces, int marginX, int marginY) {
        this.pieces = pieces;
        this.marginX = marginX;
        this.marginY = marginY;
        for (Piece piece : pieces) {
            add(piece);
        }
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
        for (Piece i : pieces) {
            for (int j : i.getValue()) {
                if (j == key && !pieceList.contains(i)) {
                    pieceList.add(i);
                }
            }
        }
        return pieceList.toArray(new Piece[0]);
    }

    public int[] findAllIndexByValue(int key) {
        ArrayList<Integer> peaceList = new ArrayList<>();
        for (int i = 0; i < pieces.length; i++) {
            for (int j : pieces[i].getValue()) {
                if (key == j) {
                    peaceList.add(i);
                }
            }
        }
        int[] res = new int[peaceList.size()];
        for (int i = 0; i < peaceList.size(); i++) {
            res[i] = peaceList.get(i);
        }
        return res;
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
        if (piece.pieceType == PieceType.BLANK) {
            if (aim.pieceType == PieceType.BLANK) {
                piece.move(direction);
                aim.move(counterDirection(direction));
                return;
            }
            Piece temp = piece;
            piece = aim;
            aim = temp;
            direction = counterDirection(direction);
        }
        switch (piece.pieceType) {
            case ONETOONE: {
                piece.move(direction);
                aim.move(counterDirection(direction));
                break;
            }
            case ONETOTWO: {
                if (direction == Direction.LEFT | direction == Direction.RIGHT) {
                    piece.move(direction);
                    aim.move(counterDirection(direction));
                    aim.move(counterDirection(direction));
                } else {
                    findPieceByCoordinate(aim.getCoordinate()[0].getX() == piece.getCoordinate()[0].getX() ? aim.getCoordinate()[0].getX() + 1 : aim.getCoordinate()[0].getX() - 1
                            , aim.getCoordinate()[0].getY()).move(counterDirection(direction));
                    aim.move(counterDirection(direction));
                    piece.move(direction);
                }
                break;
            }
            case TWOTOONE: {
                if (direction == Direction.UP | direction == Direction.DOWN) {
                    piece.move(direction);
                    aim.move(counterDirection(direction));
                    aim.move(counterDirection(direction));
                } else {
                    findPieceByCoordinate(aim.getCoordinate()[0].getX()
                            , aim.getCoordinate()[0].getY() == piece.getCoordinate()[0].getY() ? aim.getCoordinate()[0].getY() + 1 : aim.getCoordinate()[0].getY() - 1).move(counterDirection(direction));
                    aim.move(counterDirection(direction));
                    piece.move(direction);
                }
                break;
            }
            case TWOTOTWO: {
                if (direction == Direction.UP | direction == Direction.DOWN) {
                    Piece aim2 = findPieceByCoordinate(aim.getCoordinate()[0].getX() == piece.getCoordinate()[0].getX() ? aim.getCoordinate()[0].getX() + 1 : aim.getCoordinate()[0].getX() - 1
                            , aim.getCoordinate()[0].getY());
                    aim.move(counterDirection(direction));
                    aim.move(counterDirection(direction));
                    aim2.move(counterDirection(direction));
                    aim2.move(counterDirection(direction));
                    piece.move(direction);
                } else {
                    Piece aim2 = findPieceByCoordinate(aim.getCoordinate()[0].getX()
                            , aim.getCoordinate()[0].getY() == piece.getCoordinate()[0].getY() ? aim.getCoordinate()[0].getY() + 1 : aim.getCoordinate()[0].getY() - 1);
                    aim.move(counterDirection(direction));
                    aim.move(counterDirection(direction));
                    aim2.move(counterDirection(direction));
                    aim2.move(counterDirection(direction));
                    piece.move(direction);
                }
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
        lastDirection = direction;
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
        if (aim.pieceType != PieceType.BLANK) {
            return false;
        }
        switch (pieceType) {
            case ONETOONE:
            case BLANK: {
                return true;
            }
            case TWOTOONE: {
                if (direction == Direction.UP | direction == Direction.DOWN) {
                    return true;
                } else {
                    return findPieceByCoordinate(aim.getCoordinate()[0].getX(), aim.getCoordinate()[0].getY() == piece.getCoordinate()[0].getY() ? aim.getCoordinate()[0].getY() + 1 : aim.getCoordinate()[0].getY() - 1).pieceType == PieceType.BLANK;
                }
            }
            case ONETOTWO: {
                if (direction == Direction.LEFT | direction == Direction.RIGHT) {
                    return true;
                } else {
                    return findPieceByCoordinate(aim.getCoordinate()[0].getX() + 1, aim.getCoordinate()[0].getY()).pieceType == PieceType.BLANK;
                }
            }
            case TWOTOTWO: {
                if (direction == Direction.LEFT | direction == Direction.RIGHT) {
                    return findPieceByCoordinate(aim.getCoordinate()[0].getX(), aim.getCoordinate()[0].getY() + 1).pieceType == PieceType.BLANK;
                } else {
                    return findPieceByCoordinate(aim.getCoordinate()[0].getX() + 1, aim.getCoordinate()[0].getY()).pieceType == PieceType.BLANK;
                }
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
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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

    /**
     * 获取棋盘状态
     *
     * @return
     */
    public String getStatus() {
        StringBuilder status = new StringBuilder();
        for (int i = 0; i < marginY + 1; i++) {
            for (int j = 0; j < marginX + 1; j++) {
                status.append(findValueByCoordinate(j, i));
                if (i != marginY || j != marginX) {
                    status.append(",");
                }
            }
        }
        return status.toString();
    }

    /**
     * 获取正确棋盘状态
     *
     * @return
     */
    public String getCorrectStatus() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < marginY + 1; i++) {
            for (int j = 0; j < marginX + 1; j++) {
                list.add(findValueByCoordinate(j, i));
            }
        }
        String zeroStr = list.stream().filter(obj -> obj == 0)
                .map(String::valueOf).collect(Collectors.joining(","));
        String numStr = list.stream().filter(obj -> obj != 0)
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        return numStr + "," + zeroStr;
    }

    public Piece[] cloneOfPieces() throws CloneNotSupportedException {
        Piece[] newPieces = new Piece[pieces.length];
        for (int i = 0; i < pieces.length; i++) {
            newPieces[i] = (Piece) pieces[i].clone();
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
