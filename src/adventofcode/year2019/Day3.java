package adventofcode.year2019;

import adventofcode.Puzzle;

import java.util.*;
import java.util.List;

public final class Day3 extends Puzzle<Integer> {

    public Day3() {
        super("\n", "");
    }

    private static final Map<Character, int[]> DIRECTION_TABLE = Map.of(
            'L', new int[] {-1, 0},
            'R', new int[] {1, 0},
            'U', new int[] {0, 1},
            'D', new int[] {0, -1}
    );

    private int distFromOrigin(int[] pos) {
        return manhattanDist(new int[] {0, 0}, pos);
    }

    private int manhattanDist(int[] pos1, int[] pos2) {
        if (pos1 == null || pos2 == null) {
            return -1;
        }
        return Math.abs(pos1[0] - pos2[0]) + Math.abs(pos1[1] - pos2[1]);
    }

    class Line {

        private final int x1, y1;

        private final int x2, y2;

        Line(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        Line(int[] pos1, int[] pos2) {
            this(pos1[0], pos1[1], pos2[0], pos2[1]);
        }

        int length() {
            return manhattanDist(new int[] { x1, y1 }, new int[] { x2, y2 });
        }

        private boolean contains(int x, int y) {
            return x >= Math.min(x1, x2) && x <= Math.max(x1, x2) && y >= Math.min(y1, y2) && y <= Math.max(y1, y2);
        }

        int[] intersection(Line l) {
            int a1 = y2 - y1, b1 = x1 - x2, c1 = a1*x1 + b1*y1;
            int a2 = l.y2 - l.y1, b2 = l.x1 - l.x2, c2 = a2*l.x1 + b2*l.y1;

            int determinant = a1 * b2 - a2 * b1;

            if (determinant == 0) {
                return null;
            }

            int x = (b2 * c1 - b1 * c2) / determinant;
            int y = (a1 * c2 - a2 * c1) / determinant;

            if (!contains(x, y) || !l.contains(x, y)) {
                return null;
            }

            return new int[] { x, y };
        }

        @Override
        public String toString() {
            return "(" + x1 + "," + y1 + " to " + x2 + "," + y2 + ")";
        }

        @Override
        public int hashCode() {
            return Objects.hash(x1, y1, x2, y2);
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof Line)) {
                return false;
            }

            Line other = (Line) object;

            return x1 == other.x1 && y1 == other.y1 && x2 == other.x2 && y2 == other.y2;
        }

    }

    private List<Line> plotPath(String[] instructions) {
        List<Line> path = new ArrayList<>();

        int[] previousPos = { 0, 0 };

        for (String instruction : instructions) {
            int[] direction = DIRECTION_TABLE.get(instruction.charAt(0));
            int magnitude = Integer.parseInt(instruction.substring(1));

            int[] newPos = {
                previousPos[0] + (direction[0] * magnitude),
                previousPos[1] + (direction[1] * magnitude)
            };

            path.add(new Line(previousPos, newPos));

            previousPos = newPos;
        }

        return path;
    }

    @Override
    public Integer part1() {
        Set<int[]> intersections = new HashSet<>();

        List<Line> path1 = plotPath(input.get(0).split(","));
        List<Line> path2 = plotPath(input.get(1).split(","));

        for (Line line1 : path1) {
            for (Line line2 : path2) {
                intersections.add(line1.intersection(line2));
            }
        }

        return intersections.stream().map(this::distFromOrigin).filter(d -> d > 0).min(Integer::compareTo).get();
    }

    @Override
    public Integer part2() {
        Set<int[]> intersections = new HashSet<>();

        List<Line> path1 = plotPath(input.get(0).split(","));
        List<Line> path2 = plotPath(input.get(1).split(","));

        for (Line line1 : path1) {
            for (Line line2 : path2) {
                intersections.add(line1.intersection(line2));
            }
        }

        Set<Integer> totalSteps = new HashSet<>();

        for (int[] intersection : intersections) {
            if (intersection == null || (intersection[0] == 0 && intersection[1] == 0)) {
                continue;
            }

            int path1Steps = calculateSteps(path1, intersection);
            int path2Steps = calculateSteps(path2, intersection);

            totalSteps.add(path1Steps + path2Steps);
        }

        return totalSteps.stream().min(Integer::compareTo).get();
    }

    private int calculateSteps(List<Line> path, int[] intersection) {
        Line intersectedLine = null;
        int steps = 0;

        for (Line line : path) {
            if (line.contains(intersection[0], intersection[1])) {
                intersectedLine = line;
                break;
            }

            steps += line.length();
        }

        return steps + manhattanDist(intersection, new int[] { intersectedLine.x1, intersectedLine.y1 });
    }
}