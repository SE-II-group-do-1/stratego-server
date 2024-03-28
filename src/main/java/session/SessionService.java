package session;

public class SessionService implements SessionServiceI {
    private final Player playerBlue;
    private Player playerRed;
    private Board board;

    public SessionService(Player player1){
        this.playerBlue = player1;
        this.board = new Board();
    }

    public void updateBoard(int y, int x, Piece piece){
        boolean overlap = checkOverlap(y,x);
        if(!overlap) this.board.setField(y,x,piece);
    }

    public void setPieces(Board playerBoard, Player owner){
        if(owner == playerBlue || owner == playerRed){
            this.board.setBoard(playerBoard);
        }
    }

    public boolean checkOverlap(int y, int x){
        return this.board.getField(y,x) != null;
    }
}
