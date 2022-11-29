import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Collection;

public class RandomBoardGenerator {
    public static void main(String[] args) {
        Random random = new Random();
        int[][] pieceArray = arrayGenerate(random.nextInt(5) + 1, random.nextInt(5) + 1);

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
}
