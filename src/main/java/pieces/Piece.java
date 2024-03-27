package pieces;

public abstract class Piece {

    protected Color color;

    protected Piece(Color pieceColor){
        this.color = pieceColor;
    }
    public boolean defend(Piece p){
        return this.getRank() > p.getRank();
    }
    public boolean isValidMove(int fromX, int fromY, int toX, int toY){
        return false;
    }
    public static int getRank(){
        return -1;
    }
    public static int getMaxCount(){
        return -1;
    }



}
