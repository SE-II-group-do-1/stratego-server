package SessionServiceTests;

import com.example.stratego.session.exceptions.InvalidPlayerTurnException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.stratego.session.*;

import java.util.List;

import static com.example.stratego.session.GameState.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    SessionService session;
    Player testPlayer;
    Player redPlayer;
    Board boardMock;

    @BeforeEach
    void setup(){
        //board = new Board();
        testPlayer = new Player( "blue");
        redPlayer = new Player("red");
        session = new SessionService(testPlayer);

        boardMock = mock(Board.class);
    }

    @AfterEach
    void destroy(){
        session.close();
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
    void testUpdateBoard(){
        Board b = new Board();
        session.setPlayerRed(redPlayer);
        session.setPlayerBoard(redPlayer.getId(), new Board());
        session.setPlayerBoard(testPlayer.getId(), new Board());
        b.setField(0,0, new Piece(Rank.GENERAL, Color.BLUE));
        try {
            session.updateBoard(b, testPlayer.getId());
        } catch (InvalidPlayerTurnException e) {
            throw new RuntimeException(e);
        }
        assertEquals(Rank.GENERAL, session.getBoard().getField(0, 0).getRank());
    }

    @Test
    void testUpdateBoardNewPieceNull(){
        Board b = new Board();
        session.setPlayerRed(redPlayer);
        session.setPlayerBoard(redPlayer.getId(), new Board());
        session.setPlayerBoard(testPlayer.getId(), new Board());
        session.getBoard().setField(0,0, new Piece(Rank.GENERAL, Color.BLUE));
        try {
            session.updateBoard(b, testPlayer.getId());
        } catch (InvalidPlayerTurnException e) {
            throw new RuntimeException(e);
        }
        assertNull(session.getBoard().getField(0, 0));
    }

    @Test
    void testInvalidUpdateBoard(){
        session.setPlayerRed(redPlayer);
        assertThrows(InvalidPlayerTurnException.class,() -> session.updateBoard(new Board(), redPlayer.getId()));

    }
    @Test
    void testEarlyUpdateBoard(){
        assertThrows(InvalidPlayerTurnException.class,() -> session.updateBoard(new Board(), testPlayer.getId()));
    }

    @Test
    void testCheckOverlap(){
        session.getBoard().setField(1,1, new Piece(Rank.GENERAL, Color.RED));
        session.checkOverlap(new Piece(Rank.GENERAL, Color.RED), new Piece(Rank.SCOUT, Color.BLUE), 1, 1);
        assertEquals(Rank.GENERAL, session.getBoard().getField(1,1).getRank());
    }

    @Test
    void testCheckOverlapFlag(){
        session.getBoard().setField(1,1, new Piece(Rank.FLAG, Color.RED));
        assertTrue(session.checkOverlap(new Piece(Rank.FLAG, Color.RED), new Piece(Rank.SCOUT, Color.BLUE), 1, 1));
    }

    @Test
    void testCheckOverlapVictory(){
        session.getBoard().setField(1,1, new Piece(Rank.FLAG, Color.RED));
        session.checkOverlap(new Piece(Rank.FLAG, Color.RED), new Piece(Rank.SCOUT, Color.BLUE), 1, 1);
        assertSame(DONE, session.getCurrentGameState());
    }

    @Test
    void testGameStateWaiting(){
        assertEquals(GameState.WAITING, session.getCurrentGameState());
    }

    @Test
    void testGameStateIngame(){
        session.setPlayerRed(redPlayer);
        session.setPlayerBoard(testPlayer.getId(), new Board());
        session.setPlayerBoard(redPlayer.getId(), new Board());
        assertEquals(INGAME, session.getCurrentGameState());
    }


    @Test
    void testCreatePlayer(){
        testPlayer = SessionService.newPlayer("test");
        assertNotNull(testPlayer);
    }

    @Test
    void testRemovePlayerTrue(){
        testPlayer = SessionService.newPlayer("test");
        SessionService.removePlayer(testPlayer);
        assertFalse(SessionService.getActiveSessions().contains(testPlayer));
    }


    @Test
    void testListPlayers(){
        List players = SessionService.getActivePlayers();
        assertNotNull(players);
    }



    @Test
    void testClose(){
        session.close();
        assertTrue(session.isClosed());
        assertFalse(SessionService.getActiveSessions().contains(session));
    }

    @Test
    void testGetSessionByID(){
        assertEquals(session, SessionService.getSessionByID(session.getId()));
    }

    @Test
    void testGetSessionByIDFalse(){
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> SessionService.getSessionByID(99));
    }

    @Test
    void testAssignToSession(){
        assertTrue(SessionService.assignToSession(redPlayer));
    }

    @Test
    void testAssignToSessionFalse(){
        Player t = new Player("t");
        SessionService.assignToSession(redPlayer);
        assertFalse(SessionService.assignToSession(t));
    }

    @Test
    void testGetSessionByPlayer(){
        assertEquals(session, SessionService.getSessionByPlayer(testPlayer));
    }

    @Test
    void testGetSessionByPlayerFalse(){
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> SessionService.getSessionByPlayer(new Player("fake")));
    }

    @Test
    void testCloseFalse(){
        assertFalse(session.isClosed());
    }
}
