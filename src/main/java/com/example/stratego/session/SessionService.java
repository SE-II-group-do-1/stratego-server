package com.example.stratego.session;

import com.example.stratego.GamePlaySession;
import com.example.stratego.session.exceptions.InvalidPlayerTurnException;

import java.util.ArrayList;
import java.util.List;

public class SessionService implements SessionServiceI{
    private static int nextID = 0;
    private static ArrayList<SessionService> activeSessions = new ArrayList<>();
    private static ArrayList<Player> activePlayers = new ArrayList<>();
    private int id;
    private final Player playerBlue;
    private Player playerRed;
    private Board board;
    private GameState currentGameState;
    private Player currentTurn;
    private int setup;


    /**
     * Session Service - manages "lobbys", keeps track of current game/board and associated players.
     * first Player assigned to Session (upon creation of new Session) is assigned Blue Player,
     * following Player is assigned Red. Sessions automatically assigned ID, and added to activeSessions.
     * @param player1 - first Player. Session cannot be empty!
     */
    public SessionService(Player player1){
        this.id = nextID;
        this.playerBlue = player1;
        this.currentTurn = player1;
        this.currentGameState = GameState.WAITING;
        this.board = new Board();
        this.setup = 0;
        activeSessions.add(this);
        nextID++;
    }

    /**
     * updates the Player that is currently allowed to make a move.
     */
    private void updatePlayerTurn(){
        this.currentTurn = this.currentTurn == this.playerBlue? this.playerRed : this.playerBlue;
    }

    /**
     * updates Board when Player moves Piece/attacks.
     * @param y - row in Board for new position of Piece
     * @param x - column in Board for new position of Piece
     * @param initiator - player that initiated the turn/play. if incorrect player attempts a turn -> InvalidPlayerException
     * @param piece - the Piece that moved
     */
    public void updateBoard(int y, int x, Piece piece, Player initiator) throws InvalidPlayerTurnException {
        if(initiator.getId() != this.currentTurn.getId() || this.currentGameState == GameState.WAITING) throw new InvalidPlayerTurnException();
        boolean overlap = checkOverlap(y,x, piece, initiator);
        if(!overlap) this.board.setField(y,x,piece);
        updatePlayerTurn();
    }

    /**
     * Sets Pieces in Board after Player arranged pieces to their liking prior to game start.
     * @param playerBoard - Board with configuration of Player after setup.
     */
    public void setPieces(Board playerBoard){
        this.board.setBoard(playerBoard);
    }

    /**
     * checks if Pieces collide after update. Calls GamePlay Service to check outcome if so.
     * @param y - Row of updated position
     * @param x - Column of updated position
     * @return - boolean for the moment
     */
    public boolean checkOverlap(int y, int x, Piece piece, Player player){

        boolean isRedPlayer = (player == playerRed);
        Player playerToCheck = isRedPlayer ? playerRed : playerBlue;
        Color targetColor = (player == playerRed) ? Color.BLUE : Color.RED;

        boolean resultVictory = GamePlaySession.checkFlagCaptured(this.board, targetColor);
        boolean resultMoveablePiece = GamePlaySession.hasMovablePieces(this.board, targetColor);
        if (resultVictory) {
            return resultVictory;
        }
        if(resultMoveablePiece) {
            return resultMoveablePiece;
        }
        return false;
    }

    public void close(){
        this.currentGameState = GameState.DONE;
        activeSessions.remove(this);
    }

    public Player getPlayerBlue(){
        return this.playerBlue;
    }

    public Player getPlayerRed(){
        return this.playerRed;
    }

    public void setPlayerRed(Player newPlayer){
        this.playerRed = newPlayer;
        this.currentGameState = GameState.SETUP;
    }

    public void setBoard(Board board){
        setup += 1;
        this.board.setBoard(board);
        if(setup == 2) this.currentGameState = GameState.INGAME;
    }

    public Board getBoard(){
        return this.board;
    }

    public int getId(){
        return this.id;
    }

    public GameState getCurrentGameState() {
        return currentGameState;
    }

    public static List<SessionService> getActiveSessions() {
        return activeSessions;
    }

    public static Player newPlayer(String username){
        Player out = new Player(username);
        activePlayers.add(out);
        return out;
    }

    public static boolean removePlayer(Player toRemove){
        if(!activePlayers.contains(toRemove)) return false;
        activePlayers.remove(toRemove);
        return true;
    }

    public static  List<Player> getActivePlayers(){
        return activePlayers;
    }

    public boolean isClosed(){
        return this.currentGameState == GameState.DONE;
    }

    public Player getCurrentTurn() {
        return this.currentTurn;
    }
}
