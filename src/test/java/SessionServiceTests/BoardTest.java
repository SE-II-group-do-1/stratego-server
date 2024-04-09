import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.stratego.session.Board;
import com.example.stratego.session.Piece;
import com.example.stratego.session.Rank;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    Board testBoard;

    @BeforeEach
    void setup(){
        testBoard = new Board();
    }

    @AfterEach
    void tearDown(){
        testBoard = null;
    }

    @Test
    void testConstructor(){
        assertNotNull(testBoard.getBoard());
    }

    @Test
    void testLakesCreated(){
        assertEquals(Rank.LAKE, testBoard.getField(4, 2).getRank());
    }

    @Test
    void testSetAndGetField(){
        testBoard.setField(1,1, new Piece(Rank.BOMB));
        assertEquals(Rank.BOMB, testBoard.getField(1, 1).getRank());
    }

    @Test
    void testSetAndGetBoard(){
        Board secondBoard = new Board();
        secondBoard.setField(1,2, new Piece(Rank.MAJOR));
        testBoard.setBoard(secondBoard);
        assertArrayEquals(secondBoard.getBoard(), testBoard.getBoard());

    }

}
