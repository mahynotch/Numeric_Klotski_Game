import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Collection;

public class RandomBoardGenerator {
    public static void main(String[] args) {
        Random random = new Random();
        int[][] pieceArray = arrayGenerate(random.nextInt(5) + 1, random.nextInt(5) + 1);
        int numOfMorbid = random.nextInt(4);
        int[] morbids = new int[numOfMorbid];
        for (int i = 0; i < numOfMorbid; i++) {
            morbids[i] = random.nextInt(pieceArray.length * pieceArray[0].length);
        }
    }

    private static int[][] arrayGenerate(int row, int column) {
        int[][] res = new int[row][column];
        Random random = new Random();
        int emptyNum = random.nextInt(column);
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
}
