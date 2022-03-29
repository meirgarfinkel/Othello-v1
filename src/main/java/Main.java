import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        OthelloModel om = new OthelloModel();

        JFrame window = new OthelloGui(om);
    }
}
