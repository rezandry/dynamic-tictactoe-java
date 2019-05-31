import java.util.Scanner;
/**
 * Submission for F&D recruitment test
 * Name = Reza Andriyunanto
 * Email = andriyunantoreza@gmail.com
 * Requirement :
 *   - Dynamic size without hardcode
 */
public class TTTConsole {
    // Name-constants to represent the seeds and cell contents
    private static final int EMPTY = 0;
    private static final int CROSS = 1;
    private static final int NOUGHT = 2;

    // Name-constants to represent the various states of the game
    private static final int PLAYING = 0;
    private static final int DRAW = 1;
    private static final int CROSS_WON = 2;
    private static final int NOUGHT_WON = 3;

    // The game board and the game status
    private static int SIZE; // number of rows and columns
    private static int ROWS, COLS; // number of rows and columns
    private static int[][] board; // game board in 2D array

    //  containing (EMPTY, CROSS, NOUGHT)
    private static int currentState;  // the current state of the game

    // (PLAYING, DRAW, CROSS_WON, NOUGHT_WON)
    private static int currentPlayer; // the current player (CROSS or NOUGHT)
    private static int currntRow, currentCol; // current seed's row and column

    public static Scanner in = new Scanner(System.in); // the input Scanner

    /** The entry main method (the program starts here) */
    public static void main(String[] args) {
        // Initialize the game-board and current status
        setSizeBoard();
        initGame();
        // Play the game once
        do {
            playerMove(currentPlayer); // update currentRow and currentCol
            updateGame(currentPlayer, currntRow, currentCol); // update currentState
            printBoard();
            // Print message if game-over
            if (currentState == CROSS_WON) {
                System.out.println("'X' won! Bye!");
            } else if (currentState == NOUGHT_WON) {
                System.out.println("'O' won! Bye!");
            } else if (currentState == DRAW) {
                System.out.println("It's a Draw! Bye!");
            }
            // Switch player
            currentPlayer = (currentPlayer == CROSS) ? NOUGHT : CROSS;
        } while (currentState == PLAYING); // repeat if not game-over
    }

    /** Set the game-board size */
    private static void setSizeBoard(){
        System.out.print("Enter size of board : ");
        SIZE = in.nextInt();
        COLS = SIZE;
        ROWS = SIZE;
    }

    /** Initialize the game-board size */
    private static void initBoard() {
        board = new int[ROWS][COLS]; // game board in 2D array
    }

    /** Initialize the game-board contents and the current states */
    private static void initGame() {
        initBoard();
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                board[row][col] = EMPTY;  // all cells empty
            }
        }
        currentState = PLAYING; // ready to play
        currentPlayer = CROSS;  // cross plays first
    }

    /** Player with the "theSeed" makes one move, with input validation.
     Update global variables "currentRow" and "currentCol". */
    private static void playerMove(int theSeed) {
        boolean validInput = false;  // for input validation
        do {
            if (theSeed == CROSS) {
                System.out.print("Player 'X', enter your move (row[1-"+SIZE+"] column[1-"+SIZE+"]): ");
            } else {
                System.out.print("Player 'O', enter your move (row[1-"+SIZE+"] column[1-"+SIZE+"]): ");
            }
            int row = in.nextInt() - 1;  // array index starts at 0 instead of 1
            int col = in.nextInt() - 1;
            if (row >= 0 && row < ROWS && col >= 0 && col < COLS && board[row][col] == EMPTY) {
                currntRow = row;
                currentCol = col;
                board[currntRow][currentCol] = theSeed;  // update game-board content
                validInput = true;  // input okay, exit loop
            } else {
                System.out.println("This move at (" + (row + 1) + "," + (col + 1)
                        + ") is not valid. Try again...");
            }
        } while (!validInput);  // repeat until input is valid
    }

    /** Update the "currentState" after the player with "theSeed" has placed on
     (currentRow, currentCol). */
    private static void updateGame(int theSeed, int currentRow, int currentCol) {
        if (hasWon(theSeed, currentRow, currentCol)) {  // check if winning move
            currentState = (theSeed == CROSS) ? CROSS_WON : NOUGHT_WON;
        } else if (isDraw()) {  // check for draw
            currentState = DRAW;
        }
        // Otherwise, no change to currentState (still PLAYING).
    }

    /** Return true if it is a draw (no more empty cell) */
    private static boolean isDraw() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (board[row][col] == EMPTY) {
                    return false;  // an empty cell found, not draw, exit
                }
            }
        }
        return true;  // no empty cell, it's a draw
    }

    /** Return true if the player with "theSeed" has won after placing at
     (currentRow, currentCol) */
    private static boolean hasWon(int theSeed, int currentRow, int currentCol) {
        return (rowWin(theSeed, currentRow)
                || columnWin(theSeed, currentCol)
                || diagonalWin(theSeed, currentRow, currentCol)
                || reverseDiagonalWin(theSeed, currentRow, currentCol));
    }

    /** Check possibility win as straight row */
    private static boolean rowWin(int theSeed, int currentRow){
        boolean win = true;
        for (int col=0; col<SIZE; col++){
            win = win && board[currentRow][col] == theSeed;
        }
        return win;
    }

    /** Check possibility win as straight column */
    private static boolean columnWin(int theSeed, int currentCol){
        boolean win = true;
        for (int row=0; row<SIZE; row++){
            win = win && board[row][currentCol] == theSeed;
        }
        return win;
    }

    /** Check possibility win as straight diagonel */
    private static boolean diagonalWin(int theSeed, int currentRow, int currentCol){
        boolean win = currentRow == currentCol;
        for (int rowAndCol=0; rowAndCol<SIZE; rowAndCol++){
            win = win && board[rowAndCol][rowAndCol] == theSeed;
        }
        return win;
    }

    /** Check possibility win as straight reverse diagonal */
    private static boolean reverseDiagonalWin(int theSeed, int currentRow, int currentCol){
        boolean win = currentRow + currentCol == SIZE-1;
        int col = SIZE-1;
        for (int row=0; row<SIZE; row++){
            win = win && board[row][col] == theSeed;
            col = col-1;
        }
        return win;
    }

    /** Print the game board */
    private static void printBoard() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                printCell(board[row][col]); // print each of the cells
                if (col != COLS - 1) {
                    System.out.print("|");   // print vertical partition
                }
            }
            System.out.println();
            if (row != ROWS - 1) {
                for (int col = 0; col < COLS; ++col) {
                    System.out.print("----"); // print horizontal partition
                }
                System.out.println();
            }
        }
        System.out.println();
    }

    /** Print a cell with the specified "content" */
    private static void printCell(int content) {
        switch (content) {
            case EMPTY:  System.out.print("   "); break;
            case NOUGHT: System.out.print(" O "); break;
            case CROSS:  System.out.print(" X "); break;
        }
    }
}