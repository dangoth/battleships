package services;

import players.EnemyPlayer;
import players.HumanPlayer;

import java.io.PrintStream;
import java.util.stream.IntStream;

public class PlayerService {

    private final PrintStream printStream;
    private final int boardSize = 10;
    private final char[] alphabet = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};

    public PlayerService() {
        EnemyPlayer enemyPlayer = new EnemyPlayer();
        HumanPlayer humanPlayer = new HumanPlayer();
        printStream = new PrintStream(System.out);
    }

    public char[][] getEnemyGameBoard() {
        return EnemyPlayer.getGameBoard();
    }

    public char[][] getPlayerGameBoard() {
        return HumanPlayer.getGameBoard();
    }

    public void printPlayerGameBoard() {
        printStream.println("  " + String.valueOf(alphabet));
        IntStream.range(0, boardSize)
                .forEach(i -> printStream.printf("%2s", i + 1)
                        .println(HumanPlayer.gameBoard[i]));
        printStream.print("\n");
        printStream.flush();
    }

}
