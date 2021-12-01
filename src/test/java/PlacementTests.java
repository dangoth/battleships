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
        shipService.lockShipPlacement(playerService.getEnemyGameBoard(), battleship, 2, 2, Direction.HORIZONTAL);
        // Then
        assertThat(shipService.getShips(), notNullValue());
    }

    @Test
    public void enemyBoardIsUpdatedOnShipPlacement() {
        // When
        Ship battleship = new Battleship();
        shipService.lockShipPlacement(playerService.getEnemyGameBoard(), battleship, 2, 2, Direction.HORIZONTAL);
        // Then
        assertEquals(playerService.getEnemyGameBoard()[2][2], 'X');
    }


    @Test
    public void shipCannotBePlacedIncorrectly() {
        // When
        Ship battleship = new Battleship();
        Ship battleship2 = new Battleship();
        Ship destroyer = new Destroyer();
        shipService.lockShipPlacement(playerService.getEnemyGameBoard(), battleship, 2, 3, Direction.HORIZONTAL);
        shipService.lockShipPlacement(playerService.getEnemyGameBoard(), battleship2, 0, 5, Direction.VERTICAL);
        // Then
        Direction destroyerDirection = shipService.validateShipPosition(playerService.getEnemyGameBoard(),
                1, 3, destroyer.getShipLength());
        assertEquals(destroyerDirection, Direction.NEITHER);
    }

    @Test
    public void allShipsCanBePlacedCorrectly() {
        // When
        Ship battleship = new Battleship();
        Ship battleship2 = new Battleship();
        Ship destroyer = new Destroyer();
        // Then
        shipService.lockShipPlacement(playerService.getEnemyGameBoard(), battleship, 0, 0, Direction.HORIZONTAL);
        shipService.lockShipPlacement(playerService.getEnemyGameBoard(), battleship2, 2, 2, Direction.HORIZONTAL);
        shipService.lockShipPlacement(playerService.getEnemyGameBoard(), destroyer, 4, 3, Direction.EITHER);
        assertEquals(shipService.getShips().size(), 3);
    }

    @Test
    public void allShipsCanBeRandomlyPlacedCorrectly() {
        // When
        Ship battleship = new Battleship();
        Ship battleship2 = new Battleship();
        Ship destroyer = new Destroyer();
        // Then
        shipService.placeShip(battleship);
        shipService.placeShip(battleship2);
        shipService.placeShip(destroyer);
        assertEquals(shipService.getShips().size(), 3);
    }



}
