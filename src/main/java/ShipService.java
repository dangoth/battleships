import Ships.Ship;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShipService {

    private static Random rand;
    private static final int boardSize = 10;
    private static PlayerService service;
    private static List<List<String>> ships;
    private final PrintStream printStream;

    public ShipService() {
        rand = new Random();
        ships = new ArrayList<>();
        printStream = new PrintStream(System.out);
        service = new PlayerService();
    }

    public static List<List<String>> getShips() {
        return ships;
    }

    /**
     * Calculate the offset from the board to avoid the bottom right corner where the ship cannot be placed in
     * either direction. Pass the randomized starting position to validateShipPosition() to determine legal directions
     * for the ship to be placed. Finally, call lockShipPlacement() to finalize the ship placement.
     * @param ship - ship to be placed
     * @return - an updated EnemyPlayer.gameBoard with the placed ship
     */
    public static char[][] placeShip(Ship ship) {
        // determine safe range from bottom right corner
        int offset = boardSize - ship.getShipLength();
        int pos = rand.nextInt(offset * 11 + 1) + 11;
        // transform into index coordinates
        int col = (pos % 10 == 0) ? 0 : (pos % 10 - 1);
        int row = (pos - col) / 10 - 1;

        Direction canPlaceShip = validateShipPosition(service.getEnemyGameBoard(), row, col, ship.getShipLength());
        if (canPlaceShip == Direction.NEITHER) {
            EnemyPlayer.gameBoard = placeShip(ship);
        } else if (canPlaceShip == Direction.EITHER) {
            EnemyPlayer.gameBoard = lockShipPlacement(service.getEnemyGameBoard(), ship, row, col, Direction.values()[rand.nextInt(2)]);
        } else if (canPlaceShip == Direction.HORIZONTAL) {
            EnemyPlayer.gameBoard = lockShipPlacement(service.getEnemyGameBoard(), ship, row, col, Direction.HORIZONTAL);
        } else if (canPlaceShip == Direction.VERTICAL) {
            EnemyPlayer.gameBoard = lockShipPlacement(service.getEnemyGameBoard(), ship, row, col, Direction.VERTICAL);
        }
        return EnemyPlayer.gameBoard;
    }

    /**
     * Given indexes row and col, verify whether there is enough space to place the ship vertically and horizontally,
     * checking whether there is enough space and whether there are other ships in the way.
     * @param board - EnemyPlayer.gameBoard with currently placed ships
     * @param row
     * @param col
     * @param shipLength
     * @return enum Direction with legal ship placement directions
     */
    public static Direction validateShipPosition(char[][] board, int row, int col, int shipLength) {
        boolean canPlaceHorizontally = true;
        boolean canPlaceVertically = true;
        if (row + shipLength > boardSize - 1) {
            canPlaceVertically = false;
        } else {
            // check for other ships in the way vertically
            for (int i = row; i < row + shipLength; i++) {
                if (EnemyPlayer.gameBoard[i][col] == 'X') {
                    canPlaceVertically = false;
                    break;
                }
            }
        }
        if (col + shipLength > boardSize - 1) {
            canPlaceHorizontally = false;
        } else {
            // check for other ships in the way horizontally
            for (int i = col; i < col + shipLength; i++) {
                if (EnemyPlayer.gameBoard[row][i] == 'X') {
                    canPlaceHorizontally = false;
                    break;
                }
            }
        }
        if (canPlaceHorizontally && canPlaceVertically) {
            return Direction.EITHER;
        } else if (canPlaceHorizontally) {
            return Direction.HORIZONTAL;
        } else if (canPlaceVertically) {
            return Direction.VERTICAL;
        } else {
            return Direction.NEITHER;
        }
    }

    public static char[][] lockShipPlacement(char[][] board, Ship ship, int row, int col, Direction direction) {
        List<String> shipcoords = new ArrayList<String>();
        if (direction == Direction.HORIZONTAL) {
            for (int i = 0; i < ship.getShipLength(); i++) {
                EnemyPlayer.gameBoard[row][col + i] = 'X';
                String coords = row + String.valueOf(col + i);
                shipcoords.add(coords);
            }
        } else if (direction == Direction.VERTICAL) {
            for (int i = 0; i < ship.getShipLength(); i++) {
                EnemyPlayer.gameBoard[row + i][col] = 'X';
                String coords = String.valueOf(row + i) + col;
                shipcoords.add(coords);
            }
        }
        ships.add(shipcoords);
        return EnemyPlayer.gameBoard;
    }


}