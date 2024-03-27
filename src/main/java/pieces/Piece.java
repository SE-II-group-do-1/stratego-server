package pieces;

public abstract class Piece {
    protected PieceColor pieceColor;
    protected int rank;
    protected int maxCount;
    protected boolean isMovable;
    public Piece(PieceColor pieceColor, int rank, int maxCount, boolean isMovable){

        this.pieceColor = pieceColor;
        this.rank = rank;
        this.maxCount = maxCount;
        this.isMovable = isMovable;
    }
    public boolean defend(Piece p){
        return this.getRank() >= p.getRank();
    }
    public boolean isValidMove(int fromX, int fromY, int toX, int toY){
        return false;
    }
    public int getRank(){
        return rank;
    }
    public int getMaxCount(){
        return maxCount;
    }

    public boolean isMovable() {
        return isMovable;
    }

    public PieceColor getColor(){
        return this.pieceColor;
    }




}
