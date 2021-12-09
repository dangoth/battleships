package players;

import java.util.stream.IntStream;

public class EnemyPlayer {

    public char[][] gameBoard;

    public EnemyPlayer() {
        gameBoard = new char[10][];
        IntStream.range(0, 10)
                .forEach(i -> gameBoard[i] = new char[]{'0', '0', '0', '0', '0', '0', '0', '0', '0', '0'});
    }

    public char[][] getGameBoard() {
        return this.gameBoard;
    }

}
