import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.util.ArrayList;

public class Solver {
    private MinPQ<Node> initialPQ = new MinPQ<>();
    private MinPQ<Node> twinPQ = new MinPQ<>();
    private Node endNode;
    private boolean solvable;

    private class Node implements Comparable<Node>{
        Board board;
        private final int manhattan;
        private int moves;
        private int priority;
        Node prev;

        public Node(Board board, int moves) {
            this.board = board;
            this.moves = moves;
            manhattan = board.manhattan();
            priority = manhattan + moves;
        }

        @Override
        public int compareTo(Node that) {
            return (this.priority - that.priority);
        }
    }

    //find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        Board twin = initial.twin();
        Node initialNode = new Node(initial, 0);
        Node twinNode = new Node(twin, 0);

        initialPQ.insert(initialNode);
        twinPQ.insert(twinNode);

        boolean solved = false;
        boolean twinSolved = false;

        while(!solved && !twinSolved) {

            Node current = initialPQ.delMin();
            Node twinCurrent = twinPQ.delMin();

            solved = current.board.isGoal();
            twinSolved = twinCurrent.board.isGoal();

            for (Board i : current.board.neighbors()) {
                if (current.prev != null && i.equals(current.prev.board)) continue;
                Node neighborNode = new Node(i, current.moves + 1);
                neighborNode.prev = current;
                initialPQ.insert(neighborNode);
            }

            for (Board i : twinCurrent.board.neighbors()) {
                if (twinCurrent.prev != null && i.equals(twinCurrent.prev.board)) continue;
                Node neighborNode = new Node(i, twinCurrent.moves + 1);
                neighborNode.prev = twinCurrent;
                twinPQ.insert(neighborNode);
            }
            endNode = current;
            solvable = !twinSolved;
        }
    }

    //is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }

    //min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable()) return endNode.moves;
        else return -1;
    }

    //sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!solvable) return null;

        Stack<Board> boards = new Stack<>();
        Node lastNode = this.endNode;

        while (lastNode.prev != null) {
            boards.push(lastNode.board);
            lastNode = lastNode.prev;
        }
        boards.push(lastNode.board);
        return boards;
        }


    //test client
    public static void main(String[] args) {
        int[][] testTiles = {
                {8, 4, 7},
                {1, 5, 6},
                {3, 2, 0}
        };
        Board testBoard = new Board(testTiles);
        Solver testSolver = new Solver(testBoard);

        System.out.println("Solvable: " + testSolver.isSolvable());
        System.out.println("Moves: " + testSolver.moves());
        for (Board i: testSolver.solution())
            System.out.println(i.toString());
    }
}

/*
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.Queue;
public class Solver {
    MinPQ<Node> initialPQ = new MinPQ<>();
    MinPQ<Node> twinPQ = new MinPQ<>();
    private boolean solvable;
    private Node endNode;

    private class Node implements Comparable<Node> {
        Board board;
        int moves;
        Node prev;
        int priority;
        int manhattan;
        private Node(Board board, int moves) {
            this.board = board;
            this.moves = moves;
            manhattan = board.manhattan();
            this.priority = this.manhattan + this.moves;
        }
        @Override
        public int compareTo(Node that) {
            return (this.priority - that.priority);
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        Board twin = initial.twin();
        Node node = new Node(initial, 0);
        Node twinNode = new Node(twin, 0);

        solvePQ (node, twinNode);
    }

    private void solvePQ(Node initial, Node twin) {
        if (initial.board.isGoal()) {
            endNode = initial;
            solvable = true;
            return;
        }

        if (twin.board.isGoal()) {
            solvable = false;
            return;
        }

        //initialPQ.insert(ini);
        //twinPQ.insert(twi);

        boolean solved = false;
        boolean twinSolved = false;
        //Node initial = null;



            //Node initial = initialPQ.delMin();
            //solved = initial.board.isGoal();

            //Node twin = twinPQ.delMin();
            //twinSolved = twin.board.isGoal();

            for (Board i : initial.board.neighbors()) {
                Node neighborNode = new Node(i, initial.moves + 1);
                neighborNode.prev = initial;
                neighborNode.priority = neighborNode.manhattan + neighborNode.moves;
                if (initial.prev != null) {
                    if (!neighborNode.board.equals(initial.prev.board))
                        initialPQ.insert(neighborNode);
                } else initialPQ.insert(neighborNode);
            }

            for (Board i : twin.board.neighbors()) {
                Node neighborNode = new Node(i, twin.moves + 1);
                neighborNode.prev = twin;
                neighborNode.priority = neighborNode.manhattan + neighborNode.moves;
                if (twin.prev != null) {
                    if (!neighborNode.board.equals(twin.prev.board))
                        twinPQ.insert(neighborNode);
                } else twinPQ.insert(neighborNode);
            }


        Node initialMin = initialPQ.delMin();
        Node twinMin = twinPQ.delMin();
        //endNode = initial;
        //solvable = !twinSolved;
        solvePQ(initialMin, twinMin);
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable()) return endNode.moves;
        else return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        Stack<Board> boards = new Stack<>();
        Node lastNode = this.endNode;
        if(this.isSolvable()) {
            while (lastNode.prev != null) {
                boards.push(lastNode.board);
                lastNode = lastNode.prev;
            }
            boards.push(lastNode.board);
            return boards;
        }
        return null;
    }

    // test client (see below)
    public static void main(String[] args) {
        int[][] testTiles = {
                {8, 4, 7},
                {1, 5, 6},
                {3, 2, 0}
        };
        Board testBoard = new Board(testTiles);
        Solver testSolver = new Solver(testBoard);

        System.out.println("Solvable: " + testSolver.isSolvable());
        System.out.println("Moves: " + testSolver.moves());
        for (Board i: testSolver.solution())
            System.out.println(i.toString());
    }
}

 */