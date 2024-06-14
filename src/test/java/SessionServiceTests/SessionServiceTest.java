package SessionServiceTests;

import com.example.stratego.GamePlaySession;
import com.example.stratego.session.exceptions.InvalidPlayerTurnException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.stratego.session.*;

import java.awt.event.PaintEvent;
import java.util.List;

import static com.example.stratego.session.GameState.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


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
    void testCheckOverlapVictoryAgainstPiece() {
        Piece miner = new Piece(Rank.MINER, Color.BLUE);
        session.getBoard().setField(1,1, new Piece(Rank.BOMB, Color.RED));
        session.checkOverlap(new Piece(Rank.BOMB, Color.RED), miner, 1, 1);
        assertEquals(session.getBoard().getField(1,1),miner);
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
    void testCloseFalse(){
        assertFalse(session.isClosed());
    }
}
