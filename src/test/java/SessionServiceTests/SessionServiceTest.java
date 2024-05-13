package SessionServiceTests;

import com.example.stratego.GamePlaySession;
import com.example.stratego.session.exceptions.InvalidPlayerTurnException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.stratego.session.*;

import java.util.List;

import static com.example.stratego.session.GameState.INGAME;
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
    void testGameStateSetup(){
        session.setPlayerRed(redPlayer);
        assertEquals(GameState.SETUP, session.getCurrentGameState());
    }

    @Test
    void testGameStateIngame(){
        session.setPlayerRed(redPlayer);
        session.setBoard(new Board());
        session.setBoard(new Board());
        assertEquals(INGAME, session.getCurrentGameState());
    }


    @Test
    void testInvalidMoveDueToImmovablePiece() {

        boardMock = mock(Board.class);
        Piece pieceMock = mock(Piece.class);

        when(boardMock.getField(5, 5)).thenReturn(pieceMock);
        when(pieceMock.getColor()).thenReturn(Color.BLUE);
        when(pieceMock.isMovable()).thenReturn(false);  // The piece is not movable

        assertFalse(session.checkOverlap(5, 5, pieceMock, testPlayer));
    }

    @Test
    void testMoveToEmptySpace() {
        Piece pieceMock = mock(Piece.class);

        when(boardMock.getField(5, 5)).thenReturn(null);  // No piece at the target location
        when(pieceMock.isMovable()).thenReturn(true);

        assertTrue(session.checkOverlap(5, 5, pieceMock, testPlayer));
        Mockito.verify(boardMock).setField(5, 5, pieceMock);  // Should place the piece at the empty location
    }

    @Test
    void testCheckOverlapWithFlagCaptured() {
        // Setup
        Piece blueGeneral = new Piece(Rank.GENERAL, Color.BLUE);
        Piece redFlag = new Piece(Rank.FLAG, Color.RED);
        int targetX = 5;
        int targetY = 5;

        // Setup board
        boardMock.setField(targetY, targetX, redFlag);  // Red flag placed at (5, 5)
        boardMock.setField(4, 4, blueGeneral);  // Blue general placed near the flag

        // Mocking static methods in GamePlaySession for flag capture check
        GamePlaySession spySession = Mockito.spy(GamePlaySession.class);
        doReturn(true).when(spySession).checkFlagCaptured(boardMock, Color.RED);

        // Act
        boolean flagCaptured = session.checkOverlap(targetY, targetX, blueGeneral, testPlayer);

        // Assert
        assertTrue(flagCaptured);
        assertEquals(GameState.DONE, session.getCurrentGameState());
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
