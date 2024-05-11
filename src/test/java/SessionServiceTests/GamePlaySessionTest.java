package SessionServiceTests;
import com.example.stratego.session.*;

import static org.junit.jupiter.api.Assertions.*;
import com.example.stratego.GamePlaySession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class GamePlaySessionTest {

    SessionService session;
    Board board;
    Player testPlayer;
    Player redPlayer;

    @BeforeEach
    void setUp() {
        testPlayer = new Player( "blue");
        redPlayer = new Player("red");
        session = new SessionService(testPlayer);

        board = new Board();
        session.setBoard(board);

    }

    @Test
    void testCheckFlagCaptured() {
        board.setField(0, 0, new Piece(Rank.FLAG, Color.BLUE));  // Place a blue flag
        board.setField(9, 9, new Piece(Rank.GENERAL, Color.RED));  // Place a red piece

        // Test capturing blue flag
        assertTrue(GamePlaySession.checkFlagCaptured(board, Color.BLUE));
        // Test not capturing red flag
        assertFalse(GamePlaySession.checkFlagCaptured(board, Color.RED));
    }

    @Test
    void testHasMovablePieces() {
        board.setField(1, 1, new Piece(Rank.LIEUTENANT, Color.BLUE));
        board.setField(1, 2, new Piece(Rank.LIEUTENANT, Color.RED));

        assertTrue(GamePlaySession.hasMovablePieces(board, Color.BLUE));
        assertFalse(GamePlaySession.hasMovablePieces(board, Color.RED));
    }
}
