package services;

import game.Coordinates;
import game.Direction;
import players.EnemyPlayer;
import ships.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ShipService {

    private Random rand;
    private final int boardSize = 10;
    private List<List<String>> ships;
    private EnemyPlayer enemyPlayer;
    private PlayerService playerService;

    public ShipService(PlayerService playerService) {
        ships = new ArrayList<>();
        rand = new Random();
        this.playerService = playerService;
        enemyPlayer = playerService.getEnemyPlayer();
    }

    public List<List<String>> getActiveShips() {
        return ships;
    }

    public char[][] randomlyPlaceShip(Ship ship) {
        int position = rand.nextInt(99);
        int col = position % 10;
        int row = position / 10;
        Coordinates coords = new Coordinates(row, col);
        if (checkIfCoordinatesInBottomRightDeadzone(coords, ship)) {
            enemyPlayer.gameBoard = randomlyPlaceShip(ship);
            return enemyPlayer.getGameBoard();
        }
        Direction directionToPlace = determineLegalPlacementDirection(coords, ship.getShipLength());
        if (directionToPlace == Direction.NEITHER) {
            enemyPlayer.gameBoard = randomlyPlaceShip(ship);
            return enemyPlayer.getGameBoard();
        }
        enemyPlayer.gameBoard = lockShipPlacement(ship, coords, directionToPlace);
        return enemyPlayer.gameBoard;
    }

    public boolean checkIfCoordinatesInBottomRightDeadzone(Coordinates coords, Ship ship) {
        int col = coords.getColumn();
        int row = coords.getRow();
        if ((col > boardSize - ship.getShipLength()) && (row > boardSize - ship.getShipLength())) {
            return true;
        }
        return false;
    }

    public Direction determineLegalPlacementDirection(Coordinates coords, int shipLength) {
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

    public boolean checkForObstaclesVertically(Coordinates coords, int shipLength) {
        int row = coords.getRow();
        int col = coords.getColumn();
        if (row + shipLength > boardSize) {
            return false;
        } else {
            for (int i = row; i < row + shipLength; i++) {
                if (enemyPlayer.gameBoard[i][col] == 'X') {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkForObstaclesHorizontally(Coordinates coords, int shipLength) {
        int row = coords.getRow();
        int col = coords.getColumn();
        if (col + shipLength > boardSize) {
            return false;
        } else {
            for (int i = col; i < col + shipLength; i++) {
                if (enemyPlayer.gameBoard[row][i] == 'X') {
                    return false;
                }
            }
        }
        return true;
    }

    public char[][] lockShipPlacement(Ship ship, Coordinates coords, Direction direction) {
        List<String> shipCoordList = new ArrayList<>();
        int row = coords.getRow();
        int col = coords.getColumn();
        if (direction == Direction.HORIZONTAL) {
            for (int i = 0; i < ship.getShipLength(); i++) {
                enemyPlayer.gameBoard[row][col + i] = 'X';
                String shipCoords = row + String.valueOf(col + i);
                shipCoordList.add(shipCoords);
            }
        } else if (direction == Direction.VERTICAL) {
            for (int i = 0; i < ship.getShipLength(); i++) {
                enemyPlayer.gameBoard[row + i][col] = 'X';
                String shipCoords = String.valueOf(row + i) + col;
                shipCoordList.add(shipCoords);
            }
        }
        ships.add(shipCoordList);
        return enemyPlayer.gameBoard;
    }

}
