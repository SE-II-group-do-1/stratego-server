package SessionServiceTests;

import com.example.stratego.session.exceptions.InvalidPlayerTurnException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.stratego.session.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SessionServiceTest {

    SessionService session;
    Player testPlayer;
    Player redPlayer;

    @BeforeEach
    void setup(){
        testPlayer = new Player( "blue");
        redPlayer = new Player("red");
        session = new SessionService(testPlayer);
    }

    @Test
    void testCreateService(){
        assertNotNull(session);
        assertEquals(testPlayer, session.getPlayerBlue());
    }

    @Test
    void testGetSessions(){
        assertNotNull(SessionService.getActiveSessions());
    }

    @Test
    void testFindSession(){
        List current = SessionService.getActiveSessions();
        assertTrue(current.contains(session));
    }

    @Test
    void testAddPlayer(){
        Player newPlayer = new Player("2");
        session.setPlayerRed(newPlayer);
        assertEquals(newPlayer, session.getPlayerRed());
    }

    @Test
    void testSetPieces(){
        Board testBoard = new Board();
        testBoard.setField(3,4, new Piece(Rank.FLAG, Color.BLUE));
        session.setPieces(testBoard);
        assertArrayEquals(testBoard.getBoard(), session.getBoard().getBoard());
    }

    @Test
    void testUpdateBoard(){
        session.setPlayerRed(redPlayer);
        try {
            session.updateBoard(2,3, new Piece(Rank.GENERAL, Color.BLUE), testPlayer);
        } catch (InvalidPlayerTurnException e) {
            throw new RuntimeException(e);
        }
        assertEquals(Rank.GENERAL, session.getBoard().getField(2, 3).getRank());
    }

    @Test
    void testInvalidUpdateBoard(){
        session.setPlayerRed(redPlayer);
        assertThrows(InvalidPlayerTurnException.class,() -> session.updateBoard(2,3, new Piece(Rank.GENERAL, Color.RED), redPlayer));

    }
    @Test
    void testEarlyUpdateBoard(){
        assertThrows(InvalidPlayerTurnException.class,() -> session.updateBoard(2,3, new Piece(Rank.GENERAL, Color.RED), testPlayer));
    }

    @Test
    void testGameStateWaiting(){
        assertEquals(GameState.WAITING, session.getCurrentGameState());
    }

    @Test
    void testGameStateIngame(){
        session.setPlayerRed(redPlayer);
        assertEquals(GameState.INGAME, session.getCurrentGameState());
    }

    @Test
    void testClose(){
        session.close();
        assertTrue(session.isClosed());
        assertFalse(SessionService.getActiveSessions().contains(session));
    }
}
