package SessionServiceTests;

import com.example.stratego.session.Color;
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
        testBoard.setField(1,1, new Piece(Rank.BOMB, Color.BLUE));
        assertEquals(Rank.BOMB, testBoard.getField(1, 1).getRank());
    }

    @Test
    void testSetAndGetBoard(){
        Piece[][] secondBoard = new Piece[10][10];
        secondBoard[2][1] = new Piece(Rank.MAJOR, Color.BLUE);
        testBoard.setBoard(secondBoard);
        assertArrayEquals(secondBoard, testBoard.getBoard());

    }

}
