import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class GameFrame extends JFrame implements ActionListener {
    public int WIDTH;
    public int HEIGHT;
    public int CHESSBOARD_SIZE;
    Piece[] pieces;
    int marginX;
    int marginY;
    Board board;

    public GameFrame(int width, int height, Piece[] pieces, int marginX, int marginY) {
        setTitle("Numeric Klotski"); //设置标题
        this.WIDTH = width;
        this.HEIGHT = height;
        this.pieces = pieces;
        this.marginX = marginX;
        this.marginY = marginY;


        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);

        Board board = new Board(pieces, marginX, marginY);
        board.setGameFrame(this);
        board.setLocation(0, 90);
        board.setSize(500,500);
        add(board);

        JButton btnNew = new JButton("Go");
        btnNew.setActionCommand("Go");
        btnNew.addActionListener(this);
        btnNew.setLocation(0, 0);
        btnNew.setSize(500, 45);
        btnNew.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(btnNew);

        JButton btnRandom = new JButton("Random");
        btnRandom.setActionCommand("Random");
        btnRandom.addActionListener(this);
        btnRandom.setLocation(0, 45);
        btnRandom.setSize(500, 45);
        btnRandom.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(btnRandom);


        setVisible(true);
        repaint();
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
            System.out.println(board);
        } else if ("Random".equals((cmd))) {
            System.out.println("Generate a solvable board:");
        }
    }
}


