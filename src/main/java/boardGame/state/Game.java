package boardGame.state;

public class Game {

    public static int steps;
    public static int[][] board = new int[8][8];
    public static int boardLength = board.length;


    /**
     * player 1 is even starting from 0, then adding its position and add one right of it
     * player 2 is odd starting from 1, then adding its position and add one down of it
     * @param x x coordinate
     * @param y y coordinate
     * @param player
     */
    public static void addStep(int x, int y, int player) {
        if (player % 2 == 0) { //Check whether there is a number on the relative position.
            board[x][y] = player;
            board[x][y + 1] = player;
        } else {
            board[x][y] = player;
            board[x + 1][y] = player;
        }
        board[x][y] = player;
        steps++;
    }


    /**
     * reset Step to 0, create a new board
     */
    public static void resetStep() {
        steps = 0;
        board = new int[8][8];
    }


    /**
     *   for player 1, if there is space to click next to each other horizontally,return true
     *   for player 2, if there is space to click next to each other vertically, return true
     * @param x
     * @param y
     * @param player
     * @return
     * @throws ArrayIndexOutOfBoundsException
     */
    public static boolean clickable(int x, int y, int player) throws ArrayIndexOutOfBoundsException {
        if (player % 2 == 0) { //Check whether there is a number on the relative position.
            return board[x][y] == 0 && board[x][y + 1] == 0;
        } else {
            try {
                return board[x][y] == 0 && board[x + 1][y] == 0;
            } catch (ArrayIndexOutOfBoundsException ignore) {
            }
        }
        return false;
    }


    /**
     *  make sure there is space for opposite player, if not , current player win
     *
     *
     * @param player
     * @return
     */
    public static boolean isWin(int player) {
        if (player % 2 == 0) {                                      //Player A: horizontal
            for (int i = 0; i < boardLength - 1; i++) {
                for (int j = 0; j < boardLength; j++) {
                    if (board[i][j] == 0 && board[i + 1][j] == 0) return true;
                }
            }
        } else {                                              //Player B: vertical
            for (int i = 0; i < boardLength; i++) {
                for (int j = 0; j < boardLength - 1; j++) {
                    if (board[i][j] == 0 && board[i][j + 1] == 0) return true;
                }
            }
        }
        return false;
    }

}
