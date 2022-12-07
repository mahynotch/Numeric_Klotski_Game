import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class GameFrame extends JFrame implements ActionListener {
    public int WIDTH;
    public int HEIGHT;
    Piece[] pieces;
    int marginX;
    int marginY;
    Board board;

    public GameFrame(int width, int height, Piece[] pieces, int marginX, int marginY, Board board) {
        setTitle("Numeric Klotski"); //设置标题
        this.WIDTH = width;
        this.HEIGHT = height;
        this.pieces = pieces;
        this.marginX = marginX;
        this.marginY = marginY;
        this.board = board;

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);

        board = new Board(pieces, marginX, marginY);

        board.setGameFrame(this);
        board.setLocation(0, 90);
        board.setSize(500, 500);
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
    }

    public Direction StoDirection(String s) {
        Direction D = null;
        switch (s) {
            case "U": {
                D = Direction.UP;
                break;
            }
            case "D": {
                D = Direction.DOWN;
                break;
            }
            case "L": {
                D = Direction.LEFT;
                break;
            }
            case "R": {
                D = Direction.RIGHT;
                break;
            }
        }
        return D;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if ("Go".equals(cmd)) {
            remove(this.board);
            if (Arrays.equals(board.getSolution(), new String[]{"No"})) {
                JOptionPane.showMessageDialog(this.board, "No", "SOLVABLE?", JOptionPane.ERROR_MESSAGE);
            } else {
                if (board.counter >= board.solution.length) {
                    JOptionPane.showMessageDialog(this.board, "Solved", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    String s = board.solution[board.counter];
                    String[] s1 = s.split(" ");
                    Piece movedPiece = board.findPieceByValue(Integer.parseInt(s1[0]));
                    Direction direction = StoDirection(s1[1]);
                    board.move(movedPiece, direction);
                    System.out.println(board);
                    board.setCounter(board.counter + 1);
                    board.setGameFrame(this);
                    board.setLocation(0, 90);
                    board.setSize(500, 500);
                    add(board);
                    board.repaint();
                }
            }

        } else if ("Random".equals((cmd))) {
            System.out.println("Generate a solvable board");
            remove(this.board);

            try {
                this.board = RandomBoardGenerator.RBG();
            } catch (CloneNotSupportedException ex) {
                ex.printStackTrace();
            }
            board.setGameFrame(this);
            board.setLocation(0, 90);
            board.setSize(500, 500);
            for (int i = 0; i < board.pieces.length; i++) {
                System.out.println(board.pieces[i].getCoordinate()[0].getX() + ":" + board.pieces[i].getCoordinate()[0].getY() + ";" + board.pieces[i].getValue()[0]);
            }
            add(board);
            board.repaint();
        }
    }
}


