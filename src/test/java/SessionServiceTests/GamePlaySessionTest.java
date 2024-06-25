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
        movablePiece = new Piece(Rank.MARSHAL, Color.BLUE);
        immovablePiece = new Piece(Rank.BOMB, Color.RED);
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
    void testFight_SpyAttacksMarshal() {
        Piece spy = new Piece(Rank.SPY, Color.BLUE);
        Piece marshal = new Piece(Rank.MARSHAL, Color.RED);

        assertTrue(GamePlaySession.fight(spy, marshal));
    }

    @Test
    void testFight_MinerAttacksBomb() {
        Piece miner = new Piece(Rank.MINER, Color.BLUE);
        Piece bomb = new Piece(Rank.BOMB, Color.RED);

        assertTrue(GamePlaySession.fight(miner, bomb));
    }

    @Test
    void testFight_AttackerAttacksBomb() {
        Piece attacker = new Piece(Rank.LIEUTENANT, Color.BLUE);
        Piece bomb = new Piece(Rank.BOMB, Color.RED);

        assertFalse(GamePlaySession.fight(attacker, bomb));
    }

    @Test
    void testFight_NormalAttack() {
        Piece lieutenant = new Piece(Rank.LIEUTENANT, Color.BLUE);
        Piece captain = new Piece(Rank.CAPTAIN, Color.RED);

        // Since captain has a higher rank, lieutenant should lose
        assertFalse(GamePlaySession.fight(lieutenant, captain));
    }

    //TODO Should kill both pieces
    @Test
    void testFight_EqualRank() {
        Piece captainBlue = new Piece(Rank.CAPTAIN, Color.BLUE);
        Piece captainRed = new Piece(Rank.CAPTAIN, Color.RED);

        // Both pieces have the same rank, so it should result in a tie (returning false)
        assertFalse(GamePlaySession.fight(captainBlue, captainRed));
    }

    @Test
    void testFight_AttackerWins() {
        Piece general = new Piece(Rank.GENERAL, Color.BLUE);
        Piece lieutenant = new Piece(Rank.LIEUTENANT, Color.RED);

        // Since general has a higher rank, general should win
        assertTrue(GamePlaySession.fight(general, lieutenant));
    }

    @Test
    void sergeantLosingAgainstBombMakesBombVisible() {
        Piece sergant = new Piece(Rank.SERGEANT, Color.BLUE);
        Piece bomb = new Piece(Rank.BOMB, Color.RED);

        GamePlaySession.fight(sergant,bomb);

        assertTrue(bomb.isVisible());
    }

}
