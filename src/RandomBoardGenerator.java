import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class RandomBoardGenerator {
    public static void main(String[] args) throws CloneNotSupportedException {
        Random random = new Random();
        int[][] pieceArray = arrayGenerate(random.nextInt(4) + 2, random.nextInt(4) + 2);
        int numOfMorbid = random.nextInt(4);
        int[] morbids = new int[numOfMorbid];
        for (int i = 0; i < numOfMorbid; i++) {
            morbids[i] = random.nextInt((pieceArray.length - 1) * pieceArray[0].length - 1) + 1;
        }
        HashMap<Integer, String> hashMap = new HashMap<>();
        ArrayList<Integer> occupied = new ArrayList<>();
        for (int i : morbids) {
            ArrayList<PieceType> pieceTypes = new ArrayList<>();
            int[] xAndY = findValue(pieceArray, i);
            int x = xAndY[0];
            int y = xAndY[1];
            if (x < pieceArray[0].length - 1) {
                if (pieceArray[y][x + 1] != 0 && !occupied.contains(pieceArray[y][x + 1])) {
                    pieceTypes.add(PieceType.ONETOTWO);
                    if (pieceArray[y + 1][x] != 0 && !occupied.contains(pieceArray[y + 1][x]) && pieceArray[y + 1][x + 1] != 0 && !occupied.contains(pieceArray[y + 1][x + 1])) {
                        pieceTypes.add(PieceType.TWOTOTWO);
                    }
                }
            }
            if (pieceArray[y + 1][x] != 0 && !occupied.contains(pieceArray[y + 1][x])) {
                pieceTypes.add(PieceType.TWOTOONE);
            }
            if (pieceTypes.size() != 0) {
                PieceType pieceType = pieceTypes.get(random.nextInt(pieceTypes.size()));
                switch (pieceType) {
                    case TWOTOONE:
                        hashMap.put(i, "2*1");
                        occupied.add(i);
                        occupied.add(pieceArray[y + 1][x]);
                        break;
                    case ONETOTWO:
                        hashMap.put(i, "1*2");
                        occupied.add(i);
                        occupied.add(pieceArray[y][x + 1]);
                        break;
                    case TWOTOTWO:
                        hashMap.put(i, "2*2");
                        occupied.add(i);
                        occupied.add(pieceArray[y + 1][x]);
                        occupied.add(pieceArray[y][x + 1]);
                        occupied.add(pieceArray[y + 1][x + 1]);
                        break;
                }
            }
        }
        Board board = Main.arrayToBoard(hashMap, pieceArray);
        randomize(board);
        System.out.println(board);
        KlotskiSolverM solverM = new KlotskiSolverM(board);
        long startTime = System.currentTimeMillis();
        solverM.solve();
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
        System.out.println(solverM.solution.length);
    }

    private static int[][] arrayGenerate(int row, int column) {
        int[][] res = new int[row][column];
        Random random = new Random();
        int emptyNum = random.nextInt(column - 1) + 1;
        for (int i = 0; i < row; i++) {
            if (i == row - 1) {
                for (int j = 0; j < column - emptyNum; j++) {
                    res[i][j] = i * column + j + 1;
                }
                break;
            }
            for (int j = 0; j < column; j++) {
                res[i][j] = i * column + j + 1;
            }
        }
        return res;
    }

    public static int[] findValue(int[][] pieceArray, int value) {
        for (int i = 0; i < pieceArray.length; i++) {
            for (int j = 0; j < pieceArray[0].length; j++) {
                if (pieceArray[i][j] == value) {
                    return new int[]{j, i};
                }
            }
        }
        throw new IllegalArgumentException("No such value in array: " + value);
    }

    public static void randomize(Board board) {
        Piece[] pieces = board.findAllPieceByValue(0);
        for (Piece piece : pieces) {
            for (int i = 0; i < 10000; i++) {
                Direction[] directions = new Direction[]{Direction.UP, Direction.LEFT, Direction.DOWN, Direction.RIGHT};
                Random rand = new Random();
                Direction direction = directions[rand.nextInt(4)];
                if (board.zeroMovable(piece, direction)) {
                    board.move(piece, direction);
                }
            }
        }
        board.steps = new ArrayList<>();
    }
}
