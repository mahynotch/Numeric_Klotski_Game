
import java.util.*;
/*
可通过运行文件夹下的test.bat测试输入, 输入内容在./test/1.txt中
 */

public class Main {
    public static void main(String[] args) {
        CLI();
    }

    //CLI界面
    public static void CLI() {
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
        ArrayList<Piece> pieces = new ArrayList<>();
        for (int i = 0; i < boardArray.length; i++) {
            for (int j = 0; j < boardArray[i].length; j++) {
                int value = boardArray[i][j];
                if (hashMap.containsKey(value)) {
                    String type = hashMap.get(value);
                    //添加块
                    if (hashMap.get(value).equals("2*2")) {
                        try {
                            int[] values = {value, boardArray[i][j + 1], boardArray[i + 1][j], boardArray[i + 1][j + 1]};
                            boardArray[i][j + 1] = -1;
                            boardArray[i + 1][j] = -1;
                            boardArray[i + 1][j + 1] = -1;
                            Coordinate[] coordinates = {new Coordinate(j, i), new Coordinate(j + 1, i), new Coordinate(j, i + 1), new Coordinate(j + 1, i + 1)};
                            pieces.add(new Piece(values, PieceType.TWOTOTWO, coordinates));
                        } catch (Exception e) {
                            throw new ArrayIndexOutOfBoundsException("Some of the values in board is not accessible by a 2*2 block!");
                        }
                    } else if (hashMap.get(value).equals("2*1")) {
                        try {
                            int[] values = {value, boardArray[i + 1][j]};
                            boardArray[i + 1][j] = -1;
                            Coordinate[] coordinates = {new Coordinate(j, i), new Coordinate(j, i + 1)};
                            pieces.add(new Piece(values, PieceType.TWOTOONE, coordinates));
                        } catch (Exception e) {
                            throw new ArrayIndexOutOfBoundsException("Some of the values in board is not accessible by a 2*1 block!");
                        }
                    } else if (hashMap.get(value).equals("1*2")) {
                        try {
                            int[] values = {value, boardArray[i][j + 1]};
                            boardArray[i][j + 1] = -1;
                            Coordinate[] coordinates = {new Coordinate(j, i), new Coordinate(j + 1, i)};
                            pieces.add(new Piece(values, PieceType.ONETOTWO, coordinates));
                        } catch (Exception e) {
                            throw new ArrayIndexOutOfBoundsException("Some of the values in board is not accessible by a 2*2 block!");
                        }
                    }
                } else {
                    if (value != -1) {
                        pieces.add(new Piece(new int[]{value}, PieceType.ONETOONE, new Coordinate[]{new Coordinate(j, i)}));
                    }
                }
            }
        }
        Board board = new Board(pieces.toArray(new Piece[0]), boardArray[0].length - 1, boardArray.length - 1);
        //展示棋盘
        System.out.println(board.toString());
        //测试并打印移动后的棋盘
        board.Move(board.findPieceByValue(0),Direction.UP);
        System.out.println(board.toString());
    }
}