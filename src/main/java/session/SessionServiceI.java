package session;

public interface SessionServiceI {
    void updateBoard(int y, int x, Piece piece);

    void checkOverlap();
    void setPieces();
}
