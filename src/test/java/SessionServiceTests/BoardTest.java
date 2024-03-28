package SessionServiceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import session.Board;
import session.Piece;
import session.Rank;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    Board testBoard;

    @BeforeEach
    void setup(){
        testBoard = new Board();
    }

    @Test
    void testConstructor(){
        assertNotNull(testBoard.getBoard());
    }

    @Test
    void testLakesCreated(){
        assertEquals(testBoard.getField(4,2), new Piece(Rank.LAKE));
    }

    @Test
    void testSetAndGetField(){
        testBoard.setField(1,1, new Piece(Rank.BOMB));
        assertEquals(testBoard.getField(1,1), new Piece(Rank.BOMB));
    }

    @Test
    void testSetAndGetBoard(){
        Board secondBoard = new Board();
        secondBoard.setField(1,2, new Piece(Rank.MAJOR));
        secondBoard.setField(1,3, new Piece(Rank.CAPTAIN));
        testBoard.setBoard(secondBoard);
        assertEquals(secondBoard, testBoard);
    }

}
