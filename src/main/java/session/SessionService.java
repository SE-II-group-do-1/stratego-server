package session;

public class SessionService implements SessionServiceI {
    private Player playerBlue;
    private Player playerRed;
    private Board board;

    public SessionService(Player player1){
        this.playerBlue = player1;
        this.board = new Board();
    }

    public void updateBoard(int y, int x, Piece piece){
        checkOverlap();
        this.board.setField(y,x,piece);
    }

    public void setPieces(Board playerBoard, Player owner){
        if(owner == playerBlue || owner == playerRed){
            this.board.setBoard(playerBoard);
        }
    }

    public void checkOverlap(){

    }
}
