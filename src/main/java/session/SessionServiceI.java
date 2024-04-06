package session;

public interface SessionServiceI {
    void updateBoard(int y, int x, Piece piece);

    boolean checkOverlap(int y, int x);
    void setPieces(Board playerBoard);
}
