/*
Connect four is a two-player board game in which the players
alternately drop colored disks into a seven-column, six-row
vertically suspended grid. The objective of the game is to
connect four same-colored disks in a row, a column, or a diagonal
before your opponent can do likewise. The program prompts two
players to drop a red or yellow disk alternately. Whenever a disk
s dropped, the program redisplays the board on the console
and determines the status of the game (win, draw, or continue).

Author: MindEncode
*/
import java.util.Scanner;

public class ConnectFour {
    // fields
    private double score;
    private char player;

    // constructor
    public ConnectFour(char color) {
        player = color;
    }

    private void setScore(double score) { this.score = score; }
    private double getScore() { return score; }

    public static void main(String[] args) {
        ConnectFour redPlayer = new ConnectFour('R');
        ConnectFour yellowPlayer = new ConnectFour('Y');
        play(redPlayer, yellowPlayer);
    }

    public static void play(ConnectFour p1, ConnectFour p2) {
        Scanner in = new Scanner(System.in);
        // create variables, red player starts first
        int answer = 0;
        boolean play = true;
        char player = 'R';
        char[][] board = new char[6][7];

        // display initially empty board
        drawTheBoard(board);

        while (play) {
            playerDropsDisk(board, player);
            // switch player
            player = (player == 'R') ? 'Y' : 'R';
            // display stats, play again or quit
            if (weHaveWinner(board, p1, p2)) {
                displayStats(p1, p2);
                System.out.print("\nType 1 to play again or 0 to quit: ");
                play = ((answer = in.nextInt()) == 1 ? true : false);

                if (play) {
                    clearTheBoard(board);
                    player = 'R';
                }
            }
        }
    }

    public static void drawTheBoard(char[][] board) {
        for (int i=0; i<board.length; i++) {
            for (int j=0; j<board[0].length; j++) {
                if (board[i][j] == 'Y' || board[i][j] == 'R')
                    System.out.print("|" + board[i][j]);
                else
                    System.out.print("|" + " ");
            }
            System.out.print("|");
            System.out.println();
        }
        System.out.println("---------------");
        System.out.println(" 0 1 2 3 4 5 6 ");
    }

    public static void clearTheBoard(char[][] board) {
        for (int i=0; i<board.length; i++) {
            for (int j=0; j<board[0].length; j++) {
                board[i][j] = ' ';
                System.out.print("|" + board[i][j]);

            }
            System.out.print("|");
            System.out.println();
        }
    }

    public static void playerDropsDisk(char[][] board, char player) {
        Scanner in = new Scanner(System.in);
        boolean columnIsIncorrectOrFull = true;

        while (columnIsIncorrectOrFull) {
            try {
                System.out.print("Drop a " + ((player == 'R') ? "red" : "yellow") + " disk at column (0–6): ");
                int column = in.nextInt();

                if (!correctColumnNumber(column) || columnIsFull(board, column))
                    System.out.println("The selected column is full or has the wrong number");
                
                insertDisk(board, column, player);
                drawTheBoard(board);
                columnIsIncorrectOrFull = false;

            } catch (Exception ex) {
                System.out.println("The column must be ONlY a number between 0-6");
                System.out.println();
                in.nextLine();
            }
        }
    }
    
    public static void insertDisk(char[][] board, int column, char disk) {
        for (int row=board.length-1; row>=0; row--) {
            if (board[row][column] != 'R' && board[row][column] != 'Y' ) {
                board[row][column] = disk;
                break;
            }
        }
    }

    public static boolean correctColumnNumber(int column) {
        return (column == 0 || column == 1 ||
                column == 2 || column == 3 ||
                column == 4 || column == 5 ||
                column == 6);
    }

    public static boolean columnIsFull(char[][] board, int column) {
        int count = 0;
        for (int row=board.length-1; row>=0; row--) {
            if (board[row][column] == 'R' || board[row][column] == 'Y')
                count++;
        }
        return ((count == board.length) ? true : false);
    }

    public static boolean boardIsFull(char[][] board) {
        int count = 0;
        for (int i=0; i<board.length; i++) {
            for (int j=0; j<board[0].length; j++) {
                if (board[i][j] == 'R' || board[i][j] == 'Y')
                    count++;
            }
        }
        return count == 42;
    }

    public static void displayStats(ConnectFour p1, ConnectFour p2) {
        System.out.println("\nCurrent stats: " +
                           "\nPlayers | Score" +
                           "\n-----------------" +
                           "\nRed" + "     | " + p1.getScore() +
                           "\nYellow" + "  | " + p2.getScore());
    }

    public static boolean weHaveWinner(char[][] board, ConnectFour p1, ConnectFour p2) {
        char winner = getWinner(board);

        if (winner != ' ') {
            System.out.println("The" + ((winner == 'Y') ? " Yellow " : " Red " ) + "player has won");
            if (winner == 'R')
                p1.setScore(1);
            else
                p2.setScore(1);
            return true;
        }

        if (boardIsFull(board)) {
            System.out.println("It's a draw");
            p1.setScore(0.5);
            p2.setScore(0.5);
            return true;
        }
        return false;
    }

    public static char getWinner(char[][] board) {
        char win = checkRows(board);
        if (win != ' ')
            return win;

        win = checkColumns(board);
        if (win != ' ')
            return win;

        win = checkBackSlash(board);
        if (win != ' ')
            return win;

        win = checkForwardSlash(board);
        if (win != ' ')
            return win;

        return ' ';
    }

    // methods for checking rows, columns and diagonals
    public static char checkRows(char[][] m) {
        for (int i=0; i<m.length; i++) {
            int count = 1;
            for (int j=1; j<m[0].length; j++) {
                if (Character.isLetter(m[i][j]) && m[i][j-1] == m[i][j])
                    count++;
                else
                    count = 1;

                if (count == 4)
                    return m[i][j];
            }
        }
        return ' ';
    }

    public static char checkColumns(char[][] m) {
        for (int i=0; i<m.length; i++) {
            int count = 1;
            for (int j=1; j<m.length; j++) {
                if (Character.isLetter(m[j][i]) && m[j-1][i] == m[j][i])
                    count++;
                else
                    count = 1;

                if (count == 4)
                    return m[j][i];
            }
        }
        return ' ';
    }

    public static char checkForwardSlash(char[][] m) {
        for (int i=3; i<m.length; i++) {
            for (int j=0; j<m[0].length-3; j++) {
                char c = forwardSlash(m, i, j); // check diagonal
                if (c != ' ')
                    return c;
            }
        }
        return ' ';
    }

    public static char forwardSlash(char[][] m, int row, int col) {
        int count = 1;
        for (int k=0; k<3; k++, row--, col++) {
            if (Character.isLetter(m[row][col]) && m[row][col] == m[row-1][col+1])
                count++;
        }
        return (count == 4 ? m[row][col] : ' ');
    }

    public static char checkBackSlash(char[][] m) {
        for (int i=0; i<m.length-3; i++) {
            for (int j=0; j<m[0].length-3; j++) {
                char c = backSlash(m, i, j); // check diagonal
                if (c != ' ')
                    return c;
            }
        }
        return ' ';
    }

    public static char backSlash(char[][] m, int row, int col) {
        int count = 1;
        for (int k=0; k<3; k++, row++, col++) {
            if (Character.isLetter(m[row][col]) && m[row][col] == m[row+1][col+1])
                count++;
        }
        return (count == 4 ? m[row][col] : ' ');
    }
}
