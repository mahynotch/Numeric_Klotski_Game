import javax.swing.*;
import java.util.HashMap;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        GUI();
    }

    //CLI界面
    public static void CLI() {
        Scanner scanner = new Scanner(System.in);
        int row = scanner.nextInt();
        int column = scanner.nextInt();
        int[][] board = new int[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                board[i][j] = scanner.nextInt();
            }
        }
        int morbidLength = scanner.nextInt();
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

    //GUI界面
    private static void GUI() {
        JFrame frame = new JFrame("Input");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel text = new JLabel("请输入");
        text.setAlignmentX(500);
        text.setAlignmentY(500);
        frame.getContentPane().add(text);
        frame.getContentPane().add(new JTextField());
        frame.getContentPane().add(new JTextField());
        // 显示窗口
        frame.pack();
        frame.setSize(1000, 1000);
        frame.setVisible(true);
    }
}