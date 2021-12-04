import game.Coordinates;
import game.Direction;
import game.Game;
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

    private final PlayerService playerService = new PlayerService();
    private final Game game = new Game();

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
        assertThat(ShipService.getActiveShips(), notNullValue());
    }

    @Test
    public void enemyBoardIsUpdatedOnShipPlacement() {
        // When
        Ship battleship = new Battleship();
        Coordinates coords = new Coordinates(2, 2);
        ShipService.lockShipPlacement(battleship, coords, Direction.HORIZONTAL);
        // Then
        assertEquals('X', playerService.getEnemyGameBoard()[2][2]);
    }

    @Test
    public void shipCannotBePlacedInDeadZone() {
        // When
        Destroyer destroyer = new Destroyer();
        Coordinates coords = new Coordinates(8, 9);
        // Then
        Direction direction = ShipService.determineLegalPlacementDirection(coords, destroyer.getShipLength());
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
        ShipService.lockShipPlacement(battleship, coords, Direction.HORIZONTAL);
        ShipService.lockShipPlacement(battleship2, coords2, Direction.VERTICAL);
        // Then
        Coordinates coords3 = new Coordinates(1, 3);
        Direction destroyerDirection = ShipService.determineLegalPlacementDirection(coords3, destroyer.getShipLength());
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
        ShipService.lockShipPlacement(battleship, coords, Direction.HORIZONTAL);
        ShipService.lockShipPlacement(battleship2, coords2, Direction.HORIZONTAL);
        ShipService.lockShipPlacement(destroyer, coords3, Direction.VERTICAL);
        // Then
        assertEquals(3, ShipService.getActiveShips().size());

    }

    @Test
    public void allShipsCanBeRandomlyPlacedCorrectly() {
        //todo test broken
        // When
        Ship battleship = new Battleship();
        Ship battleship2 = new Battleship();
        Ship destroyer = new Destroyer();
        ShipService.randomlyPlaceShip(battleship);
        ShipService.randomlyPlaceShip(battleship2);
        ShipService.randomlyPlaceShip(destroyer);
        // Then
        List<List<String>> listOfPlacedShips = ShipService.getActiveShips();
        System.out.println(listOfPlacedShips.size());
        assertEquals(3, ShipService.getActiveShips().size());
    }

}
