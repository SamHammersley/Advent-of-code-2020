package adventofcode.year2020;

import adventofcode.Puzzle;

import java.util.List;
import java.util.stream.Collectors;

public final class Day5 extends Puzzle<Integer> {

    public Day5() {
        super("\n", "[B|F]{7}[L|R]{3}");
    }

    private int decode(String encoded, char up, int upperBound) {
        int upper = upperBound, lower = 0;

        for (char c : encoded.toCharArray()) {
            int midpoint = lower + (upper - lower) / 2;

            if (c == up) {
                upper = midpoint;
            } else {
                lower = midpoint + 1;
            }
        }

        return upper;
    }

    private final List<Integer> seatIds = mapInput(s -> decode(s.substring(0, 7), 'F', 127) * 8 + decode(s.substring(7), 'L', 7))
            .sorted()
            .collect(Collectors.toList());

    @Override
    public Integer part1() {
        return seatIds.get(0);
    }

    @Override
    public Integer part2() {
        for (int index = 0; index < seatIds.size(); index++) {
            if (seatIds.get(index) != seatIds.get(0) + index) {
                return seatIds.get(index) - 1;
            }
        }

        return null;
    }
}