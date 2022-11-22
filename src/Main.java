import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;


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
        int morbidLength = scanner.nextInt();
        HashMap<Integer, String> hashMap = new HashMap<>();
        for(int i = 0; i < morbidLength; i++) {
            hashMap.put(scanner.nextInt(), scanner.next());
        }
        for(int[] i: board){
            for (int j: i){
                System.out.print(j + " ");
            }
        }
        System.out.println(hashMap.keySet());
    }
}