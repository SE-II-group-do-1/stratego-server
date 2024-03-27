package pieces;

public class BombPiece extends Piece{

    public BombPiece(PieceColor pieceColor) {

        super(pieceColor, 11, 6, false);
    }

    @Override
    public boolean defend(Piece p) {
        return !(p instanceof MinerPiece);

    }
}
