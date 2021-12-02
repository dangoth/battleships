import Ships.Battleship;
import Ships.Destroyer;
import Ships.Ship;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class GameTests {

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
        Assertions.assertEquals(expected, matches);
    }

    @Test
    public void playerBoardIsInitiatedCorrectly() {
        assertThat(HumanPlayer.gameBoard, notNullValue());
    }

    @Test
    public void playerCanMakeAGuess() {
        // When
        Game.makeGuess(new Coordinates(0, 4));
        // Then
        Assertions.assertEquals("Miss", Game.getLastOutput());
    }

    @Test
    public void playerBoardIsUpdatedWhenGuessed() {
        // When
        Game.makeGuess(new Coordinates(0, 4));
        // Then
        Assertions.assertEquals('0', playerService.getPlayerGameBoard()[0][4]);
    }

    @Test
    public void guessingTheSameFieldTwiceShowsMessage() {
        // When
        Game.makeGuess(new Coordinates(0, 4));
        Game.makeGuess(new Coordinates(0, 4));
        // Then
        Assertions.assertEquals("You've already shot at these coordinates", Game.getLastOutput());
    }

    @Test
    public void shipCanBeHit() {
        // When
        Ship battleship = new Battleship();
        ShipService.lockShipPlacement(battleship, new Coordinates(0, 0), Direction.HORIZONTAL);
        Game.listOfPlacedShips = ShipService.getActiveShips();
        // THen
        Game.makeGuess(new Coordinates(0, 0));
        Assertions.assertEquals("Hit", Game.getLastOutput());
    }

    @Test
    public void shipCanBeSunk() {
        // When
        Ship battleship = new Battleship();
        Ship destroyer = new Destroyer();
        ShipService.placeShip(destroyer);
        ShipService.lockShipPlacement(battleship, new Coordinates(0, 0), Direction.HORIZONTAL);
        Game.listOfPlacedShips = ShipService.getActiveShips();
        // Then
        Game.makeGuess(new Coordinates(0, 0));
        Game.makeGuess(new Coordinates(0, 1));
        Game.makeGuess(new Coordinates(0, 2));
        Game.makeGuess(new Coordinates(0, 3));
        Game.makeGuess(new Coordinates(0, 4));
        Assertions.assertEquals("Sink", Game.getLastOutput());

    }

    @Test
    public void gameCanBeWon() {
        // When
        Ship battleship = new Battleship();
        ShipService.lockShipPlacement(battleship, new Coordinates(5, 5), Direction.VERTICAL);
        Game.listOfPlacedShips = ShipService.getActiveShips();
        Game.makeGuess(new Coordinates(5, 5));
        Game.makeGuess(new Coordinates(6, 5));
        Game.makeGuess(new Coordinates(7, 5));
        Game.makeGuess(new Coordinates(8, 5));
        Game.makeGuess(new Coordinates(9, 5));
        // Then
        Assertions.assertTrue(Game.listOfPlacedShips.isEmpty());
    }

}
