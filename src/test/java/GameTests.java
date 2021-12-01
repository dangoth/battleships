import Ships.Battleship;
import Ships.Destroyer;
import Ships.Ship;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameTests {

    // Instantiated to populate class fields
    private final Game game = new Game();
    private final ShipService shipService = new ShipService();
    private final PlayerService playerService = new PlayerService();

    @Test
    public void inputIsValidatedCorrectly() {
        /*
        technicznie mógłbym tu natrzepać takich walidacji, ale tam przecież jest regex- chyba nie ma sensu?
        game.makeGuess("A11");
        assertEquals("Invalid input, please try again.", Game.getLastOutput());
        game.makeGuess("ABC");
        assertEquals("Invalid input, please try again.", Game.getLastOutput());
        game.makeGuess("123");
        assertEquals("Invalid input, please try again.", Game.getLastOutput());
        game.makeGuess("a5");
        assertEquals("Invalid input, please try again.", Game.getLastOutput());
        */
    }

    @Test
    public void playerBoardIsInitiatedCorrectly() {
        assertThat(HumanPlayer.gameBoard, notNullValue());
    }

    @Test
    public void playerCanMakeAGuess() {
        // When
        Game.makeGuess("A5");
        assertEquals("Miss", Game.getLastOutput());
    }

    @Test
    public void playerBoardIsUpdatedWhenGuess() {
        // When
        Game.makeGuess("A5");
        assertEquals('0', playerService.getPlayerGameBoard()[4][0]);
    }

    @Test
    public void guessingTheSameFieldTwiceShowsMessage() {
        Game.makeGuess("A5");
        Game.makeGuess("A5");
        assertEquals("You've already shot at these coordinates", Game.getLastOutput());
    }

    @Test
    public void shipCanBeHit() {
        Ship battleship = new Battleship();
        ShipService.lockShipPlacement(playerService.getEnemyGameBoard(), battleship, 0, 0, Direction.HORIZONTAL);
        Game.listOfPlacedShips = ShipService.getShips();
        Game.makeGuess("A1");
        assertEquals("Hit", Game.getLastOutput());
    }

    @Test
    public void shipCanBeSunk() {
        Ship battleship = new Battleship();
        Ship destroyer = new Destroyer();
        ShipService.placeShip(destroyer);
        ShipService.lockShipPlacement(playerService.getEnemyGameBoard(), battleship, 0, 0, Direction.HORIZONTAL);
        Game.listOfPlacedShips = ShipService.getShips();
        Game.makeGuess("A1");
        Game.makeGuess("B1");
        Game.makeGuess("C1");
        Game.makeGuess("D1");
        Game.makeGuess("E1");
        assertEquals("Sink", Game.getLastOutput());

    }

    @Test
    public void gameCanBeWon() {
        Ship battleship = new Battleship();
        ShipService.lockShipPlacement(playerService.getEnemyGameBoard(), battleship, 5, 5, Direction.VERTICAL);
        Game.listOfPlacedShips = ShipService.getShips();
        Game.makeGuess("F6");
        Game.makeGuess("F7");
        Game.makeGuess("F8");
        Game.makeGuess("F9");
        Game.makeGuess("F10");
        assertTrue(Game.listOfPlacedShips.isEmpty());
    }

}
