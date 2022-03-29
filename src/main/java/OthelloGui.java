import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OthelloGui extends JFrame {
    private OthelloModel model;
    private JButton[][] buttons;

    OthelloGui(OthelloModel model) {
        this.model = model;
        setTitle("Othello - User is Black");
        //default size of window
        super.setSize(650, 650);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //size of board
        int size = model.GRID_SIZE;

        setLayout(new GridLayout(size, size));
        ActionListener al = new ButtonListener();
        //initializing buttons-with-points matrix
        buttons = new MyJButton[size][size];

        Font font = new Font("", Font.BOLD, 50);
        Color color = new Color(78, 111, 16);

        //filling buttons with myJButtons
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.add(buttons[i][j] = new MyJButton(new Point(i, j)));
                buttons[i][j].addActionListener(al);
                buttons[i][j].setFont(font);
                buttons[i][j].setBackground(color);
            }
        }
        //start the game
        model.setOthelloBoard();
        updateBoard();
        this.setVisible(true);
    }

    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            MyJButton mjb = (MyJButton) e.getSource();
            Point p = mjb.p;

            if (model.getUsersTurn()) {

                model.userMove(p.x, p.y, CellState.WHITE);
                updateBoard();

                model.computerMove(CellState.BLACK, true);
                updateBoard();

            } else {
                System.out.println("not your turn");
            }
        }
    }


    static class MyJButton extends JButton {
        MyJButton(Point p) {
            this.p = p;
        }

        Point p;
    }

    void updateBoard() {
        int size = model.GRID_SIZE;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttons[i][j].setText(model.getCellState(i, j).toIcon());
            }
        }
    }
}
