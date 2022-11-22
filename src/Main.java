import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int row = scanner.nextInt();
        int column = scanner.nextInt();
        int[][] board = new int[row][column];
        for(int i = 0; i < row; i++) {
            for(int j = 0; j < column; j++){
                board[i][j] = scanner.nextInt();
            }
        }
        for(int i = 0; i < scanner.nextInt(); i++) {
            
        }
        System.out.println();
    }
}