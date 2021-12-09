package players;

import java.util.stream.IntStream;

public class HumanPlayer {

    public char[][] gameBoard;

    public HumanPlayer() {
        gameBoard = new char[10][];
        IntStream.range(0, 10)
                .forEach(i -> gameBoard[i] = new char[]{'-', '-', '-', '-', '-', '-', '-', '-', '-', '-'});
    }

    public char[][] getGameBoard() {
        return this.gameBoard;
    }

}
