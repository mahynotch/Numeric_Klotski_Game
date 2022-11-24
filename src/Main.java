import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Scanner;
/*
可通过运行文件夹下的test.bat测试输入
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
        //这个board用以储存所有的块
        int[][] board = new int[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                board[i][j] = scanner.nextInt();
            }
        }
        int morbidLength = scanner.nextInt();//这个变量是特殊块数量
        //这个hashmap用以储存特殊块，key为块名，value为类型
        HashMap<Integer, String> hashMap = new HashMap<>();
        for (int i = 0; i < morbidLength; i++) {
            hashMap.put(scanner.nextInt(), scanner.next());
        }
        for (int[] i : board) {
            for (int j : i) {
                System.out.print(j + " ");
            }
        }
        System.out.println(hashMap.keySet());
    }
}