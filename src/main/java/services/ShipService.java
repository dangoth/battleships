package services;

import game.Coordinates;
import game.Direction;
import players.EnemyPlayer;
import ships.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ShipService {

    private static Random rand = new Random();
    private static final int boardSize = 10;
    private static List<List<String>> ships;

    public ShipService() {
        ships = new ArrayList<>();
    }

    public static List<List<String>> getActiveShips() {
        return ships;
    }

    public static char[][] randomlyPlaceShip(Ship ship) {
        int position = rand.nextInt(99);
        int col = position % 10;
        int row = position / 10;
        Coordinates coords = new Coordinates(row, col);
        if (checkIfCoordinatesInBottomRightDeadzone(coords, ship) == true) {
            EnemyPlayer.gameBoard = randomlyPlaceShip(ship);
            return EnemyPlayer.getGameBoard();
        }
        Direction directionToPlace = determineLegalPlacementDirection(coords, ship.getShipLength());
        if (directionToPlace == Direction.NEITHER) {
            EnemyPlayer.gameBoard = randomlyPlaceShip(ship);
            return EnemyPlayer.getGameBoard();
        }
        EnemyPlayer.gameBoard = lockShipPlacement(ship, coords, directionToPlace);
        return EnemyPlayer.gameBoard;
    }

    public static boolean checkIfCoordinatesInBottomRightDeadzone(Coordinates coords, Ship ship) {
        int col = coords.getColumn();
        int row = coords.getRow();
        if ((col > boardSize - ship.getShipLength()) && (row > boardSize - ship.getShipLength())) {
            return true;
        }
        return false;
    }

    public static Direction determineLegalPlacementDirection(Coordinates coords, int shipLength) {
        boolean canPlaceVertically = checkForObstaclesVertically(coords, shipLength);
        boolean canPlaceHorizontally = checkForObstaclesHorizontally(coords, shipLength);
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

    public static boolean checkForObstaclesVertically(Coordinates coords, int shipLength) {
        int row = coords.getRow();
        int col = coords.getColumn();
        if (row + shipLength > boardSize) {
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

    public static boolean checkForObstaclesHorizontally(Coordinates coords, int shipLength) {
        int row = coords.getRow();
        int col = coords.getColumn();
        if (col + shipLength > boardSize) {
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
