import Ships.Battleship;
import Ships.Destroyer;
import Ships.Ship;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class GameTests {

    // Instantiated to populate class fields
    private final Game game = new Game();
    private final ShipService shipService = new ShipService();
    private final PlayerService playerService = new PlayerService();

    private static final String REGEX = "[A-J](10|[1-9])";

    public Stream<Arguments> testCases() {
        return Stream.of(
                Arguments.of("", false, "empty string"),
                Arguments.of("a", false, "single lowercase non-digit"),
                Arguments.of("1", false, "single digit"),
                Arguments.of("123", false, "integer"),
                Arguments.of("abc", false, "string"),
                Arguments.of("a1", false, "lowercase row letter"),
                Arguments.of("A1", true, "valid lower-bound coordinates"),
                Arguments.of("A10", true, "valid upper-bound column coordinates"),
                Arguments.of("J1", true, "valid upper-bound row coordinates"),
                Arguments.of("J10", true, "valid upper-bound coordinates"),
                Arguments.of("A11", false, "invalid column"),
                Arguments.of("M5", false, "invalid row"),
                Arguments.of("X23", false, "invalid column and row"),
                Arguments.of("A-1", false, "negative column"),
                Arguments.of("C3", true, "valid coordinates")
        );
    }


    @ParameterizedTest(name = "{index} ==> {2}: is {0} valid coordinates? {1}")
    @MethodSource("testCases")
    public void testRegex(String input, boolean expected, String description) {
        Boolean matches = input.matches(REGEX);
        assertEquals(expected, matches);
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
        Coordinates coords = new Coordinates(0, 0);
        ShipService.lockShipPlacement(battleship, coords, Direction.HORIZONTAL);
        Game.listOfPlacedShips = ShipService.getShips();
        Game.makeGuess("A1");
        assertEquals("Hit", Game.getLastOutput());
    }

    @Test
    public void shipCanBeSunk() {
        Ship battleship = new Battleship();
        Ship destroyer = new Destroyer();
        ShipService.placeShip(destroyer);
        Coordinates coords = new Coordinates(0, 0);
        ShipService.lockShipPlacement(battleship, coords, Direction.HORIZONTAL);
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
        Coordinates coords = new Coordinates(5, 5);
        ShipService.lockShipPlacement(battleship, coords, Direction.VERTICAL);
        Game.listOfPlacedShips = ShipService.getShips();
        Game.makeGuess("F6");
        Game.makeGuess("F7");
        Game.makeGuess("F8");
        Game.makeGuess("F9");
        Game.makeGuess("F10");
        assertTrue(Game.listOfPlacedShips.isEmpty());
    }

}
