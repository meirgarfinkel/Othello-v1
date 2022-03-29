public enum CellState {
    NONE, BLACK, WHITE;

    public String toIcon() {
        if (this == CellState.WHITE) {
            return "\u25CF";
        }
        if (this == CellState.BLACK) {
            return "\u25CB";
        } else {
            return "  ";
        }
    }
}
