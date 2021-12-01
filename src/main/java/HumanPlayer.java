import java.util.stream.IntStream;

public class HumanPlayer {

    public static char[][] gameBoard;

    public HumanPlayer() {
        gameBoard = new char[10][];
        IntStream.range(0, 10)
                .forEach(i -> gameBoard[i] = new char[]{'-', '-', '-', '-', '-', '-', '-', '-', '-', '-'});
    }

    public static char[][] getGameBoard() {
        return gameBoard;
    }


}
