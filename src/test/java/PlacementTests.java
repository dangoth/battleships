import Ships.Battleship;
import Ships.Destroyer;
import Ships.Ship;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;


public class PlacementTests {

    private final ShipService shipService = new ShipService();
    private final PlayerService playerService = new PlayerService();
    private final Game game = new Game();
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();


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
        ShipService.lockShipPlacement(battleship, coords, Direction.HORIZONTAL);
        // Then
        assertThat(ShipService.getShips(), notNullValue());
    }

    @Test
    public void enemyBoardIsUpdatedOnShipPlacement() {
        // When
        Ship battleship = new Battleship();
        Coordinates coords = new Coordinates(2, 2);
        ShipService.lockShipPlacement(battleship, coords, Direction.HORIZONTAL);
        // Then
        assertEquals(playerService.getEnemyGameBoard()[2][2], 'X');
    }

    @Test
    public void shipCannotBePlacedInDeadZone() {
        // When
        Destroyer destroyer = new Destroyer();
        Coordinates coords = new Coordinates(9, 9);
        // Then
        Direction direction = ShipService.validateShipPosition(coords, destroyer.getShipLength());
        assertEquals(direction, Direction.NEITHER);
    }

    @Test
    public void shipCannotCollideWithOtherShips() {
        // When
        Ship battleship = new Battleship();
        Ship battleship2 = new Battleship();
        Ship destroyer = new Destroyer();
        Coordinates coords = new Coordinates(2, 3);
        Coordinates coords2 = new Coordinates(0, 5);
        ShipService.lockShipPlacement(battleship, coords, Direction.HORIZONTAL);
        ShipService.lockShipPlacement(battleship2, coords2, Direction.VERTICAL);
        // Then
        Coordinates coords3 = new Coordinates(1, 3);
        Direction destroyerDirection = ShipService.validateShipPosition(coords3, destroyer.getShipLength());
        assertEquals(destroyerDirection, Direction.NEITHER);
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
        ShipService.lockShipPlacement(battleship, coords, Direction.HORIZONTAL);
        ShipService.lockShipPlacement(battleship2, coords2, Direction.HORIZONTAL);
        ShipService.lockShipPlacement(destroyer, coords3, Direction.VERTICAL);
        // Then
        assertEquals(ShipService.getShips().size(), 3);
    }

    @Test
    public void allShipsCanBeRandomlyPlacedCorrectly() {
        // When
        Ship battleship = new Battleship();
        Ship battleship2 = new Battleship();
        Ship destroyer = new Destroyer();
        ShipService.placeShip(battleship);
        ShipService.placeShip(battleship2);
        ShipService.placeShip(destroyer);
        // Then
        assertEquals(ShipService.getShips().size(), 3);
    }

}
