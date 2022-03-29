public interface IOthelloModel {
    boolean makeMove(int row, int col, CellState state);

    boolean isMoveLegal(int row, int col, CellState cellstate);

    boolean isMoveFlippable(int row, int col, CellState cellstate);

    boolean isLocationInBounds(int row, int col);

    CellState getCellState(int row, int col);
}
