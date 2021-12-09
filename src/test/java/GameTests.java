import game.Coordinates;
import game.Direction;
import game.Game;
import org.junit.Before;
import org.junit.BeforeClass;
import players.EnemyPlayer;
import players.HumanPlayer;
import services.PlayerService;
import services.ShipService;
import ships.Battleship;
import ships.Destroyer;
import ships.Ship;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class GameTests {

    Game game = null;
    PlayerService playerService = null;
    ShipService shipService = null;
    HumanPlayer humanPlayer = null;
    EnemyPlayer enemyPlayer = null;

    @Before
    public void setUp() {
        game = new Game();
        playerService = game.getPlayerService();
        shipService = game.getShipService();
        humanPlayer = playerService.getHumanPlayer();
        enemyPlayer = playerService.getEnemyPlayer();
    }

    @Test
    public void playerBoardIsInitiatedCorrectly() {
        assertThat(humanPlayer.gameBoard, notNullValue());
    }

    @Test
    public void playerCanMakeAGuess() {
        // When
        game.processGuess(new Coordinates(0, 4), playerService);
        // Then
        Assertions.assertEquals("Miss", game.getLastOutput());
    }

    @Test
    public void playerBoardIsUpdatedWhenGuessed() {
        // When
        game.processGuess(new Coordinates(0, 4), playerService);
        // Then
        Assertions.assertEquals('0', playerService.getPlayerGameBoard()[0][4]);
    }

    @Test
    public void guessingTheSameFieldTwiceShowsMessage() {
        // When
        game.processGuess(new Coordinates(0, 4), playerService);
        game.processGuess(new Coordinates(0, 4), playerService);
        // Then
        Assertions.assertEquals("You've already shot at these coordinates", game.getLastOutput());
    }

    @Test
    public void shipCanBeHit() {
        // When
        Ship battleship = new Battleship();
        shipService.lockShipPlacement(battleship, new Coordinates(0, 0), Direction.HORIZONTAL);
        game.listOfPlacedShips = shipService.getActiveShips();
        // THen
        game.processGuess(new Coordinates(0, 0), playerService);
        Assertions.assertEquals("Hit", game.getLastOutput());
    }

    @Test
    public void shipCanBeSunk() {
        // When
        Ship battleship = new Battleship();
        Ship destroyer = new Destroyer();
        enemyPlayer.gameBoard = shipService.randomlyPlaceShip(destroyer);
        enemyPlayer.gameBoard = shipService.lockShipPlacement(battleship, new Coordinates(0, 0), Direction.HORIZONTAL);
        game.listOfPlacedShips = shipService.getActiveShips();
        // Then
        playerService.setPlayerGameBoard(game.processGuess(new Coordinates(0, 0), playerService));
        playerService.setPlayerGameBoard(game.processGuess(new Coordinates(0, 1), playerService));
        playerService.setPlayerGameBoard(game.processGuess(new Coordinates(0, 2), playerService));
        playerService.setPlayerGameBoard(game.processGuess(new Coordinates(0, 3), playerService));
        playerService.setPlayerGameBoard(game.processGuess(new Coordinates(0, 4), playerService));
        Assertions.assertEquals("Sink", game.getLastOutput());

    }

    @Test
    public void gameCanBeWon() {
        // When
        Ship battleship = new Battleship();
        shipService.lockShipPlacement(battleship, new Coordinates(5, 5), Direction.VERTICAL);
        game.listOfPlacedShips = shipService.getActiveShips();
        game.processGuess(new Coordinates(5, 5), playerService);
        game.processGuess(new Coordinates(6, 5), playerService);
        game.processGuess(new Coordinates(7, 5), playerService);
        game.processGuess(new Coordinates(8, 5), playerService);
        game.processGuess(new Coordinates(9, 5), playerService);
        // Then
        Assertions.assertTrue(game.listOfPlacedShips.isEmpty());
    }

}
