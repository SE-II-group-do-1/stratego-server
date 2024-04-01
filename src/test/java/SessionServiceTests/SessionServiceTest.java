import org.junit.jupiter.api.Test;
import session.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SessionServiceTest {

    SessionService session;
    Player testPlayer;

    @Test
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
        testBoard.setField(3,4, new Piece(Rank.FLAG));
        session.setPieces(testBoard);
        assertEquals();
    }
}
