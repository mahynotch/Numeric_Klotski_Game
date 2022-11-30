import javax.swing.*;
import java.util.ArrayList;

//本类用以储存棋子
public class Board extends JComponent {
    //棋子列表
    Piece[] pieces;
    //横向边缘
    final int marginX;
    //纵向边缘
    final int marginY;
    GameFrame gameFrame;

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

    public void findAllPieceByValue(ArrayList<Piece> pieceList, int key) {
        Piece piece = null;
        Boolean bool = false;
        for (Piece i : pieces) {
            for (int j : i.getValue()) {
                if (j == key && !pieceList.contains(i)) {
                    pieceList.add(i);
                    bool = true;
                    break;
                }
            }
        }
        if (!bool) {
            return;
        }
        findAllPieceByValue(pieceList, key);
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
                aim = findPieceByCoordinate(piece.pieceType == PieceType.TWOTOTWO ? x - 2 : x - 1, y);
                break;
            case RIGHT:
                aim = findPieceByCoordinate(piece.pieceType == PieceType.TWOTOTWO ? x + 2 : x + 1, y);
                break;
            case UP:
                aim = findPieceByCoordinate(x, piece.pieceType == PieceType.TWOTOTWO ? y - 2 : y - 1);
                break;
            case DOWN:
                aim = findPieceByCoordinate(x, piece.pieceType == PieceType.TWOTOTWO ? y + 2 : y + 1);
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
                return;
            }
            case ONETOTWO: {
                if (direction == Direction.LEFT | direction == Direction.RIGHT) {
                    piece.move(direction);
                    aim.move(counterDirection(direction));
                    aim.move(counterDirection(direction));
                } else {
                    findPieceByCoordinate(aim.getCoordinate()[0].getX() + 1, aim.getCoordinate()[0].getY()).move(counterDirection(direction));
                    aim.move(counterDirection(direction));
                    piece.move(direction);
                }
                return;
            }
            case TWOTOONE: {
                if (direction == Direction.UP | direction == Direction.DOWN) {
                    piece.move(direction);
                    aim.move(counterDirection(direction));
                    aim.move(counterDirection(direction));
                } else {
                    findPieceByCoordinate(aim.getCoordinate()[0].getX(), aim.getCoordinate()[0].getY() + 1).move(counterDirection(direction));
                    aim.move(counterDirection(direction));
                    piece.move(direction);
                }
                return;
            }
            case TWOTOTWO: {
                if (direction == Direction.UP | direction == Direction.DOWN) {
                    Piece aim2 = findPieceByCoordinate(aim.getCoordinate()[0].getX() + 1, aim.getCoordinate()[0].getY());
                    aim.move(counterDirection(direction));
                    aim.move(counterDirection(direction));
                    aim2.move(counterDirection(direction));
                    aim2.move(counterDirection(direction));
                    piece.move(direction);
                } else {
                    Piece aim2 = findPieceByCoordinate(aim.getCoordinate()[0].getX(), aim.getCoordinate()[0].getY() + 1);
                    aim.move(counterDirection(direction));
                    aim.move(counterDirection(direction));
                    aim2.move(counterDirection(direction));
                    aim2.move(counterDirection(direction));
                    piece.move(direction);
                }
            }
        }
    }

    public boolean moveable(Piece piece, Direction direction) {
        int x = piece.getCoordinate()[0].getX();
        int y = piece.getCoordinate()[0].getY();
        PieceType pieceType = piece.pieceType;
        Piece aim;
        switch (direction) {
            case LEFT:
                if (x == 0) return false;
                aim = findPieceByCoordinate(piece.pieceType == PieceType.TWOTOTWO ? x - 2 : x - 1, y);
                break;
            case RIGHT:
                if (x == marginX) return false;
                aim = findPieceByCoordinate(piece.pieceType == PieceType.TWOTOTWO ? x + 2 : x + 1, y);
                break;
            case UP:
                if (y == 0) return false;
                aim = findPieceByCoordinate(x, piece.pieceType == PieceType.TWOTOTWO ? y - 2 : y - 1);
                break;
            case DOWN:
                if (y == marginY) return false;
                aim = findPieceByCoordinate(x, piece.pieceType == PieceType.TWOTOTWO ? y + 2 : y + 1);
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
                    return findPieceByCoordinate(aim.getCoordinate()[0].getX(), aim.getCoordinate()[0].getY() + 1).pieceType == PieceType.BLANK;
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
        return moveable(aim, counterDirection(direction));
//        switch (aim.pieceType) {
//            case ONETOONE:
//            case BLANK: {
//                return true;
//            }
//            case TWOTOONE: {
//                if (direction == Direction.UP | direction == Direction.DOWN) {
//                    return true;
//                } else {
//                    return findPieceByCoordinate(x, aim.getCoordinate()[0].getY() == y ? y + 1 : y - 1).pieceType == PieceType.BLANK;
//                }
//            }
//            case ONETOTWO: {
//                if (direction == Direction.LEFT | direction == Direction.RIGHT) {
//                    return true;
//                } else {
//                    return findPieceByCoordinate(aim.getCoordinate()[0].getX() == x ? x + 1 : x - 1, y).pieceType == PieceType.BLANK;
//                }
//            }
//            case TWOTOTWO: {
//                if (direction == Direction.LEFT | direction == Direction.RIGHT) {
//                    if (findPieceByCoordinate(x, aim.getCoordinate()[0].getY() == y ? y + 1 : y - 1).pieceType == PieceType.BLANK) {
//                        return true;
//                    }
//                } else {
//                    if (findPieceByCoordinate(aim.getCoordinate()[0].getX() == x ? x + 1 : x - 1, y).pieceType == PieceType.BLANK) {
//                        return true;
//                    }
//                }
//            }
//        }
    }

    public Direction counterDirection(Direction direction) {
        switch (direction) {
            case RIGHT:
                return Direction.LEFT;
            case DOWN:
                return Direction.UP;
            case UP:
                return Direction.DOWN;
            case LEFT:
                return Direction.RIGHT;
            default:
                return null;
        }
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
}
