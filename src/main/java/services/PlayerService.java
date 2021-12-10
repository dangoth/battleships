package services;

import players.Player;

import java.io.PrintStream;
import java.util.stream.IntStream;

public class PlayerService {

    private final PrintStream printStream;
    private final int boardSize = 10;
    private final char[] alphabet = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
    private final Player humanPlayer;
    private final Player enemyPlayer;

    public PlayerService() {
        humanPlayer = new Player();
        enemyPlayer = new Player();
        printStream = new PrintStream(System.out);
    }

    public Player getHumanPlayer() {
        return humanPlayer;
    }

    public Player getEnemyPlayer() {
        return enemyPlayer;
    }

    public char[][] getEnemyGameBoard() {
        return enemyPlayer.getGameBoard();
    }

    public char[][] getPlayerGameBoard() {
        return humanPlayer.getGameBoard();
    }

    public void setPlayerGameBoard(char[][] board) {
        humanPlayer.gameBoard = board;
    }

    public void printPlayerGameBoard() {
        printStream.println("  " + String.valueOf(alphabet));
        IntStream.range(0, boardSize)
                .forEach(i -> printStream.printf("%2s", i + 1)
                        .println(humanPlayer.gameBoard[i]));
        printStream.print("\n");
        printStream.flush();
    }
}
