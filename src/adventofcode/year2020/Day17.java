package adventofcode.year2020;

import adventofcode.Puzzle;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Day17 extends Puzzle<Long> {

    public Day17() {
        super("\n", "");
    }

    private boolean shouldActivate(char cube, List<Character> neighbours) {
        long activeNeighbours = neighbours.stream().filter(c -> c.equals('#')).count();

        return switch (cube) {
            case '#' -> activeNeighbours == 2 || activeNeighbours == 3;
            case '.' -> activeNeighbours == 3;
            default -> false;
        };
    }

    private List<Character> neighbours(int z, int x, int y, char[][][] space) {
        List<Character> neighbours = new ArrayList<>();

        for (int nz = -1; nz <= 1; nz++) {
            if (z + nz < 0 || z + nz >= space.length) continue;

            for (int nx = -1; nx <= 1; nx++) {
                if (x + nx < 0 || x + nx >= space[0].length) continue;

                for (int ny = -1; ny <= 1; ny++) {
                    if (y + ny < 0 || y + ny >= space[0][0].length) continue;
                    if (nz == 0 && nx == 0 && ny == 0) {
                        continue;
                    }

                    neighbours.add(space[z + nz][x + nx][y + ny]);
                }
            }
        }

        return neighbours;
    }

    private char[] emptyRow(int length) {
        char[] row = new char[length];
        Arrays.fill(row, '.');
        return row;
    }

    private char[][] emptyPlane(int width, int height) {
        char[][] plane = new char[width][height];
        char[] emptyRow = emptyRow(width);
        Arrays.fill(plane, emptyRow);
        return plane;
    }

    private char[][][] emptyCube(int height, int width, int depth) {
        char[][][] cube = new char[height][width][depth];
        char[][] emptyPlane = emptyPlane(width, depth);
        Arrays.fill(cube, emptyPlane);
        return cube;
    }

    private char[][][] copySpace(char[][][] space) {
        char[][][] newSpace = new char[space.length][space[0].length][space[0][0].length];

        for (int i = 0; i < space.length; i++) {
            for (int j = 0; j < space[0].length; j++) {
                System.arraycopy(space[i][j], 0, newSpace[i][j], 0, space[0][0].length);
            }
        }

        return newSpace;
    }

    private int countActive(char[] row) {
        return (int) CharBuffer.wrap(row).chars().filter(c -> c == '#').count();
    }

    private char[][][] padInput(char[][] input, int height, int width, int depth) {
        char[][][] space = new char[height][width][depth];

        char[] emptyRow = emptyRow(depth);
        char[][] emptyPlane = emptyPlane(width, depth);

        Arrays.fill(space, emptyPlane);

        char[][] paddedInput = Arrays.copyOf(emptyPlane, emptyPlane.length);
        for (int index = 0; index < input.length; index++) {
            char[] row = input[index];
            char[] paddedRow = Arrays.copyOf(emptyRow, emptyRow.length);
            System.arraycopy(row, 0, paddedRow, (depth - row.length) / 2, row.length);

            int dstIndex = index + (depth - row.length) / 2;
            paddedInput[dstIndex] = paddedRow;
        }

        space[height / 2] = paddedInput;

        return space;
    }

    @Override
    public Long part1() {
        char[][] initialCubes = mapInput(String::toCharArray).toArray(char[][]::new);
        int cycles = 6;
        int height = 1 + cycles * 2;
        int width = initialCubes.length + cycles * 2;
        int depth = initialCubes[0].length + cycles * 2;

        char[][][] space = padInput(initialCubes, height, width, depth);
        char[][][] newSpace = copySpace(space);

        int startZ = height / 2;
        int startX = width / 2 - initialCubes.length / 2;
        int startY = depth / 2 - initialCubes[0].length / 2;
        long totalActive = Arrays.stream(initialCubes).mapToInt(this::countActive).sum();

        for (int cycle = 1; cycle <= cycles; cycle++) {
            int expansion = cycle * 2;

            for (int z = -expansion/2; z <= +expansion/2; z++) {
                char[][] plane = space[startZ + z];

                for (int x = -expansion/2; x < initialCubes.length + expansion/2; x++) {
                    char[] row = plane[startX + x];

                    for (int y = -expansion/2; y < initialCubes[0].length + expansion/2; y++) {
                        char cube = row[startY + y];

                        List<Character> neighbours = neighbours(startZ + z, startX + x, startY + y, space);
                        boolean nowActive = shouldActivate(cube, neighbours);

                        if (cube == '.' && nowActive) {
                            totalActive++;
                        } else if (cube == '#' && !nowActive) {
                            totalActive--;
                        }

                        newSpace[startZ + z][startX + x][startY + y] = nowActive ? '#' : '.';
                    }
                }
            }

            space = copySpace(newSpace);
        }

        return totalActive;
    }

    @Override
    public Long part2() {
        char[][] initialCubes = mapInput(String::toCharArray).toArray(char[][]::new);
        int cycles = 6;
        int trength = 1 + cycles * 2;
        int height = 1 + cycles * 2;
        int width = initialCubes.length + cycles * 2;
        int depth = initialCubes[0].length + cycles * 2;

        char[][][][] space = padInput(initialCubes, trength, height, width, depth);
        char[][][][] newSpace = copySpace(space);

        int startW = trength / 2;
        int startZ = height / 2;
        int startX = width / 2 - initialCubes.length / 2;
        int startY = depth / 2 - initialCubes[0].length / 2;
        long totalActive = Arrays.stream(initialCubes).mapToInt(this::countActive).sum();

        for (int cycle = 1; cycle <= cycles; cycle++) {
            int expansion = cycle * 2;

            for (int w = -expansion/2; w <= +expansion/2; w++) {
                char[][][] cube = space[startW + w];

                for (int z = -expansion / 2; z <= +expansion / 2; z++) {
                    char[][] plane = cube[startZ + z];

                    for (int x = -expansion / 2; x < initialCubes.length + expansion / 2; x++) {
                        char[] row = plane[startX + x];

                        for (int y = -expansion / 2; y < initialCubes[0].length + expansion / 2; y++) {
                            char c = row[startY + y];

                            List<Character> neighbours = neighbours(startW + w,startZ + z, startX + x, startY + y, space);
                            boolean nowActive = shouldActivate(c, neighbours);

                            if (c == '.' && nowActive) {
                                totalActive++;
                            } else if (c == '#' && !nowActive) {
                                totalActive--;
                            }

                            newSpace[startW + w][startZ + z][startX + x][startY + y] = nowActive ? '#' : '.';
                        }
                    }
                }
            }

            space = copySpace(newSpace);
        }

        return totalActive;
    }

    private char[][][][] padInput(char[][] input, int trength, int height, int width, int depth) {
        char[][][][] space = new char[trength][height][width][depth];

        char[] emptyRow = emptyRow(depth);
        char[][] emptyPlane = emptyPlane(width, depth);
        char[][][] emptyCube = emptyCube(height, width, depth);

        Arrays.fill(space, emptyCube);

        char[][][] paddedCube = Arrays.copyOf(emptyCube, emptyCube.length);
        char[][] paddedInput = Arrays.copyOf(emptyPlane, emptyPlane.length);
        for (int index = 0; index < input.length; index++) {
            char[] row = input[index];
            char[] paddedRow = Arrays.copyOf(emptyRow, emptyRow.length);
            System.arraycopy(row, 0, paddedRow, (depth - row.length) / 2, row.length);

            int dstIndex = index + (depth - row.length) / 2;
            paddedInput[dstIndex] = paddedRow;
        }

        paddedCube[height / 2] = paddedInput;
        space[trength / 2] = paddedCube;

        return space;
    }

    private char[][][][] copySpace(char[][][][] space) {
        char[][][][] newSpace = new char[space.length][space[0].length][space[0][0].length][space[0][0][0].length];

        for (int i = 0; i < space.length; i++) {
            for (int j = 0; j < space[0].length; j++) {
                for (int k = 0; k < space[0][0].length; k++) {
                    char[] source = space[i][j][k];
                    System.arraycopy(source, 0, newSpace[i][j][k], 0, source.length);
                }
            }
        }

        return newSpace;
    }

    private List<Character> neighbours(int w, int z, int x, int y, char[][][][] space) {
        List<Character> neighbours = new ArrayList<>();

        for (int nw = -1; nw <= 1; nw++) {
            if (w + nw < 0 || w + nw >= space.length) continue;

            for (int nz = -1; nz <= 1; nz++) {
                if (z + nz < 0 || z + nz >= space[0].length) continue;

                for (int nx = -1; nx <= 1; nx++) {
                    if (x + nx < 0 || x + nx >= space[0][0].length) continue;

                    for (int ny = -1; ny <= 1; ny++) {
                        if (y + ny < 0 || y + ny >= space[0][0][0].length) continue;
                        if (nw == 0 && nz == 0 && nx == 0 && ny == 0) {
                            continue;
                        }

                        neighbours.add(space[w + nw][z + nz][x + nx][y + ny]);
                    }
                }
            }
        }

        return neighbours;
    }

}