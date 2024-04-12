package com.example.stratego.session;

import java.util.ArrayList;
import java.util.List;

public class SessionService implements SessionServiceI {
    private static int nextID = 0;
    private static ArrayList<SessionService> activeSessions = new ArrayList<>();
    private int id;
    private final Player playerBlue;
    private Player playerRed;
    private Board board;
    private GameState currentGameState;
    private Player currentTurn;


    /**
     * Session Service - manages "lobbys", keeps track of current game/board and associated players.
     * first Player assigned to Session (upon creation of new Session) is assigned Blue Player,
     * following Player is assigned Red.
     * @param player1 - first Player. Session cannot be empty!
     */
    public SessionService(Player player1){
        this.playerBlue = player1;
        this.currentTurn = player1;
        this.currentGameState = GameState.WAITING;
        this.board = new Board();
        assignID(this);
        activeSessions.add(this);
    }

    private static void assignID(SessionService session){
        session.id = nextID;
        nextID++;
    }

    private void updatePlayerTurn(){
        this.currentTurn = this.currentTurn == this.playerBlue? this.playerRed : this.playerBlue;
    }

    /**
     * updates Board when Player moves Piece/attacks.
     * @param y - row in Board for new position of Piece
     * @param x - column in Board for new position of Piece
     * @param piece - the Piece that moved
     */
    public void updateBoard(int y, int x, Piece piece, Player initiator) throws InvalidPlayerTurnException{
        if(initiator.getId() != this.currentTurn.getId()) throw new InvalidPlayerTurnException();
        boolean overlap = checkOverlap(y,x);
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
    public boolean checkOverlap(int y, int x){
        return this.board.getField(y,x) != null;
    }

    public Player getPlayerBlue(){
        return this.playerBlue;
    }

    public Player getPlayerRed(){
        return this.playerRed;
    }

    public void setPlayerRed(Player newPlayer){
        this.playerRed = newPlayer;
        this.currentGameState = GameState.INGAME;
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
}
