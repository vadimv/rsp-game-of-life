package rsp.gameoflife;

public class Board {
    public static final int HEIGHT = 50;
    public static final int WIDTH = 100;

    public final Cell[][] cells;

    private Board(Cell[][] cells) {
        this.cells = cells;
    }

    public static Board create() {
        final Cell[][] c = new Cell[HEIGHT][WIDTH];
        for(int y = 0; y < HEIGHT; y++) {
            for(int x = 0; x < WIDTH; x++) {
                c[y][x] = new Cell(x, y, false);
            }
        }
        return new Board(c);
    }

    public Board setActive(int x, int y, boolean a) {
        final Cell[][] copy = new Cell[cells.length][cells[0].length];
        for (int i=0;i < cells.length;i++) {
            for(int j=0;j < cells[0].length;j++){
                copy[i][j] = i == y && j == x && cells[y][x].active != a ? new Cell(x, y, a) : cells[i][j];
            }
        }
        return new Board(copy);
    }

    public Board toggle(int x, int y) {
        return setActive(x, y, !cells[y][x].active);
    }

    public Board advance() {
        final Cell[][] copy = new Cell[cells.length][cells[0].length];
        for(int y = 0; y < HEIGHT; y++) {
            for(int x = 0; x < WIDTH; x++) {
                final int n = neighbours(x, y);
                final Cell cell = cells[y][x];
                if (cell.active) {
                    if (n < 2 || n > 3) copy[y][x] = new Cell(x, y, false);
                        else copy[y][x] = cells[y][x];
                } else {
                    if (n == 3) copy[y][x] = new Cell(x, y, true);
                        else copy[y][x] = cells[y][x];
                }
            }
        }
        return new Board(copy);
    }

    private int neighbours(int x, int y) {
        // If the cell is at the edge use as its neighbours the cells on the opposite edge
        final int topY = y - 1 < 0 ? (HEIGHT - 1) : y - 1;
        final int bottomY = (y + 1 == HEIGHT) ? 0 : y + 1;
        final int leftX = x - 1 < 0 ? (WIDTH - 1) : x - 1;
        final int rightX = (x + 1 == WIDTH) ? 0 : x + 1;

        return b2i(cells[topY][leftX].active)
                + b2i(cells[topY][x].active)
                + b2i(cells[topY][rightX].active)
                + b2i(cells[y][leftX].active)
                + b2i(cells[y][rightX].active)
                + b2i(cells[bottomY][leftX].active)
                + b2i(cells[bottomY][x].active)
                + b2i(cells[bottomY][rightX].active);
    }

    private static int b2i(boolean value) {
        return value ? 1 : 0;
    }

    public static class Cell {
        public final int x;
        public final int y;

        public final boolean active;

        public Cell(int x, int y, boolean active) {
            this.x = x;
            this.y = y;
            this.active = active;
        }
    }
}
