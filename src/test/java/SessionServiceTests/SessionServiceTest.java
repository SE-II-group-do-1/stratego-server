package SessionServiceTests;

import com.example.stratego.session.exceptions.InvalidPlayerTurnException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.stratego.session.*;

import static org.junit.jupiter.api.Assertions.*;

class SessionServiceTest {

    SessionService session;
    Player testPlayer;

    @BeforeEach
    void setup(){
        testPlayer = new Player(1, "1");
        session = new SessionService(testPlayer);
    }

    @Test
    void testCreateService(){
        assertNotNull(session);
        assertEquals(testPlayer, session.getPlayerBlue());
    }

    @Test
    void testAddPlayer(){
        Player newPlayer = new Player(2, "2");
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
        try {
            session.updateBoard(2,3, new Piece(Rank.GENERAL, Color.BLUE), testPlayer);
        } catch (InvalidPlayerTurnException e) {
            throw new RuntimeException(e);
        }
        assertEquals(Rank.GENERAL, session.getBoard().getField(2, 3).getRank());
    }
}
