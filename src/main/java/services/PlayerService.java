package services;

import players.EnemyPlayer;
import players.HumanPlayer;

import java.io.PrintStream;
import java.util.stream.IntStream;

public class PlayerService {

    private final PrintStream printStream;
    private final int boardSize = 10;
    private final char[] alphabet = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
    private EnemyPlayer enemyPlayer;
    private HumanPlayer humanPlayer;

    public PlayerService() {
        enemyPlayer = new EnemyPlayer();
        humanPlayer = new HumanPlayer();
        printStream = new PrintStream(System.out);
    }

    public HumanPlayer getHumanPlayer() {
        return humanPlayer;
    }

    public EnemyPlayer getEnemyPlayer() {
        return enemyPlayer;
    }

    public char[][] getEnemyGameBoard() {
        return enemyPlayer.getGameBoard();
    }

    public char[][] getPlayerGameBoard() {
        return humanPlayer.getGameBoard();
    }

    public void setEnemyGameBoard(char[][] board) {
        enemyPlayer.gameBoard = board;
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
