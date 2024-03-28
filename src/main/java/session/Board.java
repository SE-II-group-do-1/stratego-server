package session;

public class Board {
    private Piece[][] fields;

    public Board(){
        this.fields =  new Piece[10][10];
        // set lakes
        fields[4][2] = new Piece();
        fields[4][3] = new Piece();
        fields[5][2] = new Piece();
        fields[5][3] = new Piece();

        fields[4][6] = new Piece();
        fields[4][7] = new Piece();
        fields[5][6] = new Piece();
        fields[5][7] = new Piece();
    }

    public void setField(int y, int x, Piece piece){
        fields[y][x] = piece;
    };
    public Piece getField(int y, int x){
        return fields[y][x];
    };
    public Piece[][] getBoard(){
        return fields;
    }
}
