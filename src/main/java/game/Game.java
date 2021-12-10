package game;

import services.PlayerService;
import services.ShipService;
import ships.Battleship;
import ships.Destroyer;
import ships.Ship;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Game {

    private final PlayerService playerService;
    private final ShipService shipService;
    public List<List<String>> listOfPlacedShips;
    private final LinkedList<String> stdoutHistory = new LinkedList<String>();

    public Game() {
        playerService = new PlayerService();
        shipService = new ShipService(playerService);
    }

    public PlayerService getPlayerService() {
        return playerService;
    }

    public ShipService getShipService() {
        return shipService;
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);
        setUpGame();
        playerService.printPlayerGameBoard();
        while (!shipService.getActiveShips().isEmpty()) {
            listOfPlacedShips = shipService.getActiveShips();
            Coordinates coords = getCoordinatesInput(scanner);
            processGuess(coords, playerService);
        }
        System.out.println("You've won the game!");
    }

    public void setUpGame() {
        Ship[] newShips = createBattleshipAndTwoDestroyers();
        for (Ship s : newShips) {
            shipService.randomlyPlaceShip(s);
        }
    }

    public Ship[] createBattleshipAndTwoDestroyers() {
        Battleship battleship = new Battleship();
        Destroyer destroyer = new Destroyer();
        Destroyer destroyer2 = new Destroyer();
        return new Ship[]{battleship, destroyer, destroyer2};
    }

    public Coordinates getCoordinatesInput(Scanner scanner) {
        System.out.println("Input coordinates: ");
        String playerInput = scanner.next();
        if (playerInput.matches("[A-J](10|[1-9])")) {
            char colLetter = playerInput.charAt(0);
            int row = Integer.parseInt(playerInput.substring(1)) - 1;
            int col = colLetter - 'A';
            return new Coordinates(row, col);
        } else {
            System.out.println("Invalid input, please try again.");
            getCoordinatesInput(scanner);
        }
        return null;
    }

    public char[][] processGuess(Coordinates guessedCoords, PlayerService playerService) {
        char[][] enemyGameBoard = playerService.getEnemyGameBoard();
        char[][] playerGameBoard = playerService.getPlayerGameBoard();
        int row = guessedCoords.getRow();
        int col = guessedCoords.getColumn();

        char unusedCoords = '-';
        char miss = '-';
        char hit = 'X';

        if (playerGameBoard[row][col] != unusedCoords) {
            outputShotResult("Already shot");
            return playerGameBoard;
        } else if (enemyGameBoard[row][col] == miss) {
            playerGameBoard[row][col] = '0';
            outputShotResult("Miss");
            return playerGameBoard;
        } else if (enemyGameBoard[row][col] == hit) {
            playerGameBoard[row][col] = 'X';
            boolean sunk = strikeShip(guessedCoords);
            if (sunk) {
                outputShotResult("Sink");
                return playerGameBoard;
            }
            outputShotResult("Hit");
            return playerGameBoard;
        }
        return playerGameBoard;
    }

    public void outputShotResult(String shotResult) {
        if (shotResult.equals("Already shot")) {
            playerService.printPlayerGameBoard();
            printAndStore("You've already shot at these coordinates");
        } else {
            playerService.printPlayerGameBoard();
            printAndStore(shotResult);
        }
    }

    public boolean strikeShip(Coordinates coords) {
        String hitCoords = String.valueOf(coords.getRow()) + coords.getColumn();
        for (List<String> placedShip : listOfPlacedShips) {
            if (placedShip.contains(hitCoords)) {
                placedShip.remove(hitCoords);
                if (placedShip.isEmpty()) {
                    listOfPlacedShips.remove(placedShip);
                    return true;
                }
            }
        }
        return false;
    }

    public void printAndStore(String s) {
        System.out.println(s);
        stdoutHistory.add(s);
    }

    public String getLastOutput() {
        String last = stdoutHistory.get(stdoutHistory.size() - 1);
        return last;
    }

}
