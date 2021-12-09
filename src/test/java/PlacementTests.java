import game.Coordinates;
import game.Direction;
import game.Game;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import services.PlayerService;
import services.ShipService;
import ships.Battleship;
import ships.Destroyer;
import ships.Ship;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;


public class PlacementTests {

    Game game = null;
    PlayerService playerService = null;
    ShipService shipService = null;

    @Before
    public void setUp() {
        game = new Game();
        playerService = game.getPlayerService();
        shipService = game.getShipService();
    }

    @Test
    public void enemyBoardIsInitiated() {
        // When
        char[][] board = playerService.getEnemyGameBoard();
        // Then
        assertThat(board, notNullValue());
    }

    @Test
    public void shipCanBePlacedCorrectly() {
        // When
        Ship battleship = new Battleship();
        Coordinates coords = new Coordinates(2, 2);
        shipService.lockShipPlacement(battleship, coords, Direction.HORIZONTAL);
        // Then
        assertThat(shipService.getActiveShips(), notNullValue());
    }

    @Test
    public void enemyBoardIsUpdatedOnShipPlacement() {
        // When
        Ship battleship = new Battleship();
        Coordinates coords = new Coordinates(2, 2);
        shipService.lockShipPlacement(battleship, coords, Direction.HORIZONTAL);
        // Then
        assertEquals('X', playerService.getEnemyGameBoard()[2][2]);
    }

    @Test
    public void shipCannotBePlacedInDeadZone() {
        // When
        Destroyer destroyer = new Destroyer();
        Coordinates coords = new Coordinates(8, 9);
        // Then
        Direction direction = shipService.determineLegalPlacementDirection(coords, destroyer.getShipLength());
        assertEquals(Direction.NEITHER, direction);
    }

    @Test
    public void shipCannotCollideWithOtherShips() {
        // When
        Ship battleship = new Battleship();
        Ship battleship2 = new Battleship();
        Ship destroyer = new Destroyer();
        Coordinates coords = new Coordinates(2, 3);
        Coordinates coords2 = new Coordinates(0, 5);
        shipService.lockShipPlacement(battleship, coords, Direction.HORIZONTAL);
        shipService.lockShipPlacement(battleship2, coords2, Direction.VERTICAL);
        // Then
        Coordinates coords3 = new Coordinates(1, 3);
        Direction destroyerDirection = shipService.determineLegalPlacementDirection(coords3, destroyer.getShipLength());
        assertEquals(Direction.NEITHER, destroyerDirection);
    }

    @Test
    public void allShipsCanBePlacedCorrectly() {
        // When
        Ship battleship = new Battleship();
        Ship battleship2 = new Battleship();
        Ship destroyer = new Destroyer();
        Coordinates coords = new Coordinates(0, 0);
        Coordinates coords2 = new Coordinates(2, 2);
        Coordinates coords3 = new Coordinates(4, 3);
        shipService.lockShipPlacement(battleship, coords, Direction.HORIZONTAL);
        shipService.lockShipPlacement(battleship2, coords2, Direction.HORIZONTAL);
        shipService.lockShipPlacement(destroyer, coords3, Direction.VERTICAL);
        // Then
        assertEquals(3, shipService.getActiveShips().size());

    }

    @Test
    public void allShipsCanBeRandomlyPlacedCorrectly() {
        //todo test broken
        // When
        Ship battleship = new Battleship();
        Ship battleship2 = new Battleship();
        Ship destroyer = new Destroyer();
        shipService.randomlyPlaceShip(battleship);
        shipService.randomlyPlaceShip(battleship2);
        shipService.randomlyPlaceShip(destroyer);
        // Then
        List<List<String>> listOfPlacedShips = shipService.getActiveShips();
        System.out.println(listOfPlacedShips.size());
        assertEquals(3, shipService.getActiveShips().size());
    }

}
