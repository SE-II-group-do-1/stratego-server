package SessionServiceTests;
import com.example.stratego.session.*;

import static org.junit.jupiter.api.Assertions.*;
import com.example.stratego.GamePlaySession;
import com.example.stratego.session.exceptions.WrongConstructorException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class GamePlaySessionTest {

    Board board;
    Piece movablePiece, immovablePiece, lakePiece;

    @BeforeEach
    void setUp() {
        board = new Board(); // Reset the board before each test

        // Set up pieces
        movablePiece = new Piece(Rank.SPY, Color.BLUE);
        immovablePiece = new Piece(Rank.FLAG, Color.RED);
        try {
            lakePiece = new Piece(Rank.LAKE);
        } catch (WrongConstructorException e) {
            throw new RuntimeException(e);
        }

        // Assuming setField correctly places pieces at specified coordinates
        board.setField(5, 5, movablePiece);
        board.setField(5, 6, immovablePiece); // Directly next to a movable piece
        board.setField(4, 5, lakePiece); // Next to a movable piece on another side
    }


    @Test
    void testIsPieceMovable_NormalConditions() {
        assertTrue(GamePlaySession.isPieceMovable(board, movablePiece));
    }

    @Test
    void testIsPieceMovable_SurroundedByLakesAndPieces() {
        board.setField(5, 4, lakePiece);
        board.setField(6, 5, immovablePiece);
        assertFalse(GamePlaySession.isPieceMovable(board, movablePiece));
    }

    @Test
    void testIsPieceMovable_AtEdgeOfBoard() {
        // Place a movable piece at the edge and test
        board.setField(0, 0, new Piece(Rank.SCOUT, Color.BLUE));
        assertTrue(GamePlaySession.isPieceMovable(board, board.getField(0, 0)));
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


}
