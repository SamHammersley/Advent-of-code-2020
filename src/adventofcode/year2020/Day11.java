package adventofcode.year2020;

import adventofcode.Puzzle;

import java.util.Arrays;

public class Day11 extends Puzzle<Integer> {

    public Day11() {
        super("\n", "");
    }

    private static final char FLOOR = '.';

    private static final char EMPTY = 'L';

    private static final char OCCUPIED = '#';

    private static final int[][] INDEX_DELTA = {
            {-1, -1}, {0, -1}, {1, -1},
            {-1, 0}, {1, 0},
            {-1, 1}, {0, 1}, {1, 1}
    };

    private static boolean inBounds(char[][] grid, int col, int row) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
    }

    private int countOccupied(char[][] grid) {
        int total = 0;

        for (char[] row : grid) {
            for (char seat : row) {
                if (seat == OCCUPIED) {
                    total++;
                }
            }
        }

        return total;
    }

    private char[][] deepCopy(char[][] grid) {
        char[][] copy = new char[grid.length][grid[0].length];

        for (int i = 0; i < grid.length; i++) {
            copy[i] = Arrays.copyOf(grid[i], grid[i].length);
        }

        return copy;
    }

    @FunctionalInterface
    interface OccuppiedSeatCountFunction {

        OccuppiedSeatCountFunction ADJACENT_COUNT = (g, r, c) -> {
            int total = 0;

            for (int[] delta : INDEX_DELTA) {
                int nCol = c + delta[0];
                int nRow = r + delta[1];

                if (inBounds(g, nCol, nRow) && g[nRow][nCol] == OCCUPIED) {
                    total++;
                }
            }

            return total;
        };

        OccuppiedSeatCountFunction IN_SIGHT_COUNT = (g, r, c) -> {
            int total = 0;

            for (int[] delta : INDEX_DELTA) {
                int nCol = c + delta[0];
                int nRow = r + delta[1];

                while (inBounds(g, nCol, nRow) && g[nRow][nCol] == FLOOR) {
                    nCol += delta[0];
                    nRow += delta[1];
                }

                if (inBounds(g, nCol, nRow) && g[nRow][nCol] == OCCUPIED) {
                    total++;
                }
            }

            return total;
        };

        int countOccupied(char[][] grid, int row, int col);

    }

    private char[][] apply(int occupiedThreshold, char[][] grid, OccuppiedSeatCountFunction occupiedCountFunc) {
        char[][] copy = deepCopy(grid);

        for (int r = 0; r < grid.length; r++) {
            char[] row = grid[r];

            for (int c = 0; c < row.length; c++) {
                if (row[c] == FLOOR) {
                    continue;
                }

                int occupied = occupiedCountFunc.countOccupied(grid, r, c);
                copy[r][c] = getNewState(occupied, occupiedThreshold, row[c]);
            }
        }

        return copy;
    }

    private char getNewState(int occupied, int threshold, char currentState) {
        switch(currentState) {
            case OCCUPIED:
                return occupied >= threshold ? EMPTY : currentState;

            case EMPTY:
                return occupied == 0 ? OCCUPIED : currentState;

            default:
                return currentState;
        }
    }

    @Override
    public Integer part1() {
        char[][] initial = input.stream().map(String::toCharArray).toArray(char[][]::new);

        char[][] newGrid = apply(4, initial, OccuppiedSeatCountFunction.ADJACENT_COUNT);
        char[][] previous = null;

        while (!Arrays.deepEquals(newGrid, previous)) {
            previous = newGrid;
            newGrid = apply(4, newGrid, OccuppiedSeatCountFunction.ADJACENT_COUNT);
        }

        return countOccupied(newGrid);
    }

    @Override
    public Integer part2() {
        char[][] initial = input.stream().map(String::toCharArray).toArray(char[][]::new);

        char[][] newGrid = apply(5, initial, OccuppiedSeatCountFunction.IN_SIGHT_COUNT);
        char[][] previous = null;

        while (!Arrays.deepEquals(newGrid, previous)) {
            previous = newGrid;
            newGrid = apply(5, newGrid, OccuppiedSeatCountFunction.IN_SIGHT_COUNT);
        }

        return countOccupied(newGrid);
    }
}