package pieces;

public class MarshalPiece extends Piece{
    public MarshalPiece(PieceColor pieceColor) {

        super(pieceColor, 10, 1, true);
    }

    @Override
    public boolean defend(Piece p) {
        return !(p instanceof SpyPiece);
    }
}
