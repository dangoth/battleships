import Ships.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShipService {

    private static Random rand;
    private static final int boardSize = 10;
    private static List<List<String>> ships;

    public ShipService() {
        rand = new Random();
        ships = new ArrayList<>();
    }

    public static List<List<String>> getActiveShips() {
        return ships;
    }

    /**
     * Calculate the offset from the board to avoid the bottom right corner where the ship cannot be placed in
     * either direction. Pass the randomized starting position to validateShipPosition() to determine legal directions
     * for the ship to be placed. Finally, call lockShipPlacement() to finalize the ship placement.
     *
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
        Coordinates coords = new Coordinates(row, col);

        Direction canPlaceShip = validateShipPosition(coords, ship.getShipLength());
        if (canPlaceShip == Direction.NEITHER) {
            EnemyPlayer.gameBoard = placeShip(ship);
        } else if (canPlaceShip == Direction.HORIZONTAL) {
            EnemyPlayer.gameBoard = lockShipPlacement(ship, coords, Direction.HORIZONTAL);
        } else if (canPlaceShip == Direction.VERTICAL) {
            EnemyPlayer.gameBoard = lockShipPlacement(ship, coords, Direction.VERTICAL);
        }
        return EnemyPlayer.gameBoard;
    }

    /**
     * Given a Coordinates object with row nad col indexes, verify whether there is enough space to place the ship
     * vertically and horizontally, checking whether there is enough space and whether there are other ships in the way.
     *
     * @param coords     - object containing integer indexes for gameboard access
     * @param shipLength - length of ship being validated
     * @return enum Direction with a direction to place the ship in, or a confirmation to call for other random coords.
     */
    public static Direction validateShipPosition(Coordinates coords, int shipLength) {
        boolean canPlaceVertically = validateVertically(coords, shipLength);
        boolean canPlaceHorizontally = validateHorizontally(coords, shipLength);
        if (canPlaceHorizontally && canPlaceVertically) {
            int randomChoice = rand.nextInt(2);
            return Direction.values()[randomChoice];
        } else if (canPlaceHorizontally) {
            return Direction.HORIZONTAL;
        } else if (canPlaceVertically) {
            return Direction.VERTICAL;
        } else {
            return Direction.NEITHER;
        }
    }

    public static boolean validateVertically(Coordinates coords, int shipLength) {
        int row = coords.getRow();
        int col = coords.getColumn();
        if (row + shipLength > boardSize - 1) {
            return false;
        } else {
            for (int i = row; i < row + shipLength; i++) {
                if (EnemyPlayer.gameBoard[i][col] == 'X') {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean validateHorizontally(Coordinates coords, int shipLength) {
        int row = coords.getRow();
        int col = coords.getColumn();
        if (col + shipLength > boardSize - 1) {
            return false;
        } else {
            for (int i = col; i < col + shipLength; i++) {
                if (EnemyPlayer.gameBoard[row][i] == 'X') {
                    return false;

                }
            }
        }
        return true;
    }

    public static char[][] lockShipPlacement(Ship ship, Coordinates coords, Direction direction) {
        List<String> shipCoordList = new ArrayList<>();
        int row = coords.getRow();
        int col = coords.getColumn();
        if (direction == Direction.HORIZONTAL) {
            for (int i = 0; i < ship.getShipLength(); i++) {
                EnemyPlayer.gameBoard[row][col + i] = 'X';
                String shipCoords = row + String.valueOf(col + i);
                shipCoordList.add(shipCoords);
            }
        } else if (direction == Direction.VERTICAL) {
            for (int i = 0; i < ship.getShipLength(); i++) {
                EnemyPlayer.gameBoard[row + i][col] = 'X';
                String shipCoords = String.valueOf(row + i) + col;
                shipCoordList.add(shipCoords);
            }
        }
        ships.add(shipCoordList);
        return EnemyPlayer.gameBoard;
    }

}
