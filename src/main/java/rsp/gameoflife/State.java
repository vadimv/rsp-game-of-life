package rsp.gameoflife;


public class State {
    public final Board board;

    public State(Board board) {
        this.board = board;
    }

    public static State initialState() {
        return new State(Board.create());
    }

    public State toggleCell(int x, int y) {
        return new State(board.toggle(x, y));
    }

    public State advance() {
        return new State(board.advance());
    }
}
