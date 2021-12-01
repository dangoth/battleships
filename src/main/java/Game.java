import Ships.Battleship;
import Ships.Destroyer;
import Ships.Ship;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Game {

    private static PlayerService playerService;
    private static ShipService shipService;
    static List<List<String>> listOfPlacedShips;
    private static final LinkedList<String> stdoutHistory = new LinkedList<String>();
    private static Scanner scanner;

    public Game() {
        playerService = new PlayerService();
        shipService = new ShipService();
    }

    public static void printAndStore(String s) {
        System.out.println(s);
        stdoutHistory.add(s);
    }

    public static String getLastOutput() {
        String last = stdoutHistory.get(stdoutHistory.size() - 1);
        return last;
    }

    /**
     * If makeGuess() determines the guessed coordinates to be a ship's location, the coordinates value is removed
     * from listOfPlacedShips.
     * @param coords - Coordinates object with row and column index integers
     * @return true if the list (representing a ship) from which the guessed coordinates were removed is empty, i.e.
     * the ship has been sunk
     */
    public static boolean strikeShip(Coordinates coords) {
        String hitCoordinates = String.valueOf(coords.getRow()) + coords.getColumn();
        for (List<String> placedShip : listOfPlacedShips) {
            if (placedShip.contains(hitCoordinates)) {
                placedShip.remove(hitCoordinates);
                if (placedShip.isEmpty()) {
                    listOfPlacedShips.remove(placedShip);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check EnemyPlayer.gameBoard to verify hit/miss/sink
     * @param coords - Coordinates object with row and column index integers
     * @return HumanPlayer.playerBoard updated with the guess
     */
    public static char[][] makeGuess(Coordinates coords) {
        char[][] enemyGameBoard = playerService.getEnemyGameBoard();
        char[][] playerGameBoard = playerService.getPlayerGameBoard();
        int row = coords.getRow();
        int col = coords.getColumn();

        // any other char than '-' denotes there's already been a hit or miss at these coords
        if (playerGameBoard[row][col] != '-') {
            playerService.printPlayerGameBoard();
            printAndStore("You've already shot at these coordinates");
            return playerGameBoard;

        } else {
            // missed shot
            if (enemyGameBoard[row][col] == '-') {
                playerGameBoard[row][col] = '0';
                playerService.printPlayerGameBoard();
                printAndStore("Miss");
                return playerGameBoard;

                // hit
            } else if (enemyGameBoard[row][col] == 'X') {
                playerGameBoard[row][col] = 'X';
                boolean sunk = strikeShip(coords);
                if (sunk) {
                    playerService.printPlayerGameBoard();
                    printAndStore("Sink");
                    return playerGameBoard;
                }
            }
            playerService.printPlayerGameBoard();
            printAndStore("Hit");
            return playerGameBoard;
        }
    }


    public static void getCoordinatesInput(Scanner scanner) {
        System.out.println("Input coordinates: ");
        String playerInput = scanner.next();
        if (playerInput.matches("[A-J](10|[1-9])")) {
            char rowLetter = playerInput.charAt(0);
            int row = Integer.parseInt(playerInput.substring(1)) - 1;
            int col = rowLetter - 'A';
            Coordinates coords = new Coordinates(row, col);
            HumanPlayer.gameBoard = makeGuess(coords);
        } else {
            System.out.println("Invalid input, please try again.");
        }
    }

    public static void setUpGame() {
        Ship[] newships = createShips();
        for (Ship s : newships) {
            ShipService.placeShip(s);
        }
    }

    public static Ship[] createShips() {
        Battleship battleship = new Battleship();
        Destroyer destroyer = new Destroyer();
        Destroyer destroyer2 = new Destroyer();
        return new Ship[]{battleship, destroyer, destroyer2};
    }

    public static void play() {
        scanner = new Scanner(System.in);
        setUpGame();
        playerService.printPlayerGameBoard();
        while (!ShipService.getShips().isEmpty()) {
            listOfPlacedShips = ShipService.getShips();
            getCoordinatesInput(scanner);
        }
        System.out.println("You've won the game!");
    }

}
