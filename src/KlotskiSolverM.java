public class KlotskiSolverM {
    Board board;
    int step;
    String[] steps;

    public KlotskiSolverM(Board board) {
        this.board = board;
    }

    public void solve() {
        Piece[] blankList = board.findAllPieceByValue(0);
        if (blankList.length == 0) {

        }
    }

//    public void singleSolver
}

