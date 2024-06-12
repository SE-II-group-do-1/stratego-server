package com.example.stratego.session;

import com.example.stratego.GamePlaySession;
import com.example.stratego.session.exceptions.InvalidPlayerTurnException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
    private HashSet<Integer> setBoard;

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
        this.setBoard = new HashSet<>(2);
        this.board = new Board();
        activeSessions.add(this);
        activePlayers.add(player1);
        nextID++;
    }


    /**
     * sets Board setup for both players. returns true if both players submitted board. ready to play.
     * @param id - player id that sent their setup
     * @param board - the player's board
     * @return
     */
    public synchronized boolean setPlayerBoard(int id, Board board){
        //return false if player not in session
        if(this.playerBlue.getId() != id && this.playerRed.getId() != id) return false;
        this.board.mergeBoard(board);
        this.setBoard.add(id);
        if(this.setBoard.contains(this.playerBlue.getId()) && this.setBoard.contains(this.playerRed.getId())){
            this.currentGameState = GameState.INGAME;
            return true;
        }
        return false;
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
        Player player = getPlayerByID(initiator);
        identifyBoardChange(this.board, board, player);
        updatePlayerTurn();
    }

    /**
     * Identifies change between two boards after move made
     *
     * @param oldBoard      previous state
     * @param newBoard      new state
     * @param currentPlayer player who initiated move
     */
    public void identifyBoardChange(Board oldBoard, Board newBoard, Player currentPlayer) {
        // Check all positions on the board to find original and new position
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                Piece oldPiece = oldBoard.getField(y, x);
                Piece newPiece = newBoard.getField(y, x);
                // if new positionment of piece is a null space, simply move it.
                if (oldPiece == null) {
                    this.board.setField(y, x, newPiece);
                }
                // Check for piece in new board != old board
                else if (!newPiece.equals(oldPiece)) {
                    //check outcome of overlap
                    checkOverlap(oldPiece, newPiece, y, x);
                }
            }
        }
    }

    public boolean checkOverlap(Piece oldPiece, Piece newPiece, int y, int x){
        if (oldPiece.getRank() == Rank.FLAG) {
            GamePlaySession.checkFlagCaptured(this.board, newPiece.getColor(), y, x);
            this.currentGameState = GameState.DONE;
            return true;
        } else {
            //resolve battle
            boolean victory = GamePlaySession.fight(newPiece, oldPiece);
            if (victory) {
                // Win - replace the opponent's piece
                this.board.setField(y, x, newPiece);
                return true;
            }
            return false;
        }
    }

    public void close(){
        this.currentGameState = GameState.DONE;
        activeSessions.remove(this);
        activePlayers.remove(playerBlue);
        activePlayers.remove(playerRed);
    }

    public Player getPlayerBlue(){
        return this.playerBlue;
    }

    public Player getPlayerRed(){
        return this.playerRed;
    }

    public void setPlayerRed(Player newPlayer){
        this.playerRed = newPlayer;
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

    public static Player getPlayerByID(int id){
        return activePlayers.stream().filter(p -> p.getId() == id).toList().get(0);
    }

    public boolean isClosed(){
        return this.currentGameState == GameState.DONE;
    }

    public Player getCurrentTurn() {
        return this.currentTurn;
    }
}
