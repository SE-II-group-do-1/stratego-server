package com.example.stratego.session;

import com.example.stratego.GamePlaySession;
import com.example.stratego.session.exceptions.InvalidPlayerTurnException;

import java.util.ArrayList;
import java.util.HashSet;
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
    private Color winner;
    private HashSet<Integer> setBoard;

    private boolean redCheat = false;
    private boolean blueCheat = false;

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
     * Assigns new players to either a vacant position in a given session or creates a new session.
     * @param p - the new player to be assigned to a session
     * @return true if the session assigned to is now full, false if assigned to a new session (session not full)
     */
    public static boolean assignToSession(Player p){
        for (SessionService session : activeSessions) {
            if (session.getPlayerRed() == null && !session.getPlayerBlue().getUsername().equals(p.getUsername())) {
                session.setPlayerRed(p);
                return true;
            }
        }
        new SessionService(p);
        return false;
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
     * Sets the cheat-status of the initiator
     * @param initiator - player that sent message
     * @param cheat - true/false
     */
    public void setCheat(int initiator, boolean cheat){
        Player p = getPlayerByID(initiator);
        if(p.equals(playerBlue)){
            blueCheat = cheat;
            return;
        }
        redCheat = cheat;
    }

    /**
     * is called when a player decides to check if the other is cheating
     * @param check - true/false if player decides to check on opponent (risky - missing causes opponent to win, if opponenet was cheating, you win)
     * @param initiator - player
     */
    public void checkCheat(boolean check, int initiator){
        if(!check) return;
        Player p = getPlayerByID(initiator);
        if(p.equals(playerBlue) && redCheat){
            this.winner = Color.BLUE;
        }
        else if(p.equals(playerBlue) && !redCheat){
            this.winner = Color.RED;
        }
        else if(p.equals(playerRed) && blueCheat){
            this.winner = Color.RED;
        }
        else if(p.equals(playerRed) && !blueCheat){
            this.winner = Color.BLUE;
        }
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
        //update current player turn
        this.currentTurn = this.currentTurn == this.playerBlue? this.playerRed : this.playerBlue;
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

                if(oldPiece != null && newPiece != null){
                    checkOverlap(oldPiece, newPiece, y, x);
                }
                else if (oldPiece != null ) {
                    this.board.setField(y, x, null);
                }
                else this.board.setField(y, x, newPiece);
            }
        }
    }

    public boolean checkOverlap(Piece oldPiece, Piece newPiece, int y, int x){
        if(oldPiece.getColor() == newPiece.getColor()) return false;

        if (oldPiece.getRank() == Rank.FLAG) {
            this.board.setField(y,x, newPiece);
            this.winner = newPiece.getColor();
            this.currentGameState = GameState.DONE;
            return true;
        } else if (oldPiece.getRank() == newPiece.getRank()) {
            this.board.setField(y, x, null);
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

    public static SessionService getSessionByID(int id){
        return activeSessions.stream().filter(s -> s.getId() == id).toList().get(0);
    }

    public static SessionService getSessionByPlayer(Player p){
        return activeSessions.stream().filter(s -> s.getPlayerBlue() == p || s.getPlayerRed() == p).toList().get(0);
    }

    public boolean isClosed(){
        return this.currentGameState == GameState.DONE;
    }

    public Player getCurrentTurn() {
        return this.currentTurn;
    }

    public Color getWinner(){
        return this.winner;
    }
}
