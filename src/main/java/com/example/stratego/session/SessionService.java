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
     * @param board new board state sent by client
     * @param initiator player that initiated the turn/play. if incorrect player attempts a turn -> InvalidPlayerException
     */
    public void updateBoard(Board board, int initiator) throws InvalidPlayerTurnException {
        if (initiator != this.currentTurn.getId() || this.currentGameState == GameState.WAITING) {
            throw new InvalidPlayerTurnException();
        }

        Move move = identifyBoardChange(this.board, board);
        if (move == null) {
            // exception? message to user?
            return;
        }
        boolean overlap = checkOverlap(move.newY, move.newX, move.piece, this.currentTurn);
        if (move != null) {
            if (overlap) {
                this.board.setField(move.newY, move.newX, move.piece);
            } else {
                Piece oldPiece = board.getField(move.newY, move.newX); //get piece on the attacked position
                if (oldPiece != null && oldPiece.getColor() == move.piece.getColor()) {
                //old state remains
                } else {
                    this.board.setField(move.origY, move.origX, null);
                }
            }
        }
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
     * Identifies change between two boards after move made
     * @param oldBoard previous state
     * @param newBoard new state
     * @return object with changed parameters
     */
    public Move identifyBoardChange(Board oldBoard, Board newBoard) {
        int origY = -1, origX = -1;
        int newY = -1, newX = -1;
        Piece movedPiece = null;


        // Check all positions on the board to find original and new position
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                Piece oldPiece = oldBoard.getField(y, x);
                Piece newPiece = newBoard.getField(y, x);

                // Check for piece in new board != old board
                if (newPiece != null && (oldPiece == null || !newPiece.equals(oldPiece))) {
                    newY = y;
                    newX = x;
                    movedPiece = newPiece;
                }
                // Check for piece in the old board != new board
                if (oldPiece != null && (newPiece == null || !oldPiece.equals(newPiece))) {
                    origY = y;
                    origX = x;
                }
            }
        }

        // if change return old and new position of movedPiece
        if (movedPiece != null && origY != -1 && origX != -1 && newY != -1 && newX != -1) {
            return new Move(origY, origX, newY, newX, movedPiece);
        }

        return null; // no move made
    }



    /**
     * checks if Pieces collide after update. Calls GamePlay Service to check outcome.
     * @param y - Row of updated position
     * @param x - Column of updated position
     * @return boolean if overlap true or false
     */
    public boolean checkOverlap(int y, int x, Piece piece, Player currentPlayer) {

        Piece existingPiece = this.board.getField(y, x);

        if (existingPiece != null) {
            // Ensure piece belongs to opponent
            if (existingPiece.getColor() != piece.getColor()) {
                if (existingPiece.getRank() == Rank.FLAG) {
                    GamePlaySession.checkFlagCaptured(this.board, piece.getColor(), y,x);
                    this.currentGameState = GameState.DONE;
                    return true;
                } else {
                    //resolve battle
                    boolean victory = GamePlaySession.fight(piece, existingPiece);
                    if (victory) {
                        // Win - replace the opponent's piece
                        this.board.setField(y, x, piece);
                        return true;
                    } else {
                        // Lose - remove the attacking piece
                        return false;
                    }
                }
            } else {
                //no move possible
                this.currentGameState = GameState.DONE;
                return true;
            }
        } else {
            // Move to an empty square is always valid
            this.board.setField(y, x, piece);
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
