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

    // constructor for GameTests purpose
    public Game() {
        playerService = new PlayerService();
        shipService = new ShipService();
    }

    public static void printAndStore(String s) {
        System.out.println(s);
        stdoutHistory.add(s);
    }

    public static String getLastOutput() {
        return stdoutHistory.get(stdoutHistory.size() - 1);
    }

    /**
     * Convert the guess input string into coordinates and verify hit/miss/sink
     * @param coordinates - double-digit string of coordinates converted into gameBoard indexes
     * @return HumanPlayer.playerBoard updated with the guess
     */
    public static char[][] makeGuess(String coordinates) {
        char[][] enemyGameBoard = playerService.getEnemyGameBoard();
        char[][] playerGameBoard = playerService.getPlayerGameBoard();
        char coord = coordinates.charAt(0);
        int row = Integer.parseInt(coordinates.substring(1)) - 1;
        int col = coord - 'A';
        if (playerGameBoard[row][col] != '-') {
            playerService.printPlayerGameBoard();
            printAndStore("You've already shot at these coordinates");
            return playerGameBoard;
        } else {
            if (enemyGameBoard[row][col] == '-') {
                playerGameBoard[row][col] = '0';
                playerService.printPlayerGameBoard();
                printAndStore("Miss");
                return playerGameBoard;
            } else if (enemyGameBoard[row][col] == 'X') {
                playerGameBoard[row][col] = 'X';
                String coords = String.valueOf(row) + col;
                for (List<String> listOfPlacedShip : listOfPlacedShips) {
                    if (listOfPlacedShip.contains(coords)) {
                        listOfPlacedShip.remove(coords);
                        if (listOfPlacedShip.isEmpty()) {
                            listOfPlacedShips.remove(listOfPlacedShip);
                            playerService.printPlayerGameBoard();
                            printAndStore("Sink");
                            return playerGameBoard;
                        }
                    }
                }
                playerService.printPlayerGameBoard();
                printAndStore("Hit");
                return playerGameBoard;
            }
        }
        return playerGameBoard;
    }


    public static void getCoordinatesInput(Scanner scanner) {
        System.out.println("Input coordinates: ");
        String coords = scanner.next();
        if (coords.matches("[A-J](10|[1-9])")) {
            HumanPlayer.gameBoard = makeGuess(coords);
        } else {
            System.out.println("Invalid input, please try again.");
        }
    }

    public static void setUpGame() {
        Battleship battleship = new Battleship();
        Destroyer destroyer = new Destroyer();
        Destroyer destroyer2 = new Destroyer();
        ShipService.placeShip(battleship);
        ShipService.placeShip(destroyer);
        ShipService.placeShip(destroyer2);
    }


    public static void main(String[] args) {
        playerService = new PlayerService();
        shipService = new ShipService();
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
