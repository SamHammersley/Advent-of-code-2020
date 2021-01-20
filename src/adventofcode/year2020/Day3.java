package adventofcode.year2020;

import adventofcode.Puzzle;

import java.util.List;

public final class Day3 extends Puzzle<Long> {

    public Day3() {
        super("\n", "later bater");
    }

    @Override
    public Long part1() {
        return countTrees(input, 3, 1);
    }

    @Override
    public Long part2() {
        long slope1 = countTrees(input, 1, 1);
        long slope2 = countTrees(input, 3, 1);
        long slope3 = countTrees(input, 5, 1);
        long slope4 = countTrees(input, 7, 1);
        long slope5 = countTrees(input, 1, 2);

        return slope1 * slope2 * slope3 * slope4 * slope5;
    }

    static long countTrees(List<String> lines, int rightDist, int downDist) {
        long treeCount = 0;

        for (int row = 0, column = 0; row < lines.size(); row += downDist) {
            String line = lines.get(row);

            if (line.charAt(column) == '#') {
                treeCount++;
            }

            column += rightDist;
            column %= line.length();
        }

        return treeCount;
    }
}