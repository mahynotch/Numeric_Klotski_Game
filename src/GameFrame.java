import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class GameFrame extends JFrame implements ActionListener {
    public int WIDTH;
    public int HEIGHT;
    public int CHESSBOARD_SIZE;
    Piece[] pieces;
    public int marginX;
    public int marginY;
    public Board board;
    public static GameFrame it;


    public Board getBoard() {
        return board;
    }

    public GameFrame(int width, int height, Piece[] pieces, int MarginX, int MarginY) {
        setTitle("Numeric Klotski"); //设置标题
        this.WIDTH = width;
        this.HEIGHT = height;
        this.CHESSBOARD_SIZE = HEIGHT * 4 / 5;
        this.marginX = MarginX;
        this.marginY = MarginY;
        this.pieces = pieces;


        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);

        setSize(WIDTH, HEIGHT);
        setLocation(0, 0);
        setLayout(null);
        addBoard();


        JButton btnNew = new JButton("Go");
        btnNew.setActionCommand("Go");
        btnNew.addActionListener(this);
        btnNew.setLocation(0, 0);
        btnNew.setSize(500, 45);
        btnNew.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(btnNew);

        setVisible(true);
    }

    public void addBoard() {
        board = new Board(this.pieces, this.marginX, this.marginY);
        board.setGameFrame(this);
        board.setLocation(HEIGHT / 10, HEIGHT / 10 - 10);
        add(board);
        it = this;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("clicked");
        Scanner scanner = new Scanner(System.in);
        int movedPiece = scanner.nextInt();
        String moveDirection = scanner.next();
        Direction realDirection = null;
        switch (moveDirection) {
            case "L":
                realDirection = Direction.LEFT;
                break;
            case "R":
                realDirection = Direction.RIGHT;
                break;
            case "U":
                realDirection = Direction.UP;
                break;
            case "D":
                realDirection = Direction.DOWN;
                break;
            default:
                break;
        }
        String cmd = e.getActionCommand();
        if ("Go".equals(cmd)) {
            System.out.println("Go: " + movedPiece + " " + moveDirection);
            assert realDirection != null;
            board.move(board.findPieceByValue(movedPiece), realDirection);
            System.out.println(getBoard());
        }
    }
}
