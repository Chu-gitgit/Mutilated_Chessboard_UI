package boardGame.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    public static int[][] board = new int[8][8];
    int x = 5;
    int y = 6;


    @Test
    void clickable() {
        assertTrue(board[x][y] == 0 && board[x][y + 1] == 0);

        assertFalse( !(board[x][y] == 0 && board[x][y + 1] == 0));
    }

    public static int[][] board2 = new int[8][8];
     int i = 2;
     int j = 3;

    @Test
    void isWin() {
            assertTrue((board[i][j] == 0)&&(board[i + 1][j] == 0));

            assertFalse(!((board[i][j] == 0)&&(board[i + 1][j] == 0)));

    }


}