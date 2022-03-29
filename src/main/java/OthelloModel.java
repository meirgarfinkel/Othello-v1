import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class OthelloModel {
    public static final int GRID_SIZE = 8;
    private boolean playersTurn = true;
    private CellState[][] othelloBoard = new CellState[GRID_SIZE][GRID_SIZE];
    private Set<Point> pointSet = new HashSet<>();

    public void setOthelloBoard() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                this.othelloBoard[i][j] = CellState.NONE;
            }
        }
        //sets up first 4 pieces
        this.othelloBoard[3][3] = CellState.WHITE;
        this.othelloBoard[3][4] = CellState.BLACK;
        this.othelloBoard[4][4] = CellState.WHITE;
        this.othelloBoard[4][3] = CellState.BLACK;
        //adds surrounding points to pointSet list
        for (int i = 2; i < 6; i++) {
            pointSet.add(new Point(2, i));
        }
        for (int i = 2; i < 6; i++) {
            pointSet.add(new Point(5, i));
        }
        pointSet.add(new Point(3, 2)); pointSet.add(new Point(3, 5));
        pointSet.add(new Point(4, 2)); pointSet.add(new Point(4, 5));

    }

    //checks if point is empty and will flip anything, if not, checks if any possible place to go, if not ends users turn
    boolean userMove(int row, int col, CellState state) {
        System.out.println("user clicked " + row + ", " + col);
        //prevents player from going twice in a row
        playersTurn = false;
        //if empty box
        if (isEmpty(row, col)) {
            //if flippable pieces
            if (checkAndFlip(row, col, state, false) > 0) { // could be optimized to return true after finds at least one to flip
                //updates clicked box
                this.othelloBoard[row][col] = state;
                //flips flippable pieces
                checkAndFlip(row, col, state, true);
                System.out.println("end user turn");
                //exits players turn
                return true;
                //if NOT flippable pieces
            } else {
                System.out.println("doesn't flip anything");
                //if there are other possible moves for user
                if (computerMove(state, false) != 0) {
                    //when gui tries running computerMove, it'll quit immediately
                    playersTurn = true;
                }
                return false;
            }
        }
        //if NOT empty box
        else {
            System.out.println("can't go there");
            //if there are other possible moves for user
            if (computerMove(state, false) != 0) {
                playersTurn = true;
                return false;
            }
        }
        //never gets called
        return true;
    }

    int computerMove(CellState state, boolean aiTurn) {
        //quits if it's users turn
        if (aiTurn && getUsersTurn()) {
            return 0;
        }
        int highestFlippable = 0;
        int currentFlippable;
        Point pointCounter = new Point();
        //checks all points in pointSet and sets pointCounter to greatest flippable point
        for (Point point : pointSet) {
            if (isEmpty(point.x, point.y)) {
                currentFlippable = checkAndFlip(point.x, point.y, state, false);
                if (currentFlippable > highestFlippable) {
                    highestFlippable = currentFlippable;
                    pointCounter = new Point(point.x, point.y);
                }
            }
        }
        //if current user has nowhere to go
        if (highestFlippable < 1) {
            //flips playersTurn to opposite
            playersTurn = (!getUsersTurn());
        } else {
            //if it's ai's turn and highestFlippable has at least one
            if (aiTurn) {
                System.out.println("ai clicked " + pointCounter.x + ", " + pointCounter.y);
                //updates chosen box
                this.othelloBoard[pointCounter.x][pointCounter.y] = state;
                //flips flippable pieces
                checkAndFlip(pointCounter.x, pointCounter.y, state, true);
                System.out.println("end ai turn");
                playersTurn = true;
            } else {
                //if it's NOT ai's turn and highestFlippable has at least one
                System.out.println("go again");
            }
        }
        return highestFlippable;
    }

    int checkAndFlip(int row, int col, CellState state, boolean checkAndFlip) {

        int amountToFlipTopLeftCount = topLeftCount(row, col, state);

        int amountToFlipTopMiddleCount = topMiddleCount(row, col, state);

        int amountToFlipTopRightCount = topRightCount(row, col, state);

        int amountToFlipLeftCount = leftCount(row, col, state);

        int amountToFlipRightCount = rightCount(row, col, state);

        int amountToFlipBottomLeftCount = bottomLeftCount(row, col, state);

        int amountToFlipBottomMiddleCount = bottomMiddleCount(row, col, state);

        int amountToFlipBottomRightCount = bottomRightCount(row, col, state);

        if (checkAndFlip) {
            //not sure if this works. it's supposed to removed clicked point from pointSet
            Point point = new Point(row, col);
            pointSet.remove(point);
            //adds all boxes surrounding click to pointSet
            if (row > 0 && col > 0 && isEmpty(row-1, col-1)) {
                setAdder(row - 1, col - 1);
            }
            if (row > 0 && isEmpty(row-1, col)) {
                setAdder(row - 1, col);
            }
            if (row > 0 && col < 7 && isEmpty(row-1, col+1)) {
                setAdder(row - 1, col + 1);
            }
            if (col > 0 && isEmpty(row, col-1)) {
                setAdder(row, col - 1);
            }
            if (col < 7 && isEmpty(row, col+1)) {
                setAdder(row, col + 1);
            }
            if (row < 7 && col > 0 && isEmpty(row+1, col-1)) {
                setAdder(row + 1, col - 1);
            }
            if (row < 7 && isEmpty(row+1, col)) {
                setAdder(row + 1, col);
            }
            if (row < 7 && col < 7 && isEmpty(row+1, col+1)) {
                setAdder(row + 1, col + 1);
            }
            //flips all flippable pieces
            if (amountToFlipTopLeftCount > 0) {
                int row1 = row - 1;
                int col1 = col - 1;
                for (int i = 0; i < amountToFlipTopLeftCount; i++) {
                    System.out.println("flipped " + row1 + "," + col1);
                    this.othelloBoard[row1--][col1--] = state;
                }
            }
            if (amountToFlipTopMiddleCount > 0) {
                int row1 = row - 1;
                int col1 = col;
                for (int i = 0; i < amountToFlipTopMiddleCount; i++) {
                    System.out.println("flipped " + row1 + "," + col1);
                    this.othelloBoard[row1--][col1] = state;
                }
            }
            if (amountToFlipTopRightCount > 0) {
                int row1 = row - 1;
                int col1 = col + 1;
                for (int i = 0; i < amountToFlipTopRightCount; i++) {
                    System.out.println("flipped " + row1 + "," + col1);
                    this.othelloBoard[row1--][col1++] = state;
                }
            }
            if (amountToFlipLeftCount > 0) {
                int row1 = row;
                int col1 = col - 1;
                for (int i = 0; i < amountToFlipLeftCount; i++) {
                    System.out.println("flipped " + row1 + "," + col1);
                    this.othelloBoard[row1][col1--] = state;
                }
            }
            if (amountToFlipRightCount > 0) {
                int row1 = row;
                int col1 = col + 1;
                for (int i = 0; i < amountToFlipRightCount; i++) {
                    System.out.println("flipped " + row1 + "," + col1);
                    this.othelloBoard[row1][col1++] = state;
                }
            }
            if (amountToFlipBottomLeftCount > 0) {
                int row1 = row + 1;
                int col1 = col - 1;
                for (int i = 0; i < amountToFlipBottomLeftCount; i++) {
                    System.out.println("flipped " + row1 + "," + col1);
                    this.othelloBoard[row1++][col1--] = state;
                }
            }
            if (amountToFlipBottomMiddleCount > 0) {
                int row1 = row + 1;
                int col1 = col;
                for (int i = 0; i < amountToFlipBottomMiddleCount; i++) {
                    System.out.println("flipped " + row1 + "," + col1);
                    this.othelloBoard[row1++][col1] = state;
                }
            }
            if (amountToFlipBottomRightCount > 0) {
                int row1 = row + 1;
                int col1 = col + 1;
                for (int i = 0; i < amountToFlipBottomRightCount; i++) {
                    System.out.println("flipped " + row1 + "," + col1);
                    this.othelloBoard[row1++][col1++] = state;
                }
            }
        }

        int total = (amountToFlipTopLeftCount + amountToFlipTopMiddleCount + amountToFlipTopRightCount
                + amountToFlipLeftCount + amountToFlipRightCount + amountToFlipBottomLeftCount
                + amountToFlipBottomMiddleCount + amountToFlipBottomRightCount);

        return total;
    }

    private boolean isEmpty(int row, int col) {
        return getCellState(row, col) == CellState.NONE;
    }

    CellState getCellState(int row, int col) {
        CellState state = othelloBoard[row][col];
        return state;
    }

    private int topLeftCount(int row, int col, CellState state) {
        if (row < 2 || col < 2) {
            return 0;
        }

        int counter = 0;
        CellState oppositeState = otherState(state);

        while (getCellState(--row, --col) == oppositeState && row > 0 && col > 0) {
            counter++;
        }

        if (counter > 0 && getCellState((row), (col)).equals(state)) {
            return counter;
        } else {
            return 0;
        }
    }

    private int topMiddleCount(int row, int col, CellState state) {
        if (row < 2) {
            return 0;
        }

        int counter = 0;
        CellState oppositeState = otherState(state);

        while (getCellState(--row, col) == oppositeState && row > 0) {
            counter++;
        }

        if (counter > 0 && getCellState((row), (col)).equals(state)) {
            return counter;
        } else {
            return 0;
        }
    }

    private int topRightCount(int row, int col, CellState state) {
        if (row < 2 || col > 5) {
            return 0;
        }

        int counter = 0;
        CellState oppositeState = otherState(state);

        while (getCellState(--row, ++col) == oppositeState && row > 0 && col < 7) {
            counter++;
        }

        if (counter > 0 && getCellState((row), (col)).equals(state)) {
            return counter;
        } else {
            return 0;
        }
    }

    private int leftCount(int row, int col, CellState state) {
        if (col < 2) {
            return 0;
        }

        int counter = 0;
        CellState oppositeState = otherState(state);

        while (getCellState(row, --col) == oppositeState && col > 0) {
            counter++;
        }

        if (counter > 0 && getCellState((row), (col)).equals(state)) {
            return counter;
        } else {
            return 0;
        }
    }

    private int rightCount(int row, int col, CellState state) {
        if (col > 5) {
            return 0;
        }

        int counter = 0;
        CellState oppositeState = otherState(state);

        while (getCellState(row, ++col) == oppositeState && col < 7) {
            counter++;
        }

        if (counter > 0 && getCellState((row), (col)).equals(state)) {
            return counter;
        } else {
            return 0;
        }
    }

    private int bottomLeftCount(int row, int col, CellState state) {
        if (row > 5 || col < 2) {
            return 0;
        }

        int counter = 0;
        CellState oppositeState = otherState(state);

        while (getCellState(++row, --col) == oppositeState && row < 7 && col > 0) {
            counter++;
        }

        if (counter > 0 && getCellState((row), (col)).equals(state)) {
            return counter;
        } else {
            return 0;
        }
    }

    private int bottomMiddleCount(int row, int col, CellState state) {
        if (row > 5) {
            return 0;
        }

        int counter = 0;
        CellState oppositeState = otherState(state);

        while (getCellState(++row, col) == oppositeState && row < 7) {
            counter++;
        }

        if (counter > 0 && getCellState((row), (col)).equals(state)) {
            return counter;
        } else {
            return 0;
        }
    }

    private int bottomRightCount(int row, int col, CellState state) {
        if (row > 5 || col > 5) {
            return 0;
        }

        int counter = 0;
        CellState oppositeState = otherState(state);

        while (getCellState(++row, ++col) == oppositeState && row < 7 && col < 7) {
            counter++;
        }

        if (counter > 0 && getCellState((row), (col)).equals(state)) {
            return counter;
        } else {
            return 0;
        }
    }

    //returns opposite state
    private CellState otherState(CellState state) {
        if (state == CellState.WHITE) {
            return CellState.BLACK;
        } else {
            return CellState.WHITE;
        }
    }

    //returns true if it's users turn
    boolean getUsersTurn() {
        return playersTurn;
    }

    //adds surrounding squares to set
    private void setAdder(int row, int col) {
        Point point = new Point(row, col);
        pointSet.add(point);
    }
}