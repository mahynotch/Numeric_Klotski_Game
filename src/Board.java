import javax.swing.*;

//本类用以储存棋子
public class Board extends JComponent {
    //棋子列表
    Piece[] pieces;
    //横向边缘
    int marginX;
    //纵向边缘
    int marginY;
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
        throw new IllegalArgumentException("No piece of such key");
    }

    public Piece findPieceByCoordinate(int x, int y) {
        Coordinate coordinate = new Coordinate(x, y);
        for (Piece j : pieces) {
            for (int k = 0; k < j.getCoordinate().length; k++) {
                if (j.getCoordinate()[k].equals(coordinate)) {
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

    public boolean zeroCanMove(Piece piece, Direction Direction) {
        Coordinate[] Cor = piece.getCoordinate();
        int x = Cor[Cor.length - 1].x;
        int y = Cor[Cor.length - 1].y;
        switch (Direction) {
            case LEFT:
                return x > 0;
            case RIGHT:
                return x < marginX;
            case UP:
                return y > 0;
            case DOWN:
                return y < marginY;
        }
        return false;
    }

    public void Move(Piece piece,Direction Direction){
        Coordinate[] Cor = piece.getCoordinate();
        int x = Cor[Cor.length - 1].x;
        int y = Cor[Cor.length - 1].y;
            int a;
            switch(Direction){
                case LEFT:
                    a = findPieceByCoordinate(x-1,y).getValue()[findPieceByCoordinate(x-1,y).getValue().length-1];
                    findPieceByCoordinate(x-1,y).getValue()[0] = 0;
                    findPieceByCoordinate(x,y).getValue()[0] = a;
                    break;
                case RIGHT:
                    a = findPieceByCoordinate(x+1,y).getValue()[findPieceByCoordinate(x+1,y).getValue().length-1];
                    findPieceByCoordinate(x+1,y).getValue()[0] = 0;
                    findPieceByCoordinate(x,y).getValue()[0] = a;
                    break;
                case UP:
                    a = findPieceByCoordinate(x,y-1).getValue()[findPieceByCoordinate(x,y-1).getValue().length-1];
                    findPieceByCoordinate(x,y-1).getValue()[0] = 0;
                    findPieceByCoordinate(x,y).getValue()[0] = a;
                    break;
                case DOWN:
                    a = findPieceByCoordinate(x,y+1).getValue()[findPieceByCoordinate(x,y+1).getValue().length-1];
                    findPieceByCoordinate(x,y+1).getValue()[0] = 0;
                    findPieceByCoordinate(x,y).getValue()[0] = a;
                    break;
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
