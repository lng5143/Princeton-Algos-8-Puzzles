import java.util.ArrayList;
import java.util.Arrays;

public class Board {

    private final int n;
    private final int[][] blocks;
    private int rowOfZero;
    private int colOfZero;

    // create a board from an n-by-n array of tiles,
    //where tiles[row][col] = tiles at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        blocks = new int[n][n];

        for (int i = 0; i < n; i ++)
            blocks[i] = tiles[i].clone();

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (tiles[i][j] == 0) {
                    rowOfZero = i;
                    colOfZero = j;
                }
    }

    //string representation of this board -> passed
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(n);
        for (int i = 0; i < n; i++) {
            result.append("\n");
            for (int j = 0; j < n; j++) {
                result.append(" ");
                result.append(blocks[i][j]);
            }
        }
        return result.toString();
    }

    //board dimension n
    public int dimension() {
        return n;
    }

    //number of tiles out of place
    public int hamming() {
        int hamming = 0;

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (blocks[i][j] != n * i + j + 1 && blocks[i][j] != 0)
                    hamming++;
        return hamming;
    }

    //sum of Manhatttan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;
        int distanceOfTile;
        int rowDistance;
        int colDistance;
        int goalRow;
        int goalCol;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] != 0) {
                    goalRow = (blocks[i][j] - 1) / n;
                    goalCol = (blocks[i][j] - 1) % n;
                    rowDistance = Math.abs(goalRow - i);
                    colDistance = Math.abs(goalCol - j);
                    distanceOfTile = rowDistance + colDistance;
                    manhattan += distanceOfTile;
                }
            }
        }
        return manhattan;
    }

    //is this board the goal board? -> passed
    public boolean isGoal() {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (blocks[i][j] != 0 && blocks[i][j] != n * i + j + 1)
                    return false;
        return true;
    }

    //does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (this.getClass() != y.getClass())
            return false;

        Board that = (Board) y;
        return Arrays.deepEquals(this.blocks, that.blocks);
    }

    //all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<>();

        if (validTile((rowOfZero - 1), colOfZero)) {
            int[][] upTiles = clone2D(blocks);
            exch(upTiles, rowOfZero, colOfZero, rowOfZero - 1, colOfZero);
            final Board upBoard = new Board(upTiles);
            neighbors.add(upBoard);
        }

        if (validTile((rowOfZero + 1), colOfZero)) {
            int[][] downTiles = clone2D(blocks);
            exch(downTiles, rowOfZero, colOfZero, rowOfZero + 1, colOfZero);
            final Board downBoard = new Board(downTiles);
            neighbors.add(downBoard);
        }

        if (validTile((rowOfZero), colOfZero - 1)) {
            int[][] leftTiles = clone2D(blocks);
            exch(leftTiles, rowOfZero, colOfZero, rowOfZero, colOfZero - 1);
            final Board leftBoard = new Board(leftTiles);
            neighbors.add(leftBoard);
        }

        if (validTile((rowOfZero), colOfZero + 1)) {
            int[][] rightTiles = clone2D(blocks);
            exch(rightTiles, rowOfZero, colOfZero, rowOfZero, colOfZero + 1);
            final Board rightBoard = new Board(rightTiles);
            neighbors.add(rightBoard);
        }

        return neighbors;
    }

    //a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++)
            copy[i] = blocks[i].clone();

        if      (copy[0][0] == 0 || copy[0][1] == 0) exch(copy, 1, 0, 1, 1);
        else    exch(copy, 0, 0, 0, 1);

        Board twin = new Board(copy);
        return twin;
    }

    //helper method: exchange two tiles
    private void exch(int[][] arr, int i1, int j1, int i2, int j2) {
        int temp = arr[i1][j1];
        arr[i1][j1] = arr[i2][j2];
        arr[i2][j2] = temp;
    }

    //helper method: check if a tile at row, col is a valid tile -> passed
    private boolean validTile(int row, int col) {
        return (row >= 0 && row < n && col >= 0 && col < n);
    }

    //helper method: clone 2d array
    private int[][] clone2D(int[][] tiles) {
        int n = tiles.length;
        int [][] clone = new int[n][n];
        for (int i = 0; i < n; i++)
            clone[i] = tiles[i].clone();
        return clone;
    }

    //unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = {
                {8, 6, 7},
                {2, 5, 4},
                {3, 0, 1}};
        Board board = new Board(tiles);
        System.out.println(board.toString());

        System.out.println("Dimension: " + board.dimension());
        System.out.println("Is goal: " + board.isGoal());
        System.out.println("Hamming: " + board.hamming());
        System.out.println("Manhattan: " + board.manhattan());
        //System.out.println("Twin: \n" + board.twin().toString());

        /*int[][] copyTiles = tiles.clone();
        Board copyBoard = new Board(copyTiles);
        System.out.println(copyBoard.toString());*/

        //System.out.println(board.test().toString());
        System.out.println("\n Neighbors:");
        for (Board i : board.neighbors())
            System.out.println(i.toString());

        //System.out.println("twin" + board.twin().toString());
        //System.out.println("board after calling twin()" + board.toString());

    }
}